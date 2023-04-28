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
            makeLatLngList();
            // returns the list
            return locations;
        }
}