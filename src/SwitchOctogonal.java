import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.util.Duration;

public class SwitchOctogonal {

    private int[][] matrizEnteros;
    public Pane[][] matriz;
    private Pane[] topPanes;
    private Pane[] bottomPanes;
    private Group switchGroup;
    private Pane root;
    private boolean acoplado = false;
    private int colInicio; // Nuevo atributo para almacenar la columna de inicio
    private Timeline timeline;
    private boolean[] rectanguloEstado; // Array para almacenar el estado de los rectángulos clickeables
    private double[][] matrizvoltaje;

    private Bateria bateria;
    private double voltaje;

    public SwitchOctogonal(int[][] matrizEnteros, Pane[][] matriz,double[][] matrizvoltaje) {
        this.matrizEnteros = matrizEnteros;
        this.matriz = matriz;
        this.rectanguloEstado = new boolean[8]; // Inicializar el array de estados
        this.matrizvoltaje = matrizvoltaje;
        bateria = new Bateria();
    }

    public void drawSwitch(Pane root){
        this.root = root;
        switchGroup = new Group();

        // Crear 8 panes superiores
        topPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane topPane = new Pane();
            topPane.setPrefSize(20, 20);
            topPane.setStyle("-fx-background-color: gray;");
            topPane.setLayoutX(2 + i * 38.5);
            topPane.setLayoutY(-10);
            topPanes[i] = topPane;
            switchGroup.getChildren().add(topPane);
        }

        // Crear 8 panes inferiores
        bottomPanes = new Pane[8];
        for (int i = 0; i < 8; i++) {
            Pane bottomPane = new Pane();
            bottomPane.setPrefSize(20, 20);
            bottomPane.setStyle("-fx-background-color: gray;");
            bottomPane.setLayoutX(2 + i * 38.5);
            bottomPane.setLayoutY(90);
            bottomPanes[i] = bottomPane;
            switchGroup.getChildren().add(bottomPane);
        }

        // Crear el rectángulo rojo
        Rectangle redRectangle = new Rectangle(300, 100, Color.RED);
        switchGroup.getChildren().add(redRectangle);

        // Crear 8 rectángulos clickeables en el medio
        Rectangle[] clickableRectangles = new Rectangle[8];
        for (int i = 0; i < 8; i++) {
            Rectangle clickableRectangle = new Rectangle(20, 40, Color.WHITE);
            clickableRectangle.setLayoutX(2 + i * 38.5);
            clickableRectangle.setY(30);
            final int index = i; // Necesario para usar en el EventHandler
            clickableRectangle.setOnMouseClicked(event -> handleRectangleClick(event, index));
            clickableRectangles[i] = clickableRectangle;
            switchGroup.getChildren().add(clickableRectangle);

            Text numberText = new Text(String.valueOf(i + 1));
            numberText.setLayoutX(2 + i * 38.5 + 5); // Ajustar la posición X para centrar el texto
            numberText.setLayoutY(90); // Ajustar la posición Y para colocar el texto debajo del rectángulo
            numberText.setFill(Color.WHITE);
            numberText.setStyle("-fx-font-size: 15; -fx-font-weight: bold;"); // Poner el texto en negrita
            switchGroup.getChildren().add(numberText);
        }

        // Posicionar el grupo en las coordenadas especificadas
        switchGroup.setLayoutX(50);
        switchGroup.setLayoutY(50);

