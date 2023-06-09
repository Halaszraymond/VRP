import java.util.ArrayList;
import java.util.List;

public class ConvertToLatLng {
        private List<Location> locations;
        private Geocoder geocoder;
        private String[] postalCodes;
        private List<TimeWindow> timeWindows;
        public ConvertToLatLng(String[] postalCodes, String geocoderAPIKey) {
            this.locations = new ArrayList<>();
            // Geocoder receives an api-key so that the methods can be used to get the location of the postal code
            this.geocoder = new Geocoder(geocoderAPIKey);
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