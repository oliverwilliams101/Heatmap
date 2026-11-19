package heatmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TemperatureData {

    private final ArrayList<DataPoint> tempData;

    private final float minLongitude;
    private final float maxLongitude;
    private final float minLatitude;
    private final float maxLatitude;

    // takes a filename as a string as parameter for the constructor and calls the read temperature method using the filename
    // a FileNotFoundException is thrown if needed
    public TemperatureData(String filename) throws FileNotFoundException {
        this.tempData = this.readTemperature(filename);
        // the min longitude, latitude, and max longitude, latitude are initially set
        // the class variables set are, so you don't rerun the methods that go through all the data again
        this.minLongitude = this.calcMinLongitude();
        this.minLatitude = this.calcMinLatitude();
        this.maxLongitude = this.calcMaxLongitude();
        this.maxLatitude = this.calcMaxLatitude();
    }

    // gets the temperature data
    public ArrayList<DataPoint> getTempData() {
        return this.tempData;
    }

    // calculates the minimum longitude
    private float calcMinLongitude() {
        float smallest = Float.MAX_VALUE;
        float smallestCurrent;
        for (DataPoint dP : this.tempData) {
            smallestCurrent = dP.getLongitude();
            if (smallestCurrent < smallest) {
                smallest = smallestCurrent;
            }
        }
        return smallest;
    }

    // gets the minimum longitude
    public float getMinLongitude() {
        return this.minLongitude;
    }

    // calculates the minimum latitude
    private float calcMinLatitude() {
        float smallest = Float.MAX_VALUE;
        float smallestCurrent;
        for (DataPoint dP : this.tempData) {
            smallestCurrent = dP.getLatitude();
            if (smallestCurrent < smallest) {
                smallest = smallestCurrent;
            }
        }
        return smallest;
    }

    // gets the minimum latitude
    public float getMinLatitude() {
        return this.minLatitude;
    }

    // calculates the maximum longitude
    private float calcMaxLongitude() {
        float largest = Float.MIN_VALUE;
        float largestCurrent;
        for (DataPoint dP : this.tempData) {
            largestCurrent = dP.getLongitude();
            if (largestCurrent > largest) {
                largest = largestCurrent;
            }
        }
        return largest;
    }

    // gets the maximum longitude
    public float getMaxLongitude() {
        return this.maxLongitude;
    }

    // calculates the maximum latitude
    private float calcMaxLatitude() {
        float largest = Float.MIN_VALUE;
        float largestCurrent;
        for (DataPoint dP : this.tempData) {
            largestCurrent = dP.getLatitude();
            if (largestCurrent > largest) {
                largest = largestCurrent;
            }
        }
        return largest;
    }

    // gets the maximum latitude
    public float getMaxLatitude() {
        return this.maxLatitude;
    }

    // gets the min temperature
    public float getMinTemp() {
        return this.tempData.get(0).getTemperature();
    }

    // gets the max temperature
    public float getMaxTemp() {
        return this.tempData.get(this.tempData.size() - 1).getTemperature();
    }

    // gets the middle temperature which is used to split the gradients for the heatmap
    public float getMiddleTemp() {
        return this.tempData.get((this.tempData.size() - 1) / 2).getTemperature();
    }

    // reads the temperature data
    public ArrayList<DataPoint> readTemperature(String filename) throws FileNotFoundException {
        ArrayList<DataPoint> tempData = new ArrayList<>();

        Scanner input = new Scanner(new File(filename));

        while (input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] currentData = currentLine.split(",");

            float latitude = Float.parseFloat(currentData[0]);
            float longitude = Float.parseFloat(currentData[1]);
            float temperature = Float.parseFloat(currentData[2]);

            DataPoint dataPoint = new DataPoint(latitude, longitude, this.round(this.convertToCelsius(temperature), 1));
            tempData.add(dataPoint);

        }
        input.close();
        Collections.sort(tempData);
        return tempData;
    }

    // method to convert from kelvin to Celsius
    public float convertToCelsius(float temp) {
        return (temp - 273.15f);
    }

    public float round(float temp, int dp) {
        float rounded = Math.round(temp * (Math.pow(10, dp)));
        rounded = (float) (rounded / (Math.pow(10, dp)));
        return rounded;
    }
}
