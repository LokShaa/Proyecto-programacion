import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
    private ImageView cableAzulBateriaProto1;
    @FXML
    private ImageView cableAzulBateriaProto2;
    @FXML
    private ImageView cableRojoBateriaProto1;
    @FXML
    private ImageView cableRojoBateriaProto2;
    @FXML
    private ImageView imagenCableGris;
    @FXML
    private ImageView imagenLed;
    @FXML
    private ImageView imagenSwitch;
    @FXML
    private ImageView imagenResistencia;
    @FXML
    private ImageView imagenSwitchOctogonal;
    @FXML
    private ImageView imagenChip;
    @FXML
    private ImageView luzRoja;
    @FXML
    private ImageView luzVerde;
    @FXML
    private ImageView imagenDisplay;
    @FXML
    private Pane matrizPane;
    @FXML
    private Pane matrizPane2;
    @FXML
    private Pane matrizPane21;
    @FXML
    private Pane matrizPaneCableInferiorAzul;
    @FXML
    private Pane matrizPaneCableInferiorRojo;
    @FXML
    private Pane matrizPaneCableSuperiorAzul;
    @FXML
    private Pane matrizPaneCableSuperiorRojo;
    @FXML
    private Pane paneDibujo;
    @FXML
    private ImageView portaBaterias;

    private Cables cableActual;
    private Color colorActual;

    static Protoboard matrizCentralProtoboard = new Protoboard();
    static Protoboard matrizSuperior = new Protoboard();
    static Protoboard matrizInferior = new Protoboard();
    static Protoboard matrizCableSuperiorAzul = new Protoboard();
    static Protoboard matrizCableInferiorAzul = new Protoboard();
    static Protoboard matrizCableSuperiorRojo = new Protoboard();
    static Protoboard matrizCableInferiorRojo = new Protoboard();

    private List<Pane> matricesProto;
    private List<Pane> matrices1;
    private List<Pane> matrices2;
    private Switch switch1;
    private Led led;
    private Resistencia resistencia;

    public static boolean banderaCableAzulInferiorBateria = false;
    public static boolean banderaCableAzulSuperiorBateria = false;
    public static boolean banderaCableRojoInferiorBateria = false;
    public static boolean banderaCableRojoSuperiorBateria = false;

    private static Main instance;

    private int valorSeleccionado = 0;
    private boolean valorSeleccionadoFlag = false;
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    public boolean banderaDibujarSwitch = false;
    private boolean obtenerValorActivo = false; // Bandera para controlar la obtención de valores
    private boolean eventosActivos = false;

    @FXML
    void botonBeteriaMenu(MouseEvent event) {
        bateriaCompleta.setOnMouseClicked(clickedEvent -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem voltaje = new MenuItem("Cambiar voltaje");

            voltaje.setOnAction(e -> {
                TextInputDialog dialogo = new TextInputDialog();
                dialogo.setTitle("Cambiar Voltaje");
                dialogo.setHeaderText("Ingrese el nuevo voltaje:");
                dialogo.setContentText("Voltaje:");

                // Mostrar el diálogo y esperar la entrada del usuario
                dialogo.showAndWait().ifPresent(voltage -> {
                    try {
                        double nuevoVoltage = Double.parseDouble(voltage);
                        if (nuevoVoltage < 1.5 || nuevoVoltage > 12) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error al cambiar el voltaje");
                            alert.setContentText("El voltaje debe estar entre 1.5 y 12 V.");
                            alert.showAndWait();
                            return;
                        }
                        Bateria.voltaje = nuevoVoltage;
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error al cambiar el voltaje");
                        alert.setContentText("El voltaje debe ser un número.");
                        alert.showAndWait();
                    }
                });
            });

            // Añadir los elementos al ContextMenu
            contextMenu.getItems().addAll(voltaje);
            contextMenu.show(bateriaCompleta, event.getScreenX(), event.getScreenY());
        });
    }

    @FXML
    void initialize() {
        instance = this;
        matrizCentralProtoboard.inicializarMatrizCentral(10, 30, 20, 20, 18.6, 20, matrizPane);
        matrizSuperior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane2);
        matrizInferior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane21);
        matrizCableInferiorAzul.inicializarMatrizCablesBateriaAzul(1, 1, 10, 10, 0, 0, matrizPaneCableInferiorAzul);
        matrizCableSuperiorAzul.inicializarMatrizCablesBateriaAzul(1, 1, 10, 10, 0, 0, matrizPaneCableSuperiorAzul);
        matrizCableInferiorRojo.inicializarMatrizCablesBateriaRojo(1, 1, 10, 10, 0, 0, matrizPaneCableInferiorRojo);
        matrizCableSuperiorRojo.inicializarMatrizCablesBateriaRojo(1, 1, 10, 10, 0, 0, matrizPaneCableSuperiorRojo);

        matricesProto = new ArrayList<>();
        matrices1 = new ArrayList<>();
        matrices2 = new ArrayList<>();
        matrices1.add(matrizPane);

        // Se agregan las matrices a una lista que sera utilizada para configurar los eventos de dibujo de cables
        matricesProto.add(matrizPane);
        matricesProto.add(matrizPane2);
        matricesProto.add(matrizPane21);
        matricesProto.add(matrizPaneCableInferiorAzul);
        matricesProto.add(matrizPaneCableSuperiorAzul);
        matricesProto.add(matrizPaneCableInferiorRojo);
        matricesProto.add(matrizPaneCableSuperiorRojo);
    }
    
    public static void actualizarMatriz() {
        if (instance != null) {
            //instance.imprimirMatrices();
           //System.out.println("..............................................................");
        }
    }

    private void imprimirMatrices() {
        // Obtener las matrices de los objetos Protoboard
        int[][] matrizCentral = matrizCentralProtoboard.getMatrizEnteros();
        int[][] matrizsuperior = matrizSuperior.getMatrizEnteros();
        int[][] matrizinferior = matrizInferior.getMatrizEnteros();

         // Imprimir matriz superior
        for (int i = 0; i < matrizsuperior.length; i++) {
            for (int j = 0; j < matrizsuperior[i].length; j++) {
                System.out.print(matrizsuperior[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------");

        // Imprimir matriz central
        for (int i = 0; i < matrizCentral.length; i++) {
            if(i == 5){
                System.out.println("-----------------------------------------------------------");
            }
            for (int j = 0; j < matrizCentral[i].length; j++){
                System.out.print(matrizCentral[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------");

        // Imprimir matriz inferior
        for (int i = 0; i < matrizinferior.length; i++) {
            for (int j = 0; j < matrizinferior[i].length; j++) {
                System.out.print(matrizinferior[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void actualizarEstadoLuz() {
        instance.matrizSuperior.actualizarEstadoLuz(Bateria.banderaBateria);
        instance.matrizInferior.actualizarEstadoLuz(Bateria.banderaBateria);
        instance.matrizCentralProtoboard.actualizarEstadoLuzCentral(Bateria.banderaBateria);
    }
    
    @FXML
    void botonConDesc(ActionEvent event) {
        Bateria bateria = new Bateria();
        bateria.botonConectadoDesconectado(luzRoja,luzVerde,bateriaCortada,bateriaCompleta,portaBaterias);
        actualizarEstadoLuz();
        //imprimirMatrices();
    }
   
    public static void BotonBateria2(){
        instance.botonConDesc(null);
    }

    public static void BotonBateria3(){
        instance.botonConDesc(new ActionEvent());
    }

    @FXML
    void botonCableGris(MouseEvent event) { 
        imagenCableGris.setOnMouseEntered(enteredEvent -> { 
            Glow glowRojo = new Glow(1);
            imagenCableGris.setEffect(glowRojo);
        });

        imagenCableGris.setOnMouseExited(exitEvent -> { 
            imagenCableGris.setEffect(null);
        });

        imagenCableGris.setOnMouseClicked(clickedEvent -> {
            colorActual = Color.rgb(128, 128, 128); 
            obtenerValorActivo = true; 
            eventosActivos = true; // Activamos los eventos de selección/actualización

            configurarEventosDeDibujoCablesProtoboard(matricesProto, () -> {
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                obtenerValorActivo = false; 
                eventosActivos = false; // Desactivar los eventos después de usarlos
            });
        });
    }
    
    @FXML
    void botonResistencia(MouseEvent event) { 
        imagenResistencia.setOnMouseEntered(enteredEvent -> { 
            Glow glowRojo = new Glow(1);
            imagenResistencia.setEffect(glowRojo);
        });
    
        imagenResistencia.setOnMouseExited(exitEvent -> { 
            imagenResistencia.setEffect(null);
        });
        imagenResistencia.setOnMouseClicked(clickedEvent->{
            Pane[][] matrizCentral = matrizCentralProtoboard.getMatriz();
            int[][] matrizEnterosCentral = matrizCentralProtoboard.getMatrizEnteros();
            resistencia = new Resistencia(matrizPane, matrizCentral, matrizEnterosCentral);
            
            desactivarEventosDeDibujo(matrizPane);
    
            matrizPane.setOnMouseClicked(resistencia::handleMouseClick);
        });
    }

    @FXML
    void botonSwitchOctogonal(MouseEvent event) { 
        imagenSwitchOctogonal.setOnMouseEntered(enteredEvent -> { 
            Glow glowRojo = new Glow(1);
            imagenSwitchOctogonal.setEffect(glowRojo);
        });
    
        imagenSwitchOctogonal.setOnMouseExited(exitEvent -> { 
            imagenSwitchOctogonal.setEffect(null);
        });
    
        imagenSwitchOctogonal.setOnMouseClicked(clickedEvent -> {
            // Crear una instancia de SwitchOctogonal
            SwitchOctogonal switchOctogonal = new SwitchOctogonal(matrizCentralProtoboard.getMatrizEnteros(),matrizCentralProtoboard.getMatriz());
    
            // Llamar al método drawSwitch y pasarle el Pane donde se dibujará el switch
            switchOctogonal.drawSwitch(matrizPane);
        });
    }

    @FXML
    void botonChip(MouseEvent event) {
        imagenChip.setOnMouseEntered(enteredEvent -> {
            Glow glowRojo = new Glow(1);
            imagenChip.setEffect(glowRojo);
        });

        imagenChip.setOnMouseExited(exitEvent -> {
            imagenChip.setEffect(null);
        });

        imagenChip.setOnMouseClicked(clickEvent -> {
            // Create a ContextMenu
            ContextMenu contextMenu = new ContextMenu();

            // Create MenuItems
            MenuItem andOption = new MenuItem("AND");
            MenuItem orOption = new MenuItem("OR");
            MenuItem notOption = new MenuItem("NOT");

            // Add MenuItems to ContextMenu
            contextMenu.getItems().addAll(andOption, orOption, notOption);

            // Variable to store the selected option
            final String[] selectedOption = new String[1];

            // Set actions for each MenuItem
            andOption.setOnAction(e -> {
                selectedOption[0] = "AND";
                Chip chip = new Chip(matrizPane, 100, 200,matrizCentralProtoboard.getMatriz(),matrizCentralProtoboard.getMatrizEnteros(),selectedOption[0]);
            });

            orOption.setOnAction(e -> {
                selectedOption[0] = "OR";
                Chip chip = new Chip(matrizPane, 100, 200,matrizCentralProtoboard.getMatriz(),matrizCentralProtoboard.getMatrizEnteros(),selectedOption[0]);
            });

            notOption.setOnAction(e -> {
                selectedOption[0] = "NOT";
                Chip chip = new Chip(matrizPane, 100, 200,matrizCentralProtoboard.getMatriz(),matrizCentralProtoboard.getMatrizEnteros(),selectedOption[0]);
            });

            // Show the ContextMenu at the location of the click
            contextMenu.show(imagenChip, clickEvent.getScreenX(), clickEvent.getScreenY());
        });
    }

    @FXML
    void botonDisplay(MouseEvent event) {
        imagenDisplay.setOnMouseEntered(enteredEvent -> {
            Glow glowRojo = new Glow(1);
            imagenDisplay.setEffect(glowRojo);
        });

        imagenDisplay.setOnMouseExited(exitEvent -> {
            imagenDisplay.setEffect(null);
        });
        
        imagenDisplay.setOnMouseClicked(exitEvent -> {
            Display display = new Display(matrizPane, 100, 200,matrizCentralProtoboard.getMatriz(),matrizCentralProtoboard.getMatrizEnteros());
        });
    }

    private void configurarEventosDeDibujoCablesProtoboard(List<Pane> matrices, Runnable onComplete) {
        final int cellAlt = 20;
        final int cellAncho = 20; 
    
        for (Pane matriz : matrices) {
            if (matriz == matrizPaneCableInferiorAzul && banderaCableAzulInferiorBateria == true) {
                continue;
            }
            if (matriz == matrizPaneCableSuperiorAzul && banderaCableAzulSuperiorBateria == true) {
                continue;
            }
            if (matriz == matrizPaneCableInferiorRojo && banderaCableRojoInferiorBateria == true) {
                continue;
            }
            if (matriz == matrizPaneCableSuperiorRojo && banderaCableRojoSuperiorBateria == true) {
                continue;
            }
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                if (cableActual == null) {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int)(yLocal / cellAlt);
                        int columna = (int)(xLocal / cellAncho);
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            fila -= fila/2;
                            if (fila >= 7){
                                fila -=2;
                            }
                            columna -= columna/2;
                            if (columna > 20 ){
                                columna += 1;
                            }
                            if (fila >= 0 && fila < 10 && columna >= 0 && columna < 30) {
                                int [][] matrizActual1 = matrizCentralProtoboard.getMatrizCables();
                                int [][] matrizActual2 = matrizSuperior.getMatrizCables();
                                int [][] matrizActual3 = matrizInferior.getMatrizCables(); 
                                if(fila>1 && matrizActual == matrizPane){
                                    if (matrizActual1[fila][columna] == 1 && matrizActual == matrizPane) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }
                                    if(matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }
                                }else{
                                    if (matrizActual2[fila][columna] == 1 && matrizActual == matrizPane2) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }else if (matrizActual3[fila][columna] == 1 && matrizActual == matrizPane21) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }
                                    if(matrizSuperior.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane2){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }else if(matrizInferior.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane21){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }
                                }
                                cableActual = new Cables(matrizActual,matrizCentralProtoboard.getMatriz(), colorActual, xLocal, yLocal, matrizCentralProtoboard.getMatrizEnteros(),matrizSuperior.getMatrizEnteros(),matrizSuperior.getMatriz(),matrizInferior.getMatrizEnteros(),matrizInferior.getMatriz()); 
                                cableActual.iniciarDibujoCable(xLocal, yLocal);
                                matrizPane.toFront();
                                if (matrizActual == matrizPane) {
                                    matrizCentralProtoboard.setMatrizCables(fila, columna, 1);
                                }else if (matrizActual == matrizPane2) {
                                    matrizSuperior.setMatrizCables(fila, columna, 1);
                                }else if (matrizActual == matrizPane21) {
                                    matrizInferior.setMatrizCables(fila, columna, 1);
                                }
                                break;
                            }
                        }
                    }
                    if (matriz == matrizPaneCableInferiorAzul ) {
                        banderaCableAzulInferiorBateria = true;
                    }
                    if (matriz == matrizPaneCableSuperiorAzul) {
                        banderaCableAzulSuperiorBateria = true;
                    }
                    if (matriz == matrizPaneCableInferiorRojo) {
                        banderaCableRojoInferiorBateria = true;
                    }
                    if (matriz == matrizPaneCableSuperiorRojo) {
                        banderaCableRojoSuperiorBateria = true;
                    }
                } else {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int) (yLocal / cellAlt);
                        int columna = (int) (xLocal / cellAncho);
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            fila -= fila/2;
                            if (fila >= 7){
                                fila -=2;
                            }
                            columna -=columna/2;
                            if (columna > 20 ){
                                columna += 1;
                            }
                            if (fila >= 0 && fila < 10 && columna >= 0 && columna < 30) {
                                int [][] matrizActual1 = matrizCentralProtoboard.getMatrizCables();  
                                int [][] matrizActual2 = matrizSuperior.getMatrizCables();
                                int [][] matrizActual3 = matrizInferior.getMatrizCables();  
                                if(fila>1){
                                    if (matrizActual1[fila][columna] == 1 && matrizActual == matrizPane) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }
                                    if(matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }
                                }else{
                                    if (matrizActual2[fila][columna] == 1 && matrizActual == matrizPane2) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }else if (matrizActual3[fila][columna] == 1 && matrizActual == matrizPane21) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }
                                    if(matrizSuperior.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane2){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }else if(matrizInferior.getMatrizCortoCircuito()[fila][columna] == 1 && matrizActual == matrizPane21){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }
                                }
    
                                if (cableActual.getPane() != matrizActual) {
                                    cableActual.actualizarPane(matrizActual);
                                }
                                cableActual.finalizarDibujoCable(xLocal, yLocal);
                                if (matrizActual == matrizPane) {
                                    matrizCentralProtoboard.setMatrizCables(fila, columna, 1);
                                }else if (matrizActual == matrizPane2) {
                                    matrizSuperior.setMatrizCables(fila, columna, 1);
                                }else if (matrizActual == matrizPane21) {
                                    matrizInferior.setMatrizCables(fila, columna, 1);
                                }
                                cableActual = null;
                                onComplete.run();
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    public static void setMatrizCables(int fila, int columna, int valor) {
        matrizCentralProtoboard.setMatrizCables(fila, columna, valor);
    }
   
    public static int[][] getMatrizCables() {
        return matrizCentralProtoboard.getMatrizCables();
    }

    public void crearParticulaDeHumoCentral(double x, double y) {
        Circle particula = new Circle(3, Color.GRAY);
        particula.setOpacity(0.5);
        particula.setCenterX(x);
        particula.setCenterY(y);
        matrizPane.getChildren().add(particula);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(particula.translateXProperty(), 0),
                new KeyValue(particula.translateYProperty(), 0),
                new KeyValue(particula.opacityProperty(), 0.5)
            ),
            new KeyFrame(new Duration(3000),
                new KeyValue(particula.translateXProperty(), Math.random() * 200 - 100),
                new KeyValue(particula.translateYProperty(), Math.random() * -200 - 100),
                new KeyValue(particula.opacityProperty(), 0)
            )
        );

        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> {
            matrizPane.getChildren().remove(particula);
        });
        timeline.play();
    }

    public void crearParticulaDeHumoSup(double x, double y) {
        Circle particula = new Circle(3, Color.GRAY);
        particula.setOpacity(0.5);
        particula.setCenterX(x);
        particula.setCenterY(y);
        matrizPane2.getChildren().add(particula);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(particula.translateXProperty(), 0),
                new KeyValue(particula.translateYProperty(), 0),
                new KeyValue(particula.opacityProperty(), 0.5)
            ),
            new KeyFrame(new Duration(3000),
                new KeyValue(particula.translateXProperty(), Math.random() * 200 - 100),
                new KeyValue(particula.translateYProperty(), Math.random() * -200 - 100),
                new KeyValue(particula.opacityProperty(), 0)
            )
        );

        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> {
            matrizPane2.getChildren().remove(particula);
        });
        timeline.play();
    }
    
    public void crearParticulaDeHumoInf(double x, double y) {
        Circle particula = new Circle(3, Color.GRAY);
        particula.setOpacity(0.5);
        particula.setCenterX(x);
        particula.setCenterY(y);
        matrizPane21.getChildren().add(particula);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(particula.translateXProperty(), 0),
                new KeyValue(particula.translateYProperty(), 0),
                new KeyValue(particula.opacityProperty(), 0.5)
            ),
            new KeyFrame(new Duration(3000),
                new KeyValue(particula.translateXProperty(), Math.random() * 200 - 100),
                new KeyValue(particula.translateYProperty(), Math.random() * -200 - 100),
                new KeyValue(particula.opacityProperty(), 0)
            )
        );

        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> {
            matrizPane21.getChildren().remove(particula);
        });
        timeline.play();
    }

    public static void crearParticulaDeHumoEstatico(double x, double y) {
        instance.crearParticulaDeHumoCentral(x, y);
    }
    
    public static void crearParticulaDeHumoEstaticoSup(double x, double y) {
        instance.crearParticulaDeHumoSup(x, y);
    }
    
    public static void crearParticulaDeHumoEstaticoInf(double x, double y) {
        instance.crearParticulaDeHumoInf(x, y);
    }

    // Método auxiliar para comprobar si el clic ocurrió dentro de un cuadrado válido en alguna de las matrices
    private boolean comprobarCuadradoEnMatrices(Pane m, double x, double y) {;
        return matrizCentralProtoboard.comprobarCuadrado(10, 30, 20, 20, 18.6, 20, m, x, y) || matrizSuperior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y) || matrizInferior.comprobarCuadrado(2, 30, 20, 20, 18.6, 20, m, x, y);
    }
  
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
        matriz.setOnMouseEntered(null);
        matriz.setOnMouseExited(null);
    }

    @FXML
    void botonSwitch(MouseEvent event) { // Metodo de la imagen del switch
        imagenSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });
    
        imagenSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            imagenSwitch.setEffect(null);
        });

        imagenSwitch.setOnMouseClicked(clickedEvent ->{
            colorActual = Color.rgb(128,128,128);//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            configurarEventosDeDibujoSwitch(matrices1, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matrices1) {
                    desactivarEventosDeDibujo(matriz);
                }
            });
        });
    }

    private void configurarEventosDeDibujoSwitch(List<Pane> matrices1, Runnable onComplete) {
        final int cellAlt = 20;
        final int cellAncho = 20;
        for (Pane matriz : matrices1) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                // Convertir las coordenadas del clic a coordenadas de la escena
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                if (switch1 == null) {
                    for (Pane matrizActual : matrices1) {
                        double xinicial = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yinicial = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int)(yinicial / cellAlt); // Calcular la fila basada en la coordenada Y
                        int columna = (int)(xinicial / cellAncho); // Calcular la columna basada en la coordenada X
                        if (comprobarCuadradoEnMatrices(matrizActual, xinicial, yinicial)) {
                            fila -= fila/2;
                            if (fila >= 7){
                                fila -=2;
                            }
                            columna -= columna/2;
                            if (columna > 20 ){
                                columna += 1;
                            }
                            if (fila >= 0 && fila < 10 && columna >= 0 && columna < 30) {
                                int [][] matrizActual1 = matrizCentralProtoboard.getMatrizCables();    
                                if (matrizActual1[fila][columna] == 1 && matrizActual == matrizPane) {
                                    mostrarAlerta("El cuadrado ya está ocupado.");
                                    return;
                                }
                                if(matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1){
                                    mostrarAlerta("El cuadrado tiene un corto circuito.");
                                    return;
                                }

                                switch1 = new Switch(matrizActual, colorActual, xinicial, yinicial, imagenSwitch,matrizCentralProtoboard.getMatrizEnteros(),matrizPane);
                                switch1.iniciarDibujoCable(xinicial, yinicial);
                                if (matrizActual == matrizPane) {
                                    matrizCentralProtoboard.setMatrizCables(fila, columna, 1);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    for (Pane matrizActual : matrices1) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int) (yLocal / cellAlt); // Calcular la fila basada en la coordenada Y
                        int columna = (int) (xLocal / cellAncho); // Calcular la columna basada en la coordenada X
                        double distancia = Math.sqrt(Math.pow(xLocal - switch1.getXInicial(), 2) + Math.pow(yLocal - switch1.getYInicial(), 2));
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            if (distancia < 150) {
                                fila -= fila/2;
                                if (fila >= 7){
                                    fila -=2;
                                }
                                columna -=columna/2;
                                if (columna > 20 ){
                                    columna += 1;
                                }
                                if (fila >= 0 && fila < 10 && columna >= 0 && columna < 30) {
                                int [][] matrizActual1 = matrizCentralProtoboard.getMatrizCables();    
                                    if (matrizActual1[fila][columna] == 1) {
                                        mostrarAlerta("El cuadrado ya está ocupado.");
                                        return;
                                    }
                                    if(matrizCentralProtoboard.getMatrizCortoCircuito()[fila][columna] == 1){
                                        mostrarAlerta("El cuadrado tiene un corto circuito.");
                                        return;
                                    }
        
                                    if (switch1.getPane() != matrizActual) {
                                        switch1.actualizarPane(matrizActual, imagenSwitch);
                                    }
                                    switch1.finalizarDibujoCable(xLocal, yLocal, imagenSwitch);
                                    if (matrizActual == matrizPane){
                                        if(fila == 5){
                                            matrizCentralProtoboard.setMatrizCables(fila, columna, 1);
                                            matrizCentralProtoboard.setMatrizCables(fila-1, columna, 1);
                                            matrizCentralProtoboard.setMatrizCables(fila, columna-2, 1);
                                        }
                                        else{
                                            matrizCentralProtoboard.setMatrizCables(fila, columna, 1);
                                            matrizCentralProtoboard.setMatrizCables(fila-2, columna, 1);
                                            matrizCentralProtoboard.setMatrizCables(fila, columna-2, 1);
                                        }

                                        
                                        
                                     }
                                    switch1 = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                                    onComplete.run();
                                    break;
                                }
                            }else{
                                mostrarAlerta("El cuadrado está a mas de 120 pixeles.");
                                return;
                            }
                        }
                    }
                }
            });
        }
    }
    
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML
    public static ColorPicker colorPicker; // Asegúrate de tener un ColorPicker en tu FXML y enlazarlo aquí

    @FXML
    void botonLed(MouseEvent event) { 
        imagenLed.setOnMouseEntered(enteredEvent -> { 
            Glow glowRojo = new Glow(1);
            imagenLed.setEffect(glowRojo);
        });

        imagenLed.setOnMouseExited(exitEvent -> { 
            imagenLed.setEffect(null);
        });

        imagenLed.setOnMouseClicked(clickedEvent -> {
            // Mostrar el ColorPicker para que el usuario elija un color
            colorPicker.setVisible(true);
            colorPicker.setOnAction(colorEvent -> {
                Color colorSeleccionado = colorPicker.getValue();
                // Aplicar el color seleccionado al LED
                imagenLed.setEffect(new Glow(1)); // Puedes ajustar el efecto según el color

                // Inicializar la instancia de Led con el color seleccionado
                Pane[][] matrizCentral = matrizCentralProtoboard.getMatriz();
                int[][] matrizEnterosCentral = matrizCentralProtoboard.getMatrizEnteros();
                led = new Led(matrizPane, matrizCentral, matrizEnterosCentral, colorSeleccionado);

                // Desactivar eventos de dibujo de LED en todas las matrices
                desactivarEventosDeDibujo(matrizPane);

                // Reactivar el evento de dibujo de LED en la matriz principal
                matrizPane.setOnMouseClicked(led::handleMouseClick);

                // Ocultar el ColorPicker después de seleccionar el color
                colorPicker.setVisible(false);
            });
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PrototipoV1.fxml"));
        Parent root = loader.load();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // Ajusta el ancho del contenido al ancho del ScrollPane
        scrollPane.setFitToHeight(true); // Ajusta la altura del contenido a la altura del ScrollPane

        // Configurar las políticas de las barras de desplazamiento
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        primaryStage.setTitle("Protoboard");
        primaryStage.setScene(new Scene(scrollPane, 800, 500));
        primaryStage.show();
        Main.colorPicker = (ColorPicker) loader.getNamespace().get("colorPicker");
    }

    public static void main(String[] args){
        launch(args);
    }
}