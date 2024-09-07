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
import javafx.scene.shape.Line;

public class Main extends Application{
    @FXML
    private ImageView bateriaCompleta;
    @FXML
    private ImageView bateriaCortada;
    @FXML
    private Button botonBateria;
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
    @FXML
    private ImageView imagenCableGris;
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

    Protoboard matrizCentralProtoboard = new Protoboard();
    Protoboard matrizSuperior = new Protoboard();
    Protoboard matrizInferior = new Protoboard();
    Protoboard matrizCableSuperiorAzul = new Protoboard();
    Protoboard matrizCableInferiorAzul = new Protoboard();
    Protoboard matrizCableSuperiorRojo = new Protoboard();
    Protoboard matrizCableInferiorRojo = new Protoboard();


    private List<Pane> matricesProto;

    //Variables que se ocupan para la creacion de los objetos arrastrables
    private Switch switch1 = new Switch();
    private Led led = new Led();

    public boolean banderaCableAzulInferiorBateria = false;
    public boolean banderaCableAzulSuperiorBateria = false;
    public boolean banderaCableRojoInferiorBateria = false;
    public boolean banderaCableRojoSuperiorBateria = false;
    private static Main instance;

   
    

    @FXML
    void initialize(){
        instance = this;
        matrizCentralProtoboard.inicializarMatrizCentral(10, 30, 20, 20, 18.6, 20, matrizPane);
        matrizSuperior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane2);
        matrizInferior.inicializarMatrizSupInf(2, 30, 20, 20, 18.6, 20, matrizPane21);
        matrizCableInferiorAzul.inicializarMatrizCablesBateriaAzul(1,1, 10, 10, 0, 0, matrizPaneCableInferiorAzul);
        matrizCableSuperiorAzul.inicializarMatrizCablesBateriaAzul(1,1, 10, 10, 0, 0, matrizPaneCableSuperiorAzul);
        matrizCableInferiorRojo.inicializarMatrizCablesBateriaRojo(1,1, 10, 10, 0, 0, matrizPaneCableInferiorRojo);
        matrizCableSuperiorRojo.inicializarMatrizCablesBateriaRojo(1,1, 10, 10, 0, 0, matrizPaneCableSuperiorRojo);

        matricesProto = new ArrayList<>();
        // Se agregan las matrices a una lista que sera utilizada para configurar los eventos de dibujo de cables
        matricesProto.add(matrizPane);
        matricesProto.add(matrizPane2);
        matricesProto.add(matrizPane21);

