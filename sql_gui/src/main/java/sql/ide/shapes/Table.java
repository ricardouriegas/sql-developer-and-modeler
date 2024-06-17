package sql.ide.shapes;

import java.util.HashMap;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Table implements Shape {
    // for shape
    private double x;
    private double y;
    private double size;

    // for table
    private String name;
    private HashMap<String, String> attributes;
    private Object primaryKey;
    private List<Relation> foreignKeys;

    public Table(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**************************************************************************/
    /************************ GETTERS AND SETTERS *****************************/
    /**************************************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
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
        return x >= this.x && x <= this.x + size
                &&
                y >= this.y && y <= this.y + size;
    }
}
