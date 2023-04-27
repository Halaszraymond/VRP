import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] postalCodes = new String[]{"6712CP", "5051RB", "1106NG", "7141LD", "7881JM", "1132PA", "5035BM", "7203EA", "6171VS", "4641SJ", "5991NJ", "3032AG", "1991AT", "7523XZ", "7232DS", "5037HL"};
        ConvertToLatLng converter = new ConvertToLatLng(postalCodes);
        converter.printDistanceMatrix();


    }
}
