import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Geocoder {
    private String apiKey;
    public Geocoder(String apiKey) {
        this.apiKey = apiKey;
    }
    public Location geocode(String postalCode) throws IOException {
        URL url = new URL("https://api.opencagedata.com/geocode/v1/json?q=" + postalCode + "&key=" + apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray results = jsonResponse.getJSONArray("results");
        JSONObject result = results.getJSONObject(0);
        JSONObject geometry = result.getJSONObject("geometry");
        double latitude = geometry.getDouble("lat");
        double longitude = geometry.getDouble("lng");

        return new Location(latitude, longitude);
    }
}
