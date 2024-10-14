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
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;


public class Led{
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

    private List<Circle> leds = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
     
    private boolean quemado = false;

    private int casoEnergia = 0;//variable que se usara para ver que caso de energia se esta usando y se usara para cuando se elimine el eld y ver si este esta pasando energia
    //cuando es 1 es porque esta pasando energia positiva
    //cuando es -1 es pporque esta pasando energia negativa

    public Led(Pane matrizPane, Pane[][] matriz, int[][] matrizEnteros) {
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

    private void mostrarAlerta(String mensaje){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void drawCable(double startX, double startY, double endX, double endY) {
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;

        Line line1 = new Line(startX, startY, midX, midY);
        line1.setStroke(Color.RED);
        line1.setStrokeWidth(5);

        Line line2 = new Line(midX, midY, endX, endY);
        line2.setStroke(Color.BLUE);
        line2.setStrokeWidth(5);

        Circle circle = new Circle(midX, midY, 10, Color.DARKGREEN); // Inicializar con verde oscuro

        if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
            circle.setFill(Color.web("#00FF00")); // Verde fluorescente
            
        } else {
            circle.setFill(Color.DARKGREEN); // Verde oscuro
        }

        // Agregar manejador de eventos para borrar el LED y la línea
        circle.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                double xLocalInicial = this.startX;
                double yLocalInicial = this.startY;
                double xLocalFinal = this.endX;
                double yLocalFinal = this.endY;
                filaInicial = (int) (yLocalInicial / 20);
                columnaInicial = (int) (xLocalInicial / 20);
                filaFinal = (int) (yLocalFinal / 20);
                columnaFinal = (int) (xLocalFinal / 20);

                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);
                Main.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.setMatrizCables(filaFinal, columnaFinal, 0);

                matrizPane.getChildren().removeAll(line1, line2, circle);
                leds.remove(circle);
                lines.remove(line1);
                lines.remove(line2);
                if(casoEnergia == 1){
                    if(filaFinal < 5){
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = 0;
                            matriz[i][columnaFinal].setStyle("-fx-background-color: black;");
                        }
                    }else if(filaFinal >= 5){
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = 0;
                            matriz[i][columnaFinal].setStyle("-fx-background-color:  black;");
                        }
                    }
                }else if(casoEnergia == -1){
                    if(filaInicial < 5){
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = 0;
                            matriz[i][columnaInicial].setStyle("-fx-background-color:  black;");
                        }
                    }else if(filaInicial >= 5){
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = 0;
                            matriz[i][columnaInicial].setStyle("-fx-background-color:  black;");
                        }
                    }
                }
            }
        });

        // Agregar la línea y el círculo al Pane
        matrizPane.getChildren().addAll(line1, line2, circle);
        leds.add(circle);
        lines.add(line1);
        lines.add(line2);
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
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> actualizar()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void actualizar() {
        for (int i = 0; i < leds.size(); i++) {
            Circle led = leds.get(i);
            Line line1 = lines.get(i * 2);
            Line line2 = lines.get(i * 2 + 1);

            double startX = line1.getStartX();
            double startY = line1.getStartY();
            double endX = line2.getEndX();
            double endY = line2.getEndY();

            int valorCelda1 = obtenerValorMatrizEnteros(startX, startY);
            int valorCelda2 = obtenerValorMatrizEnteros(endX, endY);
            if(quemado == false){
                if(valorCelda1 == 1 ){
                    led.setFill(Color.web("#00FF00")); // Verde fluorescente
                    transferirEnergia(startX, startY, endX, endY, valorCelda1);
                    casoEnergia = 1;

                } else if(valorCelda2 == -1){
                    led.setFill(Color.web("#00FF00")); // Verde fluorescente
                    transferirEnergia(endX, endY, startX, startY, valorCelda2);
                    casoEnergia = -1;   
    
                }else if(valorCelda2 == 1 || valorCelda1 == -1){
                    led.setFill(Color.web("#FFA500")); // Naranja fosforescente
                    quemado = true;
        
                } else {
                    led.setFill(Color.DARKGREEN); // Verde oscuro
                }

            }else{
                led.setFill(Color.web("#FFA500")); // Naranja fosforescente
            }
           
        }   
    }

    private void transferirEnergia(double fromX, double fromY, double toX, double toY, int valor) {
        int filaFrom = (int) (fromY / 20);
        int columnaFrom = (int) (fromX / 20);
        int filaTo = (int) (toY / 20);
        int columnaTo = (int) (toX / 20);
    
        filaFrom = ajustarFila(filaFrom);
        columnaFrom = ajustarColumna(columnaFrom);
        filaTo = ajustarFila(filaTo);
        columnaTo = ajustarColumna(columnaTo);
    
        if (filaTo < 5) {
            if (valor == 1) {
                for (int i = 0; i < 5; i++) {
                    matrizEnteros[i][columnaTo] = valor;
                    matriz[i][columnaTo].setStyle("-fx-background-color: red;");
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    matrizEnteros[i][columnaTo] = valor;
                    matriz[i][columnaTo].setStyle("-fx-background-color: blue;");
                }
            }
        } else {
            if (valor == 1) {
                for (int i = 5; i < 10; i++) {
                    matrizEnteros[i][columnaTo] = valor;
                    matriz[i][columnaTo].setStyle("-fx-background-color: red;");
                }
            } else {
                for (int i = 5; i < 10; i++) {
                    matrizEnteros[i][columnaTo] = valor;
                    matriz[i][columnaTo].setStyle("-fx-background-color: blue;");
                }
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