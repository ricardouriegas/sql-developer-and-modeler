package sql.ide.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sql.ide.shapes.relation_utilities.LineMenu;

public class Relation implements Shape {
    // draw variables
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private static final double CLICK_TOLERANCE = 5; //? too hard to click a line so a little tolerance added

    // relation variables
    private final Table startTable;
    private final Table endTable;
    private String relationType;

    private LineMenu contextMenu;

    /**
     * Constructor
     * 
     * @param startTable
     * @param endTable
     */
    public Relation(Table startTable, Table endTable) {
        this.startTable = startTable;
        this.endTable = endTable;
        this.startX = startTable.getX();
        this.startY = startTable.getY();
        this.endX = endTable.getX();
        this.endY = endTable.getY();
        }

    /**
     * Method to open the context menu
     */
    public void openMenu() {
        contextMenu.show();
    }

    /**
     * Method to close the context menu
     */
    public void closeMenu() {
        contextMenu.hide();
    }

    public void setContextMenu(LineMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    /**************************************************************************/
    /************************ GETTERS AND SETTERS *****************************/
    /**************************************************************************/

    //? shape getters and setters
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartX(double deltaX) {
        this.startX = deltaX;
    }

    public void setStartY(double deltaY) {
        this.startY = deltaY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndX(double deltaX) {
        this.endX = deltaX;
    }

    public void setEndY(double deltaY) {
        this.endY = deltaY;
    }

    //? relation getters and setters
    public Table getStartTable() {
        return startTable;
    }

    public Table getEndTable() {
        return endTable;
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
