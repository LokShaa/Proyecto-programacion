import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Display {
    private Group displayGroup;
    private Pane pane;
    public Pane[][] matriz;
    private boolean acoplado = false;
    private int filaInicio,filaFin;
    private int colInicio; // Nuevo atributo para almacenar la columna de inicio
    private int[][] matrizEnteros;
    private Timeline timeline; // Declarar el Timeline aquí
    private List<List<Rectangle>> leds; // Lista de listas para almacenar los LEDs
    private Circle ledCircular;
    private boolean displayQuemado = false;
    private boolean led1 = false,led2 = false,led3=false,led4=false,led5 = false,led6=false,led7=false,led8 = false;

    public Display(Pane pane, double x, double y, Pane[][] matriz, int[][] matrizEnteros) {
        this.pane = pane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        this.displayGroup = new Group();
        this.leds = new ArrayList<>();
        dibujarRectanguloMovible(x, y);
        addEventHandlers();
        pane.getChildren().add(displayGroup);
        startMonitoring(); // Iniciar el monitoreo
    }

    private void dibujarRectanguloMovible(double x, double y) {
        crearPatasSuperiores();
        crearPatasInferiores();
        crearRectangulo();
        crearLeds(8); // Crear los LEDs dentro del rectángulo
        crearLedCircular();
        posicionarGrupo(x, y);
    }

    private void crearLeds(int numero) {
        // Definir las posiciones y tamaños de los rectángulos para cada segmento
        double[][] posiciones = {
            {45, 20, 60, 15}, // Segmento superior (más grueso)
            {105, 35, 15, 70}, // Segmento superior derecho (más grueso)
            {105, 115, 15, 70}, // Segmento inferior derecho (más grueso)
            {45, 180, 60, 15}, // Segmento inferior (más grueso)
            {30, 115, 15, 70}, // Segmento inferior izquierdo (más grueso)
            {30, 35, 15, 70}, // Segmento superior izquierdo (más grueso)
            {45, 100, 60, 15} // Segmento medio
        };

        // Crear los rectángulos para el número especificado y almacenarlos en la lista
        for (int i = 0; i < 7; i++) {
            List<Rectangle> segmento = new ArrayList<>();
            Rectangle led = new Rectangle(posiciones[i][0], posiciones[i][1], posiciones[i][2], posiciones[i][3]);
            led.setFill(Color.GRAY); 
            displayGroup.getChildren().add(led);
            segmento.add(led);
            leds.add(segmento);
        }
    }

    private void crearLedCircular() {
        ledCircular = new Circle(10, Color.GRAY);
        ledCircular.setLayoutX(150);
        ledCircular.setLayoutY(190);
        displayGroup.getChildren().add(ledCircular);
    }

    private void crearPatasSuperiores() {
        for (int i = 0; i < 5; i++) {
            Pane pataSuperior = new Pane();
            pataSuperior.setPrefSize(15, 15);
            pataSuperior.setStyle("-fx-background-color: black;");
            pataSuperior.setLayoutX(2 + i * 38.5);
            pataSuperior.setLayoutY(0);
            displayGroup.getChildren().add(pataSuperior);
        }
    }

    private void crearPatasInferiores() {
        for (int i = 0; i < 5; i++) {
            Pane pataInferior = new Pane();
            pataInferior.setPrefSize(15, 15);
            pataInferior.setStyle("-fx-background-color: black;");
            pataInferior.setLayoutX(2 + i * 38.5);
            pataInferior.setLayoutY(195);
            displayGroup.getChildren().add(pataInferior);
        }
    }

    private void crearRectangulo() {
        Rectangle rectangulo = new Rectangle(175, 210);
        rectangulo.setFill(Color.BLACK);
        displayGroup.getChildren().add(rectangulo);
    }

    private void posicionarGrupo(double x, double y) {
        displayGroup.setLayoutX(x);
        displayGroup.setLayoutY(y);
    }

    private void addEventHandlers() {
        displayGroup.setOnMousePressed(event -> {
            if (!acoplado) {
                displayGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), displayGroup.getTranslateX(), displayGroup.getTranslateY()});
            }
        });
    
        displayGroup.setOnMouseDragged(event -> {
            if (!acoplado) {
                double[] data = (double[]) displayGroup.getUserData();
                double deltaX = event.getSceneX() - data[0];
                double deltaY = event.getSceneY() - data[1];
                displayGroup.setTranslateX(data[2] + deltaX);
                displayGroup.setTranslateY(data[3] + deltaY);
                verificarPatas(2, 0, getColInicio());
                verificarPatas(3, 0, getColInicio());
                verificarPatas(4, 0, getColInicio());
                verificarPatas(5, 5, getColInicio());
                verificarPatas(6, 5, getColInicio()); 
                verificarPatas(7, 5, getColInicio());  // Cambiado a 5
            }
        });
    
        displayGroup.setOnMouseReleased(event -> {
            if (!acoplado) {
                acoplarDisplaySiEsPosible();
            }
        });
    
        displayGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                mostrarMenuContextual(event);
            }
        });
    }

    private void mostrarMenuContextual(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
    
        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop(); // Detener el monitoreo
            }
            pane.getChildren().remove(displayGroup);
            if(filaInicio == 2 && filaFin == 5){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(2, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
                }
            }else if(filaInicio == 3 && filaFin == 6){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(3, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(6, colInicio + i, 0);
                }
            }else if(filaInicio == 4 && filaFin == 7){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(7, colInicio + i, 0);
                }
            }
            Main.BotonBateria2();
            Main.BotonBateria3();
        });
    
        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop(); // Detener el monitoreo
            }
            if(filaInicio == 2 && filaFin == 5){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(2, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
                }
            }else if(filaInicio == 3 && filaFin == 6){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(3, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(6, colInicio + i, 0);
                }
            }else if(filaInicio == 4 && filaFin == 7){
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                    Main.matrizCentralProtoboard.setMatrizCables(7, colInicio + i, 0);
                }
            }
            acoplado = false;
        });
    
        contextMenu.getItems().addAll(eliminarItem, editarItem);
        contextMenu.show(displayGroup, event.getScreenX(), event.getScreenY());
    }
    
    private void acoplarDisplaySiEsPosible() {
        for (int col = 0; col <= matriz[0].length - 5; col++) {
            boolean patasSuperioresAcopladas2 = verificarPatas(2, 0, col);
            boolean patasSuperioresAcopladas3 = verificarPatas(3, 0, col);
            boolean patasSuperioresAcopladas4 = verificarPatas(4, 0, col);
            boolean patasInferioresAcopladas5 = verificarPatas(5, 5, col);
            boolean patasInferioresAcopladas6 = verificarPatas(6, 5, col);
            boolean patasInferioresAcopladas7 = verificarPatas(7, 5, col);
    
            if (patasSuperioresAcopladas2 && patasInferioresAcopladas5) {
                acoplado = true;
                displayGroup.setTranslateX(0);
                displayGroup.setTranslateY(0);
                posicionarGrupo(matriz[2][col].getLayoutX(), matriz[2][col].getLayoutY()+2);
                colInicio = col; // Actualizar el atributo colInicio
                startMonitoring(); // Reiniciar el monitoreo
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(2, colInicio + i, 1);
                    Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 1); 
                }
                filaInicio = 2;
                filaFin = 5;
                Main.BotonBateria2();
                Main.BotonBateria3();
                break;
            }else if(patasSuperioresAcopladas3 && patasInferioresAcopladas6){
                acoplado = true;
                displayGroup.setTranslateX(0);
                displayGroup.setTranslateY(0);
                posicionarGrupo(matriz[3][col].getLayoutX(), matriz[3][col].getLayoutY() + 2);
                colInicio = col; // Actualizar el atributo colInicio
                startMonitoring(); // Reiniciar el monitoreo
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(3, colInicio + i, 1);
                    Main.matrizCentralProtoboard.setMatrizCables(6, colInicio + i, 1);
                }
                filaInicio = 3;
                filaFin = 6;
                Main.BotonBateria2();
                Main.BotonBateria3();
                break;
            }else if(patasSuperioresAcopladas4 && patasInferioresAcopladas7){
                acoplado = true;
                displayGroup.setTranslateX(0);
                displayGroup.setTranslateY(0);
                posicionarGrupo(matriz[4][col].getLayoutX(), matriz[4][col].getLayoutY() + 2);
                colInicio = col; // Actualizar el atributo colInicio
                startMonitoring(); // Reiniciar el monitoreo
                for(int i = 0; i < 5; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 1);
                    Main.matrizCentralProtoboard.setMatrizCables(7, colInicio + i, 1);
                }
                filaInicio = 4;
                filaFin = 7;
                Main.BotonBateria2();
                Main.BotonBateria3();
                break;
            }
        }
    }
    
    private boolean verificarPatas(int fila, int offset, int colInicio) {
        boolean todasPatasAcopladas = true;
        for (int i = 0; i < 5; i++) {
            if (i + offset >= displayGroup.getChildren().size()) {
                return false;
            }
            Pane pata = (Pane) displayGroup.getChildren().get(i + offset);
            double pataX = displayGroup.getLayoutX() + displayGroup.getTranslateX() + pata.getLayoutX();
            double pataY = displayGroup.getLayoutY() + displayGroup.getTranslateY() + pata.getLayoutY();
    
            if (fila >= matriz.length || colInicio + i >= matriz[fila].length) {
                return false;
            }
            Pane celda = matriz[fila][colInicio + i];
            double celdaX = celda.getLayoutX();
            double celdaY = celda.getLayoutY();
    
            if (Math.abs(pataX - celdaX) <= 10 && Math.abs(pataY - celdaY) <= 10) {
                celda.setStyle("-fx-background-color: green;");
            } else {
                celda.setStyle("-fx-background-color: black;");
                todasPatasAcopladas = false;
            }
        }
        return todasPatasAcopladas;
    }
    
    private void startMonitoring() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> monitorDisplay()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void monitorDisplay() {
        if (Bateria.banderaBateria) {
            if (comprobarQuemadoDisplay()) {
                displayQuemado = true;
                Main.BotonBateria2();
                Main.BotonBateria3();
            } else {
                actualizarLeds(2, 5);
                actualizarLeds(3, 6);
                actualizarLeds(4, 7);
            }
        } else {
            resetearLeds();
        }
    }

    private boolean comprobarQuemadoDisplay() {
        return (matrizEnteros[2][colInicio + 2] == 1 || matrizEnteros[5][colInicio + 2] == 1 ||
                matrizEnteros[3][colInicio + 2] == 1 || matrizEnteros[6][colInicio + 2] == 1 ||
                matrizEnteros[4][colInicio + 2] == 1 || matrizEnteros[7][colInicio + 2] == 1) && !displayQuemado;
    }

    private void actualizarLeds(int fila1, int fila2) {
        if ((matrizEnteros[fila1][colInicio + 2] == -1 || matrizEnteros[fila2][colInicio + 2] == -1) && !displayQuemado) {
            if(led7 == false){
                setLedState(6, matrizEnteros[fila1][colInicio] == 1);
            }if(led6 == false){
                setLedState(5, matrizEnteros[fila1][colInicio + 1] == 1);
            }if(led1 == false){
                setLedState(0, matrizEnteros[fila1][colInicio + 3] == 1);
            }if(led2 == false){
                setLedState(1, matrizEnteros[fila1][colInicio + 4] == 1);
            }if(led5 == false){
                setLedState(4, matrizEnteros[fila2][colInicio] == 1);
            }if(led4 == false){
                setLedState(3, matrizEnteros[fila2][colInicio + 1] == 1);
            }if(led3 == false){
                setLedState(2, matrizEnteros[fila2][colInicio + 3] == 1);
            }if(led8 == false){
                ledCircular.setFill(matrizEnteros[fila2][colInicio + 4] == 1 ? Color.RED : Color.GRAY);
            }if(matrizEnteros[fila1][colInicio] == -1){
                led7 = true;
            }if(matrizEnteros[fila1][colInicio + 1] == -1){
                led6 = true;
            }if(matrizEnteros[fila1][colInicio + 3] == -1){
                led1 = true;
            }if(matrizEnteros[fila1][colInicio + 4] == -1){
                led2 = true;
            }if(matrizEnteros[fila2][colInicio] == -1){
                led5 = true;
            }if(matrizEnteros[fila2][colInicio + 1] == -1){
                led4 = true;
            }if(matrizEnteros[fila2][colInicio + 3] == -1){
                led3= true;
            }if(matrizEnteros[fila2][colInicio + 4] == -1){
                led8 = true;
            }
        } else {
            resetearLeds();
        }
    }

    private void resetearLeds() {
        setLedState(0, false);
        setLedState(1, false);
        setLedState(2, false);
        setLedState(3, false);
        setLedState(4, false);
        setLedState(5, false);
        setLedState(6, false);
        ledCircular.setFill(Color.GRAY);
    }
    
    public void setLedState(int fila, boolean estado) {
        if (fila >= 0 && fila < leds.size()) {
            for (Rectangle led : leds.get(fila)) {
                led.setFill(estado ? Color.RED : Color.GRAY);
            }
        }
    }
    
    private int getColInicio() {
        double chipX = displayGroup.getLayoutX() + displayGroup.getTranslateX();
        for (int col = 0; col <= matriz[0].length - 7; col++) {
            double celdaX = matriz[4][col].getLayoutX();
            if (Math.abs(chipX - celdaX) <= 10) {
                return col;
            }
        }
        return 0; // Default to 0 if no match found
    }
}