package sql.ide.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Relation implements Shape {
    // draw variables
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private static final double CLICK_TOLERANCE = 5; //? too hard to click a line so a little tolerance added

    // relation variables
    private Table startTable;
    private Table endTable;
    private String relationType;

    /**
     * Constructor
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public Relation(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**************************************************************************/
    /************************ GETTERS AND SETTERS *****************************/
    /**************************************************************************/

    //? shape getters and setters
    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    //? relation getters and setters
    public Table getStart() {
        return startTable;
    }

    public void setStart(Table startTable) {
        this.startTable = startTable;
    }

    public Table getEnd() {
        return endTable;
    }

    public void setEnd(Table endTable) {
        this.endTable = endTable;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
    
    /**************************************************************************/
    /************************ INTERFACE METHODS *******************************/
    /**************************************************************************/

    @Override
    public void draw(GraphicsContext gc) {
        // draw the line
        gc.setStroke(Color.RED);
        gc.strokeLine(startX, startY, endX, endY);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        // basically move the start and end points
        this.startX += deltaX;
        this.startY += deltaY;
        this.endX += deltaX;
        this.endY += deltaY;
    }

    @Override
    public boolean contains(double x, double y) {
        // check if the point is near the line (with near i mean CLICK_TOLERANCE value)
        double distance = pointLineDistance(startX, startY, endX, endY, x, y);
        return distance < CLICK_TOLERANCE;
    }

    /**************************************************************************/
    /****************************** METHODS ***********************************/
    /**************************************************************************/

    private double pointLineDistance(double x1, double y1, double x2, double y2, double px, double py) {
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) {
            param = dot / len_sq;
        }

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = px - xx;
        double dy = py - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
