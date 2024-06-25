package sql.ide.shapes;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sql.ide.shapes.relation_utilities.Relation;
import sql.ide.shapes.table_utilities.SquareMenu;

public class Table implements Shape {
    // shape variables
    private double x;
    private double y;
    private double size;

    // table variables
    private String name;
    private List<String> attributes;
    private Object primaryKey;
    private List<Relation> foreignKeys;
    private final SquareMenu contextMenu = new SquareMenu(this);

    /**
     * Constructor
     * 
     * @param x
     * @param y
     * @param size
     */
    public Table(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
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

    /**************************************************************************/
    /************************ GETTERS AND SETTERS *****************************/
    /**************************************************************************/
    
    //? shape getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    //? table getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Relation> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Relation> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    /**************************************************************************/
    /************************ INTERFACE METHODS *******************************/
    /**************************************************************************/

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(125, 240, 255));
        gc.fillRect(x, y, size, size);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    @Override
    public boolean contains(double x, double y) {
        // check if the point is inside the table
        return x >= this.x && x <= this.x + size
                &&
                y >= this.y && y <= this.y + size;
    }

    /**************************************************************************/
    /************************ OTHER METHODS *******************************/
    /**************************************************************************/
    public void deleteShapeFromController(){
        //TODO: WIP
    }
}
