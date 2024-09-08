import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Led {
    private Pane matrizPane;
    private Pane[][] matriz;
    private int[][] matrizEnteros;
    private double startX, startY, endX, endY;
    private boolean firstClick = true;
    private int valorCelda1 = 0;
    private int valorCelda2 = 0;

    public Led(Pane matrizPane, Pane[][] matriz, int[][] matrizEnteros) {
        this.matrizPane = matrizPane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
    }

    public void handleMouseClick(MouseEvent event) {
        if (firstClick) {
            startX = event.getX();
            startY = event.getY();

            if (!comprobarCuadradoEnMatrices(matrizPane, startX, startY)) {
                return;
            }
            valorCelda1 = obtenerValorMatrizEnteros(event); // Obtener el valor de la primera celda
            firstClick = false;
        } else {
            endX = event.getX();
            endY = event.getY();

            if (!comprobarCuadradoEnMatrices(matrizPane, endX, endY)) {
                return;
            }
            valorCelda2 = obtenerValorMatrizEnteros(event); // Obtener el valor de la segunda celda
            firstClick = true;
            drawCable(startX, startY, endX, endY);
        }
    }

    private void drawCable(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(5);

        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;

        Circle circle = new Circle(midX, midY, 10, Color.RED);

        if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
            circle.setFill(Color.GREEN);
        } else {
            circle.setFill(Color.RED); 
        }

        // Agregar manejador de eventos para borrar el LED y la línea
        circle.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                matrizPane.getChildren().removeAll(line, circle);
            }
        });

        // Agregar la línea y el círculo al Pane
        matrizPane.getChildren().addAll(line, circle);
    }

    private boolean comprobarCuadradoEnMatrices(Pane matrizPane, double x, double y) {
        return x >= 0 && x <= matrizPane.getWidth() && y >= 0 && y <= matrizPane.getHeight();
    }

    private int obtenerValorMatrizEnteros(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                Pane celda = matriz[i][j];
                if (celda.getBoundsInParent().contains(x, y)) {
                    return matrizEnteros[i][j];
                }
            }
        }
        return -1;
    }
}