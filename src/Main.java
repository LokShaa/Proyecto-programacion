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
import javafx.scene.layout.Pane;

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
    private Pane paneDibujo;
    @FXML
    private Pane botonCableAzul1;
    @FXML
    private Pane botonCableAzul2;
    @FXML
    private Pane botonCableRojo1;
    @FXML
    private Pane botonCableRojo2;
    @FXML
    private ImageView cableAzulBateriaProto1;
    @FXML
    private ImageView cableAzulBateriaProto2;
    @FXML
    private ImageView cableRojoBateriaProto1;
    @FXML
    private ImageView cableRojoBateriaProto2;

    private Cables cableActual;

    @FXML
    void botonConDesc(ActionEvent event) { //Metodo para hacer aparecer y desaparecer la bateria completa y la bateria cortada, ademas las luces roja y verde
        luzRoja.setVisible(!luzRoja.isVisible()); //Se hace invisible la luz roja
        luzVerde.setVisible(luzVerde.isVisible()); //Se hace visible la luz verde
        portaBaterias.setVisible(!portaBaterias.isVisible()); //Se hace invisible el porta baterias
        bateriaCompleta.setVisible(!bateriaCompleta.isVisible()); //Se hace invisible la bateria completa
        bateriaCortada.setVisible(bateriaCortada.isVisible()); //Se hace visible la bateria cortada
    }
    
    @FXML
    void botonCableAzul(MouseEvent event) { //Metodo de la imagen del cable azul
        imagenCableAzul.setOnMouseEntered(enteredEvent -> { //Brillo para el cable
            Glow glowAzul = new Glow(1);
            imagenCableAzul.setEffect(glowAzul);
        });

        imagenCableAzul.setOnMouseExited(exitEvent -> { //Se quita el brillo del cable
            imagenCableAzul.setEffect(null);
        });
        //FALTA AGREGAR LA CONDICION PARA DESOUES DE HACER CLICK SE EMPIECE A DIBUJAR EL CABLE AZUL
    }

    @FXML
    void botonCableRojo(MouseEvent event) { //Metodo de la imagen del cable rojo
        imagenCableRojo.setOnMouseEntered(enteredEvent -> { //Brillo para el cable
            Glow glowRojo = new Glow(1);
            imagenCableRojo.setEffect(glowRojo);
        });

        imagenCableRojo.setOnMouseExited(exitEvent -> { //Se quita el brillo del cable
            imagenCableRojo.setEffect(null);
        });
        
        paneDibujo.setOnMousePressed(mousePressedEvent -> iniciarDibujoCable(mousePressedEvent)); //Se inicia el dibujo del cable
        paneDibujo.setOnMouseReleased(mouseReleasedEvent -> finalizarDibujoCable(mouseReleasedEvent)); //Se finaliza el dibujo del cable
    }

    @FXML
    void botonLed(MouseEvent event) { //Metodo de la imagen del led
        imagenLed.setOnMouseEntered(enteredEvent -> { //Brillo para el led
            Glow glowLed = new Glow(1);
            imagenLed.setEffect(glowLed);
        });

        imagenLed.setOnMouseExited(exitEvent -> { //Se quita el brillo del led
            imagenLed.setEffect(null);
        });
    }

    @FXML
    void botonSwitch(MouseEvent event) { //Metodo de la imagen del switch
        imagenSwitch.setOnMouseEntered(enteredEvent -> { //Brillo para el switch
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });

        imagenSwitch.setOnMouseExited(exitEvent -> { //Se quita el brillo del switch
            imagenSwitch.setEffect(null);
        });
    }
    //metodo para iniciar el dibujo del cable pero falta implementarlo en la clase cable, esta es solo una prueba de como dibujar 
    //cables sobre el protoboard
    void iniciarDibujoCable(MouseEvent event){
        cableActual = new Cables(paneDibujo);//Creamos un cable
        cableActual.setStartX(event.getX());
        cableActual.setStartY(event.getY());
    }
    
    void finalizarDibujoCable(MouseEvent event){ //Metodo para finalizar el dibujo del cable
        if(cableActual != null){
            cableActual.setEndX(event.getX());
            cableActual.setEndY(event.getY());
            cableActual = null;
        }
    }

    @FXML
    void cableAzulInferior(MouseEvent event) { //Metodo para el cable azul inferior
        botonCableAzul2.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable azul inferior
            cableAzulBateriaProto1.setVisible(!cableAzulBateriaProto1.isVisible()); //Se hace invisible el cable azul superior
        });

        botonCableAzul2.setOnMouseEntered(enteredEvent -> { //Brillo para el boton del cable azul inferior
            Glow glowSwitch = new Glow(1);
            botonCableAzul2.setEffect(glowSwitch);
        });

        botonCableAzul2.setOnMouseExited(exitEvent -> { //Se quita el brillo del boton del cable azul inferior
            botonCableAzul2.setEffect(null);
        });
    }

    @FXML
    void cableAzulSuperior(MouseEvent event) { //Metodo para el cable azul superior
        botonCableAzul1.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable azul superior
            cableAzulBateriaProto2.setVisible(!cableAzulBateriaProto2.isVisible()); //Se hace invisible el cable azul inferior
        });

        botonCableAzul1.setOnMouseEntered(enteredEvent -> { //Brillo para el boton del cable azul superior
            Glow glowSwitch = new Glow(1);
            botonCableAzul1.setEffect(glowSwitch);
        });

        botonCableAzul1.setOnMouseExited(exitEvent -> { //Se quita el brillo del boton del cable azul superior
            botonCableAzul1.setEffect(null);
        });

    }

    @FXML
    void cableRojoInferior(MouseEvent event) { //Metodo para el cable rojo inferior
        botonCableRojo2.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable rojo inferior
            cableRojoBateriaProto1.setVisible(!cableRojoBateriaProto1.isVisible()); //Se hace invisible el cable rojo superior
        });

        botonCableRojo2.setOnMouseEntered(enteredEvent -> { //Brillo para el boton del cable rojo inferior
            Glow glowSwitch = new Glow(1);
            botonCableRojo2.setEffect(glowSwitch);
        });

        botonCableRojo2.setOnMouseExited(exitEvent -> { //Se quita el brillo del boton del cable rojo inferior
            botonCableRojo2.setEffect(null);
        });

    }

    @FXML
    void cableRojoSuperior(MouseEvent event) { //Metodo para el cable rojo superior
        botonCableRojo1.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable rojo superior
            cableRojoBateriaProto2.setVisible(!cableRojoBateriaProto2.isVisible()); //Se hace invisible el cable rojo inferior
        });

        botonCableRojo1.setOnMouseEntered(enteredEvent -> { //Brillo para el boton del cable rojo superior
            Glow glowSwitch = new Glow(1);
            botonCableRojo1.setEffect(glowSwitch);
        });

        botonCableRojo1.setOnMouseExited(exitEvent -> { //Se quita el brillo del boton del cable rojo superior
            botonCableRojo1.setEffect(null);
        });

    }

    @Override
    public void start (Stage primaryStage) throws Exception { //Metodo para iniciar la aplicacion
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