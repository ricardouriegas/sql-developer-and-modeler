package sql.ide.shapes;

import javafx.scene.canvas.GraphicsContext;

public interface Shape {
    void draw(GraphicsContext gc); // function to draw on the canvas

    void move(double deltaX, double deltaY); // function to move the shape on the canvas

    boolean contains(double x, double y); // function to check if the cursor is inside the shape
}
