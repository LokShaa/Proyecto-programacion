import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Resistencia {
    private static final double MAX_DISTANCE = 130.0; // Distancia máxima permitida en píxeles

    private Pane matrizPane;
    private Pane[][] matriz;
    private int[][] matrizEnteros;
    private double startX, startY, endX, endY;
    private boolean firstClick = true;
    private int valorCelda1 = 0;
    private int valorCelda2 = 0;
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;

    private List<Rectangle> resistenciaList = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();

    public Resistencia(Pane matrizPane, Pane[][] matriz, int[][] matrizEnteros) {
        this.matrizPane = matrizPane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        startMonitoring();
    }

    public void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (!comprobarCuadradoEnMatrices(matrizPane, x, y)) {
            return;
        }

        int fila = (int) (y / 20);
        int columna = (int) (x / 20);
        fila = ajustarFila(fila);
        columna = ajustarColumna(columna);

        if (firstClick) {
            startX = x;
            startY = y;

            if (Main.getMatrizCables()[fila][columna] == 1) {
                mostrarAlerta("El cuadrado ya está ocupado.");
                return;
            }
            if(Main.matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1){
                mostrarAlerta("El cuadrado tiene un corto circuito.");
                return;
            }

            Main.setMatrizCables(fila, columna, 1);
            valorCelda1 = obtenerValorMatrizEnteros(event); // Obtener el valor de la primera celda
            firstClick = false;

        } else {
            endX = x;
            endY = y;

            if (Main.getMatrizCables()[fila][columna] == 1) {
                mostrarAlerta("El cuadrado ya está ocupado.");
                return;
            }
            if(Main.matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1){
                mostrarAlerta("El cuadrado tiene un corto circuito.");
                return;
            }

            // Calcular la distancia entre los puntos de inicio y fin
            double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            if (distance > MAX_DISTANCE) {
                mostrarAlerta("La distancia entre los puntos es demasiado grande.");
                return;
            }

            Main.setMatrizCables(fila, columna, 1);
            valorCelda2 = obtenerValorMatrizEnteros(event); // Obtener el valor de la segunda celda
            firstClick = true;
            drawCable(startX, startY, endX, endY);
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void drawCable(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(5);
    
        // Calcular el punto medio
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;
        
        // Ajustar las coordenadas del rectángulo para que su centro esté en el punto medio
        double rectWidth = 50;
        double rectHeight = 20;
        Rectangle rectangle = new Rectangle(midX - rectWidth / 2, midY - rectHeight / 2, rectWidth, rectHeight);
        rectangle.setFill(Color.PALEGOLDENROD);
    
        // Calcular el ángulo de rotación
        double angulo = Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        rectangle.setRotate(angulo);
    
        // Agregar manejador de eventos para borrar el LED y la línea
        rectangle.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                double xLocalInicial = startX;
                double yLocalInicial = startY;
                double xLocalFinal = endX;
                double yLocalFinal = endY;
                int filaInicial = (int) (yLocalInicial / 20);
                int columnaInicial = (int) (xLocalInicial / 20);
                int filaFinal = (int) (yLocalFinal / 20);
                int columnaFinal = (int) (xLocalFinal / 20);
    
                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);
                Main.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.setMatrizCables(filaFinal, columnaFinal, 0);
    
                matrizPane.getChildren().removeAll(line, rectangle);
                resistenciaList.remove(rectangle);
                lines.remove(line);
            }
        });
    
        // Agregar la línea y el rectángulo al Pane
        matrizPane.getChildren().addAll(line, rectangle);
        resistenciaList.add(rectangle);
        lines.add(line);
    }

    // Método para ajustar la fila según las reglas específicas
    private int ajustarFila(int fila) {
        fila -= (fila / 2);
        if (fila >= 7) {
            fila -= 2;
        }
        return fila;
    }

    // Método para ajustar la columna según las reglas específicas
    private int ajustarColumna(int columna) {
        columna -= (columna / 2);
        if (columna > 20) {
            columna += 1;
        }
        return columna;
    }

    private boolean comprobarCuadradoEnMatrices(Pane matrizPane, double x, double y) {
        return Main.matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, matrizPane, x, y);
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
        //Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> actualizar()));
        //timeline.setCycleCount(Timeline.INDEFINITE);
        //timeline.play();
    }

    /*private void actualizar() {
        for (int i = 0; i < resistenciaList.size(); i++) {
            Rectangle rectangle = resistenciaList.get(i);
            Line line = lines.get(i);

            double startX = line.getStartX();
            double startY = line.getStartY();
            double endX = line.getEndX();
            double endY = line.getEndY();

            int valorCelda1 = obtenerValorMatrizEnteros(startX, startY);
            int valorCelda2 = obtenerValorMatrizEnteros(endX, endY);

            if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
                led.setFill(Color.web("#00FF00")); // Verde fluorescente
            } else {
                led.setFill(Color.DARKGREEN); // Verde oscuro
            }
        }
    }*/

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