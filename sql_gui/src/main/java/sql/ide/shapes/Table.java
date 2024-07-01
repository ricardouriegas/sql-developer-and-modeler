package sql.ide.shapes;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sql.ide.shapes.table_utilities.SquareMenu;
import sql.ide.shapes.table_utilities.Attribute;

public class Table implements Shape {
    // shape variables
    private double x;
    private double y;
    private double size;

    // table variables
    private String name;
    private List<Attribute> attributes = new ArrayList<>();
    private List<Relation> foreignKeys; // List of foreign keys
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
        foreignKeys = new java.util.ArrayList<>();
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

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
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

    /**************************************************************************/
    public void addRelation(Relation relation) {
        foreignKeys.add(relation);
    }

    /************************ ATTRIBUTE METHODS *******************************/
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void removeAttribute(Attribute attribute) {
        attributes.remove(attribute);
    }

    public Attribute getAttribute(Attribute attribute) {
        return attributes.get(attributes.indexOf(attribute));
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
        // update the positions of the table
        this.x += deltaX;
        this.y += deltaY;

        // update the position of every relationship
        for(Relation relation : foreignKeys){
            if(relation.getStartTable() == this){
                // update the line that connects to the table
                relation.setStartX(relation.getStartX() + deltaX);
                relation.setStartY(relation.getStartY() + deltaY);
            }

            if(relation.getEndTable() == this){
                relation.setEndX(relation.getEndX() + deltaX);
                relation.setEndY(relation.getEndY() + deltaY);
            }
        }
    }

    @Override
    public boolean contains(double x, double y) {
        // check if the point is inside the table
        return x >= this.x && x <= this.x + size
                &&
                y >= this.y && y <= this.y + size;
    }


    /**************************************************************************/
    /************************ EXPORT & IMPORT ********************************/
    /**************************************************************************/

    public String export(){
        String export = "CREATE TABLE " + name + " (\n"; // SQL code to create the table

        if (attributes.isEmpty()) // We cannot create a table without attributes
            return "";
        
        // Iterate over the attributes to create the columns
        for (Attribute attribute : attributes) {
            export += "\t" + attribute.getName() + " " + attribute.getDataType(); // Add the name and data type of the attribute
            if (attribute.isPrimaryKey()) // If the attribute is a primary key, add PRIMARY KEY
                export += " PRIMARY KEY";
            
            if (attribute.isMandatory()) // If the attribute is mandatory, add NOT NULL
                export += " NOT NULL";
            
            export += ",\n";
        }

        export = export.substring(0, export.length() - 2) + "\n); \n\n" ; // Remove the last comma and add the closing parenthesis
        return export;
    }
}
