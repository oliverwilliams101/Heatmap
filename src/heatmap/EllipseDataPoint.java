package heatmap;

import java.awt.geom.Ellipse2D;

// inherits from Ellipse2D.Float
// used to allow a datapoint as a parameter
public class EllipseDataPoint extends Ellipse2D.Float {
    private final DataPoint dataPoint;

    public EllipseDataPoint(float x, float y, float width, float height, DataPoint dataPoint) {
        super(x, y, width, height);
        this.dataPoint = dataPoint;
    }

    // gets the datapoint variable
    public DataPoint getDataPoint() {
        return this.dataPoint;
    }
}
