import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.util.Duration;

public class SwitchOctogonal {

    private int[][] matrizEnteros;
    public Pane[][] matriz;
    private Pane[] topPanes;
    private Pane[] bottomPanes;

    public SwitchOctogonal(int[][] matrizEnteros, Pane[][] matriz) {
        this.matrizEnteros = matrizEnteros;
        this.matriz = matriz;
    }

    public void drawSwitch(Pane root) {
        Group switchGroup = new Group();

        // Crear 8 panes superiores
        topPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane topPane = new Pane();
            topPane.setPrefSize(20, 20);
            topPane.setStyle("-fx-background-color: gray;");
            topPane.setLayoutX(2 + i * 38.5);
            topPane.setLayoutY(-10);
            topPanes[i] = topPane;
            switchGroup.getChildren().add(topPane);
        }

        // Crear 8 panes inferiores
        bottomPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane bottomPane = new Pane();
            bottomPane.setPrefSize(20, 20);
            bottomPane.setStyle("-fx-background-color: gray;");
            bottomPane.setLayoutX(2 + i * 38.5);
            bottomPane.setLayoutY(90);
            bottomPanes[i] = bottomPane;
            switchGroup.getChildren().add(bottomPane);
        }

        // Crear el rectángulo rojo
        Rectangle redRectangle = new Rectangle(300, 100, Color.RED);
        switchGroup.getChildren().add(redRectangle);

        // Crear 8 rectángulos clickeables en el medio
        Rectangle[] clickableRectangles = new Rectangle[8];
        for (int i = 0; i < 8; i++) {
            Rectangle clickableRectangle = new Rectangle(20, 40, Color.WHITE);
            clickableRectangle.setLayoutX(2 + i * 38.5);
            clickableRectangle.setY(30);
            clickableRectangle.setOnMouseClicked(this::handleRectangleClick);
            clickableRectangles[i] = clickableRectangle;
            switchGroup.getChildren().add(clickableRectangle);

            Text numberText = new Text(String.valueOf(i + 1));
            numberText.setLayoutX(2 + i * 38.5 + 5); // Ajustar la posición X para centrar el texto
            numberText.setLayoutY(90); // Ajustar la posición Y para colocar el texto debajo del rectángulo
            numberText.setFill(Color.WHITE);
            numberText.setStyle("-fx-font-size: 15; -fx-font-weight: bold;"); // Poner el texto en negrita
            switchGroup.getChildren().add(numberText);
        }

        // Posicionar el grupo en las coordenadas especificadas
        switchGroup.setLayoutX(50);
        switchGroup.setLayoutY(50);

        // Añadir manejadores de eventos para hacer el grupo movible
        switchGroup.setOnMousePressed(event -> {
            switchGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), switchGroup.getTranslateX(), switchGroup.getTranslateY()});
        });

        switchGroup.setOnMouseDragged(event -> {
            double[] data = (double[]) switchGroup.getUserData();
            double deltaX = event.getSceneX() - data[0];
            double deltaY = event.getSceneY() - data[1];
            switchGroup.setTranslateX(data[2] + deltaX);
            switchGroup.setTranslateY(data[3] + deltaY);
        });

        // Añadir manejador de eventos para eliminar el grupo con clic derecho
        switchGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                root.getChildren().remove(switchGroup);
            }
        });

        root.getChildren().add(switchGroup);
    }

    public void iniciarMonitoreo() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            //obtenerYActualizarValores();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /*private void obtenerYActualizarValores() {
        for (int i = 0; i < 8; i++) {
            // Obtener el valor del pane inferior
            int valorInferior = matrizEnteros[5][i];

            // Actualizar el pane superior con el valor del pane inferior
            if (valorInferior == 1 || valorInferior == -1) {
                topPanes[i].setStyle("-fx-background-color: " + (valorInferior == 1 ? "green;" : "red;"));

                // Pasar el valor del pane inferior al pane superior en la matriz de enteros
                matrizEnteros[4][i] = valorInferior;

                // Actualizar el color del pane superior en la matriz de panes
                matriz[4][i].setStyle("-fx-background-color: " + (valorInferior == 1 ? "green;" : "red;"));
            }
        }
    }*/

    private void handleRectangleClick(MouseEvent event) {
        Rectangle rect = (Rectangle) event.getSource();
        rect.setFill(rect.getFill() == Color.WHITE ? Color.GREEN : Color.WHITE);
    }
}