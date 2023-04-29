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
        // Makes an ArrayList<List<Double>> of the locations and returns it
        ArrayList list = new ArrayList();
        for (Location location : locations) {
            ArrayList<Double> newLocation = new ArrayList<Double>();
            newLocation.add(location.getLongitude());
            newLocation.add(location.getLatitude());
            list.add(newLocation);
        }
        return list;
    }

    public long[][] createMatrix() throws IOException {
        Client client = ClientBuilder.newClient();
        ArrayList<List<Double>> jsonList = convertListToJSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(jsonList);
        // Formats the locations as JSON array
        jsonString = "{\"locations\":" + jsonString + "}";
        // Creates the request
        Entity<String> payload = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        // Gets the response
        Response response = client.target("https://api.openrouteservice.org/v2/matrix/driving-car")
                .request()
                .header("Authorization", apiKey)
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .post(payload);

        // Prints the response and status of the request
        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        String responseBody = response.readEntity(String.class);
        System.out.println("body:" + responseBody + "\n");

        // Converts the response to JSON
        JsonNode matrix = objectMapper.readTree(responseBody).get("durations");
        // Converts the JSON array to a long[][]
        int size = matrix.size();
        long[][] timeDistanceMatrix = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                timeDistanceMatrix[i][j] = (long) matrix.get(i).get(j).asDouble();
            }
        }
        return timeDistanceMatrix;
    }

    public void printMatrix() throws IOException {
        // This method prints the matrix in the format that is provided by OR-Tools
        long[][] timeDistanceMatrix = createMatrix();
        System.out.print("{\n");
        for (int i = 0; i < timeDistanceMatrix.length; i++) {
            System.out.print("    {");
            for (int j = 0; j < timeDistanceMatrix[i].length; j++) {
                System.out.print(timeDistanceMatrix[i][j]);
                if (j < timeDistanceMatrix[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("}");
            if (i < timeDistanceMatrix.length - 1) {
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.print("}");
    }
}