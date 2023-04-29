import java.util.logging.Logger;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;

public class DataModel {
    private final Logger logger = Logger.getLogger(DataModel.class.getName());
    private final long[][] distanceMatrix;
    private final int numberOfVehicles;
    private final int depot;

    public DataModel(long[][] distanceMatrix, int numberOfVehicles, int depot) {
        this.distanceMatrix = distanceMatrix;
        this.numberOfVehicles = numberOfVehicles;
        this.depot = depot;
    }

    public long[][] getDistanceMatrix() {
        return distanceMatrix;
    }
    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }
    public int getDepot() {
        return depot;
    }

    /// @brief Print the solution.
    public void getSolution(DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        // Solution cost.
        //logger.info("Objective : " + solution.objectiveValue());
        // Inspect solution.
        long maxRouteDistance = 0;
        for (int i = 0; i < data.numberOfVehicles; ++i) {
            long index = routing.start(i);
            //logger.info("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            String route = "";
            while (!routing.isEnd(index)) {
                route += manager.indexToNode(index) + " -> ";
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            System.out.println("Vehicle " + (i + 1) + ": " + route + manager.indexToNode(index));
            //logger.info(route + manager.indexToNode(index));
            //logger.info("Distance of the route: " + routeDistance + "m");
            maxRouteDistance = Math.max(routeDistance, maxRouteDistance);
        }
        //logger.info("Maximum of the route distances: " + maxRouteDistance + "m");
    }
}
