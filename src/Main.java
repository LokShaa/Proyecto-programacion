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
    @FXML
    private Pane matrizPaneCableInferiorRojo;
    @FXML
    private Pane matrizPaneCableInferiorAzul;
    @FXML
    private Pane matrizPaneCableSuperiorRojo;
    @FXML
    private Pane matrizPaneCableSuperiorAzul;

    Protoboard matrizCentralProtoboard = new Protoboard();
    Protoboard matrizSuperior = new Protoboard();
    Protoboard matrizInferior = new Protoboard();
    Protoboard matrizCableSuperiorAzul = new Protoboard();
    Protoboard matrizCableInferiorAzul = new Protoboard();
    Protoboard matrizCableSuperiorRojo = new Protoboard();
    Protoboard matrizCableInferiorRojo = new Protoboard();

    private List<Pane> matricesProto;

    @FXML
    void initialize(){
        matrizCentralProtoboard.inicializarMatrizCentral(10, 30, 20, 20, 18.6, 20, matrizPane);
        matrizSuperior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane2);
        matrizInferior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane21);
        //matrizCablesSuperiores.inicializarMatrizCablesBateria(1,2, 15, 17, 31, 20, matrizPaneCablesSuperiores);
        matrizCableInferiorAzul.inicializarMatrizCablesBateriaAzul(1,1, 10, 10, 0, 0, matrizPaneCableInferiorAzul);
        matrizCableSuperiorAzul.inicializarMatrizCablesBateriaAzul(1,1, 10, 10, 0, 0, matrizPaneCableSuperiorAzul);
        matrizCableInferiorRojo.inicializarMatrizCablesBateriaRojo(1,1, 10, 10, 0, 0, matrizPaneCableInferiorRojo);
        matrizCableSuperiorRojo.inicializarMatrizCablesBateriaRojo(1,1, 10, 10, 0, 0, matrizPaneCableSuperiorRojo);

        matricesProto = new ArrayList<>();
        // Se agregan las matrices a una lista que sera utilizada para configurar los eventos de dibujo de cables
        matricesProto.add(matrizPane);
        matricesProto.add(matrizPane2);
        matricesProto.add(matrizPane21);
    }

    @FXML
    void botonConDesc(ActionEvent event) {
        Bateria bateria = new Bateria();
        bateria.botonConectadoDesconectado(luzRoja,luzVerde,bateriaCortada,bateriaCompleta,portaBaterias);
    }

    @FXML
    void botonCableAzul(MouseEvent event){
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
            configurarEventosDeDibujoCablesProtoboard(matricesProto,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
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
            configurarEventosDeDibujoCablesProtoboard(matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }

            });
        });
    }
    
    private void configurarEventosDeDibujoCablesProtoboard(List<Pane> matrices, Runnable onComplete) {
        for (Pane matriz : matrices) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                // Convertir las coordenadas del clic a coordenadas de la escena
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                if (cableActual == null) {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            cableActual = new Cables(matrizActual, colorActual, xLocal, yLocal);
                            cableActual.iniciarDibujoCable(xLocal, yLocal);
                            break;
                        }
                    }
                } else {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            if (cableActual.getPane() != matrizActual) {
                                cableActual.actualizarPane(matrizActual);
                            }
                            cableActual.finalizarDibujoCable(xLocal, yLocal);
                            cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                            onComplete.run();
                            break;
                        }
                    }
                }
            });
        }
    }
    
    private void configurarEventosDeDibujoCablesProtoboardBateria(List<Pane> matrices,Pane matrizInicial,Runnable onComplete) {
        matrizInicial.setOnMouseClicked(mouseClickedEvent -> {
            // Convertir las coordenadas del clic a coordenadas de la escena
            double xEscena = mouseClickedEvent.getSceneX();
            double yEscena = mouseClickedEvent.getSceneY();
            double xLocal = matrizInicial.sceneToLocal(xEscena, yEscena).getX();
            double yLocal = matrizInicial.sceneToLocal(xEscena, yEscena).getY();
    
            if (cableActual == null) {
                if (comprobarCuadradoEnMatricesBateria(matrizInicial, xLocal, yLocal)) {
                    cableActual = new Cables(matrizInicial, colorActual, xLocal, yLocal);
                    cableActual.iniciarDibujoCable(xLocal, yLocal);
                }
            }
        });
        
        for (Pane matriz : matrices) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                // Convertir las coordenadas del clic a coordenadas de la escena
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                double xLocal = matriz.sceneToLocal(xEscena, yEscena).getX();
                double yLocal = matriz.sceneToLocal(xEscena, yEscena).getY();
    
                if (cableActual != null) {
                    if (comprobarCuadradoEnMatrices(matriz, xLocal, yLocal)) {
                        if (cableActual.getPane() != matriz) {
                            cableActual.actualizarPane(matriz);
                        }
                        cableActual.finalizarDibujoCable(xLocal, yLocal);
                        cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                        onComplete.run();
                    }
                } 
            });
        }
    }
    
    // Método auxiliar para comprobar si el clic ocurrió dentro de un cuadrado válido en alguna de las matrices
    private boolean comprobarCuadradoEnMatrices(Pane m, double x, double y) {;
        return matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, m, x, y) || matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y) || matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y);
    }
    private boolean comprobarCuadradoEnMatricesBateria(Pane m, double x, double y) {;
        return matrizCableInferiorAzul.comprobarCuadrado(1, 1, 10, 10, 0, 0, m, x, y) ;
    }

    // Método para desactivar los eventos de dibujo
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
    }

    @FXML
    void botonLed(MouseEvent event){ //Metodo de la imagen del led
        Led led = new Led();
        led.metodosLed(imagenLed, imagenLed2, paneDibujo);
    }

    @FXML
    void botonSwitch(MouseEvent event){ // Metodo de la imagen del switch
        Switch switch1 = new Switch();
        switch1.metodosSwitch(imagenSwitch, paneDibujo);
    }
    
    
    @FXML
    void cableAzulInferior(MouseEvent event) { //Metodo para el cable azul inferior
        botonCableAzul2.setOnMouseClicked(clickedEvent -> { // Botón clickeable para el cable azul inferior
            colorActual = Color.rgb(2, 113, 245); // Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableInferiorAzul, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                desactivarEventosDeDibujo(matrizPaneCableInferiorAzul); // Desactivar también en la matriz inicial
            });
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
            colorActual = Color.rgb(2,113,245);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableSuperiorAzul,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                desactivarEventosDeDibujo(matrizPaneCableSuperiorAzul);
            });
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
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesProto,matrizPaneCableInferiorRojo, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                desactivarEventosDeDibujo(matrizPaneCableInferiorRojo);
            });
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
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableSuperiorRojo,() -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                desactivarEventosDeDibujo(matrizPaneCableSuperiorRojo);
            });
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