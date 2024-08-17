import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.Glow;

public class Main extends Application{
    @FXML
    private ImageView bateriaCompleta;
    @FXML
    private ImageView bateriaCortada;
    @FXML
    private Button botonBateria;
    @FXML
    private ImageView imagenCableAzul;
    @FXML
    private ImageView imagenCableRojo;
    @FXML
    private ImageView imagenLed;
    @FXML
    private ImageView imagenSwitch;
    @FXML
    private ImageView luzRoja;
    @FXML
    private ImageView luzVerde;
    @FXML
    private ImageView portaBaterias;
    

    @FXML
    void botonConDesc(ActionEvent event) {
        luzRoja.setVisible(!luzRoja.isVisible());
        luzVerde.setVisible(luzVerde.isVisible());
        portaBaterias.setVisible(!portaBaterias.isVisible());
        bateriaCompleta.setVisible(!bateriaCompleta.isVisible());
        bateriaCortada.setVisible(bateriaCortada.isVisible());
    }
    
    @FXML
    void botonCableAzul(MouseEvent event) {
        imagenCableAzul.setOnMouseEntered(enteredEvent -> {
            Glow glowAzul = new Glow(1);
            imagenCableAzul.setEffect(glowAzul);
        });

        imagenCableAzul.setOnMouseExited(exitEvent -> {
            imagenCableAzul.setEffect(null);
        });
    }

    @FXML
    void botonCableRojo(MouseEvent event) {
        imagenCableRojo.setOnMouseEntered(enteredEvent -> {
            Glow glowRojo = new Glow(1);
            imagenCableRojo.setEffect(glowRojo);
        });

        imagenCableRojo.setOnMouseExited(exitEvent -> {
            imagenCableRojo.setEffect(null);
        });
    }

    @FXML
    void botonLed(MouseEvent event) {
        imagenLed.setOnMouseEntered(enteredEvent -> {
            Glow glowLed = new Glow(1);
            imagenLed.setEffect(glowLed);
        });

        imagenLed.setOnMouseExited(exitEvent -> {
            imagenLed.setEffect(null);
        });
    }

    @FXML
    void botonSwitch(MouseEvent event) {
        imagenSwitch.setOnMouseEntered(enteredEvent -> {
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });

        imagenSwitch.setOnMouseExited(exitEvent -> {
            imagenSwitch.setEffect(null);
        });
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PrototipoV1.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Prototipo");
        primaryStage.setScene(new Scene(root,1920,1000));
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}