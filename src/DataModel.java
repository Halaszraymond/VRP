import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import java.util.logging.Logger;

public class DataModel {
    private static final Logger logger = Logger.getLogger(DataModel.class.getName());
    private final long[][] distanceMatrix;
    private final long[] demands;
    private final long[] vehicleCapacities;
    private final int numberOfVehicles;
    private final int depot;

    public DataModel(long[][] distanceMatrix, long[] demands, long[] vehicleCapacities, int numberOfVehicles, int depot) {
        this.distanceMatrix = distanceMatrix;
        this.demands = demands;
        this.vehicleCapacities = vehicleCapacities;
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
    public long[] getDemands() {
        return demands;
    }
    public long[] getVehicleCapacities() {
        return vehicleCapacities;
    }

    /// @brief Print the solution.
    public void getSolution(
            DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        // Solution cost.
        //logger.info("Objective: " + solution.objectiveValue());
        // Inspect solution.
        long totalDistance = 0;
        long totalLoad = 0;
        for (int i = 0; i < this.numberOfVehicles; ++i) {
            long index = routing.start(i);
            //logger.info("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            long routeLoad = 0;
            String route = "";
            while (!routing.isEnd(index)) {
                long nodeIndex = manager.indexToNode(index);
                routeLoad += data.demands[(int) nodeIndex];
                route += nodeIndex + " Load(" + routeLoad + ") -> ";
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            System.out.println("Vehicle " + i + ": " + route + "Total distance: " + (routeDistance/60) + " minutes");
            route += manager.indexToNode(routing.end(i));
            //logger.info(route);
            //logger.info("Distance of the route: " + routeDistance + "m");
            totalDistance += routeDistance;
            totalLoad += routeLoad;
        }
        //logger.info("Total distance of all routes: " + totalDistance + "m");
        //logger.info("Total load of all routes: " + totalLoad);
    }
}
