import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class Geocoder {
    private String apiKey;
    public Geocoder(String apiKey) {
        // Receives the API key from convertToLatLng
        this.apiKey = apiKey;
    }
    // Converts a postal code to latitude and longitude
    public Location geocode(String postalCode) throws IOException {
        // Creates the URL
        URL url = new URL("https://api.opencagedata.com/geocode/v1/json?q=" + postalCode + "&key=" + apiKey);
        // Creates the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Sets the request method to GET
        connection.setRequestMethod("GET");
        // Initiatlizes the connection
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        // Reads the response
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // Converts the response to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.toString());
        // Gets the latitude and longitude
        JsonNode results = jsonResponse.get("results");
        JsonNode result = results.get(0);
        JsonNode geometry = result.get("geometry");
        double latitude = geometry.get("lat").asDouble();
        double longitude = geometry.get("lng").asDouble();

        // Creates the location using Location class
        return new Location(latitude, longitude);
    }
}
