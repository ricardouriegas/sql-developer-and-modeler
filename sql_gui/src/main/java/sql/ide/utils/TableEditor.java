package sql.ide.utils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TableEditor {
    private Stage stage; // Main stage
    private VBox optionsBox; // VBox for the options of the editor (General, Attributes, Relationships)

    // Attributes menu
    private TableView<Attribute> attributesTable; // Table for the attributes (number, name, data type)
    private TextField nameField; // Field for the name of the attribute
    private ComboBox<String> dataTypeComboBox; // ComboBox for the data type of the attribute
    private TextField lengthField; // Field for the length of the attribute
    private CheckBox primaryUIDCheckBox; // CheckBox for the primary UID of the attribute
    private CheckBox mandatoryCheckBox; // CheckBox for the mandatory of the attribute
    private Button addButton; // Button to add an attribute
    private Button deleteButton; // Button to delete an attribute
    private VBox rightBox; // VBox for attribute properties
    private VBox attributesBox; // VBox for attribute table and buttons
    private BorderPane attributesLayout; // Layout for attributes menu
    // End of attributes menu

    private VBox generalBox; // VBox for General menu
    private VBox relationshipsBox; // VBox for Relationships menu

    private BorderPane mainLayout; // Main layout of the editor

    public TableEditor() {
        stage = new Stage();
        stage.setTitle("Table editor");

        // Main layout
        mainLayout = new BorderPane();

        // Options box
        optionsBox = new VBox(10);
        optionsBox.setPrefWidth(150);
        optionsBox.setPadding(new Insets(10));

        // Toggle buttons for the options
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton generalButton = new ToggleButton("General");
        ToggleButton attributesButton = new ToggleButton("Attributes");
        ToggleButton relationshipsButton = new ToggleButton("Relationships");

        generalButton.setToggleGroup(toggleGroup);
        attributesButton.setToggleGroup(toggleGroup);
        relationshipsButton.setToggleGroup(toggleGroup);

        generalButton.setOnAction(event -> showGeneralMenu());
        attributesButton.setOnAction(event -> showAttributesMenu());
        relationshipsButton.setOnAction(event -> showRelationshipsMenu());

        optionsBox.getChildren().addAll(generalButton, attributesButton, relationshipsButton);

        // Initialize components for each menu
        initializeGeneralMenu();
        initializeAttributesMenu();
        initializeRelationshipsMenu();

        // Add elements to the main layout
        mainLayout.setLeft(optionsBox);
        showGeneralMenu(); // Show general menu by default

        Scene scene = new Scene(mainLayout, 800, 400);
        stage.setScene(scene);
    }

    private void initializeGeneralMenu() {
        generalBox = new VBox();
        generalBox.setPadding(new Insets(10));
        generalBox.setMaxWidth(300);
        // TODO: Add elements for the General menu
    }

    private void initializeAttributesMenu() {
        nameField = new TextField();
        nameField.setPromptText("Name");

        dataTypeComboBox = new ComboBox<>();
        dataTypeComboBox.getItems().addAll("Varchar", "Integer", "Float", "Boolean", "Double");
        dataTypeComboBox.setPromptText("Select Data Type");
        dataTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;

            if (newValue.equals("Double") || newValue.equals("Float") || newValue.equals("Integer")
                    || newValue.equals("Varchar")) {
                lengthField.setVisible(true);
            } else {
                lengthField.setVisible(false);
            }
        });

        lengthField = new TextField();
        lengthField.setPromptText("Length");
        lengthField.setVisible(false);

        primaryUIDCheckBox = new CheckBox("Primary UID");
        mandatoryCheckBox = new CheckBox("Mandatory");

        rightBox = new VBox(10);
        rightBox.setPrefWidth(300);
        rightBox.setPadding(new Insets(10));
        rightBox.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Data Type:"), dataTypeComboBox,
                lengthField,
                primaryUIDCheckBox, mandatoryCheckBox);

        attributesTable = new TableView<>();

        TableColumn<Attribute, Integer> numberColumn = new TableColumn<>("#");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Attribute, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Attribute, String> dataTypeColumn = new TableColumn<>("Data Type");
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("dataType"));

        TableColumn<Attribute, Boolean> mandatoryColumn = new TableColumn<>("Mandatory");
        mandatoryColumn.setCellValueFactory(new PropertyValueFactory<>("mandatory"));

        TableColumn<Attribute, Boolean> primaryKeyColumn = new TableColumn<>("Primary Key");
        primaryKeyColumn.setCellValueFactory(new PropertyValueFactory<>("primaryKey"));

        attributesTable.getColumns().addAll(numberColumn, nameColumn, dataTypeColumn, mandatoryColumn,
                primaryKeyColumn);

        addButton = new Button("Add");
        deleteButton = new Button("Delete");

        addButton.setOnAction(event -> addAttribute());
        deleteButton.setOnAction(event -> deleteAttribute());

        HBox buttonBox = new HBox(5, addButton, deleteButton);
        attributesBox = new VBox(5, attributesTable, buttonBox);
        attributesBox.setPrefWidth(250);

        attributesLayout = new BorderPane();
        attributesLayout.setLeft(rightBox);
        attributesLayout.setCenter(attributesBox);

        attributesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAttribute(newSelection);
            } else {
                clearAttributeFields();
            }
        });

        // Listeners to update attribute dynamically
        nameField.textProperty().addListener((obs, oldText, newText) -> updateSelectedAttribute());
        dataTypeComboBox.valueProperty().addListener((obs, oldType, newType) -> updateSelectedAttribute());
        lengthField.textProperty().addListener((obs, oldText, newText) -> updateSelectedAttribute());
        primaryUIDCheckBox.selectedProperty().addListener((obs, oldSelected, newSelected) -> updateSelectedAttribute());
        mandatoryCheckBox.selectedProperty().addListener((obs, oldSelected, newSelected) -> updateSelectedAttribute());
    }

    private void initializeRelationshipsMenu() {
        relationshipsBox = new VBox();
        relationshipsBox.setPadding(new Insets(10));
        relationshipsBox.setMaxWidth(300);
        // TODO: Add elements for the Relationships menu
    }

    public void showGeneralMenu() {
        mainLayout.setCenter(generalBox);
    }

    public void showAttributesMenu() {
        mainLayout.setCenter(attributesLayout);
    }

    public void showRelationshipsMenu() {
        mainLayout.setCenter(relationshipsBox);
    }

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

        // Clear the input fields after adding
        clearAttributeFields();
    }

    private void deleteAttribute() {
        Attribute selectedAttribute = attributesTable.getSelectionModel().getSelectedItem();
        if (selectedAttribute != null) {
            attributesTable.getItems().remove(selectedAttribute);
            reindexAttributes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No attribute selected for deletion.");
            alert.showAndWait();
        }
    }

    private void reindexAttributes() {
        int index = 1;
        for (Attribute attribute : attributesTable.getItems()) {
            attribute.setNumber(index++);
        }
        attributesTable.refresh();
    }

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

    private void clearAttributeFields() {
        nameField.clear();
        dataTypeComboBox.getSelectionModel().clearSelection();
        lengthField.clear();
        lengthField.setVisible(false);
        primaryUIDCheckBox.setSelected(false);
        mandatoryCheckBox.setSelected(false);
    }

    private void updateSelectedAttribute() {
        Attribute selectedAttribute = attributesTable.getSelectionModel().getSelectedItem();
        if (selectedAttribute != null) {
            String name = nameField.getText();
            String dataType = dataTypeComboBox.getValue();
            boolean isMandatory = mandatoryCheckBox.isSelected();
            boolean isPrimaryKey = primaryUIDCheckBox.isSelected();
            String length = lengthField.getText();

            if (name.isEmpty() || dataType == null) {
                return;
            }

            String fullDataType = dataType;
            if (!length.isEmpty() && lengthField.isVisible()) {
                fullDataType += "(" + length + ")";
            }

            selectedAttribute.setName(name);
            selectedAttribute.setDataType(fullDataType);
            selectedAttribute.setMandatory(isMandatory);
            selectedAttribute.setPrimaryKey(isPrimaryKey);

            attributesTable.refresh();
        }
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }
}
