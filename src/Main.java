import java.util.List;

public class Main {
    public static void main(String[] args) {
        // List of postal codes, we need to extract these from the database
        String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL"};
        // Initialize the converter and pass the list of postal codes
        ConvertToLatLng converter = new ConvertToLatLng(postalCodes);
        // Print the locations to visualize the input
        System.out.println(converter.getLocations());
        // Print the distance matrix
        converter.printDistanceMatrix();


    }
}
