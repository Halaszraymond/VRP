import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
public class OpenRouteServiceDistanceMatrix {
    private final String apiKey;
    public OpenRouteServiceDistanceMatrix(String apiKey) {
        this.apiKey = apiKey;
    }
    public double[][] getTimeDistance(List<Location> locations) throws Exception {
        int numLocations = locations.size();
        double[][] timeDistanceMatrix = new double[numLocations][numLocations];

        String url = "https://api.openrouteservice.org/v2/matrix/driving-car";
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<List<Double>> coordinates = new ArrayList<>();
        for (Location location : locations) {
            List<Double> coordinate = new ArrayList<>();
            coordinate.add(location.getLongitude());
            coordinate.add(location.getLatitude());
            coordinates.add(coordinate);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(coordinates);

        StringEntity entity = new StringEntity("{\"locations\":" + json + ",\"metrics\":[\"duration\"]}");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
        httpPost.setHeader("Authorization", apiKey);
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());

        //JsonNode durations = objectMapper.readTree(result).get("durations").get(0);

        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < numLocations; j++) {
                int index = i * numLocations + j;
                double duration = objectMapper.readTree(result).at("/durations/0/" + index).asDouble();
                timeDistanceMatrix[i][j] = duration;
            }
        }
        return timeDistanceMatrix;
    }
}
