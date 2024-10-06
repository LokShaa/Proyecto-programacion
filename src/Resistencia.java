import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;

public class Resistencia {
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

    private boolean quemado = false;

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
            if (distance > 120) {
                mostrarAlerta("La distancia entre los puntos es demasiado grande.");
                return;
            }
            if(distance < 80){
                mostrarAlerta("La distancia entre los puntos es demasiado pequeña.");
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
        Rectangle rectangulo = new Rectangle(midX - rectWidth / 2, midY - rectHeight / 2, rectWidth, rectHeight);
        rectangulo.setFill(Color.PALEGOLDENROD);
        
        // Calcular el ángulo de rotación
        double angulo = Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        rectangulo.setRotate(angulo);
    
        // Crear bandas de colores
        double bandWidth = rectWidth / 9;
        double bandHeight = rectHeight; 
    
        Rectangle primerBanda = new Rectangle(bandWidth, 0, bandWidth, bandHeight);
        primerBanda.setFill(Color.RED);
    
        Rectangle segundaBanda = new Rectangle(3 * bandWidth, 0, bandWidth, bandHeight);
        segundaBanda.setFill(Color.RED);
    
        Rectangle terceraBanda = new Rectangle(5 * bandWidth, 0, bandWidth, bandHeight);
        terceraBanda.setFill(Color.MAROON);
    
        Rectangle cuartaBanda = new Rectangle(7 * bandWidth, 0, bandWidth, bandHeight);
        cuartaBanda.setFill(Color.GOLD);
    
        // Crear un grupo para las bandas y aplicar la rotación y traslación
        Group bandsGroup = new Group(primerBanda, segundaBanda, terceraBanda, cuartaBanda);
        bandsGroup.setLayoutX(midX - rectWidth / 2);
        bandsGroup.setLayoutY(midY - rectHeight / 2);
        bandsGroup.getTransforms().add(new Rotate(angulo, rectWidth / 2, rectHeight / 2));
    
        // Agregar manejador de eventos para borrar el LED y la línea
        rectangulo.setOnMouseClicked(event -> {
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
    
                matrizPane.getChildren().removeAll(line, rectangulo, bandsGroup);
                resistenciaList.remove(rectangulo);
                lines.remove(line);
            }
        });
    
        // Agregar la línea, el rectángulo y las bandas de colores al Pane
        matrizPane.getChildren().addAll(line, rectangulo, bandsGroup);
        resistenciaList.add(rectangulo);
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
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> actualizarMatrizCentral()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void generarHumo(double x, double y) {
    // Crear varios círculos pequeños que simularán el humo
    for (int i = 0; i < 5; i++) {
        Circle humo = new Circle(5 + Math.random() * 10); // Tamaños aleatorios
        humo.setCenterX(x);
        humo.setCenterY(y);
        humo.setFill(Color.GRAY);
        humo.setOpacity(0.8); // Humo inicialmente visible

        // Animación de translación (movimiento hacia arriba)
        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), humo);
        translate.setByY(-50 - Math.random() * 30); // Subir el humo

        // Animación de desvanecimiento
        FadeTransition fade = new FadeTransition(Duration.seconds(2), humo);
        fade.setToValue(0); // Se desvanece a 0

        // Combinar animaciones en una transición paralela
        ParallelTransition transition = new ParallelTransition(translate, fade);
        transition.setOnFinished(event -> matrizPane.getChildren().remove(humo)); // Remover círculo cuando termine la animación

        // Añadir el humo al Pane y a la lista para su control
        matrizPane.getChildren().add(humo);
       

        // Iniciar la animación
        transition.play();
    }
}

    private void actualizarMatrizCentral() {
        for (int i = 0; i < resistenciaList.size(); i++) {
            int valorCelda1 = obtenerValorMatrizEnteros(startX, startY);
            int valorCelda2 = obtenerValorMatrizEnteros(endX, endY);
            if(quemado == false){
                if(valorCelda1 == 1 || valorCelda1 == -1){
                    transferirEnergia(startX, startY, endX, endY, valorCelda1);
                } 
                else if(valorCelda2 == 1 || valorCelda2 == -1){
                    quemado = true;
                    generarHumo((startX + endX) / 2, (startY + endY) / 2);
                }
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