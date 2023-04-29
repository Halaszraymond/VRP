import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;

public class Main {
    public static void main(String[] args) throws IOException {
        // List of postal codes, we need to extract these from the database
        String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL"};
        // Initialize the converter and pass the list of postal codes
        ConvertToLatLng converter = new ConvertToLatLng(postalCodes);
        // apikey to openroute service
        String apiKey = "5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a";
        // LatLong coordinates for the postalcodes
        List<Location> locations = converter.getLocations();
        // Initialize the TimeDistanceMatrix
        OpenRouteServiceDistanceMatrix matrix = new OpenRouteServiceDistanceMatrix(apiKey, locations);
        // Print the matrix
        long[][] timeDistanceMatrix = matrix.createMatrix();
        // Number of vehicles
        int numberOfVehicles = 4;
        int depot = 0;


        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        final DataModel data = new DataModel(timeDistanceMatrix, numberOfVehicles, depot);

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
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);

        // Print solution on console.
        data.printSolution(data, routing, manager, solution);
    }
}
