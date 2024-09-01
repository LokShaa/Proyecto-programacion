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
import javafx.geometry.Bounds;

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
    private Pane matrizPaneCablesInferiores;
    @FXML
    private Pane matrizPaneCablesSuperiores;

    Protoboard matrizCentralProtoboard = new Protoboard();
    Protoboard matrizSuperior = new Protoboard();
    Protoboard matrizInferior = new Protoboard();
    Protoboard matrizCablesSuperiores = new Protoboard();
    Protoboard matrizCablesInferiores = new Protoboard();

    private List<Pane> matricesProto;
    private List<Pane> matricesCables;

    // Método para comprobar si el clic está dentro de los límites de un Pane específico
private boolean esClickEnPane(Pane pane, double x, double y) {
    Bounds bounds = pane.getBoundsInParent();
    return x >= bounds.getMinX() && x <= bounds.getMaxX() && y >= bounds.getMinY() && y <= bounds.getMaxY();
}
   
    
    @FXML
    void initialize(){
        matrizCentralProtoboard.inicializarMatrizCentral(10, 30, 20, 20, 18.6, 20, matrizPane);
        matrizSuperior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane2);
        matrizInferior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane21);
        matrizCablesSuperiores.inicializarMatrizCablesBateria(1,2, 15, 17, 31, 20, matrizPaneCablesSuperiores);
        matrizCablesInferiores.inicializarMatrizCablesBateria(2,1, 15, 17, 31, 8, matrizPaneCablesInferiores);
        matrizPaneCablesInferiores.setVisible(false);
        matrizPaneCablesSuperiores.setVisible(false);
        
        matricesCables = new ArrayList<>();
        matricesCables.add(matrizPaneCablesSuperiores);
        matricesCables.add(matrizPaneCablesInferiores);
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
            configurarEventosDeDibujoCablesProtoboard(matricesProto, () -> {
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
    
    private void configurarEventosDeDibujoCablesProtoboardBateria(List<Pane> matricesC, List<Pane> matricesProtoboard,Runnable onComplete) {
        for (Pane matriz : matricesC) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                // Convertir las coordenadas del clic a coordenadas de la escena
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                if (cableActual == null) {
                    for (Pane matrizActual : matricesC) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        //if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            cableActual = new Cables(matrizActual, colorActual, xLocal, yLocal);
                            cableActual.iniciarDibujoCable(xLocal, yLocal);
                            break;
                        //}
                    }
                } else {
                    for (Pane matrizActual : matricesProtoboard) {
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
    // Método para desactivar los eventos de dibujo
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
    }
    // Método auxiliar para comprobar si el clic ocurrió dentro de un cuadrado válido en alguna de las matrices
    private boolean comprobarCuadradoEnMatrices(Pane m, double x, double y) {;
        return matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, m, x, y) || matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y) || matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y);
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
            matrizPaneCablesInferiores.setVisible(true);//Hacemos visible la matriz de cables inferiores
            colorActual = Color.rgb(2,113,245);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesCables,matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
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
            matrizPaneCablesSuperiores.setVisible(true);//Hacemos visible la matriz de cables inferiores
            colorActual = Color.rgb(2,113,245);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesCables,matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
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
            matrizPaneCablesInferiores.setVisible(true);//Hacemos visible la matriz de cables inferiores
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesCables,matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
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
            matrizPaneCablesSuperiores.setVisible(true);//Hacemos visible la matriz de cables inferiores
            colorActual = Color.rgb(236,63,39);//Le damos el color del cable
            configurarEventosDeDibujoCablesProtoboardBateria(matricesCables,matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
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