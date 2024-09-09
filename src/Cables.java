import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line {
    private String tipo;
    private Pane pane;
    private int[][] matrizEnteros;
    private Pane[][] matrizPane;
    private int segundaCeldaX;
    private int segundaCeldaY;

    public Cables(Pane pane, Pane[][] matrizPane, Color color, double startX, double startY, int[][] matrizEnteros) {
        this.pane = pane;
        this.matrizPane = matrizPane;
        this.matrizEnteros = matrizEnteros;
        this.setStroke(color);
        this.setStrokeWidth(10);

        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX);
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this);

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                pane.getChildren().remove(this);
                if (segundaCeldaY >= 0 && segundaCeldaY < matrizEnteros.length && segundaCeldaX >= 0 && segundaCeldaX < matrizEnteros[0].length) {
                    matrizEnteros[segundaCeldaY][segundaCeldaX] = 0;
                }
                Main.actualizarMatriz();
            }
        });
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
                if (segundaCeldaY >= 0 && segundaCeldaY < matrizEnteros.length && segundaCeldaX >= 0 && segundaCeldaX < matrizEnteros[0].length) {
                    matrizEnteros[segundaCeldaY][segundaCeldaX] = 0;
                }
                Main.actualizarMatriz();
            }
        });
    }

    private void imprimirMatriz() {
        for (int i = 0; i < matrizEnteros.length; i++) {
            if (i == 5) {
                System.out.println("-----------------------------------------------------------");
            }
            for (int j = 0; j < matrizEnteros[i].length; j++) {
                System.out.print(matrizEnteros[i][j] + " ");
            }
            System.out.println();
        }
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
}