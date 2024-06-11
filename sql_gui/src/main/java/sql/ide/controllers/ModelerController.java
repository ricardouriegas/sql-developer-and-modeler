package sql.ide.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
// import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import sql.ide.shapes.*;

public class ModelerController {
    @FXML
    Canvas canva;

    private double initialX;
    private double initialY;
    private boolean dragging = false;
    private boolean drawingLine = false;
    private double lineStartX;
    private double lineStartY;
    private List<ShapeInterface> shapes = new ArrayList<>();
    private ShapeInterface selectedShape;

    private static final double SQUARE_SIZE = 50; // square size

    /**
     * Initialize function to everything that will run when the program
     * initilize
     */
    @FXML
    public void initialize() {
        // listeners
        canva.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
        canva.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canva.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canva.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
    }

    /**************************************************************************/
    /************************ Import and Export Functions ***********************/
    /**************************************************************************/
    public void importFile(ActionEvent event) {

    }

    public void exportFile(ActionEvent event) {

    }

    public void returnDeveloper(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sql/ide/fxml/SimpleFileEditor.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/sql/ide/css/highlight.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Modeler Editor");
        stage.setScene(scene);
        stage.show();

        // close window
        ((Stage) ((Pane) canva.getParent()).getScene().getWindow()).close();

    }

    /**************************************************************************/
    /**************************** Help Functions ********************************/
    /**************************************************************************/
    public void usageWindow(ActionEvent event) {
        // open usage window
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Usage");
        alert.setHeaderText("Usage");
        alert.setContentText("This is a simple usage window");

        alert.showAndWait();
    }

    public void aboutWindow(ActionEvent event) {
        // open about window
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("This is a simple about window");

        alert.showAndWait();
    }

    /**************************************************************************/
    /****************************** Handle Mouse ********************************/
    /**************************************************************************/
    private void handleMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double x = event.getX();
            double y = event.getY();
            ModelerTable square = new ModelerTable(x - SQUARE_SIZE / 2, y - SQUARE_SIZE / 2, SQUARE_SIZE);
            shapes.add(square);
            drawShapes();
        } else {
            double x = event.getX();
            double y = event.getY();
            if (drawingLine) {
                ModelerLine line = (ModelerLine) selectedShape;
                line.setEndX(x);
                line.setEndY(y);
                drawingLine = false;
            } else {
                lineStartX = x;
                lineStartY = y;
                ModelerLine line = new ModelerLine(lineStartX, lineStartY, lineStartX, lineStartY);
                shapes.add(line);
                selectedShape = line;
                drawingLine = true;
            }
            drawShapes();
        }
    }

    private void handleMousePressed(MouseEvent event) {
        selectedShape = getShapeAt(event.getX(), event.getY());
        if (selectedShape != null) {
            initialX = event.getX();
            initialY = event.getY();
            dragging = true;
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (dragging && selectedShape != null) {
            double deltaX = event.getX() - initialX;
            double deltaY = event.getY() - initialY;
            selectedShape.move(deltaX, deltaY);
            initialX = event.getX();
            initialY = event.getY();
            drawShapes();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        dragging = false;
    }

    private void drawShapes() {
        GraphicsContext gc = canva.getGraphicsContext2D();
        gc.clearRect(0, 0, canva.getWidth(), canva.getHeight());
        for (ShapeInterface shape : shapes) {
            shape.draw(gc);
        }
    }

    private ShapeInterface getShapeAt(double x, double y) {
        for (ShapeInterface shape : shapes) {
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    /**************************************************************************/
    /************************** Shapes Functions ********************************/
    /**************************************************************************/
    public void addRectangle(ActionEvent event) {

    }

    public void addCircle(ActionEvent event) {

    }

    public void addLine(ActionEvent event) {

    }

    /**************************************************************************/
    /********************************** Exit ************************************/
    /**************************************************************************/
    /**
     * Exit Application
     * 
     * @param event
     */
    public void exitApplication(ActionEvent event) {
        System.exit(0);
    }

}