        // Añadir manejadores de eventos para hacer el grupo movible
        switchGroup.setOnMousePressed(event -> {
            if (!acoplado) {
                switchGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY(), switchGroup.getTranslateX(), switchGroup.getTranslateY()});
            }
        });

        switchGroup.setOnMouseDragged(event -> {
            if (!acoplado) {
                double[] data = (double[]) switchGroup.getUserData();
                double deltaX = event.getSceneX() - data[0];
                double deltaY = event.getSceneY() - data[1];
                switchGroup.setTranslateX(data[2] + deltaX);
                switchGroup.setTranslateY(data[3] + deltaY);
                verificarPatas(4, 0, getColInicio());
                verificarPatas(5, 8, getColInicio());
            }
        });

        switchGroup.setOnMouseReleased(event -> {
            if (!acoplado) {
                acoplarSwitchSiEsPosible();
            }
        });

        switchGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                mostrarMenuContextual(event);
            }
        });

        root.getChildren().add(switchGroup);
    }

    private void mostrarMenuContextual(MouseEvent event){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> {
            root.getChildren().remove(switchGroup);
            if (timeline != null) {
                timeline.stop();
            }
            for(int i = 0; i < 8; i++){
                Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
            }
            Main.BotonBateria2();
            Main.BotonBateria3();
        });

        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> {
            acoplado = false;
            timeline.stop(); // Detener el monitoreo constante
            for(int i = 0; i < 8; i++){
                Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 0);
                Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 0);
            }
            Main.BotonBateria2();
            Main.BotonBateria3();
        });
        contextMenu.getItems().addAll(eliminarItem, editarItem);
        contextMenu.show(switchGroup, event.getScreenX(), event.getScreenY());
    }

    private void acoplarSwitchSiEsPosible(){
        for (int col = 0; col <= matriz[0].length - 8; col++) {
            boolean patasSuperioresAcopladas = verificarPatas(4, 0, col);
            boolean patasInferioresAcopladas = verificarPatas(5, 8, col);

            if (patasSuperioresAcopladas && patasInferioresAcopladas) {
                acoplado = true;
                switchGroup.setTranslateX(0);
                switchGroup.setTranslateY(0);
                posicionarGrupo(matriz[4][col].getLayoutX(), matriz[4][col].getLayoutY() + 15);
                colInicio = col; // Actualizar el atributo colInicio
                iniciarMonitoreo(); // Iniciar el monitoreo constante
                for(int i = 0; i < 8; i++){
                    Main.matrizCentralProtoboard.setMatrizCables(4, colInicio + i, 1);
                    Main.matrizCentralProtoboard.setMatrizCables(5, colInicio + i, 1);
                }
                Main.BotonBateria2();
                Main.BotonBateria3();
                break;
            }
        }
    }

    private boolean verificarPatas(int fila, int offset, int colInicio){
        boolean todasPatasAcopladas = true;
        for (int i = 0; i < 8; i++) {
            Pane pata = (Pane) switchGroup.getChildren().get(i + offset);
            double pataX = switchGroup.getLayoutX() + switchGroup.getTranslateX() + pata.getLayoutX();
            double pataY = switchGroup.getLayoutY() + switchGroup.getTranslateY() + pata.getLayoutY();

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

    private int getColInicio(){
        double switchX = switchGroup.getLayoutX() + switchGroup.getTranslateX();
        for (int col = 0; col <= matriz[0].length - 8; col++) {
            double celdaX = matriz[4][col].getLayoutX();
            if (Math.abs(switchX - celdaX) <= 10) {
                return col;
            }
        }
        return 0; // Default to 0 if no match found
    }

    private void posicionarGrupo(double x, double y){
        switchGroup.setLayoutX(x);
        switchGroup.setLayoutY(y);
    }

    private void handleRectangleClick(MouseEvent event, int index) {
        int col = colInicio + index;
        boolean energiaPositivaArriba = matrizEnteros[4][col] == 1;
        boolean energiaNegativaAbajo = matrizEnteros[5][col] == -1;

        boolean energiaNegativaArriba = matrizEnteros[4][col] == -1;
        boolean energiaPositivaAbajo = matrizEnteros[5][col] == 1;

       
    
        if ((energiaPositivaArriba && energiaNegativaAbajo) || (energiaNegativaArriba && energiaPositivaAbajo) ) {
            // Quemar el botón
            Rectangle rect = (Rectangle) event.getSource();
            rect.setFill(Color.BLACK);
            rect.setDisable(true); // Deshabilitar el rectángulo
        } else {
            // Cambiar el estado del rectángulo
            rectanguloEstado[index] = !rectanguloEstado[index];
            Rectangle rect = (Rectangle) event.getSource();
            if (rectanguloEstado[index]) {
                rect.setFill(Color.LIGHTGRAY);
                rect.setY(20); // Mover el rectángulo hacia arriba
            } else {
                rect.setFill(Color.WHITE);
                rect.setY(30); // Mover el rectángulo hacia abajo
            }
            Main.BotonBateria2();
            Main.BotonBateria3();
        }
    }

    private void iniciarMonitoreo(){
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> monitorearEstado()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void monitorearEstado(){
        boolean[] fila4Actualizada = new boolean[8];
        boolean[] fila5Actualizada = new boolean[8];
        
        voltaje = bateria.getVoltaje();
        for (int i = 0; i < 8; i++) {
            int col = colInicio + i;
            if (rectanguloEstado[i]) {
                if (matrizEnteros[5][col] == 1 || matrizEnteros[5][col] == -1){
                    int valor = matrizEnteros[5][col];
                    for (int fila = 0; fila <= 4; fila++) {
                        matrizEnteros[fila][col] = valor;
                        matrizvoltaje[fila][col] = voltaje;

                        cambiarColorCelda(fila, col, valor);
                    }
                    fila5Actualizada[i] = true;
                }
                if (matrizEnteros[4][col] == 1 || matrizEnteros[4][col] == -1){
                    int valor = matrizEnteros[4][col];
                    for (int fila = 5; fila <= 9; fila++) {
                        matrizEnteros[fila][col] = valor;
                        matrizvoltaje[fila][col] = voltaje;
                        cambiarColorCelda(fila, col, valor);
                    }
                    fila4Actualizada[i] = true;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            int col = colInicio + i;
            if (rectanguloEstado[i]) {
                if (fila5Actualizada[i] && (matrizEnteros[4][col] == 1 || matrizEnteros[4][col] == -1)) {
                    int valor = matrizEnteros[4][col];
                    for (int fila = 5; fila <= 9; fila++) {
                        matrizEnteros[fila][col] = valor;
                        matrizvoltaje[fila][col] = voltaje;
                        cambiarColorCelda(fila, col, valor);
                    }
                }
                if (fila4Actualizada[i] && (matrizEnteros[5][col] == 1 || matrizEnteros[5][col] == -1)) {
                    int valor = matrizEnteros[5][col];
                    for (int fila = 0; fila <= 4; fila++) {
                        matrizEnteros[fila][col] = valor;
                        matrizvoltaje[fila][col] = voltaje;
                        cambiarColorCelda(fila, col, valor);
                    }
                }
            }
        }
    }

    private void cambiarColorCelda(int fila, int col, int valor){
        Pane targetCell = matriz[fila][col];
        if (valor == 1) {
            targetCell.setStyle("-fx-background-color: red;");
        } else if (valor == -1) {
            targetCell.setStyle("-fx-background-color: blue;");
        } else {
            targetCell.setStyle("-fx-background-color: black;");
        }
    }
}