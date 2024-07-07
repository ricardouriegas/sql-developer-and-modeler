package sql.ide.shapes.table_utilities;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sql.ide.controllers.ModelerController;
import sql.ide.shapes.Table;

import java.util.ArrayList;
import java.util.List;

public class SquareMenu {
    private Table table; // Reference obtained table, all changes in this instance of table also affects the original one
    private ModelerController modelerController; // Reference to the modeler controller

    private Stage stage; // Main stage
    private VBox optionsBox; // VBox for the options of the editor (General, Attributes, Relationships)

    // General menu variables
    private VBox generalBox; // VBox for General menu
    private BorderPane generalLayout;
    private TextField tableNameField; // Field for the name of the table
    // End of General menu variables

    // Attributes menu
    private TableView<Attribute> attributesTable; // Table for the attributes (number, name, data type)
    private TextField nameField; // Field for the name of the attribute
    private ComboBox<String> dataTypeComboBox; // ComboBox for the data type of the attribute
    private TextField lengthField; // Field for the length of the attribute
    private CheckBox primaryUIDCheckBox; // CheckBox for the primary UID of the attribute
    private CheckBox mandatoryCheckBox; // CheckBox for the mandatory of the attribute
    private Button addButton; // Button to add an attribute
    private Button editButton; // Button to edit an attribute
    private Button deleteButton; // Button to delete an attribute
    private VBox rightBox; // VBox for attribute properties
    private VBox attributesBox; // VBox for attribute table and buttons
    private BorderPane attributesLayout; // Layout for attributes menu
    // End of attributes menu

    private BorderPane mainLayout; // Main layout of the editor

    /**
     * Constructor
     * @param table Table to edit
     * @param modelerController Modeler controller instance
      */
    public SquareMenu(Table table, ModelerController modelerController) {
        this.table = table;
        this.modelerController = modelerController;

        stage = new Stage();
        stage.setTitle("Table editor");

        // Set the stage to be a modal window so the user can't interact with the main window
        stage.initModality(Modality.APPLICATION_MODAL);

        // Main layout
        mainLayout = new BorderPane();

        // Options box (General, Attributes, Relationships)
        optionsBox = new VBox(10);
        optionsBox.setPrefWidth(150);
        optionsBox.setPadding(new Insets(10));

        // Toggle buttons for the options (General, Attributes, Relationships)
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton generalButton = new ToggleButton("General");
        ToggleButton attributesButton = new ToggleButton("Attributes");

        generalButton.setToggleGroup(toggleGroup);
        attributesButton.setToggleGroup(toggleGroup);

        // Set actions for the buttons to show the corresponding menu
        generalButton.setOnAction(event -> showGeneralMenu());
        attributesButton.setOnAction(event -> showAttributesMenu());

        // Add buttons to the options box
        optionsBox.getChildren().addAll(generalButton, attributesButton);

        // Initialize components for each menu
        initializeGeneralMenu();
        initializeAttributesMenu();

        // Add elements to the main layout
        mainLayout.setLeft(optionsBox);
        showGeneralMenu(); // Show general menu by default

        Scene scene = new Scene(mainLayout, 800, 400);
        stage.setScene(scene);
    }

