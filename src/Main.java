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
import javafx.scene.paint.Color;

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
    private Color colorActual;

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
        imagenCableAzul.setOnMouseClicked(clickedEvent ->{
            colorActual = Color.BLUE;//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            configurarEventosDeDibujo();//LLAMAMOS A LA FUNCION QUE CONFIGURA LOS EVENTOS DE DIBUJO
        });
 
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

        imagenCableRojo.setOnMouseClicked(clickedEvent ->{
            colorActual = Color.RED;//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            configurarEventosDeDibujo();//LLAMAMOS A LA FUNCION QUE CONFIGURA LOS EVENTOS DE DIBUJO
        });
    }

    //metodo para donde se configuran las condiciones para ver que cable dibujaremos 
    private void configurarEventosDeDibujo(){
        paneDibujo.setOnMouseClicked(mouseClickedEvent ->{
            if(cableActual == null){//iniciamos con la condicion si no existe un cable actual
                double startX = mouseClickedEvent.getX();
                double startY = mouseClickedEvent.getY();
                cableActual = new Cables(paneDibujo, colorActual, startX, startY);//creamos una clase cable con el color y los atributos de coordenadas donde fue el click
                cableActual.iniciarDibujoCable(mouseClickedEvent.getX(), mouseClickedEvent.getY());//iniciamos el dibujo del cable
            } else {
                cableActual.finalizarDibujoCable(mouseClickedEvent.getX(), mouseClickedEvent.getY());
                cableActual = null;//finalizamos el dibujo del cable haciendo que sea null otra vez
            }
        });
    }
    private void cablesBateriaProtoboard(){
        paneDibujo.setOnMouseClicked(mouseClickedEvent ->{
            if(cableActual == null){//iniciamos con la condicion si no existe un cable actual
                double startX = mouseClickedEvent.getX();
                double startY = mouseClickedEvent.getY();
                cableActual = new Cables(paneDibujo, colorActual, startX, startY);//creamos una clase cable con el color y los atributos de coordenadas donde fue el click
                cableActual.iniciarDibujoCable(mouseClickedEvent.getX(), mouseClickedEvent.getY());//iniciamos el dibujo del cable
            } else {
                cableActual.finalizarDibujoCable(mouseClickedEvent.getX(), mouseClickedEvent.getY());
                cableActual = null;//finalizamos el dibujo del cable haciendo que sea null otra vez
                botonCableAzul2.toFront();
                botonCableRojo1.toFront();
                botonCableAzul1.toFront();
                botonCableRojo2.toFront();    
            }
        });
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
   
    @FXML
    void cableAzulInferior(MouseEvent event) { //Metodo para el cable azul inferior
        
        botonCableAzul2.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable azul inferior
            cableAzulBateriaProto1.setVisible(!cableAzulBateriaProto1.isVisible()); //Se hace invisible el cable azul superior
            botonCableAzul1.setVisible(!botonCableAzul1.isVisible()); //Se hace invisible el boton del cable azul superior
            paneDibujo.toFront();
            colorActual = Color.BLUE;//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            cablesBateriaProtoboard();//LLAMAMOS A LA FUNCION QUE CONFIGURA LOS EVENTOS DE DIBUJO
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
            botonCableAzul2.setVisible(!botonCableAzul2.isVisible()); //Se hace invisible el boton del cable azul inferior
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
            botonCableRojo1.setVisible(!botonCableRojo1.isVisible()); //Se hace invisible el boton del cable rojo superior
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
            botonCableRojo2.setVisible(!botonCableRojo2.isVisible()); //Se hace invisible el boton del cable rojo inferior
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