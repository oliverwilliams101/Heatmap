package heatmap;

public class DataPoint implements Comparable<DataPoint> {

    private final float latitude;
    private final float longitude;
    private final float temperature;

    // takes in latitude, longitude, and temperature as variables set by the constructor
    // showValues is set to false because it doesn't want to show its values initially
    public DataPoint(float latitude, float longitude, float temperature) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
    }

    // gets the temperature
    public float getTemperature() {
        return this.temperature;
    }

    // gets the longitude
    public float getLongitude() {
        return this.longitude;
    }

    // gets the latitude
    public float getLatitude() {
        return this.latitude;
    }

    // compareTo method for datapoint, sorted based on temperature to easily get the min, middle and max
    @Override
    public int compareTo(DataPoint dataPoint) {
        float result = dataPoint.temperature - this.temperature;
        if (result > 0) {
            return -1;
        } else if (result < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // shows the info for the datapoint
    public void showInfo() {
        System.out.println(this.latitude + ", " + this.longitude);
        System.out.println("Temperature: " + this.temperature);
    }
}
