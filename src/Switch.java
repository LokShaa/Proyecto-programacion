import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Switch extends Line {
    private Pane pane; // Atributo para saber en que pane se dibujara el cable
    private ImageView imagenSwitch;
    private Circle circle;
    private int[][] matrizEnteros; // Matriz de enteros
    private Pane matrizPane; //Pane que contiene la matriz
    private boolean estadoSwitch = false; // Estado del switch (false = rojo, true = verde)
    private boolean SwitchQuemado = false; 
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;
    private Timeline timeline;
    
    double[][] matrizVoltajes;
    private Bateria bateria;
    double voltaje;

    public Switch(Pane pane, Color color, double startX, double startY, ImageView imagenSwitch, int[][] matrizEnteros, Pane matrizPane,double[][]matrizVoltajes){
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(10);
        this.matrizEnteros = matrizEnteros;
        this.matrizPane = matrizPane;
        this.matrizVoltajes = matrizVoltajes;
        bateria = new Bateria();
        // Inicializamos las coordenadas del cable
        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX); //Inicialmente el final es el mismo que el inicio para solucionar el bug de la linea
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this); // Añadimos el cable al pane
       
        iniciarMonitoreo(); // Iniciar el monitoreo constante
        // Agregar EventHandler para detectar clic derecho
       
    }

    // Método para ajustar la fila según las reglas específicas
    private int ajustarFila(int fila) {
        fila -= (fila / 2);
        if (fila >= 7) {
            fila -= 2;
        }
        return fila;
    }

    // Método para ajustar la columna según las reglas específicas
    private int ajustarColumna(int columna){
        columna -= (columna / 2);
        if (columna > 20) {
            columna += 1;
        }
        return columna;
    }

    private void crearImagenSwitch(ImageView imagenSwitch, double width, double height) {
        // Calcular el punto medio del cable
        double midX = (this.getStartX() + this.getEndX()) / 2;
        double midY = (this.getStartY() + this.getEndY()) / 2;
    
        // Crear la imagen
        this.imagenSwitch = new ImageView(imagenSwitch.getImage());
        this.imagenSwitch.setFitWidth(width + 5); // Ancho
        this.imagenSwitch.setFitHeight(height + 2); // Alto
        this.imagenSwitch.setX(midX - width / 2);
        this.imagenSwitch.setY((midY - 5) - height / 2);
    
        // Crear el círculo clickeable
        circle = new Circle(midX+3, midY-5, 28, Color.RED);
        circle.setOnMouseClicked(this::manejarClickCirculo);
    
        // Añadir EventHandler para eliminar el switch al hacer clic en la imagen
        this.imagenSwitch.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Verificar si es clic izquierdo
                System.out.println("Clic derecho detectado");
                double xLocalInicial = this.getStartX();
                double yLocalInicial = this.getStartY();
                double xLocalFinal = this.getEndX();
                double yLocalFinal = this.getEndY();
                filaInicial = (int) (yLocalInicial / 20);
                columnaInicial = (int) (xLocalInicial / 20);
                filaFinal = (int) (yLocalFinal / 20);
                columnaFinal = (int) (xLocalFinal / 20);
                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);

                System.out.println(filaInicial);
                System.out.println(columnaInicial);
                System.out.println(filaFinal);
                System.out.println(columnaFinal);

                // Eliminar los espacios ocupados en matrizCables
                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaFinal, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaFinal, 0);

                pane.getChildren().remove(this); // Eliminar el cable del pane
                pane.getChildren().remove(this.imagenSwitch); // Eliminar la imagen del switch del pane
                pane.getChildren().remove(circle); // Eliminar el círculo del switch del pane
                estadoSwitch = false;
                Main.BotonBateria2();
                Main.BotonBateria3();
                timeline.stop(); // Detener el monitoreo constante
             }
        });
    
        // Añadir la imagen y el círculo al pane
        pane.getChildren().addAll(this.imagenSwitch, circle);
    }
   
    private void manejarClickCirculo(MouseEvent event) {
        double xLocalInicial = this.getStartX();
        double yLocalInicial = this.getStartY();
        double xLocalFinal = this.getEndX();
        double yLocalFinal = this.getEndY();
        filaInicial = (int) (yLocalInicial / 20);
        columnaInicial = (int) (xLocalInicial / 20);
        filaFinal = (int) (yLocalFinal / 20);
        columnaFinal = (int) (xLocalFinal / 20);
        filaInicial = ajustarFila(filaInicial);
        columnaInicial = ajustarColumna(columnaInicial);
        filaFinal = ajustarFila(filaFinal);
        columnaFinal = ajustarColumna(columnaFinal);
        
        if (!estadoSwitch) {
            circle.setFill(Color.GREEN);
            estadoSwitch = true;
        } else if (estadoSwitch) {
            circle.setFill(Color.RED);
            estadoSwitch = false;
            actualizarApagado();
        }
    }

    private void actualizarApagado(){
        if(filaFinal>=0 && filaFinal<5){
            for(int i = 0; i < 5; i++){
                if(matrizEnteros[filaInicial][columnaInicial] == -1) {
                    matrizEnteros[i][columnaFinal] = 0;
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 0; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
                else {
                    matrizEnteros[filaInicial][columnaInicial] = 0;
                    matrizEnteros[filaFinal][columnaFinal] = 0; 
                    cambiarColorCelda(filaInicial, columnaInicial, Color.BLACK);
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
            }
        }
        if(filaFinal>=5 && filaFinal<10){
            for(int i = 5; i < 10; i++){
                if(matrizEnteros[filaInicial][columnaInicial] == -1) {
                    matrizEnteros[i][columnaFinal] = 0;
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 0; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
                else {
                    matrizEnteros[filaInicial][columnaInicial] = 0;
                    matrizEnteros[filaFinal][columnaFinal] = 0; 
                    cambiarColorCelda(filaInicial, columnaInicial, Color.BLACK);
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
            }
        }
        Main.BotonBateria2();
        Main.BotonBateria3();
    }
    
    // Método auxiliar para cambiar el color de una celda en la matriz
    private void cambiarColorCelda(int fila, int columna, Color color) {
        Pane targetCell = (Pane) matrizPane.getChildren().get(fila * matrizEnteros[fila].length + columna);
        if(color == Color.RED){
            if (fila >=0 && fila <5){
                for(int i = 0; i < 5; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: red;");
                }
            }
            if(fila >=5 && fila <10){
                for(int i = 5; i < 10; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: red;");
                }
            }

        }else if(color == Color.BLUE){
            if (fila >=0 && fila <5){
                for(int i = 0; i < 5; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: blue;");
                }
            }
            if(fila >=5 && fila <10){
                for(int i = 5; i < 10; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: blue;");
                }
            }
        }else if(color == Color.BLACK){
            if (fila >=0 && fila <5){
                for(int i = 0; i < 5; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: black;");
                }
            }
            if(fila >=5 && fila <10){
                for(int i = 5; i < 10; i++){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: black;");
                }
            }

        }
        
    }

    private void iniciarMonitoreo() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> monitorearEstado()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void monitorearEstado(){
        double xLocalInicial = this.getStartX();
        double yLocalInicial = this.getStartY();
        double xLocalFinal = this.getEndX();
        double yLocalFinal = this.getEndY();
        filaInicial = (int) (yLocalInicial / 20);
        columnaInicial = (int) (xLocalInicial / 20);
        filaFinal = (int) (yLocalFinal / 20);
        columnaFinal = (int) (xLocalFinal / 20);
        filaInicial = ajustarFila(filaInicial);
        columnaInicial = ajustarColumna(columnaInicial);
        filaFinal = ajustarFila(filaFinal);
        columnaFinal = ajustarColumna(columnaFinal);
        if(SwitchQuemado == false){
            //esquina superior izquierda con valor
            if(matrizEnteros[filaInicial][columnaInicial] == 1){
                if(filaInicial>=0 && filaInicial<5){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaFinal] = 1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                    }
                }else if(filaInicial>=5 && filaInicial<10){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaFinal] = 1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                    }
                }
            }
            if(matrizEnteros[filaInicial][columnaInicial] == -1){
                if(filaInicial>=0 && filaInicial<5){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaFinal] = -1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                    }
                }else if(filaInicial>=5 && filaInicial<10){
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaFinal] = -1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                    }
                }
            //esquina superior derecha con valor
            } 
            if(matrizEnteros[filaInicial][columnaFinal] == 1){
                if(filaInicial>=0 && filaInicial<5){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaInicial] = 1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                    }
                }else if(filaInicial>=5 && filaInicial<10){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaInicial] = 1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                    }
                }
            }
            if(matrizEnteros[filaInicial][columnaFinal] == -1){
                if(filaInicial>=0 && filaInicial<5){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaInicial] = -1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                    }
                }else if(filaInicial>=5 && filaInicial<10){
                    voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaInicial] = -1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                    }
                }
            //esquina inferior izquierda con valor
            }
            if(matrizEnteros[filaFinal][columnaInicial] == 1){
                if(filaFinal>=0 && filaFinal<5){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaFinal] = 1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                    }
                }else if(filaFinal>=5 && filaFinal<10){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaFinal] = 1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                    }
                }
            }
            if(matrizEnteros[filaFinal][columnaInicial] == -1){
                if(filaFinal>=0 && filaFinal<5){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaFinal] = -1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                    }
                }else if(filaFinal>=5 && filaFinal<10){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaFinal] = -1;
                        Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                        cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                    }
                }
            //esquina inferior derecha con valor
            }
            if(matrizEnteros[filaFinal][columnaFinal] == 1){
                if(filaFinal>=0 && filaFinal<5){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaInicial] = 1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                    }
                }else if(filaFinal>=5 && filaFinal<10){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaInicial] = 1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                    }
                }
            }
            if(matrizEnteros[filaFinal][columnaFinal] == -1){
                if(filaFinal>=0 && filaFinal<5){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaInicial] = -1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                    }
                }else if(filaFinal>=5 && filaFinal<10){
                    voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                    for(int i = 5; i < 10; i++){
                        matrizEnteros[i][columnaInicial] = -1;
                        Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                        cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                    }
                }
            }
            if (estadoSwitch == true && SwitchQuemado == false){
                //aun no es funcional
                if((matrizEnteros[filaInicial][columnaInicial] == 1 && matrizEnteros[filaFinal][columnaInicial] == -1) || 
                (matrizEnteros[filaInicial][columnaInicial] == -1 && matrizEnteros[filaFinal][columnaInicial] == 1) || 
                (matrizEnteros[filaInicial][columnaFinal] == 1 && matrizEnteros[filaFinal][columnaFinal] == -1) ||
                (matrizEnteros[filaInicial][columnaFinal] == -1 && matrizEnteros[filaFinal][columnaFinal] == 1) ){
                    SwitchQuemado = true;
                    circle.setFill(Color.BLACK);
                    estadoSwitch = false;
                }
    
                else if(matrizEnteros[filaInicial][columnaInicial] == 1){
                    if(filaFinal>=0 && filaFinal<5){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                        }
                    }else if(filaFinal>=5 && filaFinal<10){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                        }
                    }

                }else if(matrizEnteros[filaInicial][columnaInicial] == -1){
                    if(filaFinal>=0 && filaFinal<5){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaInicial);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                        }
                    }else if(filaFinal>=5 && filaFinal<10){
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                        }
                    }
                //esquina superior derecha con valor
                }else if(matrizEnteros[filaInicial][columnaFinal] == 1){
                    if(filaFinal>=0 && filaFinal<5){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);;
                            cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                        }
                    }else if(filaFinal>=5 && filaFinal<10){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.RED);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                        }
                    }
                }else if(matrizEnteros[filaInicial][columnaFinal] == -1){
                    if(filaFinal>=0 && filaFinal<5){
                        voltaje = Cables.getMatrizVoltaje(filaInicial, columnaFinal);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                    }else if(filaFinal>=5 && filaFinal<10){
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaFinal, columnaInicial, Color.BLUE);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                    }
                //esquina inferior izquierda con valor
                }else if(matrizEnteros[filaFinal][columnaInicial] == 1){
                    if(filaInicial>=0 && filaInicial<5){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                        }
                    }else if(filaInicial>=5 && filaInicial<10){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                        }
                        
                    }
                }else if(matrizEnteros[filaFinal][columnaInicial] == -1){
                    if(filaInicial>=0 && filaInicial<5){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                        }
                    }else if(filaInicial>=5 && filaInicial<10){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaInicial);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                        }
                    }
                //esquina inferior derecha con valor
                }else if(matrizEnteros[filaFinal][columnaFinal] == 1){
                    if(filaInicial>=0 && filaInicial<5){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                        }
                    }else if(filaInicial>=5 && filaInicial <10){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = 1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.RED);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = 1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.RED);
                        }
                    }
                }else if(matrizEnteros[filaFinal][columnaFinal] == -1){
                    if(filaInicial>=0 && filaInicial<5){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                        }
                        for(int i = 0; i < 5; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                        }
                    }else if(filaInicial>=5 && filaInicial <10){
                        voltaje = Cables.getMatrizVoltaje(filaFinal, columnaFinal);
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaInicial] = -1;
                            Cables.setMatrizVoltaje2(i, columnaInicial, voltaje);
                            cambiarColorCelda(filaInicial, columnaInicial, Color.BLUE);
                        }
                        for(int i = 5; i < 10; i++){
                            matrizEnteros[i][columnaFinal] = -1;
                            Cables.setMatrizVoltaje2(i, columnaFinal, voltaje);
                            cambiarColorCelda(filaInicial, columnaFinal, Color.BLUE);
                        }
                    }
                }
            }
        }else if(SwitchQuemado == true){
            circle.setFill(Color.BLACK);
            
        }
    }

    public void iniciarDibujoCable(double startX, double startY) {
        this.setStartX(startX);
        this.setStartY(startY);
    }

    public void finalizarDibujoCable(double endX, double endY, ImageView imagenSwitch) {
        this.setEndX(endX);
        this.setEndY(endY);
    
        // Calculate filaInicial and filaFinal
        filaInicial = (int) (this.getStartY() / 20);
        filaFinal = (int) (endY / 20);
        // Determine the size of the image based on filaInicial and filaFinal
        double imageWidth, imageHeight;
        if (filaInicial == 8 && filaFinal == 14 || filaFinal == 8 && filaInicial == 14) {
            imageWidth = 90; // Set width to 120
            imageHeight = 125; // Set height to 130
        } else {
            imageWidth = 90; // Default width
            imageHeight = 90; // Default height
        }
    
        crearImagenSwitch(imagenSwitch, imageWidth, imageHeight); // Update the image and circle when the cable drawing is finished
        
    }
    
    public void actualizarPane(Pane nuevoPane, ImageView imagenSwitch) {
        // Remover el cable del pane actual
        this.pane.getChildren().remove(this);
        // Actualizar el pane
        this.pane = nuevoPane;
        // Añadir el cable al nuevo pane
        nuevoPane.getChildren().add(this);
        // Volver a asignar el EventHandler de clic derecho para eliminar el cable
        crearImagenSwitch(imagenSwitch, 90, 100); // Update the image and circle when the cable drawing is finished
        
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
}