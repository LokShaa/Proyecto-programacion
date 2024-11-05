import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Chip {
    private Group chipGroup;
    private Pane pane;
    public Pane[][] matriz;
    private boolean acoplado = false;
    private int colInicio; // Nuevo atributo para almacenar la columna de inicio
    private int[][] matrizEnteros;
    private String tipoString;  

    public Chip(Pane pane, double x, double y, Pane[][] matriz, int[][] matrizEnteros, String tipo) {
        this.pane = pane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        this.tipoString = tipo;
        this.chipGroup = new Group();
        dibujarRectanguloMovible(x, y);
        addEventHandlers();
        pane.getChildren().add(chipGroup);
    }

    private void dibujarRectanguloMovible(double x, double y) {
        crearPatasSuperiores();
        crearPatasInferiores();
        crearRectangulo();
        crearTextoTipo();
        posicionarGrupo(x, y);
    }

    private void crearPatasSuperiores() {
        for (int i = 0; i < 7; i++) {
            Pane pataSuperior = new Pane();
            pataSuperior.setPrefSize(15, 15);
            pataSuperior.setStyle("-fx-background-color: gray;");
            pataSuperior.setLayoutX(2 + i * 38.5);
            pataSuperior.setLayoutY(-10);
            chipGroup.getChildren().add(pataSuperior);
        }
    }

    private void crearPatasInferiores() {
        for (int i = 0; i < 7; i++) {
            Pane pataInferior = new Pane();
            pataInferior.setPrefSize(15, 15);
            pataInferior.setStyle("-fx-background-color: gray;");
            pataInferior.setLayoutX(2 + i * 38.5);
            pataInferior.setLayoutY(105);
            chipGroup.getChildren().add(pataInferior);
        }
    }

    private void crearRectangulo() {
        Rectangle rectangulo = new Rectangle(250, 110);
        rectangulo.setFill(Color.BLACK);
        chipGroup.getChildren().add(rectangulo);
    }
    
    private void crearTextoTipo() {
        Text textoTipo = new Text(tipoString);
        textoTipo.setFill(Color.WHITE);
        textoTipo.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        textoTipo.setX(100); // Ajusta la posición X del texto
        textoTipo.setY(55);  // Ajusta la posición Y del texto
        chipGroup.getChildren().add(textoTipo);
    }

    private void posicionarGrupo(double x, double y) {
        chipGroup.setLayoutX(x);
        chipGroup.setLayoutY(y);
    }

    private void addEventHandlers() {
        chipGroup.setOnMousePressed(event -> {
            if (!acoplado) {
                chipGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), chipGroup.getTranslateX(), chipGroup.getTranslateY()});
            }
        });

        chipGroup.setOnMouseDragged(event -> {
            if (!acoplado) {
                double[] data = (double[]) chipGroup.getUserData();
                double deltaX = event.getSceneX() - data[0];
                double deltaY = event.getSceneY() - data[1];
                chipGroup.setTranslateX(data[2] + deltaX);
                chipGroup.setTranslateY(data[3] + deltaY);
                verificarPatas(4, 0, getColInicio());
                verificarPatas(5, 7, getColInicio());
            }
        });

        chipGroup.setOnMouseReleased(event -> {
            if (!acoplado) {
                acoplarChipSiEsPosible();
            }
        });

        chipGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                mostrarMenuContextual(event);
            }
        });
    }

    private void mostrarMenuContextual(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> {
            restaurarColorPanes();
            pane.getChildren().remove(chipGroup);
            Main.BotonBateria2();
            Main.BotonBateria3();
        });

        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> acoplado = false);

        contextMenu.getItems().addAll(eliminarItem, editarItem);
        contextMenu.show(chipGroup, event.getScreenX(), event.getScreenY());
    }

    private void acoplarChipSiEsPosible() {
        for (int col = 0; col <= matriz[0].length - 7; col++) {
            boolean patasSuperioresAcopladas = verificarPatas(4, 0, col);
            boolean patasInferioresAcopladas = verificarPatas(5, 7, col);

            if (patasSuperioresAcopladas && patasInferioresAcopladas) {
                acoplado = true;
                chipGroup.setTranslateX(0);
                chipGroup.setTranslateY(0);
                posicionarGrupo(matriz[4][col].getLayoutX(), matriz[4][col].getLayoutY() + 10);
                colInicio = col; // Actualizar el atributo colInicio
                break;
            }
        }
    }

    private boolean verificarPatas(int fila, int offset, int colInicio) {
        boolean todasPatasAcopladas = true;
        for (int i = 0; i < 7; i++) {
            Pane pata = (Pane) chipGroup.getChildren().get(i + offset);
            double pataX = chipGroup.getLayoutX() + chipGroup.getTranslateX() + pata.getLayoutX();
            double pataY = chipGroup.getLayoutY() + chipGroup.getTranslateY() + pata.getLayoutY();

            Pane celda = matriz[fila][colInicio + i];
            double celdaX = celda.getLayoutX();
            double celdaY = celda.getLayoutY();

            if (Math.abs(pataX - celdaX) <= 10 && Math.abs(pataY - celdaY) <= 10) {
                celda.setStyle("-fx-background-color: green;");
            } else {
                celda.setStyle("-fx-background-color: black;");
                todasPatasAcopladas = false;
            }
        }
        return todasPatasAcopladas;
    }

    private int getColInicio() {
        double chipX = chipGroup.getLayoutX() + chipGroup.getTranslateX();
        for (int col = 0; col <= matriz[0].length - 7; col++) {
            double celdaX = matriz[4][col].getLayoutX();
            if (Math.abs(chipX - celdaX) <= 10) {
                return col;
            }
        }
        return 0; // Default to 0 if no match found
    }

    public int getColInicioGuardado() {
        return colInicio;
    }

    private void restaurarColorPanes() {
        for (int fila = 4; fila <= 5; fila++) {
            for (int col = 0; col < matriz[0].length; col++) {
                matriz[fila][col].setStyle("-fx-background-color: black;");
            }
        }
    }
}