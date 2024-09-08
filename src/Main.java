import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.util.Duration;

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
    private Led led;

    public boolean banderaCableAzulInferiorBateria = false;
    public boolean banderaCableAzulSuperiorBateria = false;
    public boolean banderaCableRojoInferiorBateria = false;
    public boolean banderaCableRojoSuperiorBateria = false;

    private static Main instance;
    
    private int valorSeleccionado = 0;
    private boolean valorSeleccionadoFlag = false;
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;

    private boolean obtenerValorActivo = false; // Bandera para controlar la obtención de valores
    private boolean eventosActivos = false;

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
         // Obtener las matrices de Pane y enteros
         Pane[][] matrizCentral = matrizCentralProtoboard.getMatriz();
         int[][] matrizEnterosCentral = matrizCentralProtoboard.getMatrizEnteros();
 
         // Crear una instancia de la clase Led y pasarle las matrices
         led = new Led(matrizPane, matrizCentral, matrizEnterosCentral);
        matricesProto = new ArrayList<>();
        // Se agregan las matrices a una lista que sera utilizada para configurar los eventos de dibujo de cables
        matricesProto.add(matrizPane);
        matricesProto.add(matrizPane2);
        matricesProto.add(matrizPane21);

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
    
    private void configurarEventosDeSeleccion(int[][] matriz, Pane matrizPane) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                final int fila = i;
                final int columna = j;
                Pane cell = (Pane) matrizPane.getChildren().get(i * matriz[i].length + j);
                cell.setOnMouseClicked(event -> {
                    if (eventosActivos){ 
                        valorSeleccionado = matriz[fila][columna];
                        valorSeleccionadoFlag = true;
                        filaSeleccionada = fila;
                        columnaSeleccionada = columna;
                        System.out.println("Valor seleccionado: " + valorSeleccionado);
                    }
                });
            }
        }
    }

    private void configurarEventosDeActualizacion(int[][] matrizEnteros, Pane matrizPane) {
        for (int i = 0; i < matrizEnteros.length; i++) {
            for (int j = 0; j < matrizEnteros[i].length; j++) {
                final int fila = i;
                final int columna = j;
                Pane cell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[i].length + j);
                cell.setOnMouseClicked(event -> {
                    // Verificamos si los eventos están activos antes de proceder
                    if (eventosActivos) {
                        if (valorSeleccionadoFlag) {
                            if (matrizEnteros[filaSeleccionada][columnaSeleccionada] == 0 && (matrizEnteros[fila][columna] == 1 || matrizEnteros[fila][columna] == -1)) {
                                // Cambiar la columna completa donde fue el primer clic
                                if (filaSeleccionada >= 0 && filaSeleccionada < 5) {
                                    for (int k = 0; k < 5; k++) {
                                        matrizEnteros[k][columnaSeleccionada] = matrizEnteros[fila][columna];
                                        Pane targetCell = (Pane) matrizPane.getChildren().get(k * matrizEnteros[k].length + columnaSeleccionada);
                                        if (matrizEnteros[fila][columna] != 0) {
                                            targetCell.setStyle("-fx-background-color: yellow;");
                                        }
                                    }
                                } else if (filaSeleccionada >= 5 && filaSeleccionada < 10) {
                                    for (int k = 5; k < 10; k++) {
                                        matrizEnteros[k][columnaSeleccionada] = matrizEnteros[fila][columna];
                                        Pane targetCell = (Pane) matrizPane.getChildren().get(k * matrizEnteros[k].length + columnaSeleccionada);
                                        if (matrizEnteros[fila][columna] != 0) {
                                            targetCell.setStyle("-fx-background-color: yellow;");
                                        }
                                    }
                                }
                                System.out.println("Columna actualizada debido a la condición especial.");
                            } else {
                                //Lógica existente para actualizar la columna
                                if (fila >= 0 && fila < 5) {
                                    for (int k = 0; k < 5; k++) {
                                        matrizEnteros[k][columna] = valorSeleccionado;
                                        Pane targetCell = (Pane) matrizPane.getChildren().get(k * matrizEnteros[k].length + columna);
                                        if (valorSeleccionado != 0) {
                                            targetCell.setStyle("-fx-background-color: yellow;");
                                        }
                                    }
                                } else if (fila >= 5 && fila < 10) {
                                    for (int k = 5; k < 10; k++) {
                                        matrizEnteros[k][columna] = valorSeleccionado;
                                        Pane targetCell = (Pane) matrizPane.getChildren().get(k * matrizEnteros[k].length + columna);
                                        if (valorSeleccionado != 0) {
                                            targetCell.setStyle("-fx-background-color: yellow;");
                                        }
                                    }
                                }
                                System.out.println("Columna actualizada con el valor: " + valorSeleccionado);
                            }
                            valorSeleccionadoFlag = false;
                            eventosActivos = false; //Desactivar eventos después de usar
                        } else {
                            valorSeleccionado = matrizEnteros[fila][columna];
                            valorSeleccionadoFlag = true;
                            filaSeleccionada = fila;
                            columnaSeleccionada = columna;
                            //Mensaje en la terminal mostrando el valor seleccionado
                            System.out.println("Valor seleccionado: " + valorSeleccionado + " en la fila " + filaSeleccionada + ", columna " + columnaSeleccionada);
                        }
                    }
                });
            }
        }
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
            configurarEventosDeSeleccion(matrizSuperior.getMatrizEnteros(), matrizPane2);
            configurarEventosDeSeleccion(matrizInferior.getMatrizEnteros(), matrizPane21);
            configurarEventosDeActualizacion(matrizCentralProtoboard.getMatrizEnteros(), matrizPane);

            configurarEventosDeDibujoCablesProtoboard(matricesProto, () -> {
                for (Pane matriz : matricesProto) {
                    desactivarEventosDeDibujo(matriz);
                }
                obtenerValorActivo = false; 
                eventosActivos = false; // Desactivar los eventos después de usarlos
            });
        });
}
    
    private void configurarEventosDeDibujoCablesProtoboard(List<Pane> matrices, Runnable onComplete) {
        final int cellAlt = 20;
        final int cellAncho = 20; 
    
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
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.setTitle("Información");
                                    alert.setHeaderText(null);
                                    alert.setContentText("El cuadrado ya está ocupado.");
                                    alert.showAndWait();
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
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.setTitle("Información");
                                    alert.setHeaderText(null);
                                    alert.setContentText("El cuadrado ya está ocupado.");
                                    alert.showAndWait();
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
  
    // Método para desactivar los eventos de dibujo
    private void desactivarEventosDeDibujo(Pane matriz) {
        matriz.setOnMouseClicked(null);
        matriz.setOnMouseEntered(null);
        matriz.setOnMouseExited(null);
    }

    @FXML
    void botonLed(MouseEvent event) { // Método de la imagen del led
        imagenLed.setOnMouseEntered(enteredEvent -> { // Brillo para el cable
            Glow glowRojo = new Glow(1);
            imagenLed.setEffect(glowRojo);
        });

        imagenLed.setOnMouseExited(exitEvent -> { // Se quita el brillo del cable
            imagenLed.setEffect(null);
        });

        // Configurar el evento de clic en matrizPane
        matrizPane.setOnMouseClicked(led::handleMouseClick);
    }

    @FXML
    void botonSwitch(MouseEvent event){ // Metodo de la imagen del switch
        Switch switch1 = new Switch();
        switch1.brilloSwitch(imagenSwitch);
        switch1.switchArrastrable(imagenSwitch, paneDibujo);
    }

    @FXML
    void cableAzulInferior(MouseEvent event){ //Metodo para el cable azul inferior
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
    void cableAzulSuperior(MouseEvent event){ //Metodo para el cable azul superior
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
    void cableRojoInferior(MouseEvent event){ //Metodo para el cable rojo inferior
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
    void cableRojoSuperior(MouseEvent event){ //Metodo para el cable rojo superior
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