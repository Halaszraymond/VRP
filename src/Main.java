import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // List of postal codes, we need to extract these from the database
        String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL"};
        // Initialize the converter and pass the list of postal codes
        ConvertToLatLng converter = new ConvertToLatLng(postalCodes);
        // apikey to openroute service
        String apiKey = "5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a";
        // LatLong coordinates for the postalcodes
        List<Location> locations = converter.getLocations();
        // Initialize the TimeDistanceMatrix
        OpenRouteServiceDistanceMatrix matrix = new OpenRouteServiceDistanceMatrix(apiKey, locations);
        // Print the matrix
        matrix.printMatrix();
    }
}
