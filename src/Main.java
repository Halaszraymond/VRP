import java.io.IOException;

public class Main {
    // apikey to geoccoder service (Getting LatLong)
    private static String geocoderAPIKey = "c9941b30ff46475186fc03e68861bf3d";
    // apikey to openroute service (Create TimeDistanceMatrix)
    private static String openRouteServiceAPIKey = "5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a";
    // List of postal codes, we need to extract these from the database
    private static String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL"};
    // Number of vehicles
    private static int numberOfVehicles = 4;
    // Location of the depot
    private static int depot = 0;
    public static void main(String[] args) throws IOException {
        // Initialize the VRP calculator
        VRPCalculator calculator = new VRPCalculator(geocoderAPIKey, openRouteServiceAPIKey, postalCodes, numberOfVehicles, depot);
        // Calculates the best routes
        calculator.calculateVRP();
    }
}
