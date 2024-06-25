package sql.ide.shapes;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
    private SquareMenu contextMenu;

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

    public SquareMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(SquareMenu contextMenu) {
        this.contextMenu = contextMenu;
    }
    /**************************************************************************/
    /************************ INTERFACE METHODS *******************************/
    /**************************************************************************/

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(125, 240, 255));
        gc.fillRect(x, y, size, size);

        // Draw the title text
        if (name != null && !name.isEmpty()) {
            gc.setFill(Color.BLACK); // Set the color for the text
            gc.fillText(name, x + size / 2 - (name.length() * 3), y - 10); // Draw the name text above the rectangle and center it
        }
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
}
