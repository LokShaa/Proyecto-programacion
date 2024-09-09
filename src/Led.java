import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Led{
    private Pane matrizPane;
    private Pane[][] matriz;
    private int[][] matrizEnteros;
    private double startX, startY, endX, endY;
    private boolean firstClick = true;
    private int valorCelda1 = 0;
    private int valorCelda2 = 0;

    private List<Circle> leds = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();

    public Led(Pane matrizPane, Pane[][] matriz, int[][] matrizEnteros) {
        this.matrizPane = matrizPane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        startMonitoring();
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

        Circle circle = new Circle(midX, midY, 10, Color.DARKGREEN); // Inicializar con verde oscuro

        if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
            circle.setFill(Color.web("#00FF00")); //Verde fluorescente
        } else {
            circle.setFill(Color.DARKGREEN); //Verde oscuro
        }

        //Agregar manejador de eventos para borrar el LED y la línea
        circle.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                matrizPane.getChildren().removeAll(line, circle);
                leds.remove(circle);
                lines.remove(line);
            }
        });

        //Agregar la línea y el círculo al Pane
        matrizPane.getChildren().addAll(line, circle);
        leds.add(circle);
        lines.add(line);
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

    private void startMonitoring() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> actualizar()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void actualizar() {
        for (int i = 0; i < leds.size(); i++) {
            Circle led = leds.get(i);
            Line line = lines.get(i);

            double startX = line.getStartX();
            double startY = line.getStartY();
            double endX = line.getEndX();
            double endY = line.getEndY();

            int valorCelda1 = obtenerValorMatrizEnteros(startX, startY);
            int valorCelda2 = obtenerValorMatrizEnteros(endX, endY);

            if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
                led.setFill(Color.web("#00FF00")); //Verde fluorescente
            } else {
                led.setFill(Color.DARKGREEN); //Verde oscuro
            }
        }
    }

    private int obtenerValorMatrizEnteros(double x, double y) {
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