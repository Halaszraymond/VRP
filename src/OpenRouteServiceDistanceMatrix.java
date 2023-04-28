import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenRouteServiceDistanceMatrix {
    private String apiKey;
    private List<Location> locations;
    public OpenRouteServiceDistanceMatrix (String apiKey, List<Location> locations) {
        this.apiKey = apiKey;
        this.locations = locations;
    }
    // Converting a list of locations to a JSON array
    public ArrayList<List<Double>> convertListToJSONArray() {
        ArrayList list = new ArrayList();
        for (Location location : locations) {
            ArrayList<Double> newLocation = new ArrayList<Double>();
            newLocation.add(location.getLongitude());
            newLocation.add(location.getLatitude());
            list.add(newLocation);
        }
        return list;
    }

    public double[][] apiCall() throws IOException {
        Client client = ClientBuilder.newClient();
        ArrayList<List<Double>> jsonList = convertListToJSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(jsonList);
        jsonString = "{\"locations\":" + jsonString + "}";
        Entity<String> payload = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = client.target("https://api.openrouteservice.org/v2/matrix/driving-car")
                .request()
                .header("Authorization", apiKey)
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        String responseBody = response.readEntity(String.class);
        System.out.println("body:" + responseBody);

        JsonNode matrix = objectMapper.readTree(responseBody).get("durations");
        int size = matrix.size();
        double[][] timeDistanceMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                timeDistanceMatrix[i][j] = matrix.get(i).get(j).asDouble();
            }
        }
        return timeDistanceMatrix;
    }

    public void printMatrix() throws IOException {
        double[][] timeDistanceMatrix = apiCall();
        System.out.print("\n[");
        for (int i = 0; i < timeDistanceMatrix.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print("[");
            for (int j = 0; j < timeDistanceMatrix[i].length; j++) {
                if (j > 0) {
                    System.out.print(", ");
                }
                System.out.printf("%.0f", timeDistanceMatrix[i][j]);
            }
            System.out.print("]");
        }
        System.out.println("]");
    }
}