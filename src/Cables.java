import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Cables extends Line {
    private Pane pane;
    private static final int CELL_SIZE = 20;
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;

    private static int[][] matrizEnteros;
    private Pane[][] matrizPane;

    private int[][] matriSup;
    private Pane[][] matrizPaneSup;

    private int[][] matriInf;
    private Pane[][] matrizPaneInf;

    private int segundaCeldaX;
    private int segundaCeldaY;

    private Timeline timeline;
    
    private int caso; //se usa para ver que paso en el paso de corriente, si es 1 se paso de click inicial al final , si es dos se paso del final al inicial
    
    public Cables(Pane pane, Pane[][] matrizPane, Color color, double startX, double startY, int[][] matrizEnteros, int[][] matriSup, Pane[][] matrizPaneSup, int[][] matriInf, Pane[][] matrizPaneInf) {
        this.pane = pane;
        this.matrizPane = matrizPane;
        this.matrizEnteros = matrizEnteros;
        this.matriSup = matriSup;
        this.matrizPaneSup = matrizPaneSup;
        this.matriInf = matriInf;
        this.matrizPaneInf = matrizPaneInf;

        this.setStroke(color);
        this.setStrokeWidth(8);

        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX);
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this);

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY){ // Verificar si es clic derecho
                double xLocalInicial = this.getStartX();
                double yLocalInicial = this.getStartY();
                double xLocalFinal = this.getEndX();
                double yLocalFinal = this.getEndY();
                int filaInicial = (int) (yLocalInicial / CELL_SIZE);
                int columnaInicial = (int) (xLocalInicial / CELL_SIZE);
                int filaFinal = (int) (yLocalFinal / CELL_SIZE);
                int columnaFinal = (int) (xLocalFinal / CELL_SIZE);

                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);
        
                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaFinal, 0);
                Main.banderaCableAzulInferiorBateria = 0;
                Main.banderaCableRojoInferiorBateria = false;
                Main.banderaCableAzulSuperiorBateria = false;
                Main.banderaCableRojoSuperiorBateria = false;

                pane.getChildren().remove(this); // Eliminar el cable del pane

                // Detener el monitoreo
                if (timeline != null) {
                    timeline.stop();
                }

                if(caso == 1){
                    eliminarValoresEnergia(filaFinal, columnaFinal);
                    Main.BotonBateria2();
                    Main.BotonBateria3();
                   
                }else if(caso == 2){
                    eliminarValoresEnergia(filaInicial, columnaInicial);
                    Main.BotonBateria2();
                    Main.BotonBateria3();
                    
                }
                Main.actualizarMatriz();
                
            }
        });

        // Monitoreo constante de las celdas
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.005), event -> {
            monitorearCeldas();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static Map<String, Long> animacionTiempos = new HashMap<>();

    public static void revisarYMantenerMatrizCentral(int[][] matriz, Pane[][] matrizPane) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                if (matriz[i][j] == 1) {
                    //matrizEnteros[i][j] = 0;
                    String key = i + "," + j;
                    if (!animacionTiempos.containsKey(key)) {
                        animacionTiempos.put(key, currentTime);
                    }
                    long startTime = animacionTiempos.get(key);
                    if (currentTime - startTime <= 3000) { // 3000 ms = 3 segundos
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                        Main.crearParticulaDeHumoEstatico(matrizPane[i][j].getLayoutX(), matrizPane[i][j].getLayoutY());
                    } else {
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                    }
                }
            }
        }
    }
  
    private void revisarYMantenerMatrizSup(int[][] matriz, Pane[][] matrizPane) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 30; j++) {
                if (matriz[i][j] == 1) {
                    //matriSup[i][j] = 0;
                    String key = i + "," + j;
                    if (!animacionTiempos.containsKey(key)) {
                        animacionTiempos.put(key, currentTime);
                    }
                    long startTime = animacionTiempos.get(key);
                    if (currentTime - startTime <= 3000) { // 3000 ms = 3 segundos
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                        Main.crearParticulaDeHumoEstaticoSup(matrizPane[i][j].getLayoutX(), matrizPane[i][j].getLayoutY());
                    } else {
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                    }
                }
            }
        }
    }
    
    private void revisarYMantenerMatrizInf(int[][] matriz, Pane[][] matrizPane) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 30; j++) {
                if (matriz[i][j] == 1) {
                    //matriInf[i][j] = 0;
                    String key = i + "," + j;
                    if (!animacionTiempos.containsKey(key)) {
                        animacionTiempos.put(key, currentTime);
                    }
                    long startTime = animacionTiempos.get(key);
                    if (currentTime - startTime <= 3000) { // 3000 ms = 3 segundos
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                        Main.crearParticulaDeHumoEstaticoInf(matrizPane[i][j].getLayoutX(), matrizPane[i][j].getLayoutY());
                    } else {
                        matrizPane[i][j].setStyle("-fx-background-color: orange;");
                    }
                }
            }
        }
    }

    private void monitorearCeldas() {
        double xLocalInicial = this.getStartX();
        double yLocalInicial = this.getStartY();
        double xLocalFinal = this.getEndX();
        double yLocalFinal = this.getEndY();
    
        double xGlobalInicial = pane.localToScene(xLocalInicial, yLocalInicial).getX();
        double yGlobalInicial = pane.localToScene(xLocalInicial, yLocalInicial).getY();
        double xGlobalFinal = pane.localToScene(xLocalFinal, yLocalFinal).getX();
        double yGlobalFinal = pane.localToScene(xLocalFinal, yLocalFinal).getY();
    
        filaInicial = (int) (yLocalInicial / CELL_SIZE);
        columnaInicial = (int) (xLocalInicial / CELL_SIZE);
        filaFinal = (int) (yLocalFinal / CELL_SIZE);
        columnaFinal = (int) (xLocalFinal / CELL_SIZE);
    
        String matrizInicial = identificarMatriz(xGlobalInicial, yGlobalInicial);
        String matrizFinal = identificarMatriz(xGlobalFinal, yGlobalFinal);
        //System.out.println("Matriz inicial: " + matrizInicial);
        //System.out.println("Matriz final: " + matrizFinal);
        revisarYMantenerMatrizCentral(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), Main.matrizCentralProtoboard.getMatriz());
        revisarYMantenerMatrizSup(Main.matrizSuperior.getMatrizCortoCircuito(), Main.matrizSuperior.getMatriz());
        revisarYMantenerMatrizInf(Main.matrizInferior.getMatrizCortoCircuito(), Main.matrizInferior.getMatriz());


        if (matrizInicial.equals("central") && matrizFinal.equals("central")){
            //funciona
            filaInicial = ajustarFila(filaInicial);
            columnaInicial = ajustarColumna(columnaInicial);
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            
            actualizarMatrizCentral(filaInicial, columnaInicial, filaFinal, columnaFinal);
      
        } else if (matrizInicial.equals("superior") && matrizFinal.equals("central")){
            //FALTA AJUSTAR LAS COLUMNAS YA QUE DESPUES DE CIERTA COLUMNA FALLA
           //ajustamos fila y columna para que se ajuste a la matriz central
           filaFinal = ajustarFila(filaFinal);
           columnaFinal = ajustarColumna(columnaFinal);
           //ajustamos fila y columna para que se ajuste a la matriz superior
           filaInicial = ajustarFilaMatrizSup(filaInicial);
           columnaInicial = ajustarColumna(columnaInicial);
           //revisarYMantenerMatrizCentral(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), Main.matrizCentralProtoboard.getMatriz());
           actualizarMatrizSuperiorACentral(filaInicial, columnaInicial, filaFinal, columnaFinal);

        }else if (matrizInicial.equals("central") && matrizFinal.equals("superior")){
            //funciona
            filaInicial = ajustarFilaCentralSuperior(filaInicial);
            columnaInicial = ajustarColumna(columnaInicial);
            
            if(filaFinal == 2){
                filaFinal = 1;
            }
            columnaFinal = ajustarColumna(columnaFinal);
            //revisarYMantenerMatrizSup(Main.matrizSuperior.getMatrizCortoCircuito(), Main.matrizSuperior.getMatriz());
            actualizarMatrizCentralASuperior(filaInicial, columnaInicial, filaFinal, columnaFinal);

        } else if (matrizInicial.equals("central") && matrizFinal.equals("inferior")) {
            //funciona
            columnaInicial = ajustarColumna(columnaInicial);
            filaInicial = ajustarFilaCentralInferior(filaInicial);
            if(filaFinal == 2){
                filaFinal = 1;
            }
            columnaFinal = ajustarColumna(columnaFinal);
            //revisarYMantenerMatrizInf(Main.matrizInferior.getMatrizCortoCircuito(), Main.matrizInferior.getMatriz());
            actualizarMatrizCentralAInferior(filaInicial, columnaInicial, filaFinal, columnaFinal);

        } else if (matrizInicial.equals("inferior") && matrizFinal.equals("central")){
            //funciona
            filaInicial = ajustarFilaMatrizInf(filaInicial);
            columnaInicial = ajustarColumna(columnaInicial);
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            //revisarYMantenerMatrizCentral(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), Main.matrizCentralProtoboard.getMatriz());
            actualizarMatrizInferiorACentral(filaInicial, columnaInicial, filaFinal, columnaFinal);
            
        }else if (matrizInicial.equals("superior") && matrizFinal.equals("inferior")) {
            //funciona
            // Lógica para primer clic en matriz superior y segundo en matriz inferior
            columnaInicial = ajustarColumna(columnaInicial);
            columnaFinal = ajustarColumna(columnaFinal);

            if(filaInicial == -34){
                filaInicial = 0;
            }else if(filaInicial == -32){
                filaInicial = 1;
            }if(filaFinal == 2){
                filaFinal = 1;
            }
            //revisarYMantenerMatrizInf(Main.matrizInferior.getMatrizCortoCircuito(), Main.matrizInferior.getMatriz());
            actualizarMatrizSuperiorAInferior(filaInicial, columnaInicial, filaFinal, columnaFinal);

        }else if (matrizInicial.equals("inferior") && matrizFinal.equals("superior")) {
            columnaInicial = ajustarColumna(columnaInicial);
            columnaFinal = ajustarColumna(columnaFinal);
            if(filaInicial == 35){
                filaInicial = 0;
            }
            if(filaInicial == 37){
                filaInicial = 1;
            }
            if(filaFinal == 2){
                filaFinal = 1;
            }
            //Lógica para primer clic en matriz inferior y segundo en matriz superior
            //revisarYMantenerMatrizSup(Main.matrizSuperior.getMatrizCortoCircuito(), Main.matrizSuperior.getMatriz());
            actualizarMatrizInferiorASuperior(filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
        else if (matrizInicial.equals("azulInf") && matrizFinal.equals("central")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            actualizarMatrizCentralBateria(filaFinal, columnaFinal,-1);
        }
        else if (matrizInicial.equals("rojoInf") && matrizFinal.equals("central")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            actualizarMatrizCentralBateria(filaFinal, columnaFinal,1);
        }
        else if (matrizInicial.equals("azulSup") && matrizFinal.equals("central")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            actualizarMatrizCentralBateria(filaFinal, columnaFinal,-1);
        }
        else if (matrizInicial.equals("rojoSup") && matrizFinal.equals("central")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            actualizarMatrizCentralBateria(filaFinal, columnaFinal,1);
        }
        else if (matrizInicial.equals("rojoSup") && matrizFinal.equals("superior")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            if(filaFinal == 2){
                filaFinal = 1;
            }
            actualizarMatrizBateriaASuperior(filaFinal, columnaFinal,1);
        }
        else if (matrizInicial.equals("azulSup") && matrizFinal.equals("superior")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            if(filaFinal == 2){
                filaFinal = 1;
            }
            actualizarMatrizBateriaASuperior(filaFinal, columnaFinal,-1);
        }
        else if (matrizInicial.equals("rojoInf") && matrizFinal.equals("inferior")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            if(filaFinal == 2){
                filaFinal = 1;
            }
            actualizarMatrizBateriaAInferior(filaFinal, columnaFinal,1);
        }
        else if (matrizInicial.equals("azulInf") && matrizFinal.equals("inferior")){
            filaFinal = ajustarFila(filaFinal);
            columnaFinal = ajustarColumna(columnaFinal);
            if(filaFinal == 2){
                filaFinal = 1;
            }
            actualizarMatrizBateriaAInferior(filaFinal, columnaFinal,-1);
        }
    }

    private void actualizarMatrizCentralBateria(int filaFinal, int columnaFinal, int valorInicial){
  
        if (filaFinal >= 0 && filaFinal < matrizEnteros.length && columnaFinal >= 0 && columnaFinal < matrizEnteros[0].length) {
            int valorFinal = matrizEnteros[filaFinal][columnaFinal];
            
            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizCentralProtoboard.setMatrizCortoCircuito(filaFinal, columnaFinal, 1);
            }

            else if (!(valorInicial != 0 && valorFinal != 0)) {
                if ((valorInicial == 1  || valorInicial == -1) && Bateria.banderaBateria == true){
                    actualizarCeldasProto(filaFinal, columnaFinal, valorInicial, matrizEnteros, matrizPane);
                    caso = 1;
                }
            }
            revisarMatrizCorto(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }

    //metodo para actualizar matriz central a superior
    private void actualizarMatrizBateriaASuperior(int filaFinal, int columnaFinal,int valorInicial) {
        if (filaFinal >= 0 && filaFinal < matriSup.length && columnaFinal >= 0 && columnaFinal < matriSup[0].length) {
            int valorFinal = matriSup[filaFinal][columnaFinal];

            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizSuperior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }
            else if ((valorInicial == 1 || valorInicial == -1) && Bateria.banderaBateria == true) {
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriSup, matrizPaneSup);
                caso = 1;
            }
            revisarMatrizCortoSupInf(Main.matrizSuperior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        } 
    }
    private void actualizarMatrizBateriaAInferior(int filaFinal, int columnaFinal,int valorInicial) {
        if (filaFinal >= 0 && filaFinal < matriSup.length && columnaFinal >= 0 && columnaFinal < matriSup[0].length) {
            int valorFinal = matriInf[filaFinal][columnaFinal];

            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizSuperior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }
            else if ((valorInicial == 1 || valorInicial == -1) && Bateria.banderaBateria == true) {
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriInf, matrizPaneInf);
                caso = 1;
            }
            revisarMatrizCortoSupInf(Main.matrizSuperior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        } 
    }
    
    private void actualizarCeldasProto(int fila, int columna, int valor, int[][] matriz, Pane[][] matrizPane) {
        if (fila >= 0 && fila < matriz.length && columna >= 0 && columna < matriz[0].length) {
            if (fila < 5) {
                if(valor == 1){
                    for (int i = 0; i < 5; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: red;");
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: blue;");
                    }
                }

            } else if(fila >= 5 && fila < 10){
                if(valor == 1){
                    for (int i = 5; i < 10; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: red;");
                    }
                } else {
                    for (int i = 5; i < 10; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: blue;");
                    }
                }
            } else {
                if(valor == 0){
                    for (int i = 0; i < matriz.length; i++) {
                        matriz[i][columna] = 0;
                        matrizPane[i][columna].setStyle("-fx-background-color: black;");
                    }
                }
            }
        }
    }

    private void actualizarMatrizCentral(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal){
        // System.out.println("Fila inicial: " + filaInicial);
        // System.out.println("Columna inicial: " + columnaInicial);
        // System.out.println("Fila final: " + filaFinal);
        // System.out.println("Columna final: " + columnaFinal);
        if (filaInicial >= 0 && filaInicial < matrizEnteros.length && columnaInicial >= 0 && columnaInicial < matrizEnteros[0].length &&
            filaFinal >= 0 && filaFinal < matrizEnteros.length && columnaFinal >= 0 && columnaFinal < matrizEnteros[0].length) {
            int valorInicial = matrizEnteros[filaInicial][columnaInicial];
            int valorFinal = matrizEnteros[filaFinal][columnaFinal];
            //System.out.println("Valor inicial en matriz central: " + valorInicial);
            //System.out.println("Valor final en matriz central: " + valorFinal);
            
            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizCentralProtoboard.setMatrizCortoCircuito(filaFinal, columnaFinal, 1);
            }

            else if (!(valorInicial != 0 && valorFinal != 0)) {
                if (valorInicial == 1  || valorInicial == -1){
                    actualizarCeldas(filaFinal, columnaFinal, valorInicial, matrizEnteros, matrizPane);
                    caso = 1;
                }

                if (valorFinal == 1 || valorFinal == -1) {
                    actualizarCeldas(filaInicial, columnaInicial, valorFinal, matrizEnteros, matrizPane);
                    caso = 2;
                }
            }
            revisarMatrizCorto(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }
  
    public void revisarMatrizCorto(int[][] matriz,int filaInicial,int columnaInicial,int filaFinal,int columnaFinal){
        if(matriz[filaFinal][columnaFinal] == 1){
            timeline.stop();
            Main.BotonBateria2();
            Main.BotonBateria3();
        }
    }

    public void revisarMatrizCortoSupInf(int[][] matriz,int filaInicial,int columnaInicial,int filaFinal,int columnaFinal){
        if(matriz[filaFinal][columnaFinal] == 1){
            timeline.stop();
            Main.BotonBateria2();
            Main.BotonBateria3();
        }
    }

    //metodo para actualizar matriz superior a central
    private void actualizarMatrizSuperiorACentral(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
        //System.out.println("Fila inicial: " + filaInicial);
        //System.out.println("Columna inicial: " + columnaInicial);
        //System.out.println("Fila final: " + filaFinal);
        //System.out.println("Columna final: " + columnaFinal);
    
        if (filaInicial >= 0 && filaInicial < matriSup.length && columnaInicial >= 0 && columnaInicial < matriSup[0].length &&
            filaFinal >= 0 && filaFinal < matrizEnteros.length && columnaFinal >= 0 && columnaFinal < matrizEnteros[0].length) {
            int valorInicial = matriSup[filaInicial][columnaInicial];
            int valorFinal = matrizEnteros[filaFinal][columnaFinal];
            //System.out.println("Valor inicial en matriz superior: " + valorInicial);
            //System.out.println("Valor final en matriz central: " + valorFinal);

            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizCentralProtoboard.setMatrizCortoCircuito(filaFinal, columnaFinal, 1);
            }
            else if ((valorInicial == 1 || valorInicial == -1) && valorFinal == 0) {
                actualizarCeldas(filaFinal, columnaFinal, valorInicial, matrizEnteros, matrizPane);
                caso = 1;
            }else if(valorFinal == 1 || valorFinal == -1){
                actualizarceldasSUPEINF(filaInicial, columnaInicial, valorFinal, matriSup, matrizPaneSup);
                caso = 2;
            }
            revisarMatrizCortoSupInf(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }
    
    //metodo para actualizar matriz inferior a central
    private void actualizarMatrizInferiorACentral(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
        //System.out.println("Fila inicial: " + filaInicial);
        //System.out.println("Columna inicial: " + columnaInicial);
        //System.out.println("Fila final: " + filaFinal);
        //System.out.println("Columna final: " + columnaFinal);
    
        if (filaInicial >= 0 && filaInicial < matriInf.length && columnaInicial >= 0 && columnaInicial < matriInf[0].length &&
            filaFinal >= 0 && filaFinal < matrizEnteros.length && columnaFinal >= 0 && columnaFinal < matrizEnteros[0].length) {
            int valorInicial = matriInf[filaInicial][columnaInicial];
            int valorFinal = matrizEnteros[filaFinal][columnaFinal];
            //System.out.println("Valor inicial en matriz superior: " + valorInicial);
            //System.out.println("Valor final en matriz central: " + valorFinal);

            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizCentralProtoboard.setMatrizCortoCircuito(filaFinal, columnaFinal, 1);
            }
            else if ((valorInicial == 1 || valorInicial == -1) && valorFinal == 0) {
                actualizarCeldas(filaFinal, columnaFinal, valorInicial, matrizEnteros, matrizPane);
                caso = 1;
            }else if(valorFinal == 1 || valorFinal == -1){
                actualizarceldasSUPEINF(filaInicial, columnaInicial, valorFinal, matriInf, matrizPaneInf);
                caso = 2;
            }
            revisarMatrizCortoSupInf(Main.matrizCentralProtoboard.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }
    
    //metodo para actualizar matriz central a superior
    private void actualizarMatrizCentralASuperior(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
       // System.out.println("Fila inicial: " + filaInicial);
       // System.out.println("Columna inicial: " + columnaInicial);
       // System.out.println("Fila final: " + filaFinal);
       // System.out.println("Columna final: " + columnaFinal);
        if (filaInicial >= 0 && filaInicial < matrizEnteros.length && columnaInicial >= 0 && columnaInicial < matrizEnteros[0].length &&
            filaFinal >= 0 && filaFinal < matriSup.length && columnaFinal >= 0 && columnaFinal < matriSup[0].length) {
            int valorInicial = matrizEnteros[filaInicial][columnaInicial];
            int valorFinal = matriSup[filaFinal][columnaFinal];
            System.out.println("Valor inicial en matriz central: " + valorInicial);
            System.out.println("Valor final en matriz superior: " + valorFinal);
            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizSuperior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }
            else if (valorInicial == 1 || valorInicial == -1) {
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriSup, matrizPaneSup);
                caso = 1;
            }else if(valorFinal == 1 || valorFinal == -1){
                actualizarCeldas(filaInicial, columnaInicial, valorFinal, matrizEnteros, matrizPane);
                caso = 2;   
            }
            revisarMatrizCortoSupInf(Main.matrizSuperior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        } 
    }
   
    //metodo para actualizar matriz central a inferior
    private void actualizarMatrizCentralAInferior(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
         // System.out.println("Fila inicial: " + filaInicial);
         // System.out.println("Columna inicial: " + columnaInicial);
         // System.out.println("Fila final: " + filaFinal);
         // System.out.println("Columna final: " + columnaFinal);
        if (filaInicial >= 0 && filaInicial < matrizEnteros.length && columnaInicial >= 0 && columnaInicial < matrizEnteros[0].length &&
            filaFinal >= 0 && filaFinal < matriInf.length && columnaFinal >= 0 && columnaFinal < matriInf[0].length) {
            int valorInicial = matrizEnteros[filaInicial][columnaInicial];
            int valorFinal = matriInf[filaFinal][columnaFinal];
           // System.out.println("Valor inicial en matriz central: " + valorInicial);
           // System.out.println("Valor final en matriz inferior: " + valorFinal);

            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizInferior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }    
            else if ((valorInicial == 1 || valorInicial == -1) && valorFinal == 0) {
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriInf, matrizPaneInf);
                caso = 1;
            } else if (valorFinal == 1 || valorFinal == -1) {
                actualizarCeldas(filaInicial, columnaInicial, valorFinal, matrizEnteros, matrizPane);
                caso = 2;
            }
            revisarMatrizCortoSupInf(Main.matrizInferior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        } 
        
    }
    
    //metodo para actualizar matriz superior a inferior
    private void actualizarMatrizSuperiorAInferior(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
        //System.out.println("Fila inicial: " + filaInicial);
        //System.out.println("Columna inicial: " + columnaInicial);
        //System.out.println("Fila final: " + filaFinal);
        //System.out.println("Columna final: " + columnaFinal);
        if(filaInicial >= 0 && filaInicial < matriSup.length && columnaInicial >= 0 && columnaInicial < matriSup[0].length &&
            filaFinal >= 0 && filaFinal < matriInf.length && columnaFinal >= 0 && columnaFinal < matriInf[0].length){
            int valorInicial = matriSup[filaInicial][columnaInicial];
            int valorFinal = matriInf[filaFinal][columnaFinal];
            //System.out.println("Valor inicial en matriz superior: " + valorInicial);
           // System.out.println("Valor final en matriz inferior: " + valorFinal);
            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizInferior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }
            else if((valorInicial == 1 || valorInicial == -1)){
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriInf, matrizPaneInf);
                caso = 1;
            }else if(valorFinal == 1 || valorFinal == -1){
                actualizarceldasSUPEINF(filaInicial, columnaInicial, valorFinal, matriInf,  matrizPaneInf);
                caso = 2;   
            }
            revisarMatrizCortoSupInf(Main.matrizInferior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }
    
    //metodo para actualizar matriz inferior a superior
    private void actualizarMatrizInferiorASuperior(int filaInicial, int columnaInicial, int filaFinal, int columnaFinal) {
        //System.out.println("Fila inicial: " + filaInicial);
        //System.out.println("Columna inicial: " + columnaInicial);
       // System.out.println("Fila final: " + filaFinal);
        //System.out.println("Columna final: " + columnaFinal);
        if(filaInicial >= 0 && filaInicial < matriInf.length && columnaInicial >= 0 && columnaInicial < matriInf[0].length &&
            filaFinal >= 0 && filaFinal < matriSup.length && columnaFinal >= 0 && columnaFinal < matriSup[0].length){
            int valorInicial = matriInf[filaInicial][columnaInicial];
            int valorFinal = matriSup[filaFinal][columnaFinal];
            //System.out.println("Valor inicial en matriz inferior: " + valorInicial);
            //System.out.println("Valor final en matriz superior: " + valorFinal);
            if ((valorInicial == 1 && valorFinal == -1) || (valorInicial == -1 && valorFinal == 1)) {
                Main.matrizSuperior.setMatrizCortoCircuitoSupInf(filaFinal, columnaFinal, 1);
            }
            else if((valorInicial == 1 || valorInicial == -1)){
                actualizarceldasSUPEINF(filaFinal, columnaFinal, valorInicial, matriSup, matrizPaneSup);
                caso = 1;
            }else if(valorFinal == 1 || valorFinal == -1){
                actualizarceldasSUPEINF(filaInicial, columnaInicial, valorFinal, matriInf, matrizPaneInf);
                caso = 2;
            }
            revisarMatrizCortoSupInf(Main.matrizSuperior.getMatrizCortoCircuito(), filaInicial, columnaInicial, filaFinal, columnaFinal);
        }
    }
   
    private void actualizarceldasSUPEINF(int fila, int columna, int valor, int[][] matriz, Pane[][] matrizPane) {
        if (fila >= 0 && fila < matriz.length) {
            for (int i = 0; i < matriz[0].length; i++) {
                matriz[fila][i] = valor;
                if (valor == 1) {
                    matrizPane[fila][i].setStyle("-fx-background-color: red;");
                } else if(valor == -1){
                    matrizPane[fila][i].setStyle("-fx-background-color: blue;");
                }
            }
        }
    }
   
    private void actualizarCeldas(int fila, int columna, int valor, int[][] matriz, Pane[][] matrizPane) {
        if (fila >= 0 && fila < matriz.length && columna >= 0 && columna < matriz[0].length) {
            if (fila < 5) {
                if(valor == 1){
                    for (int i = 0; i < 5; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: red;");
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: blue;");
                    }
                }

            } else if(fila >= 5 && fila < 10){
                if(valor == 1){
                    for (int i = 5; i < 10; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: red;");
                    }
                } else {
                    for (int i = 5; i < 10; i++) {
                        matriz[i][columna] = valor;
                        matrizPane[i][columna].setStyle("-fx-background-color: blue;");
                    }
                }
            } else {
                if(valor == 0){
                    for (int i = 0; i < matriz.length; i++) {
                        matriz[i][columna] = 0;
                        matrizPane[i][columna].setStyle("-fx-background-color: black;");
                    }
                }
            }
        }
    }

    public int getFilaInicial() {
        return filaInicial;
    }

    public int getColumnaInicial() {
        return columnaInicial;
    }

    public int getFilaFinal() {
        return filaFinal;
    }

    public int getColumnaFinal() {
        return columnaFinal;
    }

    private int ajustarFila(int fila) {
        fila -= (fila / 2);
        if (fila >= 7) {
            fila -= 2;
        }
        return fila;
    }
    
    //metodo solo para central a superior
    private int ajustarFilaCentralSuperior(int fila){
        if(fila == 8){
            fila = 0;
        }else if(fila == 7){
            fila = 0;
        }else if(fila == 10){
            fila = 1;
        }else if(fila == 12){
            fila = 2;
        }else if(fila == 13){
            fila = 3;
        }else if(fila == 14){
            fila = 3;
        }else if(fila == 15){
            fila = 4;
        }else if(fila == 16){
            fila = 4;
        }else if(fila == 21){
            fila = 5;
        }else if(fila == 22){
            fila = 6;
        }else if(fila == 23){
            fila = 6;
        }else if(fila == 24){
            fila = 6;
        }else if(fila == 25){
            fila = 7;
        }else if(fila == 26){
            fila = 8;
        }else if(fila == 27){
            fila = 8;
        }else if(fila == 29){
            fila = 9;
        }
        return fila; // Ajusta según sea necesario
    }
    
    private int ajustarFilaCentralInferior(int fila){
        if(fila == -26){
            fila = 0;
        }else if(fila == -24){
            fila = 1;
        }else if(fila == -22){
            fila = 2;
        }else if(fila == -20){
            fila = 3;
        }else if(fila == -18){
            fila = 4;
        }else if(fila == -13){
            fila = 5;
        }else if(fila == -12){
            fila = 5;
        }else if(fila == -11){
            fila = 6;
        } else if (fila == -10) {
            fila = 6;
        } else if (fila == -9) {

        }else if(fila == -8){
            fila = 7;
        }else if(fila == -6){
            fila = 8;
        }else if(fila == -4){
            fila = 9;
        }
        return fila;
    }
    
    // Método para ajustar la fila según las reglas específicas para la matriz superior
    private int ajustarFilaMatrizSup(int fila) {
        if(fila == -7){
            fila = 0;
        }  
        if(fila == -6){
            fila = 0;
        }
        else if(fila == -5){
            fila = 1;
        }
        else if(fila == -4){
            fila = 1;
        }
        return fila; // Ajusta según sea necesario
    }
    
    private int ajustarFilaMatrizInf(int fila) {
        if(fila == 27){
            fila = 0;
        }  
        if(fila == 29){
            fila = 1;
        }
        return fila; // Ajusta según sea necesario
    }

    // Método para ajustar la columna según las reglas específicas
    private int ajustarColumna(int columna) {
        columna -= (columna / 2);
        if (columna > 20) {
            columna += 1;
        }
        return columna;
    }

    public void iniciarDibujoCable(double startX, double startY) {
        this.setStartX(startX);
        this.setStartY(startY);
    }

    public void finalizarDibujoCable(double endX, double endY) {
        this.setEndX(endX);
        this.setEndY(endY);

        // Guardar las coordenadas de la segunda celda
        segundaCeldaX = obtenerIndiceMatrizX(endX);
        segundaCeldaY = obtenerIndiceMatrizY(endY);
    }

    public void actualizarPane(Pane nuevoPane){
        double xGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getX();
        double yGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getY();
        double xGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getX();
        double yGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getY();

        this.pane.getChildren().remove(this);
        this.pane = nuevoPane;
        nuevoPane.getChildren().add(this);

        double xLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getX();
        double yLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getY();
        double xLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getX();
        double yLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getY();

        this.setStartX(xLocalesIniciales);
        this.setStartY(yLocalesIniciales);
        this.setEndX(xLocalesFinales);
        this.setEndY(yLocalesFinales);

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                pane.getChildren().remove(this);
                if (timeline != null) {
                    timeline.stop();
                }
                if(caso == 1){
                    eliminarValoresEnergia(filaFinal, columnaFinal);
                    Main.BotonBateria2();
                    Main.BotonBateria3();
                }else if(caso == 2){
                    eliminarValoresEnergia(filaInicial, columnaInicial);
                    Main.BotonBateria2();
                    Main.BotonBateria3();
                }
                //if (segundaCeldaY >= 0 && segundaCeldaY < matrizEnteros.length && segundaCeldaX >= 0 && segundaCeldaX < matrizEnteros[0].length) {
                   // matrizEnteros[segundaCeldaY][segundaCeldaX] = 0;
                //}
                Main.actualizarMatriz();
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
    
    public double getXInicial() {
        return this.getStartX();
    }

    public double getYInicial() {
        return this.getStartY();
    }

    private int obtenerIndiceMatrizX(double x) {
        for (int i = 0; i < matrizPane.length; i++) {
            for (int j = 0; j < matrizPane[i].length; j++) {
                Pane celda = matrizPane[i][j];
                if (celda.getBoundsInParent().contains(x, this.getEndY())) {
                    return j;
                }
            }
        }
        return -1;
    }

    private int obtenerIndiceMatrizY(double y) {
        for (int i = 0; i < matrizPane.length; i++) {
            for (int j = 0; j < matrizPane[i].length; j++) {
                Pane celda = matrizPane[i][j];
                if (celda.getBoundsInParent().contains(this.getEndX(), y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String identificarMatriz(double x, double y) {
        if (estaEnMatrizGlobal(x, y, matrizPane)) {
            return "central";
        } else if (estaEnMatrizGlobal(x, y, matrizPaneSup)) {
            return "superior";
        } else if (estaEnMatrizGlobal(x, y, matrizPaneInf)) {
            return "inferior";
        }else if (estaEnMatrizGlobal(x, y, Main.matrizCableInferiorAzul.getMatriz())) {
            return "azulInf";
        }else if (estaEnMatrizGlobal(x, y, Main.matrizCableInferiorRojo.getMatriz())) {
            return "rojoInf";
        }else if (estaEnMatrizGlobal(x, y, Main.matrizCableSuperiorAzul.getMatriz())) {
            return "azulSup";
        }else if (estaEnMatrizGlobal(x, y, Main.matrizCableSuperiorRojo.getMatriz())) {
            return "rojoSup";
        }
        return "desconocida";
    }
    
    private boolean estaEnMatrizGlobal(double x, double y, Pane[][] matriz) {
        for (Pane[] fila : matriz) {
            for (Pane celda : fila) {
                double celdaX = celda.localToScene(celda.getBoundsInLocal()).getMinX();
                double celdaY = celda.localToScene(celda.getBoundsInLocal()).getMinY();
                double celdaWidth = celda.getBoundsInLocal().getWidth();
                double celdaHeight = celda.getBoundsInLocal().getHeight();
                
                if (x >= celdaX && x <= celdaX + celdaWidth && y >= celdaY && y <= celdaY + celdaHeight) {
                    //System.out.println("Celda encontrada en matriz: " + celda + " con coordenadas globales (" + x + ", " + y + ")");
                    return true;
                }
            }
        }
        //System.out.println("No se encontró celda en matriz con coordenadas globales (" + x + ", " + y + ")");
        return false;
    } 
   
    private void eliminarValoresEnergia(int fila, int columna){
        timeline.stop();
        if (fila >= 0 && fila <= 4) {
            for (int i = 0; i < 5; i++) {
                matrizEnteros[i][columna] = 0;
                matrizPane[i][columna].setStyle("-fx-background-color: black;");
            }
        } else if (fila >= 5 && fila <= 9) {
            for (int i = 5; i < 10; i++) {
                matrizEnteros[i][columna] = 0;
                matrizPane[i][columna].setStyle("-fx-background-color: black;");
            }
        }
        
    }
}