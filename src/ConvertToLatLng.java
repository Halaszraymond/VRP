import java.util.ArrayList;
import java.util.List;

public class ConvertToLatLng {

        private List<Location> locations;
        private Geocoder geocoder;
        private String[] postalCodes;
        private double[][] timeDistanceMatrix;
        private List<TimeWindow> timeWindows;
        private List<List<Location>> vehicleRoutes;
        private int numVehicles;
        private int vehicleCapacity;
        public ConvertToLatLng(String[] postalCodes) {
            this.locations = new ArrayList<>();
            // Geocoder receives an api-key so that the methods can be used to get the location of the postal code
            this.geocoder = new Geocoder("c9941b30ff46475186fc03e68861bf3d");
            this.postalCodes = postalCodes;
            // makes a list of Locations based on all postal codes
            //makeLatLngList();
            // Creates a time distance matrix based on all postal codes
            createDistanceMatrix();
        }
        public void makeLatLngList() {
            // Uses every postal code to get the latitude and longitude
            for (String postalCode : postalCodes) {
                try {
                    // using the geocode-api to convert a postal code to latitude and longitude
                    Location location = geocoder.geocode(postalCode);
                    // adding the location to the list
                    locations.add(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public List<Location> getLocations() {
            // Fills list of locations
           // makeLatLngList();
            // returns the list
            return locations;
        }
        public void createDistanceMatrix() {
            try {
                makeLatLngList();
                // // OpenRouteService receives an api-key so that the methods can be used to get a time distance matrix
                OpenRouteServiceDistanceMatrix distanceMatrix = new OpenRouteServiceDistanceMatrix("5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a");
                // Uses the getTimeDistanceMatrix to get the time distance matrix based on all locations
                this.timeDistanceMatrix = distanceMatrix.getTimeDistance(locations);
            } catch (Exception e) {
                System.err.println("An error occurred while initializing the time distance matrix: " + e.getMessage());
                e.printStackTrace();
            }
        }
        public void printDistanceMatrix() {
            // shows every row in the matrix
            System.out.println(timeDistanceMatrix);
        }
}