public class Location {
    private double latitude;
    private double longitude;
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Location(Location otherLocation) {
        this.latitude = otherLocation.latitude;
        this.longitude = otherLocation.longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
