import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;

public class Led {
    public void brilloLed(ImageView imagenLed) {
        imagenLed.setOnMouseEntered(enteredEvent -> { //Brillo para el led
            Glow glowLed = new Glow(1);
            imagenLed.setEffect(glowLed);
        });

        imagenLed.setOnMouseExited(exitEvent -> { //Se quita el brillo del led
            imagenLed.setEffect(null);
        });
    }
}
