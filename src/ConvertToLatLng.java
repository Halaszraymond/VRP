import java.util.ArrayList;
import java.util.List;

public class ConvertToLatLng {
        private int numVehicles;
        private int vehicleCapacity;
        private List<Location> locations;
        private Geocoder geocoder = new Geocoder("c9941b30ff46475186fc03e68861bf3d");
        private String[] postalCodes;
        private double[][] timeDistanceMatrix;
        private List<TimeWindow> timeWindows;
        private List<List<Location>> vehicleRoutes;
        public ConvertToLatLng(String[] postalCodes) {
            this.locations = new ArrayList<>();
            this.geocoder = new Geocoder("c9941b30ff46475186fc03e68861bf3d");
            this.postalCodes = postalCodes;
            printLatLng();
            createDistanceMatrix();
        }
        public void printLatLng() {
            for (String postalCode : postalCodes) {
                try {
                    Location location = geocoder.geocode(postalCode);
                    Location newLocation = new Location(location.getLatitude(), location.getLongitude());
                    locations.add(newLocation);
    //                    System.out.println("postalCode: " + postalCode);
    //                    System.out.println("Latitude: " + location.getLatitude());
    //                    System.out.println("Longitude: " + location.getLongitude());
    //                    System.out.println("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public List<Location> getLocations() {
            printLatLng();
            return locations;
        }
        public void createDistanceMatrix() {
            try {
                OpenRouteServiceDistanceMatrix distanceMatrix = new OpenRouteServiceDistanceMatrix("5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a");
                this.timeDistanceMatrix = distanceMatrix.getTimeDistance(locations);
            } catch (Exception e) {
                System.err.println("An error occurred while initializing the time distance matrix: " + e.getMessage());
                e.printStackTrace();
            }
        }
        public void printDistanceMatrix() {
            System.out.println(timeDistanceMatrix);
        }
}