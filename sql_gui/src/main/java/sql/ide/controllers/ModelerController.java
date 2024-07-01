package sql.ide.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sql.ide.shapes.Relation;
import sql.ide.shapes.Shape;
import sql.ide.shapes.Table;
import sql.ide.shapes.relation_utilities.LineMenu;
import sql.ide.shapes.table_utilities.SquareMenu;

public class ModelerController {
    @FXML
    private Canvas canva; // canvas

    private double initialX; // initial x position of the shape
    private double initialY; // initial y position of the shape
    private boolean dragging = false; // is the shape being dragged
    private boolean drawingLine = false; // is the user drawing a line
    private double lineStartX; // start x position of the line
    private double lineStartY; // start y position of the line
    private Shape selectedShape; // selected shape (for dragging)
    private List<Shape> shapes = new ArrayList<>(); // list of shapes

    private int tableCounter = 1; // used for table default name assignation

    private static final double SQUARE_SIZE = 50; // square size

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // listeners
        canva.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
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
     * @param event
     */
    public void exportFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save SQL File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showSaveDialog(new Stage()); // show the save dialog

        if (file != null) {
            String content = "";
            for (Shape shape : shapes) 
                if (shape instanceof Table) 
                    content += (((Table) shape)).export(); // Call the export method from all the tables

            if(content.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("There are no tables to export.");
                alert.showAndWait();
                return;
            }

            if(!file.getName().endsWith(".sql")) // if the file doesn't have the .sql extension we add it
                file = new File(file.getAbsolutePath() + ".sql");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) { // write the content to the file
                writer.write(content);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while saving the file.");
                alert.showAndWait();
            }
        }
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
    private void handleMouseClicked(MouseEvent event) {

        // if for the double click to create a table
        if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
            double x = event.getX();
            double y = event.getY();

            Table square = new Table(x - SQUARE_SIZE / 2, y - SQUARE_SIZE / 2, SQUARE_SIZE);

            // set default table name
            square.setName("Table_" + tableCounter);
            tableCounter++;

            square.setContextMenu(new SquareMenu(square, this));
            shapes.add(square);
            drawShapes();

            square.openMenu(); // We open the context menu

            /**
             * if for the right click
             */
        } else if (event.getButton() == MouseButton.SECONDARY) {

            // get the shape that was clicked
            Shape shape = getShapeAt(event.getX(), event.getY());

            // if the user right clicked outside a shape then we return
            if (shape == null)
                return;

            if (shape instanceof Table) // if the user right clicked inside a table then we open the context menu
                ((Table) shape).openMenu();

            if (shape instanceof Relation) // if the user right clicked inside a relation then we open the context menu
                ((Relation) shape).openMenu();

            /**
             * If's for the line drawing
             */
        } else if (drawingLine) {
            // Create the shapes
            Shape startShape = getShapeAt(lineStartX, lineStartY);
            Shape endShape = getShapeAt(event.getX(), event.getY());

            // verify that the clicked shape is a Table
            if (!(startShape instanceof Table && endShape instanceof Table)) {
                drawingLine = false;
                return;
            }

            // verify that the clicked shapes aren't the same
            if (((Table) startShape).getX() == ((Table) endShape).getX()
                    && ((Table) startShape).getY() == ((Table) endShape).getY()) {
                drawingLine = false;
                return;
            }

            Table startTable = (Table) startShape;
            Table endTable = (Table) endShape;

            // verify that there isn't an existing relation between selected shapes
            for (Shape shape : shapes) {
                if (shape instanceof Relation relation) {
                    if (relation.getStartTable() == startTable && relation.getEndTable() == endTable) {
                        drawingLine = false;
                        return;
                    }

                    if (relation.getStartTable() == endTable && relation.getEndTable() == startTable) {
                        drawingLine = false;
                        return;
                    }
                }
            }

            Relation relation = new Relation(startTable, endTable); // create the Relation
            relation.setContextMenu(new LineMenu(relation, this));

            // add relation to the shapes
            shapes.add(relation);

            // add relation to the tables
            startTable.addRelation(relation);
            endTable.addRelation(relation);

            drawShapes();
            drawingLine = false;
            relation.openMenu();
        } else if (event.getClickCount() == 1) {
            Shape shape = getShapeAt(event.getX(), event.getY());

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
     * Handle when the mouse is pressed
     * 
     * @param event
     */
    private void handleMousePressed(MouseEvent event) {
        selectedShape = getShapeAt(event.getX(), event.getY());
        if (selectedShape != null && selectedShape instanceof Table) { // if a shape is selected
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
            drawingLine = false;
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
    public void drawShapes() {
        GraphicsContext gc = canva.getGraphicsContext2D();
        gc.clearRect(0, 0, canva.getWidth(), canva.getHeight());
        for (Shape shape : shapes)
            shape.draw(gc);

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

    /**
     * Get a list of existing tables
     * @return
     */
    public List<Table> getTableShapes() {
        List<Table> tables = new ArrayList<>();

        for (Shape shape : shapes) {
            if (shape instanceof Table) {
                tables.add((Table) shape);
            }
        }

        return tables;
    }

    /**
     * Deletes the shape at given coords
     * 
     * @param x
     * @param y
     */
    public void deleteShape(Double x, Double y) {
        Shape shape = getShapeAt(x, y);
        if (shape != null) {
            shapes.remove(shape);
        }
    }

    /**
     * Deletes a shape given the shape (not the coords)
     * 
     * @param shape
     */
    public void deleteShape(Shape shape) {
        shapes.remove(shape);
        drawShapes();
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