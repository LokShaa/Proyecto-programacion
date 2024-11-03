import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;

public class Chip {

    public void dibujarRectanguloMovible(Pane pane, double x, double y) {
        Group chipGroup = new Group();
        // Crear y posicionar los 7 cuadrados en el borde superior
        for (int i = 0; i < 7; i++) {
            Pane pataSuperior = new Pane();
            pataSuperior.setPrefSize(15, 15);
            pataSuperior.setStyle("-fx-background-color: gray;");
            pataSuperior.setLayoutX( 2+ i * 38.5);
            pataSuperior.setLayoutY(-10);
            chipGroup.getChildren().add(pataSuperior);
        }

        // Crear y posicionar los 7 cuadrados en el borde inferior
        for (int i = 0; i < 7; i++) {
            Pane pataInferior = new Pane();
            pataInferior.setPrefSize(15, 15);
            pataInferior.setStyle("-fx-background-color: gray;");
            pataInferior.setLayoutX(2 + i * 38.5);
            pataInferior.setLayoutY(105); 
            chipGroup.getChildren().add(pataInferior);
        }

        Rectangle rectangulo = new Rectangle(250, 110);
        rectangulo.setFill(Color.BLACK);
        chipGroup.getChildren().add(rectangulo);

        // Posicionar el grupo en las coordenadas especificadas
        chipGroup.setLayoutX(x);
        chipGroup.setLayoutY(y);

        // Añadir manejadores de eventos para hacer el grupo movible
        chipGroup.setOnMousePressed(event -> {
            chipGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), chipGroup.getTranslateX(), chipGroup.getTranslateY()});
        });

        chipGroup.setOnMouseDragged(event -> {
            double[] data = (double[]) chipGroup.getUserData();
            double deltaX = event.getSceneX() - data[0];
            double deltaY = event.getSceneY() - data[1];
            chipGroup.setTranslateX(data[2] + deltaX);
            chipGroup.setTranslateY(data[3] + deltaY);
        });

        // Añadir manejador de eventos para eliminar el grupo con clic derecho
        chipGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                pane.getChildren().remove(chipGroup);
            }
        });

        pane.getChildren().add(chipGroup);
    }
}