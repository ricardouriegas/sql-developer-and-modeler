package sql.ide.shapes;

import javafx.scene.canvas.GraphicsContext;

public interface ShapeInterface {
    void draw(GraphicsContext gc);

    void move(double deltaX, double deltaY);

    boolean contains(double x, double y);
}
