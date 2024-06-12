package sql.ide.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TableDraw implements ShapeInterface {
    private double x;
    private double y;
    private double size;

    public TableDraw(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(125, 240, 255));
        gc.fillRect(x, y, size, size);
    }

    @Override
    public void move(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x + size && y >= this.y && y <= this.y + size;
    }
}
