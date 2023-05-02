import java.io.IOException;

public class Main {
    // apikey to geocoder service (Getting LatLong)
    private static String geocoderAPIKey = "c9941b30ff46475186fc03e68861bf3d";
    // apikey to openroute service (Create TimeDistanceMatrix)
    private static String openRouteServiceAPIKey = "5b3ce3597851110001cf62486ddae1168c244a1fb63e38ce8c161e4a";
    // List of postal codes, first PostalCode is the DEPOT!!! we need to extract these from the database (Use https://www.bestrandoms.com/random-address-in-nl for random but EXISTING Dutch postal codes)
    private static String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL", "5731XX", "8251KW", "3341LB", "6591TW", "6021EG"};
    // Demands per postal Code, keep in mind that the total demands must not exceed the total capacity of all vehicles
    private static long[] demands = {0, 1, 1, 2, 4, 2, 4, 8, 8, 1, 2, 1, 2, 4, 4, 8, 8, 6, 4, 5, 7};
    // Capacity of each vehicle, keep in mind that the total demands must not exceed the total capacity of all vehicles
    private static long vehicleCapacity = 30;
    // Number of vehicles
    private static int numberOfVehicles = 10;
    // Location of the depot
    private static int depot = 0;
    public static void main(String[] args) throws IOException {
        // Initialize the VRP calculator
        VRPCalculator calculator = new VRPCalculator(geocoderAPIKey, openRouteServiceAPIKey, postalCodes, demands, vehicleCapacity, numberOfVehicles, depot);
        // Calculates the best routes, building needs some time (+- 20 seconds)
        calculator.calculateVRP();
    }
}
