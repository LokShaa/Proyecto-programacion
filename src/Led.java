import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Led {
    private Pane matrizPane;
    private double startX, startY, endX, endY;
    private boolean firstClick = true;

    public Led(Pane matrizPane) {
        this.matrizPane = matrizPane;
    }

    public void handleMouseClick(MouseEvent event) {
        if (firstClick) {
            startX = event.getX();
            startY = event.getY();
            
            if (!comprobarCuadradoEnMatrices(matrizPane, startX, startY)) {
                return;
            }
            firstClick = false;
        } else {
            endX = event.getX();
            endY = event.getY();
            
            if (!comprobarCuadradoEnMatrices(matrizPane, endX, endY)) {
                return;
            }
            firstClick = true;
            drawCable(startX, startY, endX, endY);
        }
    }
    private void drawCable(double startX, double startY, double endX, double endY) {
        // Dibujar la línea del cable
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(5);

        // Calcular el punto medio
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;

        // Crear el círculo en el punto medio
        Circle circle = new Circle(midX, midY, 10, Color.RED);

        // Agregar la línea y el círculo al Pane
        matrizPane.getChildren().addAll(line, circle);
    }

    private boolean comprobarCuadradoEnMatrices(Pane matrizPane, double x, double y) {
        return x >= 0 && x <= matrizPane.getWidth() && y >= 0 && y <= matrizPane.getHeight();
    }
}