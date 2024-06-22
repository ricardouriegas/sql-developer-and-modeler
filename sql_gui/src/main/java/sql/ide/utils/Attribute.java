package sql.ide.utils;

public class Attribute {
    private int number;
    private String name;
    private String dataType;
    private boolean isMandatory;
    private boolean isPrimaryKey;

    public Attribute(int number, String name, String dataType, boolean isMandatory, boolean isPrimaryKey) {
        this.number = number;
        this.name = name;
        this.dataType = dataType;
        this.isMandatory = isMandatory;
        this.isPrimaryKey = isPrimaryKey;
    }

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
