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
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

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
    private ImageView imagenLed2;
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
    private Circle switchDer1;
    @FXML
    private Circle switchDer2;
    @FXML
    private Circle switchIzq1;
    @FXML
    private Circle switchIzq2;
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
    private Pane matrizPane;
    @FXML
    private Pane matrizPane2;
    @FXML
    private Pane matrizPane21;

    Protoboard matrizCentralProtoboard = new Protoboard();
    Protoboard matrizSuperior = new Protoboard();
    Protoboard matrizInferior = new Protoboard();
    private List<Pane> matrices;

    

    @FXML
    void initialize(){
        matrizCentralProtoboard.inicializarMatrizCentral(10, 30, 20, 20, 18.6, 20, matrizPane);
        matrizSuperior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane2);
        matrizInferior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane21);
        matrices = new ArrayList<>();
    // Agrega las matrices a la lista
        matrices.add(matrizPane);
        matrices.add(matrizPane2);
        matrices.add(matrizPane21);
    }

    @FXML
    void botonConDesc(ActionEvent event) {
        Bateria bateria = new Bateria();
        bateria.botonConectadoDesconectado(luzRoja,luzVerde,bateriaCortada,bateriaCompleta,portaBaterias);
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
    
        imagenCableAzul.setOnMouseClicked(clickedEvent -> {
            // Configura el color actual para el cable azul
            colorActual = Color.rgb(2, 113, 245);
            /*configurarEventosDeDibujoCables(matrizPane,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane);
            });
            configurarEventosDeDibujoCables(matrizPane2,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane2);
            });
            configurarEventosDeDibujoCables(matrizPane21,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane21);
            });*/
            configurarEventosDeDibujoCables(matrices, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matrices) {
                    desactivarEventosDeDibujo(matriz);
                }
            });
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
            colorActual = Color.rgb(236,63,39);//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            // Configura los eventos de dibujo y desactívalos una vez que se coloque un cable
            /*configurarEventosDeDibujoCables(matrizPane,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane);
            });
            
            configurarEventosDeDibujoCables(matrizPane2,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane2);
            });
            configurarEventosDeDibujoCables(matrizPane21,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo(matrizPane21);
            });*/
        });
    }
    private void configurarEventosDeDibujoCables(List<Pane> matrices, Runnable onComplete) {
        for (Pane matriz : matrices) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                double x = mouseClickedEvent.getX();
                double y = mouseClickedEvent.getY();
                boolean cableIniciado = false;
    
                if (cableActual == null) {
                    // Iniciar el dibujo del cable
                    for (Pane m : matrices) {
                        if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, m, x, y) || 
                            matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y) || 
                            matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y)) {
                            cableActual = new Cables(m, colorActual, x, y);
                            cableActual.iniciarDibujoCable(x, y);
                            cableIniciado = true;
                            break;
                        }
                    }
                } else {
                    // Finalizar el dibujo del cable
                    for (Pane m : matrices) {
                        if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, m, x, y) || 
                            matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y) || 
                            matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y)) {
                            cableActual.finalizarDibujoCable(x, y);
                            cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                            onComplete.run();
                            break;
                        }
                    }
                }
            });
        }
    }
    //METODO QUE FUNCIONA SOLO PARA LOS CABLES AZUL Y ROJO DONDE EXISTE EL METODO SOLO PARA QUE SE COLOQUE UN CABLE
    /*private void configurarEventosDeDibujoCables(Pane matriz, Runnable onComplete) {
        matriz.setOnMouseClicked(mouseClickedEvent -> {
            double x = mouseClickedEvent.getX();
            double y = mouseClickedEvent.getY();
            boolean cableIniciado = false;

            if (cableActual == null) {
                // Iniciar el dibujo del cable
                if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, matriz, x, y)) {
                    cableActual = new Cables(matriz, colorActual, x, y);
                    cableActual.iniciarDibujoCable(x, y);
                    cableIniciado = true;
                }
            } else {
                // Finalizar el dibujo del cable
                if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, matriz, x, y) || matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y) || matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y)) {
                    cableActual.finalizarDibujoCable(x, y);
                    cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                    botonCableAzul1.toFront();
                    botonCableAzul2.toFront();
                    botonCableRojo1.toFront();
                    botonCableRojo2.toFront();
                    onComplete.run();
                }
            }

            if (!cableIniciado && cableActual != null) {
                // Si no se inició el cable y cableActual no es null, reiniciar cableActual
                cableActual = null;
            }
        });
    }*/
    /*private void configurarEventosDeDibujoCables(List<Pane> matrices, Runnable onComplete) {
        for (Pane matriz : matrices) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                double x = mouseClickedEvent.getX();
                double y = mouseClickedEvent.getY();
                boolean cableIniciado = false;
    
                if (cableActual == null) {
                    // Iniciar el dibujo del cable
                    if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, matriz, x, y) || matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y) || 
                    matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y)) {
                        cableActual = new Cables(matriz, colorActual, x, y);
                        cableActual.iniciarDibujoCable(x, y);
                        cableIniciado = true;
                    }
                } else {
                    // Finalizar el dibujo del cable
                    if (matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, matriz, x, y) || 
                        matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y) || 
                        matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, matriz, x, y)) {
                        cableActual.finalizarDibujoCable(x, y);
                        cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                        onComplete.run();
                    }
                }
            });
        }
    }*/

    // Método para desactivar los eventos de dibujo
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
    }

    @FXML
    void botonLed(MouseEvent event) { //Metodo de la imagen del led
        Led led = new Led();
        led.metodosLed(imagenLed, imagenLed2, paneDibujo);
    }

    @FXML
    void botonSwitch(MouseEvent event) { // Metodo de la imagen del switch
        Switch switch1 = new Switch();
        switch1.metodosSwitch(imagenSwitch, paneDibujo);
        
    }
    @FXML
    void cableAzulInferior(MouseEvent event) { //Metodo para el cable azul inferior
        botonCableAzul2.setOnMouseClicked(clickedEvent -> { //Boton clickeable para el cable azul inferior
            paneDibujo.toFront();//Llevamos al frente el pane para poder dibujar y que deje de ser clickeable el boton
            colorActual = Color.rgb(2,113,245);//Le damos el color del cable
            /*configurarEventosDeDibujoCables(() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo();
            });*/
            botonCableAzul2.setVisible(!botonCableAzul2.isVisible()); //Se hace invisible el boton del cable azul inferior
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
            paneDibujo.toFront();//Llevamos al frente el pane para poder dibujar y que deje de ser clickeable el boton
            colorActual = Color.rgb(2,113,245);//Le damos el color del cable
            /*configurarEventosDeDibujoCables(() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo();
            });*/
            botonCableAzul1.setVisible(!botonCableAzul1.isVisible()); //Se hace invisible el boton del cable azul superior
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
            paneDibujo.toFront();//Llevamos al frente el pane para poder dibujar y que deje de ser clickeable el boton
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            /*configurarEventosDeDibujoCables(() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo();
            });*/
            botonCableRojo2.setVisible(!botonCableRojo2.isVisible()); //Se hace invisible el boton del cable azul inferior
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
            paneDibujo.toFront();//Llevamos al frente el pane para poder dibujar y que deje de ser clickeable el boton
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            /*configurarEventosDeDibujoCables(() -> {
                //Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                desactivarEventosDeDibujo();
            });*/
            botonCableRojo1.setVisible(!botonCableRojo1.isVisible()); //Se hace invisible el boton del cable azul superior
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
        primaryStage.setTitle("Protoboard");
        primaryStage.setScene(new Scene(root,1920,1000));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}