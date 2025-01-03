import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.effect.Glow;

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
    private Color color;
    private double[][] matrizVoltajes;

    public Led(Pane matrizPane, Pane[][] matriz, int[][] matrizEnteros, Color color,double[][] matrizVoltajes) {
        this.matrizPane = matrizPane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        this.color = color;
        this.matrizVoltajes = matrizVoltajes;
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

        Circle circle = new Circle(midX, midY, 10, Color.WHITE); // Inicializar con blanco // Inicializar con verde oscuro
        circle.setOpacity(0.8);
        if ((valorCelda1 == 1 && valorCelda2 == -1) || (valorCelda1 == -1 && valorCelda2 == 1)) {
            circle.setFill(color); 
            circle.setEffect(new Glow(0.8));
            circle.setOpacity(1.0);
        } else {
            circle.setFill(Color.WHITE); // Verde oscuro
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

            double voltajeInicial = obtenervalorVoltaje(startX, startY);
            double voltajeFinal = obtenervalorVoltaje(endX, endY);
            
            if (quemado == false) {
                if ((valorCelda1 == 1 && valorCelda2 == -1)&&(voltajeInicial > 1.5 && voltajeFinal > 1.5)) {
                    led.setFill(color);
                    led.setOpacity(1.0);
                    led.setEffect(new Glow(1.0));
                } else if (valorCelda2 == 1 || valorCelda1 == -1) {
                    led.setFill(Color.BLACK); // Naranja fosforescente
                    quemado = true;
                } else {
                    led.setFill(Color.WHITE); // Verde oscuro
                    led.setOpacity(0.8);
                }
            }
    
            led.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteItem = new MenuItem("Eliminar");
                    deleteItem.setOnAction(e -> {
                        leds.remove(led);
                        lines.remove(line1);
                        lines.remove(line2);
                        matrizPane.getChildren().removeAll(led, line1, line2);

                    });
            
                    MenuItem editColorItem = new MenuItem("Editar color");
                    if(quemado == false){
                        editColorItem.setOnAction(e -> {
                            // Mostrar el ColorPicker del Main
                            Main.colorPicker.setVisible(true);
                            Main.colorPicker.setOnAction(colorEvent -> {
                                Color colorSeleccionado = Main.colorPicker.getValue();
                                setColor(led, colorSeleccionado); // Actualizar el color del LED específico
                                Main.colorPicker.setVisible(false);
                            });
                        });

                    }
                    contextMenu.getItems().addAll(deleteItem, editColorItem);
                    contextMenu.show(led, event.getScreenX(), event.getScreenY());
                }
            });
        }
    }
   
    public void setColor(Circle led, Color newColor) {
        this.color = newColor;
        led.setFill(newColor);
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

    private double obtenervalorVoltaje(double x,double y){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                Pane celda = matriz[i][j];
                if (celda.getBoundsInParent().contains(x, y)) {
                    return matrizVoltajes[i][j];
                }
            }
        }
        return -1;
    }
}