        // Agregar eventos de clic a las celdas de matrizCentralProtoboard para dibujar cables desde el switch
        Pane[][] matriz = matrizCentralProtoboard.getMatriz();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                Pane cell = matriz[i][j];
                cell.setOnMouseClicked(event -> {
                    Circle selectedCircle = switch1.getSelectedCircle();
                    Circle selectedCircle2 = led.getSelectedCircle();
                    if (selectedCircle != null) {
                        dibujarCableSwitch_Led(selectedCircle, cell);
                        switch1.setSelectedCircle(null); // Deseleccionar el círculo después de dibujar el cable
                    }
                    if(selectedCircle2 != null){
                        dibujarCableSwitch_Led(selectedCircle2, cell);
                        led.setSelectedCircle(null); // Deseleccionar el círculo después de dibujar el cable
                    }
                });
            }
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
    }

    @FXML
    void botonConDesc(ActionEvent event) {
        Bateria bateria = new Bateria();
        bateria.botonConectadoDesconectado(luzRoja,luzVerde,bateriaCortada,bateriaCompleta,portaBaterias);
        actualizarEstadoLuz();
        imprimirMatrices();
    }

    @FXML
    void botonCableGris(MouseEvent event) { //Metodo de la imagen del cable rojo
        imagenCableGris.setOnMouseEntered(enteredEvent -> { //Brillo para el cable
            Glow glowRojo = new Glow(1);
            imagenCableGris.setEffect(glowRojo);
        });

        imagenCableGris.setOnMouseExited(exitEvent -> { //Se quita el brillo del cable
            imagenCableGris.setEffect(null);
        });

        imagenCableGris.setOnMouseClicked(clickedEvent ->{
            colorActual = Color.rgb(128,128,128);//ESTABLECEMOS EL COLOR DEL CABLE QUE SE USARA
            configurarEventosDeDibujoCablesProtoboard(matricesProto, () -> {
                // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
            });
        });
    }

    private void configurarEventosDeDibujoCablesProtoboard(List<Pane> matrices, Runnable onComplete) {
        final int cellAlt = 20; // Altura de la celda
        final int cellAncho = 20; // Ancho de la celda
    
        for (Pane matriz : matrices) {
            matriz.setOnMouseClicked(mouseClickedEvent -> {
                // Convertir las coordenadas del clic a coordenadas de la escena
                double xEscena = mouseClickedEvent.getSceneX();
                double yEscena = mouseClickedEvent.getSceneY();
                if (cableActual == null) {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int)(yLocal / cellAlt); // Calcular la fila basada en la coordenada Y
                        int columna = (int)(xLocal / cellAncho); // Calcular la columna basada en la coordenada X
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            fila -= fila/2;
                            if (fila >= 7){
                                fila -=2;
                            }
                            columna -= columna/2;
                            if (columna > 20 ){
                                columna += 1;
                            }
                            if (fila >= 0 && fila < Protoboard.matrizCables.length && columna >= 0 && columna < Protoboard.matrizCables[0].length) {
                                if (Protoboard.matrizCables[fila][columna] == 1) {
                                    // Si ya hay un cable en esta celda, no permitir iniciar el dibujo
                                    return;
                                }
                                cableActual = new Cables(matrizActual, colorActual, xLocal, yLocal);
                                cableActual.iniciarDibujoCable(xLocal, yLocal);
                                Protoboard.matrizCables[fila][columna] = 1; // Marcar la celda como ocupada
                                break;
                            }
                        }
                    }
                } else {
                    for (Pane matrizActual : matrices) {
                        double xLocal = matrizActual.sceneToLocal(xEscena, yEscena).getX();
                        double yLocal = matrizActual.sceneToLocal(xEscena, yEscena).getY();
                        int fila = (int) (yLocal / cellAlt); // Calcular la fila basada en la coordenada Y
                        int columna = (int) (xLocal / cellAncho); // Calcular la columna basada en la coordenada X
                        if (comprobarCuadradoEnMatrices(matrizActual, xLocal, yLocal)) {
                            fila -= fila/2;
                            if (fila >= 7){
                                fila -=2;
                            }
                            columna -=columna/2;
                            if (columna > 20 ){
                                columna += 1;
                            }
                            if (fila >= 0 && fila < Protoboard.matrizCables.length && columna >= 0 && columna < Protoboard.matrizCables[0].length) {
                                if (Protoboard.matrizCables[fila][columna] == 1) {
                                    return;
                                }
    
                                if (cableActual.getPane() != matrizActual) {
                                    cableActual.actualizarPane(matrizActual);
                                }
                                cableActual.finalizarDibujoCable(xLocal, yLocal);
                                Protoboard.matrizCables[fila][columna] = 1; // Marcar la celda como ocupada
                                cableActual = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                                onComplete.run();
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void configurarEventosDeDibujoCablesProtoboardBateria(List<Pane> matrices,Pane matrizInicial,Runnable onComplete) {
        matrizInicial.setOnMouseClicked(mouseClickedEvent ->{
            
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
            matriz.setOnMouseClicked(mouseClickedEvent ->{
                //Convertir las coordenadas del clic a coordenadas de la escena

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
                        cableActual = null; //Finalizamos el dibujo del cable haciendo que sea null otra vez
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

    // Método para dibujar un cable desde un círculo de un switch a una celda de la matriz central de la protoboard
    private void dibujarCableSwitch_Led(Circle circle, Pane cell) {
        // Verificar si el cable ya fue dibujado desde este círculo
        if (switch1.isCableDibujado(circle)) {
            return;
        }
        if (led.isCableDibujado(circle)) {
            return;
        }

        // Calcular las coordenadas de inicio y fin del cable
        double startX = circle.getParent().getLayoutX() + circle.getCenterX();
        double startY = circle.getParent().getLayoutY() + circle.getCenterY();
        double endX = cell.getParent().getLayoutX() + cell.getLayoutX() + cell.getWidth() / 2;
        double endY = cell.getParent().getLayoutY() + cell.getLayoutY() + cell.getHeight() / 2;

        // Calcular la distancia entre los puntos de inicio y fin
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

        // Dibujar el cable si la distancia es menor o igual a 60 píxeles
        if (distance <= 70) {
            Line cable = new Line();
            cable.setStartX(startX);
            cable.setStartY(startY);
            cable.setEndX(endX);
            cable.setEndY(endY);
            cable.setStroke(Color.rgb(178, 180, 181));
            cable.setStrokeWidth(5);
            paneDibujo.getChildren().add(cable);
            switch1.setCableDibujado(circle, true); // Marcar que el cable ha sido dibujado desde este círculo
            led.setCableDibujado(circle, true); // Marcar que el cable ha sido dibujado desde este círculo|
        } else {
            System.out.println("La distancia es mayor a 70 píxeles. No se dibuja el cable.");
        }
    }

    // Método para desactivar los eventos de dibujo
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
        matriz.setOnMouseEntered(null);
        matriz.setOnMouseExited(null);
    }

    @FXML
    void botonLed(MouseEvent event) { //Metodo de la imagen del led
        Led led = new Led();
        led.brilloLed(imagenLed);
        led.ledArrastrable(imagenLed, imagenLed2, paneDibujo);
    }

    @FXML
    void botonSwitch(MouseEvent event) { // Metodo de la imagen del switch
        Switch switch1 = new Switch();
        switch1.brilloSwitch(imagenSwitch);
        switch1.switchArrastrable(imagenSwitch, paneDibujo);
    }

    @FXML
    void cableAzulInferior(MouseEvent event) { //Metodo para el cable azul inferior
        botonCableAzul2.setOnMouseClicked(clickedEvent -> { // Botón clickeable para el cable azul inferior
            if (banderaCableAzulInferiorBateria == false){
                matrizInferior.configurarManejadoresDeEventosSupInf(-1);
                matrizSuperior.configurarManejadoresDeEventosSupInf(-1);
                colorActual = Color.rgb(2, 113, 245); // Le damos el color del cable
                configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableInferiorAzul, () -> {
                    // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                    for (Pane matriz : matricesProto) {
                        desactivarEventosDeDibujo(matriz);
                    }
                    desactivarEventosDeDibujo(matrizPaneCableInferiorAzul); // Desactivar también en la matriz inicial
                    matrizInferior.desactivarEventosDeDibujo(); // Desactivar eventos en la matriz inferior
                    matrizSuperior.desactivarEventosDeDibujo();
                });
                banderaCableAzulInferiorBateria = true;
            }
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
            if(banderaCableAzulSuperiorBateria == false){
                colorActual = Color.rgb(2,113,245);//Le damos el color del cable
                matrizInferior.configurarManejadoresDeEventosSupInf(-1);
                matrizSuperior.configurarManejadoresDeEventosSupInf(-1);
                configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableSuperiorAzul,() -> {
                    // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                    for (Pane matriz : matricesProto) {
                        desactivarEventosDeDibujo(matriz);
                    }
                    desactivarEventosDeDibujo(matrizPaneCableSuperiorAzul);
                    matrizInferior.desactivarEventosDeDibujo(); // Desactivar eventos en la matriz inferior
                    matrizSuperior.desactivarEventosDeDibujo();
                });
                banderaCableAzulSuperiorBateria = true;
            }
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
            if (banderaCableRojoInferiorBateria == false){
                colorActual = Color.rgb(236,63,39);//Le damos el color del cable
                matrizInferior.configurarManejadoresDeEventosSupInf(1);
                matrizSuperior.configurarManejadoresDeEventosSupInf(1);
                configurarEventosDeDibujoCablesProtoboardBateria(matricesProto,matrizPaneCableInferiorRojo, () -> {
                    // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                    for (Pane matriz : matricesProto) {
                        desactivarEventosDeDibujo(matriz);
                    }
                    desactivarEventosDeDibujo(matrizPaneCableInferiorRojo);
                    matrizInferior.desactivarEventosDeDibujo(); // Desactivar eventos en la matriz inferior
                    matrizSuperior.desactivarEventosDeDibujo();
                });
                banderaCableRojoInferiorBateria = true;
            }
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
            if (banderaCableRojoSuperiorBateria == false){
                colorActual = Color.rgb(236,63,39);//Le damos el color del cable
                matrizInferior.configurarManejadoresDeEventosSupInf(1);
                matrizSuperior.configurarManejadoresDeEventosSupInf(1);
                configurarEventosDeDibujoCablesProtoboardBateria(matricesProto, matrizPaneCableSuperiorRojo,() -> {
                    // Después de dibujar el cable, desactiva la posibilidad de seguir dibujando
                    for (Pane matriz : matricesProto) {
                        desactivarEventosDeDibujo(matriz);
                    }
                    desactivarEventosDeDibujo(matrizPaneCableSuperiorRojo);
                    matrizInferior.desactivarEventosDeDibujo(); // Desactivar eventos en la matriz inferior
                    matrizSuperior.desactivarEventosDeDibujo();
                });
                banderaCableRojoSuperiorBateria = true;
            }
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