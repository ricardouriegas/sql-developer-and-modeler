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
import javafx.stage.Stage;
import sql.ide.shapes.Relation;
import sql.ide.shapes.Shape;
import sql.ide.shapes.Table;

public class ModelerController {
    @FXML
    private Canvas canva;

    private double initialX;
    private double initialY;
    private boolean dragging = false;
    private boolean drawingLine = false;
    private double lineStartX;
    private double lineStartY;
    private List<Shape> shapes = new ArrayList<>();
    private Shape selectedShape;

    private static final double SQUARE_SIZE = 50; // square size

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // listeners
        canva.addEventHandler(MouseEvent.MOUSE_CLICKED, arg0 -> {
            try {
                handleMouseClicked(arg0);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        canva.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canva.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canva.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);

        // bind canvas size to the stage size
        Pane parent = (Pane) canva.getParent();
        canva.widthProperty().bind(parent.widthProperty());
        canva.heightProperty().bind(parent.heightProperty());

        // redraw shapes when the canvas size changes
        canva.widthProperty().addListener(evt -> drawShapes());
        canva.heightProperty().addListener(evt -> drawShapes());

    }

    /**************************************************************************/
    /********************** Import and Export Functions ***********************/
    /**************************************************************************/
    /**
     * Import the file (import the model from a sql file)
     * 
     * @param event
     */
    public void importFile(ActionEvent event) {

    }

    /**
     * Export the file (export the model to a sql file)
     * 
     * @param event
     */
    public void exportFile(ActionEvent event) {

    }

    /**
     * Return th user to the developer window
     * 
     * @param event
     * @throws IOException
     */
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
    /*************************** Help Functions *******************************/
    /**************************************************************************/
    /**
     * Open the usage window
     * 
     * @param event
     */
    public void usageWindow(ActionEvent event) {
        // open usage window
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Usage");
        alert.setHeaderText("Usage");
        alert.setContentText("Double click to Create a table " +
                "\n Click and drag to move a table " +
                "\n Click inside a table to start/end a relationship between 2 tables" +
                "\n Right click to open the context menu " +
                "\n Use the context menu to add a table, add a relationship, or delete a table " +
                "\n Use the File menu to import or export a model " +
                "\n Use the Help menu to view the usage information or about window " +
                "\n Use the Exit menu to exit the application");

        alert.showAndWait();
    }

    /**
     * Open the about window
     * 
     * @param event
     */
    public void aboutWindow(ActionEvent event) {
        // open about window
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("This is a simple about window");

        alert.showAndWait();
    }

    /**************************************************************************/
    /****************************** Handle Mouse ******************************/
    /**************************************************************************/
    /**
     * Handle when the mouse is clicked
     * 
     * @param event
     * @throws InterruptedException 
     */
    private void handleMouseClicked(MouseEvent event) throws InterruptedException {

        // if for the double click to create a table
        if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
            double x = event.getX();
            double y = event.getY();

            Table square = new Table(x - SQUARE_SIZE / 2, y - SQUARE_SIZE / 2, SQUARE_SIZE);
            shapes.add(square);
            drawShapes();

            Thread.sleep(500); // We wait for the square to be drawn
            square.openMenu(); // We open the context menu

            // if for the right click
        } else if (event.getButton() == MouseButton.SECONDARY) {

            // get the shape that was clicked
            Shape shape = getClickedShape(event.getX(), event.getY());

            // if the user right clicked outside a shape then we return
            if(shape == null) 
                return;
            
            if(shape instanceof Table) // if the user right clicked inside a table then we open the context menu
                ((Table)shape).openMenu();
            

            // the user right clicked inside a shape
            // TODO: open the context menu

            
            /**
             * If's for the line drawing
             */
        } else if (drawingLine) {
            Shape shape = getClickedShape(event.getX(), event.getY());

            // verify that the clicked shape is a Table
            if (!(shape instanceof Table)
                    // TODO: or the user clicked on the same shape 
                    // TODO: or the user clicked on a shape that has a relation with the selected shape
                    ) {
                return;
            }

            // if the user is drawing a line
            double x = event.getX();
            double y = event.getY();
            // TODO: open context menu

            Relation relation = new Relation(lineStartX, lineStartY, x, y);
            shapes.add(relation);
            drawShapes();
            drawingLine = false;
        } else if (event.getClickCount() == 1) {
            Shape shape = getClickedShape(event.getX(), event.getY());

            // verify that the clicked shape is a Table
            if (!(shape instanceof Table)) {
                return;
            }

            // if the user is not drawing a line,
            // start drawing a line
            lineStartX = event.getX();
            lineStartY = event.getY();

            drawingLine = true;
        }
    }

    /**
     * Check if the click is inside a shape
     * 
     * @param x
     * @param y
     * @return
     */
    private Shape getShape(double x, double y) {
        for (Shape shape : shapes)
            if (shape.contains(x, y)) 
                return shape;
            
        
        return null;
    }

    /**
     * Returns a shape if a shape is clicked
     * 
     * @param event
     */
    private Shape getClickedShape(double x, double y) {
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                return shape;
            }
        }

        return null;
    }

    /**
     * Handle when the mouse is pressed
     * 
     * @param event
     */
    private void handleMousePressed(MouseEvent event) {
        selectedShape = getShapeAt(event.getX(), event.getY());
        if (selectedShape != null) { // if a shape is selected
            initialX = event.getX();
            initialY = event.getY();
            dragging = true;
        }
    }

    /**
     * Handle when the mouse is dragged (moved)
     * 
     * @param event
     */
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

    /**
     * Handle when the mouse is released
     * 
     * @param event
     */
    private void handleMouseReleased(MouseEvent event) {
        dragging = false;
    }

    /**************************************************************************/
    /*************************** Shapes Functions *****************************/
    /**************************************************************************/
    /**
     * Draw the shapes
     */
    private void drawShapes() {
        GraphicsContext gc = canva.getGraphicsContext2D();
        gc.clearRect(0, 0, canva.getWidth(), canva.getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

    /**
     * Get the shape at the given x and y
     * 
     * @param x
     * @param y
     * @return
     */
    private Shape getShapeAt(double x, double y) {
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    /**************************************************************************/
    /********************************* Exit ***********************************/
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