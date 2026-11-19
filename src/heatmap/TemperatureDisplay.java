package heatmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Objects;
import java.awt.image.BufferedImage;

public class TemperatureDisplay extends JComponent implements ActionListener, MouseListener {

    // Saves the heatmap image
    private BufferedImage heatmapImage;

    // temperature data that consists of an arraylist containing datapoints
    private final TemperatureData temperatureData;

    // use color is initially set to true, and a button is created to switch between color and grey scale
    private boolean useColor = true;
    private final JToggleButton colorSwitch;

    // contains all the ellipse data points
    private ArrayList<EllipseDataPoint> ellipseDataPoints;

    // the current point on the graph that's been pressed by the mouse
    private EllipseDataPoint currentEllipseDataPoint;

    // constructor takes in temperature data and the button to switch color
    public TemperatureDisplay(TemperatureData temperatureData, JToggleButton colorSwitch) {
        this.temperatureData = temperatureData;
        this.colorSwitch = colorSwitch;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // only re-creates the map if it's null
        if (this.ellipseDataPoints == null) {
            this.ellipseDataPoints = this.createCordsMap();

            // creates the buffered image
            this.heatmapImage = new BufferedImage(
                this.getWidth(), 
                this.getHeight(), 
                BufferedImage.TYPE_INT_ARGB
            );

            // creates graphics for buffered image
            Graphics2D mapGraphics = this.heatmapImage.createGraphics();
            this.drawHeatmap(mapGraphics);
            mapGraphics.dispose();
        }

        graphics.drawImage(this.heatmapImage, 0, 0, null);

        // draws the cross-hair that tells you the info on the heatmap
        this.crossHair((Graphics2D) graphics);
    }

    // creates the crosshair that tells you the longitude, latitude, and temperature on the heatmap
    private void crossHair(Graphics2D g) {
        final int OFFSET = 10;
        if (this.currentEllipseDataPoint != null) {
            if (this.useColor) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setStroke(new BasicStroke(3));
            Line2D horizontalAxis = new Line2D.Float(0, this.currentEllipseDataPoint.y, this.getWidth(), this.currentEllipseDataPoint.y);
            Line2D verticalAxis = new Line2D.Float(this.currentEllipseDataPoint.x, 0, this.currentEllipseDataPoint.x, this.getHeight());
            g.draw(horizontalAxis);
            g.draw(verticalAxis);

            g.setColor(Color.BLACK);
            g.drawString("Temperature: " + this.currentEllipseDataPoint.getDataPoint().getTemperature(), this.getWidth() - 200, this.getHeight() - OFFSET);
            g.drawString(String.valueOf(this.currentEllipseDataPoint.getDataPoint().getLongitude()), 0, this.currentEllipseDataPoint.y - OFFSET);
            g.drawString(String.valueOf(this.currentEllipseDataPoint.getDataPoint().getLatitude()), this.currentEllipseDataPoint.x + OFFSET, (2 * OFFSET));
        }
    }

    // method to draw the heatmap
    public void drawHeatmap(Graphics2D g) {
        float lowTemp = this.temperatureData.getMinTemp();
        float middleTemp = this.temperatureData.getMiddleTemp();
        float highTemp = this.temperatureData.getMaxTemp();

        int color;
        int maxColor = 255;

        Color currentColor;

        // goes through the ellipse data point list while producing the main color being used based on the temperature
        // and draws each ellipse
        for (EllipseDataPoint ellipseDataPoint : this.ellipseDataPoints) {
            float currentTemp = ellipseDataPoint.getDataPoint().getTemperature();
            if (this.useColor) {
                // for the half of the dataset that's the hottest
                if (currentTemp > middleTemp) {
                    color = this.rgbValueCalc(middleTemp, highTemp, maxColor, currentTemp);
                    currentColor = new Color(maxColor, color, 0);
                    // for the second half of the dataset that's the coolest
                } else {
                    color = this.rgbValueCalc(lowTemp, middleTemp, maxColor, currentTemp);
                    currentColor = new Color(0, color, maxColor);
                }
            } else {
                color = this.rgbValueCalc(lowTemp, highTemp, maxColor, currentTemp);
                currentColor = new Color(color, color, color);
            }

            g.setColor(currentColor);
            g.fill(ellipseDataPoint);
        }
    }

    // produces an array list that contains the ellipse data point objects which contains a datapoint attribute
    private ArrayList<EllipseDataPoint> createCordsMap() {

        // array list containing the ellipse
        ArrayList<EllipseDataPoint> ellipseDataPoints = new ArrayList<>();

        // gets the arraylist containing datapoints
        ArrayList<DataPoint> dataPoints = this.temperatureData.getTempData();

        // gets the min and max longitude values
        float minLong = this.temperatureData.getMinLongitude();
        float maxLong = this.temperatureData.getMaxLongitude();

        // gets the min and max latitude values
        float minLat = this.temperatureData.getMinLatitude();
        float maxLat = this.temperatureData.getMaxLatitude();

        int heatMapXEnd = this.getWidth();
        int heatMapYEnd = this.getHeight();

        // goes through all datapoints while creating the coordinates based on the min and max longitude and latitude values
        // and the current longitude and latitude values generated in the for loop
        for (DataPoint dataPoint : dataPoints) {
            float current = dataPoint.getLongitude();
            float percentageWidthX = (current - minLong) / (maxLong - minLong);
            float xCord = (heatMapXEnd * percentageWidthX);

            current = dataPoint.getLatitude();
            float percentageHeightY = (current - minLat) / (maxLat - minLat);
            float yCord = (heatMapYEnd - (heatMapYEnd * percentageHeightY));

            // creates an ellipse data point object and puts it in the array list
            int diameterPoint = 3;
            ellipseDataPoints.add(new EllipseDataPoint(xCord, yCord, diameterPoint, diameterPoint, dataPoint));
        }
        return ellipseDataPoints;
    }

    // calculates the rgb value a point on the heatmap
    public int rgbValueCalc(float lowTemp, float highTemp, float highColor, float currentTemp) {
        float percentageForHue = (highTemp - currentTemp) / (highTemp - lowTemp);
        return (int) (highColor * percentageForHue);
    }

    // action performed method that changes this.useColor to what isn't currently in use,
    // and changes the message on the button
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.colorSwitch) {
            this.useColor = !this.useColor;
            this.heatmapImage = null;
            this.repaint();
            if (Objects.equals(this.colorSwitch.getText(), "No Color")) {
                this.colorSwitch.setText("Color");
            } else {
                this.colorSwitch.setText("No Color");
            }
        }
    }

    // goes through each ellipse point while checking if it contains the mouses x and y positions
    // and prints the datapoint info to the console when an ellipse point is pressed as well as sets the current ellipse point
    @Override
    public void mousePressed(MouseEvent e) {
        // the top of the screen in the frame
        for (EllipseDataPoint ellipseDataPoint : this.ellipseDataPoints) {
            if (ellipseDataPoint.contains(e.getX(), e.getY())) {
                ellipseDataPoint.getDataPoint().showInfo();
                this.currentEllipseDataPoint = ellipseDataPoint;
                break;
            }
        }
        repaint();
    }

    // unused
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // unused
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // unused
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // unused
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
