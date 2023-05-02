import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;
import com.google.ortools.Loader;

import java.io.IOException;
import java.util.List;

public class VRPCalculator {
    private String geocoderAPIKey;
    private String openRouteServiceAPIKey;
    private String[] postalCodes;
    private ConvertToLatLng converter;
    private List<Location> Locations;
    private OpenRouteServiceDistanceMatrix matrix;
    private long[][] timeDistanceMatrix;
    private long[] demands;
    private long vehicleCapacity;
    private long[] vehicleCapacities;
    private int numberOfVehicles;
    private int depot;
    public VRPCalculator(String geocoderAPIKey, String openRouteServiceAPIKey, String[] postalCodes, long[] demands, long vehicleCapacity, int numberOfVehicles, int depot) throws IOException, IOException {
        // ApiKeys to be used for the program
        this.geocoderAPIKey = geocoderAPIKey;
        this.openRouteServiceAPIKey = openRouteServiceAPIKey;
        // postalCodes which will be used to get the LatLong coordinates
        this.postalCodes = postalCodes;
        // Initialize the converter and pass the list of postal codes
        this.converter = new ConvertToLatLng(this.postalCodes, this.geocoderAPIKey);
        // LatLong coordinates for the postalcodes
        this.Locations = converter.getLocations();
        // Initialize the TimeDistanceMatrix
        this.matrix = new OpenRouteServiceDistanceMatrix(this.openRouteServiceAPIKey, this.Locations);
        // create the matrix
        this.timeDistanceMatrix = this.matrix.createMatrix();
        // demands per location
        this.demands = demands;
        // Capacity per vehicle
        this.vehicleCapacity = vehicleCapacity;
        // Determines the amount of vehicles used in the algorithm
        this.numberOfVehicles = numberOfVehicles;
        // Capacity for all vehicles
        this.vehicleCapacities = getVehicleCapacities();
        // Gets the depot location in the matrix
        this.depot = depot;
    }
    public long[] getVehicleCapacities() {
        long[] vehicleCapacities = new long[this.numberOfVehicles];
        for (int i = 0; i < this.numberOfVehicles; i++) {
            vehicleCapacities[i] = this.vehicleCapacity;
        }
        return vehicleCapacities;
    }
    public int[][] calculateVRP() throws IOException {
        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        final DataModel data = new DataModel(this.timeDistanceMatrix, this.demands, this.vehicleCapacity, this.vehicleCapacities, this.numberOfVehicles, this.depot);

        // Create Routing Index Manager
        RoutingIndexManager manager =
                new RoutingIndexManager(data.getDistanceMatrix().length, data.getNumberOfVehicles(), data.getDepot());

        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback.
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return data.getDistanceMatrix()[fromNode][toNode];
                });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Capacity constraint.
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            // Convert from routing variable Index to user NodeIndex.
            int fromNode = manager.indexToNode(fromIndex);
            return data.getDemands()[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, // null capacity slack
                data.getVehicleCapacities(), // vehicle maximum capacities
                true, // start cumul to zero
                "Capacity");

        // Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.SAVINGS)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(Duration.newBuilder().setSeconds(10).build())
                        .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);
        if (solution != null) {
            // Print solution on console.
            int[][] routesList = data.getSolution(data, routing, manager, solution);
            return routesList;
        } else {
            System.out.println("No solution found, make sure that the total capacity of the vans does not exceed the total demand. If it doesnt help, change the algorithm");
            return null;
        }
    }
}
