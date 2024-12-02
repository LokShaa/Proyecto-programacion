import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Chip {
    private Group chipGroup;
    private Pane pane;
    public Pane[][] matriz;
    private boolean acoplado = false;
    private int colInicio; // Nuevo atributo para almacenar la columna de inicio
    private int[][] matrizEnteros;
    private String tipoString;  
    private Timeline timeline; // Declarar el Timeline aquí

    public Chip(Pane pane, double x, double y, Pane[][] matriz, int[][] matrizEnteros, String tipo,double[][] matrizVoltajes) {
        this.pane = pane;
        this.matriz = matriz;
        this.matrizEnteros = matrizEnteros;
        this.tipoString = tipo;
        this.chipGroup = new Group();
        dibujarRectanguloMovible(x, y);
        addEventHandlers();
        pane.getChildren().add(chipGroup);
        startMonitoring(); // Iniciar el monitoreo
    }

    private void dibujarRectanguloMovible(double x, double y) {
        crearPatasSuperiores();
        crearPatasInferiores();
        crearRectangulo();
        crearTextoTipo();
        posicionarGrupo(x, y);
    }

    private void crearPatasSuperiores() {
        for (int i = 0; i < 7; i++) {
            Pane pataSuperior = new Pane();
            pataSuperior.setPrefSize(15, 15);
            pataSuperior.setStyle("-fx-background-color: gray;");
            pataSuperior.setLayoutX(2 + i * 38.5);
            pataSuperior.setLayoutY(-10);
            chipGroup.getChildren().add(pataSuperior);
        }
    }

    private void crearPatasInferiores() {
        for (int i = 0; i < 7; i++) {
            Pane pataInferior = new Pane();
            pataInferior.setPrefSize(15, 15);
            pataInferior.setStyle("-fx-background-color: gray;");
            pataInferior.setLayoutX(2 + i * 38.5);
            pataInferior.setLayoutY(105);
            chipGroup.getChildren().add(pataInferior);
        }
    }

    private void crearRectangulo() {
        Rectangle rectangulo = new Rectangle(250, 110);
        rectangulo.setFill(Color.BLACK);
        chipGroup.getChildren().add(rectangulo);
    }
    
    private void crearTextoTipo() {
        Text textoTipo = new Text(tipoString);
        textoTipo.setFill(Color.WHITE);
        textoTipo.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        textoTipo.setX(100); // Ajusta la posición X del texto
        textoTipo.setY(55);  // Ajusta la posición Y del texto
        chipGroup.getChildren().add(textoTipo);
    }

    private void posicionarGrupo(double x, double y) {
        chipGroup.setLayoutX(x);
        chipGroup.setLayoutY(y);
    }

    private void addEventHandlers() {
        chipGroup.setOnMousePressed(event -> {
            if (!acoplado) {
                chipGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), chipGroup.getTranslateX(), chipGroup.getTranslateY()});
            }
        });
    
        chipGroup.setOnMouseDragged(event -> {
            if (!acoplado) {
                double[] data = (double[]) chipGroup.getUserData();
                double deltaX = event.getSceneX() - data[0];
                double deltaY = event.getSceneY() - data[1];
                chipGroup.setTranslateX(data[2] + deltaX);
                chipGroup.setTranslateY(data[3] + deltaY);
                verificarPatas(4, 0, getColInicio());
                verificarPatas(5, 7, getColInicio());
            }
        });
    
        chipGroup.setOnMouseReleased(event -> {
            if (!acoplado) {
                acoplarChipSiEsPosible();
            }
        });
    
        chipGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                mostrarMenuContextual(event);
            }
        });
    }

    private void restaurarColorPanes() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setStyle("-fx-background-color: black;");
            }
        }
    }

    private void mostrarMenuContextual(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
    
        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop(); // Detener el monitoreo
            }
            limpiarEstadoMatriz();
            limpiarValoresChip();
            restaurarColorPanes();
            pane.getChildren().remove(chipGroup);
            for(int i = 0; i < 7; i++){
                Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
            }
            Main.BotonBateria2();
            Main.BotonBateria3();
        });
    
        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop(); // Detener el monitoreo
            }
            limpiarEstadoMatriz();
            limpiarValoresChip();
            for(int i = 0; i < 7; i++){
                Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
            }
            acoplado = false;
        });
    
        contextMenu.getItems().addAll(eliminarItem, editarItem);
        contextMenu.show(chipGroup, event.getScreenX(), event.getScreenY());
    }
    
    private void limpiarEstadoMatriz() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matrizEnteros[i][j] != 0) {
                    matrizEnteros[i][j] = 0;
                    matriz[i][j].setStyle("-fx-background-color: black;");
                }
            }
        }
    }
    
    private void limpiarValoresChip() {
        int fila4 = 4;
        int fila5 = 5;
        int colFin = colInicio + 6;
    
        // Limpiar los valores establecidos por el chip en la matriz
        for (int i = 0; i < 5; i++) {
            matrizEnteros[i][colInicio + 2] = 0;
            matriz[i][colInicio + 2].setStyle("-fx-background-color: black;");
            matrizEnteros[i][colInicio + 4] = 0;
            matriz[i][colInicio + 4].setStyle("-fx-background-color: black;");
            matrizEnteros[i][colInicio + 6] = 0;
            matriz[i][colInicio + 6].setStyle("-fx-background-color: black;");
        }
    
        for (int i = 5; i < 10; i++) {
            matrizEnteros[i][colInicio + 1] = 0;
            matriz[i][colInicio + 1].setStyle("-fx-background-color: black;");
            matrizEnteros[i][colInicio + 3] = 0;
            matriz[i][colInicio + 3].setStyle("-fx-background-color: black;");
            matrizEnteros[i][colInicio + 5] = 0;
            matriz[i][colInicio + 5].setStyle("-fx-background-color: black;");
        }
    }
    
    private void acoplarChipSiEsPosible() {
        for (int col = 0; col <= matriz[0].length - 7; col++) {
            boolean patasSuperioresAcopladas = verificarPatas(4, 0, col);
            boolean patasInferioresAcopladas = verificarPatas(5, 7, col);
    
            if (patasSuperioresAcopladas && patasInferioresAcopladas) {
                acoplado = true;
                chipGroup.setTranslateX(0);
                chipGroup.setTranslateY(0);
                posicionarGrupo(matriz[4][col].getLayoutX(), matriz[4][col].getLayoutY() + 10);
                colInicio = col; // Actualizar el atributo colInicio
                startMonitoring(); // Reiniciar el monitoreo
                for(int i = 0; i < 7; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 1);
                    Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 1);
                }
                Main.BotonBateria2();
                Main.BotonBateria3();
                break;
            }
        }
    }
    
    private boolean verificarPatas(int fila, int offset, int colInicio) {
        boolean todasPatasAcopladas = true;
        for (int i = 0; i < 7; i++) {
            Pane pata = (Pane) chipGroup.getChildren().get(i + offset);
            double pataX = chipGroup.getLayoutX() + chipGroup.getTranslateX() + pata.getLayoutX();
            double pataY = chipGroup.getLayoutY() + chipGroup.getTranslateY() + pata.getLayoutY();
    
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
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> monitorChip()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void monitorChip() {
        int fila4 = 4;
        int fila5 = 5;
        int colFin = colInicio + 6;
        double voltaje;
        double voltaje1,voltaje2;
        if (tipoString.equals("NOT")) {
            if (matrizEnteros[fila4][colInicio] == 1 && matrizEnteros[fila5][colFin] == -1) {
                // Revisamos fila 4
                if (matrizEnteros[fila4][colInicio + 1] == 1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 1);
                        matrizEnteros[i][colInicio + 2] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje);
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: blue;");
                    }
                }
                if (matrizEnteros[fila4][colInicio + 1] == -1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 1);
                        matrizEnteros[i][colInicio + 2] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje);
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: red;");
                    }
                }
    
                if (matrizEnteros[fila4][colInicio + 3] == 1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 3);
                        matrizEnteros[i][colInicio + 4] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 4, voltaje);
                        matriz[i][colInicio + 4].setStyle("-fx-background-color: blue;");
                    }
                }
                if (matrizEnteros[fila4][colInicio + 3] == -1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 3);
                        matrizEnteros[i][colInicio + 4] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 4, voltaje);
                        matriz[i][colInicio + 4].setStyle("-fx-background-color: red;");
                    }
                }
    
                if (matrizEnteros[fila4][colInicio + 5] == -1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje);
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: red;");
                    }
                }
                if (matrizEnteros[fila4][colInicio + 5] == 1) {
                    for (int i = 0; i < 5; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje);
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: blue;");
                    }
                }
    
                // Revisamos fila 5
                if (matrizEnteros[fila5][colInicio] == 1) {
                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio);
                        matrizEnteros[i][colInicio + 1] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 1, voltaje);
                        matriz[i][colInicio + 1].setStyle("-fx-background-color: blue;");
                    }
                }
                if (matrizEnteros[fila5][colInicio] == -1) {
                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio);
                        matrizEnteros[i][colInicio + 1] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 1, voltaje);
                        matriz[i][colInicio + 1].setStyle("-fx-background-color: red;");
                    }
                }
    
                if (matrizEnteros[fila5][colInicio + 2] == 1) {
                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje);
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: blue;");
                    }
                }
                if (matrizEnteros[fila5][colInicio + 2] == -1) {
                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje);
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: red;");
                    }
                }
                if (matrizEnteros[fila5][colInicio + 4] == -1) {
                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 4);
                        matrizEnteros[i][colInicio + 5] = 1;
                        Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje);
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: red;");
                    }
                }
                if (matrizEnteros[fila5][colInicio + 4] == 1) {

                    for (int i = 5; i < 10; i++) {
                        voltaje = Cables.getMatrizVoltaje(i, colInicio + 4);
                        matrizEnteros[i][colInicio + 5] = -1;
                        Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje);
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: blue;");
                    }
                }
            }
        }else if(tipoString.equals("AND")){
            if (matrizEnteros[fila4][colInicio] == 1 && matrizEnteros[fila5][colFin] == -1) {
                //revisamos fila 4
                
                if(matrizEnteros[fila4][colInicio + 1] == 1 && matrizEnteros[fila4][colInicio + 2] == 1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: red;");
                    }
                }else if(matrizEnteros[fila4][colInicio + 1] == -1 && matrizEnteros[fila4][colInicio + 2] == -1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: blue;");
                    }
                }else if(matrizEnteros[fila4][colInicio + 1] == 1 && matrizEnteros[fila4][colInicio + 2] == -1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: blue;");
                    }
                }else if (matrizEnteros[fila4][colInicio + 1] == -1 && matrizEnteros[fila4][colInicio + 2] == 1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: blue;");
                    }
                }

                if(matrizEnteros[fila4][colInicio + 4] == 1 && matrizEnteros[fila4][colInicio + 5] == 1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila4][colInicio + 4] == -1 && matrizEnteros[fila4][colInicio + 5] == -1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: blue;");
                    }
                }else if ((matrizEnteros[fila4][colInicio + 4] == 1 && matrizEnteros[fila4][colInicio + 5] == -1) || (matrizEnteros[fila4][colInicio + 4] == -1 && matrizEnteros[fila4][colInicio + 5] == 1)){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: blue;");
                    }
                }else if (matrizEnteros[fila4][colInicio + 4] == -1 && matrizEnteros[fila4][colInicio + 5] == 1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: blue;");
                    }
                }
    
                //revisamos fila 5
                if(matrizEnteros[fila5][colInicio] == 1 && matrizEnteros[fila5][colInicio + 1] == 1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila5][colInicio] == -1 && matrizEnteros[fila5][colInicio + 1] == -1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: blue;");
                    }
                }else if (matrizEnteros[fila5][colInicio] == 1 && matrizEnteros[fila5][colInicio + 1] == -1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: blue;");
                    }
                }else if(matrizEnteros[fila5][colInicio] == -1 && matrizEnteros[fila5][colInicio + 1] == 1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: blue;");
                    }
                }
                
                if(matrizEnteros[fila5][colInicio + 3] == 1 && matrizEnteros[fila5][colInicio + 4] == 1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila5][colInicio + 3] == -1 && matrizEnteros[fila5][colInicio + 4] == -1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: blue;");
                    }
                }else if (matrizEnteros[fila5][colInicio + 3] == 1 && matrizEnteros[fila5][colInicio + 4] == -1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: blue;");
                    }
                }else if(matrizEnteros[fila5][colInicio + 3] == -1 && matrizEnteros[fila5][colInicio + 4] == 1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: blue;");
                    }
                }
            }
        }else if(tipoString.equals("OR")){
            if (matrizEnteros[fila4][colInicio] == 1 && matrizEnteros[fila5][colFin] == -1) {
                //revisamos fila 4
                if ((matrizEnteros[fila4][colInicio + 1] == 1 && matrizEnteros[fila4][colInicio + 2] == -1) || (matrizEnteros[fila4][colInicio + 1] == -1 && matrizEnteros[fila4][colInicio + 2] == 1) || (matrizEnteros[fila4][colInicio + 1] == 1 && matrizEnteros[fila4][colInicio + 2] == 1)){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: red;");
                    }
                }else if(matrizEnteros[fila4][colInicio + 1] == -1 && matrizEnteros[fila4][colInicio + 2] == -1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 1);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 2);
                        matrizEnteros[i][colInicio + 3] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 3, voltaje1);
                        }
                        matriz[i][colInicio + 3].setStyle("-fx-background-color: blue;");
                    }
                }

                if ((matrizEnteros[fila4][colInicio + 4] == 1 && matrizEnteros[fila4][colInicio + 5] == -1) || (matrizEnteros[fila4][colInicio + 4] == -1 && matrizEnteros[fila4][colInicio + 5] == 1) || (matrizEnteros[fila4][colInicio + 4] == 1 && matrizEnteros[fila4][colInicio + 5] == 1)){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila4][colInicio + 4] == -1 && matrizEnteros[fila4][colInicio + 5] == -1){
                    for (int i = 0; i < 5; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio + 4);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio + 5);
                        matrizEnteros[i][colInicio + 6] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 6, voltaje1);
                        }
                        matriz[i][colInicio + 6].setStyle("-fx-background-color: blue;");
                    }
                }

                //revisamos fila 5
                if ((matrizEnteros[fila5][colInicio] == 1 && matrizEnteros[fila5][colInicio+1] == -1) || (matrizEnteros[fila5][colInicio] == -1 && matrizEnteros[fila5][colInicio+1] == 1) || (matrizEnteros[fila5][colInicio] == 1 && matrizEnteros[fila5][colInicio + 1] == 1)){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila5][colInicio] == -1 && matrizEnteros[fila5][colInicio + 1] == -1){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+1);
                        matrizEnteros[i][colInicio + 2] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 2, voltaje1);
                        }
                        matriz[i][colInicio + 2].setStyle("-fx-background-color: blue;");
                    }
                }

                if ((matrizEnteros[fila5][colInicio + 3] == 1 && matrizEnteros[fila5][colInicio + 4] == -1) || (matrizEnteros[fila5][colInicio + 3] == -1 && matrizEnteros[fila5][colInicio + 4] == 1) || (matrizEnteros[fila5][colInicio + 3] == 1 && matrizEnteros[fila5][colInicio + 4] == 1)){
                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = 1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: red;");
                    }
                }else if (matrizEnteros[fila5][colInicio + 3] == -1 || matrizEnteros[fila5][colInicio + 4] == -1){

                    for (int i = 5; i < 10; i++) {
                        voltaje1 = Cables.getMatrizVoltaje(i, colInicio+3);
                        voltaje2 = Cables.getMatrizVoltaje(i, colInicio+4);
                        matrizEnteros[i][colInicio + 5] = -1;
                        if(voltaje1>voltaje2){
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje2);
                        }else{
                            Cables.setMatrizVoltaje2(i, colInicio + 5, voltaje1);
                        }
                        matriz[i][colInicio + 5].setStyle("-fx-background-color: blue;");
                    }
                }
            }
        }
    }
    
    private int getColInicio() {
        double chipX = chipGroup.getLayoutX() + chipGroup.getTranslateX();
        for (int col = 0; col <= matriz[0].length - 7; col++) {
            double celdaX = matriz[4][col].getLayoutX();
            if (Math.abs(chipX - celdaX) <= 10) {
                return col;
            }
        }
        return 0; // Default to 0 if no match found
    }
}