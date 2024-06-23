package sql.ide.shapes.table_utilities;

/**
 * This class represents an attribute of a table.
 * It contains the attribute number, name, data type, and whether it is mandatory or a primary key.
  */
public class Attribute {
    private int number; // The number of the attribute in the table
    private String name; // The name of the attribute
    private String dataType; // The data type of the attribute
    private boolean isMandatory; // Whether the attribute is mandatory
    private boolean isPrimaryKey; // Whether the attribute is a primary key

    public Attribute(int number, String name, String dataType, boolean isMandatory, boolean isPrimaryKey) {
        this.number = number;
        this.name = name;
        this.dataType = dataType;
        this.isMandatory = isMandatory;
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * Setters and getters for the attribute's properties.
      */
    
    // Setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    // Getters
    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
}
