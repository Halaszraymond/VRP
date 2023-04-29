import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;
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
    private int numberOfVehicles;
    private int depot;
    public VRPCalculator(String geocoderAPIKey, String openRouteServiceAPIKey, String[] postalCodes, int numberOfVehicles, int depot) throws IOException {
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
        // Determines the amount of vehicles used in the algorithm
        this.numberOfVehicles = numberOfVehicles;
        // Gets the depot location in the matrix
        this.depot = depot;
    }
    public void calculateVRP() throws IOException {
        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        DataModel data = new DataModel(this.timeDistanceMatrix, this.numberOfVehicles, this.depot);
        // Create Routing Index Manager
        RoutingIndexManager manager = new RoutingIndexManager(data.getDistanceMatrix().length, data.getNumberOfVehicles(), data.getDepot());
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
        // Add Distance constraint.
        routing.addDimension(transitCallbackIndex, 0, 70000,
                true, // start cumul to zero
                "Duration");
        RoutingDimension distanceDimension = routing.getMutableDimension("Duration");
        distanceDimension.setGlobalSpanCostCoefficient(70000);
        // Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.SAVINGS)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(Duration.newBuilder().setSeconds(30).build())
                        .setLogSearch(true)
                        .build();
        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);
        // Print solution on console.
        data.getSolution(data, routing, manager, solution);
    }
}
