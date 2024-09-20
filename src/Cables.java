import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Cables extends Line {
    private String tipo; // Atributo para saber si es cable positivo o negativo
    private Pane pane; // Atributo para saber en qué pane se dibujará el cable
    private static final int CELL_SIZE = 20;
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;

    private int[][] matrizEnteros;
    private Pane[][] matrizPane;

    private int[][] matriSup;
    private Pane[][] matrizPaneSup;

    private int[][] matriInf;
    private Pane[][] matrizPaneInf;

    private int segundaCeldaX;
    private int segundaCeldaY;

    private Timeline timeline; 

    public Cables(Pane pane, Pane[][] matrizPane, Color color, double startX, double startY, int[][] matrizEnteros, int[][] matriSup, Pane[][] matrizPaneSup, int[][] matriInf, Pane[][] matrizPaneInf) {
        this.pane = pane;
        this.matrizPane = matrizPane;
        this.matrizEnteros = matrizEnteros;
        this.setStroke(color);
        this.setStrokeWidth(8);

        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX);
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this);

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Verificar si es clic derecho
                double xLocalInicial = this.getStartX();
                double yLocalInicial = this.getStartY();
                double xLocalFinal = this.getEndX();
                double yLocalFinal = this.getEndY();
                filaInicial = (int) (yLocalInicial / CELL_SIZE);
                columnaInicial = (int) (xLocalInicial / CELL_SIZE);
                filaFinal = (int) (yLocalFinal / CELL_SIZE);
                columnaFinal = (int) (xLocalFinal / CELL_SIZE);

                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);

                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaFinal, 0);

                pane.getChildren().remove(this); // Eliminar el cable del pane

                // Detener el monitoreo
                if (timeline != null) {
                    timeline.stop();
                }

                // Eliminar solo los valores de energía del segundo clic
                eliminarValoresEnergia(filaFinal, columnaFinal);

                if (segundaCeldaY >= 0 && segundaCeldaY < matrizEnteros.length && segundaCeldaX >= 0 && segundaCeldaX < matrizEnteros[0].length) {
                    matrizEnteros[segundaCeldaY][segundaCeldaX] = 0;
                }
                Main.actualizarMatriz();
            }
        });

        // Monitoreo constante de las celdas
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            monitorearCeldas();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void monitorearCeldas() {
        double xLocalInicial = this.getStartX();
        double yLocalInicial = this.getStartY();
        double xLocalFinal = this.getEndX();
        double yLocalFinal = this.getEndY();
        filaInicial = (int) (yLocalInicial / CELL_SIZE);
        columnaInicial = (int) (xLocalInicial / CELL_SIZE);
        filaFinal = (int) (yLocalFinal / CELL_SIZE);
        columnaFinal = (int) (xLocalFinal / CELL_SIZE);
    
        filaInicial = ajustarFila(filaInicial);
        columnaInicial = ajustarColumna(columnaInicial);
        filaFinal = ajustarFila(filaFinal);
        columnaFinal = ajustarColumna(columnaFinal);
    
        // Verificar que los índices estén dentro de los límites de la matriz
        if (filaInicial >= 0 && filaInicial < matrizEnteros.length && columnaInicial >= 0 && columnaInicial < matrizEnteros[0].length &&
            filaFinal >= 0 && filaFinal < matrizEnteros.length && columnaFinal >= 0 && columnaFinal < matrizEnteros[0].length) {

            // Monitorear y actualizar valores de las celdas
            int valorInicial = matrizEnteros[filaInicial][columnaInicial];
            int valorFinal = matrizEnteros[filaFinal][columnaFinal];

            if (!(valorInicial != 0 && valorFinal != 0)) {
                if (valorInicial == 1 || valorInicial == -1) {
                    if (columnaFinal == 0) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 1) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 2) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 3) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 4) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 5) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 6) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 7) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 8){
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaFinal == 9){
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaFinal] = valorInicial;
                            matrizPane[i][columnaFinal].setStyle("-fx-background-color: yellow;");
                        }
                    } else {
                        matrizPane[filaFinal][columnaFinal].setStyle("-fx-background-color: black;");
                    }
                }
            
                if (valorFinal == 1 || valorFinal == -1) {
                    if (filaInicial == 0) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 1) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 2) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 3) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 4) {
                        for (int i = 0; i < 5; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 5) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 6) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 7) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 8) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else if (filaInicial == 9) {
                        for (int i = 5; i < 10; i++) {
                            matrizEnteros[i][columnaInicial] = valorFinal;
                            matrizPane[i][columnaInicial].setStyle("-fx-background-color: yellow;");
                        }
                    } else {
                        matrizPane[filaFinal][columnaFinal].setStyle("-fx-background-color: black;");
                    }
                }
            
                Main.actualizarMatriz();
            }
        }
    }

    public int getFilaInicial() {
        return filaInicial;
    }

    public int getColumnaInicial() {
        return columnaInicial;
    }

    public int getFilaFinal() {
        return filaFinal;
    }

    public int getColumnaFinal() {
        return columnaFinal;
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

    public void iniciarDibujoCable(double startX, double startY) {
        this.setStartX(startX);
        this.setStartY(startY);
    }

    public void finalizarDibujoCable(double endX, double endY) {
        this.setEndX(endX);
        this.setEndY(endY);

        // Guardar las coordenadas de la segunda celda
        segundaCeldaX = obtenerIndiceMatrizX(endX);
        segundaCeldaY = obtenerIndiceMatrizY(endY);
    }

    public void actualizarPane(Pane nuevoPane) {
        double xGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getX();
        double yGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getY();
        double xGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getX();
        double yGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getY();

        this.pane.getChildren().remove(this);
        this.pane = nuevoPane;
        nuevoPane.getChildren().add(this);

        double xLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getX();
        double yLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getY();
        double xLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getX();
        double yLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getY();

        this.setStartX(xLocalesIniciales);
        this.setStartY(yLocalesIniciales);
        this.setEndX(xLocalesFinales);
        this.setEndY(yLocalesFinales);

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                pane.getChildren().remove(this);
                if (timeline != null) {
                    timeline.stop();
                }
                eliminarValoresEnergia(filaFinal, columnaFinal);
                if (segundaCeldaY >= 0 && segundaCeldaY < matrizEnteros.length && segundaCeldaX >= 0 && segundaCeldaX < matrizEnteros[0].length) {
                    matrizEnteros[segundaCeldaY][segundaCeldaX] = 0;
                }
                Main.actualizarMatriz();
            }
        });
    }

    public void setTipo(Color color) {
        if (color.equals(Color.RED)) {
            this.tipo = "Positivo";
        } else if (color.equals(Color.BLUE)) {
            this.tipo = "Negativo";
        }
    }

    public Pane getPane() {
        return pane;
    }
    
    public double getXInicial() {
        return this.getStartX();
    }

    public double getYInicial() {
        return this.getStartY();
    }

    private int obtenerIndiceMatrizX(double x) {
        for (int i = 0; i < matrizPane.length; i++) {
            for (int j = 0; j < matrizPane[i].length; j++) {
                Pane celda = matrizPane[i][j];
                if (celda.getBoundsInParent().contains(x, this.getEndY())) {
                    return j;
                }
            }
        }
        return -1;
    }

    private int obtenerIndiceMatrizY(double y) {
        for (int i = 0; i < matrizPane.length; i++) {
            for (int j = 0; j < matrizPane[i].length; j++) {
                Pane celda = matrizPane[i][j];
                if (celda.getBoundsInParent().contains(this.getEndX(), y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void eliminarValoresEnergia(int fila, int columna){
        timeline.stop();
        if (fila >= 0 && fila <= 4) {
            for (int i = 0; i < 5; i++) {
                matrizEnteros[i][columna] = 0;
                matrizPane[i][columna].setStyle("-fx-background-color: black;");
            }
        } else if (fila >= 5 && fila <= 9) {
            for (int i = 5; i < 10; i++) {
                matrizEnteros[i][columna] = 0;
                matrizPane[i][columna].setStyle("-fx-background-color: black;");
            }
        }
    }
}
