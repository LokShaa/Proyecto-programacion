import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Led {
    public void metodosLed(ImageView imagenLed, ImageView imagenLed2, Pane paneDibujo){
        imagenLed.setOnMouseEntered(enteredEvent -> { //Brillo para el led
            Glow glowLed = new Glow(1);
            imagenLed.setEffect(glowLed);
        });

        imagenLed.setOnMouseExited(exitEvent -> { //Se quita el brillo del led
            imagenLed.setEffect(null);
        });

        imagenLed.setOnMouseClicked(clickedEvent -> { // Crear imagen arrastrable
            ImageView nuevoLed = new ImageView(imagenLed2.getImage());
            nuevoLed.setX(200); // Posición inicial X
            nuevoLed.setY(200); // Posición inicial Y

            // Establecer el tamaño del led
            nuevoLed.setFitWidth(75); // Ancho
            nuevoLed.setFitHeight(110); // Alto

            paneDibujo.getChildren().add(nuevoLed);

            // Hace el led arrastrable
            nuevoLed.setOnMousePressed(pressEvent -> {
            nuevoLed.setUserData(new double[]{pressEvent.getSceneX(), pressEvent.getSceneY(), nuevoLed.getLayoutX(), nuevoLed.getLayoutY()});
            });

            nuevoLed.setOnMouseDragged(dragEvent -> {
                double[] data = (double[]) nuevoLed.getUserData();
                double deltaX = dragEvent.getSceneX() - data[0];
                double deltaY = dragEvent.getSceneY() - data[1];
                nuevoLed.setLayoutX(data[2] + deltaX);
                nuevoLed.setLayoutY(data[3] + deltaY);
            });
        });
    }
}
