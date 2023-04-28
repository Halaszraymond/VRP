public class Location {
    private double latitude;
    private double longitude;
    public Location(double latitude, double longitude) {
        // Initialize instance variables
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String toString() {
        // Return a string representation of the location
        return "(" + latitude + ", " + longitude + ")";
    }
}