    /**
     * Initialize the components for the General Menu
     */
    private void initializeGeneralMenu() {
        Label warningLabel = new Label();
        warningLabel.setTextFill(Color.color(1,0,0));
        warningLabel.setVisible(false);

        Label nameLabel = new Label("Table Name:"); // A simple label
        tableNameField = new TextField(); // Create a new TextField for the name of the table
        tableNameField.setText(table.getName()); // Setting a default text

        Button deleteTableButton = new Button("Delete Table");
        deleteTableButton.setOnAction(event -> deleteTable()); // Set an action for the delete button

        // Add a listener to the table name field to update the table name
        tableNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean validName = true;

            // if the name is left empy
            if(newValue == null || newValue.isEmpty()) {
                warningLabel.setText("Table Name cannot be empty");
                warningLabel.setVisible(true);
                validName = false;
            }

            ArrayList<String> tableNames = new ArrayList<>();
            for (Table table : modelerController.getTableShapes()){
                if(table == this.table){
                    continue;
                }
                tableNames.add(table.getName());
            }

            // if the name is occupied
            if(tableNames.contains(newValue)){
                warningLabel.setText("Table Name already exists");
                warningLabel.setVisible(true);
                validName = false;
            }

            // if the name contains '/' (forbidden printable ascii character in filenames in linux)
            if(newValue != null && newValue.contains("/")){
                warningLabel.setText("Table Name can't contain <</>>");
                warningLabel.setVisible(true);
                validName = false;
            }

            if(validName){
                table.setName(newValue);
                warningLabel.setVisible(false);
            }

            modelerController.drawShapes();
        });

        generalBox = new VBox(10);
        generalBox.setPrefWidth(150);
        generalBox.setPadding(new Insets(10));
        generalBox.getChildren().addAll(warningLabel, nameLabel, tableNameField, deleteTableButton);

        generalLayout = new BorderPane();
        generalLayout.setLeft(generalBox);

        stage.setOnCloseRequest(event -> {
            tableNameField.setText(table.getName());
        });
    }

    /**
     * Initialize the components for the Attributes menu
     */
    @SuppressWarnings("unchecked")
    private void initializeAttributesMenu() {
        nameField = new TextField(); // Create a new TextField for the name of the attribute
        nameField.setPromptText("Name"); // Set a placeholder text for the field
    
        // Create a ComboBox for the data type of the attribute
        dataTypeComboBox = new ComboBox<>();
        dataTypeComboBox.getItems().addAll("Varchar", "Integer", "Float", "Double"); // ! Data types
        dataTypeComboBox.setPromptText("Select Data Type"); // Set a placeholder text for the ComboBox
        dataTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;
    
            // ? Show the length field if the data type is Double, Float, Integer or Varchar
            if (newValue.equals("Double") || newValue.equals("Float") || newValue.equals("Varchar")) {
                lengthField.setVisible(true);
            } else {
                lengthField.setVisible(false);
            }
        });
    
        // Create a TextField for the length of the attribute
        lengthField = new TextField();
        lengthField.setPromptText("Length");
        lengthField.setVisible(false); // Hide the length field by default until a data type is selected
    
        primaryUIDCheckBox = new CheckBox("Primary UID"); // Create a CheckBox for the primary UID of the attribute
        mandatoryCheckBox = new CheckBox("Mandatory"); // Create a CheckBox for the mandatory of the attribute
    
        // Add a listener to the primaryUIDCheckBox
        primaryUIDCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            //? If the primaryUIDCheckBox is selected, disable the mandatoryCheckBox
            if (newValue) {
                mandatoryCheckBox.setSelected(true); // Set the mandatoryCheckBox to true
                mandatoryCheckBox.setDisable(true); // Disable the mandatoryCheckBox
            } else {
                mandatoryCheckBox.setDisable(false); // Enable the mandatoryCheckBox if the primaryUIDCheckBox is not selected
            }
        });
    
        rightBox = new VBox(10);
        rightBox.setPrefWidth(300);
        rightBox.setPadding(new Insets(10));
        rightBox.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Data Type:"), dataTypeComboBox,
                lengthField,
                primaryUIDCheckBox, mandatoryCheckBox);
    
        // Create a TableView for the attributes
        attributesTable = new TableView<>();
    
        // * Create columns for the attributes table
    
        TableColumn<Attribute, Integer> numberColumn = new TableColumn<>("#"); // Create a column for the number of the attribute
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number")); 
    
        TableColumn<Attribute, String> nameColumn = new TableColumn<>("Name"); // Create a column for the name of the attribute
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    
        TableColumn<Attribute, String> dataTypeColumn = new TableColumn<>("Data Type"); // Create a column for the data type of the attribute
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("dataType"));
    
        TableColumn<Attribute, Boolean> mandatoryColumn = new TableColumn<>("Mandatory"); // Create a column for the mandatory of the attribute
        mandatoryColumn.setCellValueFactory(new PropertyValueFactory<>("mandatory"));
    
        TableColumn<Attribute, Boolean> primaryKeyColumn = new TableColumn<>("Primary Key"); // Create a column for the primary key of the attribute
        primaryKeyColumn.setCellValueFactory(new PropertyValueFactory<>("primaryKey"));
    
        attributesTable.getColumns().addAll(numberColumn, nameColumn, dataTypeColumn, mandatoryColumn, primaryKeyColumn); // Add the columns to the table
    
        // Create buttons for adding, editing and deleting attributes
        addButton = new Button("Add"); 
        editButton = new Button("Edit");
        deleteButton = new Button("Delete");
    
        // Set actions for the buttons
        addButton.setOnAction(event -> addAttribute()); 
        editButton.setOnAction(event -> editAttribute());
        deleteButton.setOnAction(event -> deleteAttribute());
    
        // Create an HBox for the buttons
        HBox buttonBox = new HBox(5, addButton, editButton, deleteButton);
        attributesBox = new VBox(5, attributesTable, buttonBox);
        attributesBox.setPrefWidth(250);
    
        // Create a BorderPane for the attributes menu
        attributesLayout = new BorderPane();
        attributesLayout.setLeft(rightBox);
        attributesLayout.setCenter(attributesBox);
    
        // Add a listener to the table to load the selected attribute
        attributesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAttribute(newSelection);
            } else {
                clearAttributeFields();
            }
        });
    }

    /**
     * Show the General menu
      */
    public void showGeneralMenu() {
        mainLayout.setCenter(generalBox);
    }

    /**
     * Show the Attributes menu
      */
    public void showAttributesMenu() {
        mainLayout.setCenter(attributesLayout);
    }

    /**
     * Add an attribute to the table
      */
    private void addAttribute() {
        String name = nameField.getText();
        String dataType = dataTypeComboBox.getValue();
        boolean isMandatory = mandatoryCheckBox.isSelected();
        boolean isPrimaryKey = primaryUIDCheckBox.isSelected();
        String length = lengthField.getText();

        if (name.isEmpty() || dataType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name and Data Type are required.");
            alert.showAndWait();
            return;
        }

        String fullDataType = dataType;
        if (!length.isEmpty() && lengthField.isVisible()) {
            fullDataType += "(" + length + ")";
        }

        int maxNumber = attributesTable.getItems().stream()
                .mapToInt(Attribute::getNumber)
                .max()
                .orElse(0);
        int number = maxNumber + 1;

        Attribute newAttribute = new Attribute(number, name, fullDataType, isMandatory, isPrimaryKey);
        attributesTable.getItems().add(newAttribute);
        table.addAttribute(newAttribute);
        
        // Clear the input fields after adding
        clearAttributeFields();
    }

    /**
     * Delete the selected attribute from the table
      */
    private void deleteAttribute() {
        Attribute selectedAttribute = attributesTable.getSelectionModel().getSelectedItem();
        if (selectedAttribute != null) {
            attributesTable.getItems().remove(selectedAttribute); // remove from the gui table
            table.removeAttribute(selectedAttribute); // remove from the table object itself
            reindexAttributes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No attribute selected for deletion.");
            alert.showAndWait();
        }
    }

    /**
     * Edit the selected attribute in the table
      */
    private void editAttribute() {
        Attribute selectedAttribute = attributesTable.getSelectionModel().getSelectedItem();
        if (selectedAttribute == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No attribute selected for editing.");
            alert.showAndWait();
            return;
        }

        String name = nameField.getText();
        String dataType = dataTypeComboBox.getValue();
        boolean isMandatory = mandatoryCheckBox.isSelected();
        boolean isPrimaryKey = primaryUIDCheckBox.isSelected();
        String length = lengthField.getText();

        if (name.isEmpty() || dataType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name and Data Type are required.");
            alert.showAndWait();
            return;
        }

        String fullDataType = dataType;
        if (!length.isEmpty() && lengthField.isVisible()) {
            fullDataType += "(" + length + ")";
        }

        // Update the attribute in the table object
        table.getAttribute(selectedAttribute).setName(name);
        table.getAttribute(selectedAttribute).setDataType(fullDataType);
        table.getAttribute(selectedAttribute).setMandatory(isMandatory);
        table.getAttribute(selectedAttribute).setPrimaryKey(isPrimaryKey);

        // Update the attribute in the GUI table
        selectedAttribute.setName(name);
        selectedAttribute.setDataType(fullDataType);
        selectedAttribute.setMandatory(isMandatory);
        selectedAttribute.setPrimaryKey(isPrimaryKey);

        attributesTable.refresh();
        clearAttributeFields();
    }

    private void reindexAttributes() {
        int index = 1;
        for (Attribute attribute : attributesTable.getItems()) {
            attribute.setNumber(index);
            table.getAttribute(attribute).setNumber(index);
            index++;
        }
        attributesTable.refresh();
    }

    /**
     * Load the selected attribute into the input fields
     * @param attribute
     */
    private void loadAttribute(Attribute attribute) {
        nameField.setText(attribute.getName());
        String dataType = attribute.getDataType();
        if (dataType.contains("(")) {
            int start = dataType.indexOf("(");
            int end = dataType.indexOf(")");
            String length = dataType.substring(start + 1, end);
            dataType = dataType.substring(0, start);
            lengthField.setText(length);
        }
        dataTypeComboBox.setValue(dataType);
        primaryUIDCheckBox.setSelected(attribute.isPrimaryKey());
        mandatoryCheckBox.setSelected(attribute.isMandatory());
    }

    /**
     * Clear the input fields for the attributes
      */
    private void clearAttributeFields() {
        nameField.clear();
        dataTypeComboBox.getSelectionModel().clearSelection();
        lengthField.clear();
        lengthField.setVisible(false);
        primaryUIDCheckBox.setSelected(false);
        mandatoryCheckBox.setSelected(false);
    }

    /**
     * Delete the table from the modeler
     * This method will show a confirmation dialog before deleting the table
     * If the user confirms the deletion, the table will be removed from the modeler 
      */
    private void deleteTable() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Table");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this table?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            modelerController.deleteShape(table);
            stage.close();
        }
    }

    /**
     * Method to import settings from a table
     * @param name
     * @param attributes
      */
    public void importSettings(Table table){
        List<Attribute> attributes = table.getAttributes();
        for(Attribute attribute : attributes)
            attributesTable.getItems().add(attribute);
        
        tableNameField.setText(table.getName());
    }

    /**
     * Method to show the editor
      */
    public void show() {
        stage.show();
    }

    /**
     * Method to hide the editor
      */
    public void hide() {
        stage.hide();
    }
}