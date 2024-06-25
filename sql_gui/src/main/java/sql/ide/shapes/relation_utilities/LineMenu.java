package sql.ide.shapes.relation_utilities;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LineMenu {
    Relation relation; // Reference obtained relation, all changes of this instance of relation also affect the original

    private final Stage stage = new Stage();
    private final VBox relationBox = new VBox();
    private final BorderPane mainLayout = new BorderPane();
    private ComboBox<String> originCardinalityComboBox;
    private CheckBox optionalOriginCheckBox;
    private ComboBox<String> destinationCardinalityComboBox;
    private CheckBox optionalDestinationCheckBox;

    /**
     * Constructor
     *
     * @param relation
     */
    public LineMenu(Relation relation) {
        this.relation = relation;
        stage.setTitle("Relation Properties");

        initializePropertiesMenu();
        showPropertiesMenu();

        Scene scene = new Scene(mainLayout, 500, 300);
        stage.setScene(scene);
    }

    /**
     * Initialize the components for the menu
     */
    private void initializePropertiesMenu(){
        BorderPane borderPane = new BorderPane();

        // Main VBOX
        VBox vbox = new VBox(10);
        vbox.setPrefWidth(150);
        vbox.setPadding(new Insets(10));
        borderPane.setCenter(vbox);

        // Origin Cardinality Section
        GridPane originGrid = new GridPane();
        originGrid.setHgap(10);
        originGrid.setVgap(5);

        //Label originTableName = new Label("Origin Table:" + relation.getStartTable().getName());
        originCardinalityComboBox = new ComboBox<>();
        originCardinalityComboBox.getItems().addAll("1", "N");
        optionalOriginCheckBox = new CheckBox("Optional Origin");

        //originGrid.add(originTableName, 0, 0);
        originGrid.add(new Label("Cardinality from Origin to Destination:"), 0, 4);
        originGrid.add(originCardinalityComboBox, 1, 4);
        originGrid.add(optionalOriginCheckBox, 1, 5);

        // Section separator
        Separator separator = new Separator();
        originGrid.add(separator, 5, 5);

        // Destination Cardinality Section
        GridPane destinationGrid = new GridPane();
        destinationGrid.setHgap(10);
        destinationGrid.setVgap(5);

        //Label destinationTableName = new Label("Destination Table:" + relation.getEndTable().getName());
        destinationCardinalityComboBox = new ComboBox<>();
        destinationCardinalityComboBox.getItems().addAll("1", "N");
        optionalDestinationCheckBox = new CheckBox("Optional Destination");

        //destinationGrid.add(destinationTableName, 0, 0);
        destinationGrid.add(new Label("Cardinality from Destination to Origin:"), 0, 4);
        destinationGrid.add(destinationCardinalityComboBox, 1, 4);
        destinationGrid.add(optionalDestinationCheckBox, 1, 5);

        vbox.getChildren().addAll(originGrid, destinationGrid);
        relationBox.getChildren().add(vbox);

        // Apply Changes when user closes window
        stage.setOnCloseRequest(event -> {
            verifyConfiguration(event);
        });
    }

    /**
     * Show the properties menu
     */
    private void showPropertiesMenu(){
        mainLayout.setCenter(relationBox);
    }

    /**
     * Verifies the configuration of the relation
     */
    private void verifyConfiguration(Event event){
        String originCardinality = originCardinalityComboBox.getValue();
        String destinationCardinality = destinationCardinalityComboBox.getValue();
        boolean optionalOrigin = optionalOriginCheckBox.isSelected();
        boolean optionalDestination = optionalDestinationCheckBox.isSelected();

        if(originCardinality == null || destinationCardinality == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Origin and Destination Cardinality Required.");
            alert.showAndWait();
            event.consume();
            return;
        }

        if(!optionalOrigin && !optionalDestination){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("A relationship cannot be obligatory on both sides.");
            alert.showAndWait();
            event.consume();
        }
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }
}
