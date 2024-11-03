import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;

public class SwitchOctogonal {

    public void drawSwitch(Pane root) {
        // Crear el rectángulo rojo
        Rectangle redRectangle = new Rectangle(300, 100, Color.RED);

        Group switchGroup = new Group();
        switchGroup.getChildren().add(redRectangle);

        // Crear 8 panes superiores
        Pane[] topPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane topPane = new Pane();
            topPane.setPrefSize(20, 20);
            topPane.setStyle("-fx-background-color: gray;");
            topPane.setLayoutX( 2+ i * 38.5);
            topPane.setLayoutY(-10);
            topPanes[i] = topPane;
            switchGroup.getChildren().add(topPane);
        }

        // Crear 8 panes inferiores
        Pane[] bottomPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane bottomPane = new Pane();
            bottomPane.setPrefSize(20, 20);
            bottomPane.setStyle("-fx-background-color: gray;");
            bottomPane.setLayoutX( 2+ i * 38.5);
            bottomPane.setLayoutY(90);
            bottomPanes[i] = bottomPane;
            switchGroup.getChildren().add(bottomPane);
        }

        // Crear 8 rectángulos clickeables en el medio
        Rectangle[] clickableRectangles = new Rectangle[8];
        for (int i = 0; i < 8; i++) {
            Rectangle clickableRectangle = new Rectangle(20, 40, Color.BLUE);
            clickableRectangle.setLayoutX( 2+ i * 38.5);
            clickableRectangle.setY(35);
            clickableRectangle.setOnMouseClicked(this::handleRectangleClick);
            clickableRectangles[i] = clickableRectangle;
            switchGroup.getChildren().add(clickableRectangle);
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

    private void handleRectangleClick(MouseEvent event) {
        Rectangle rect = (Rectangle) event.getSource();
        rect.setFill(rect.getFill() == Color.BLUE ? Color.GREEN : Color.BLUE);
    }
}