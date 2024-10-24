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
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;
    private Timeline timeline;

    public Switch(Pane pane, Color color, double startX, double startY, ImageView imagenSwitch, int[][] matrizEnteros, Pane matrizPane){
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(10);
        this.matrizEnteros = matrizEnteros;
        this.matrizPane = matrizPane;
       
        // Inicializamos las coordenadas del cable
        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX); // Inicialmente el final es el mismo que el inicio para solucionar el bug de la linea
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this); // Añadimos el cable al pane

        // Agregar EventHandler para detectar clic derecho
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Verificar si es clic derecho
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

                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaFinal, 0);
                pane.getChildren().remove(this); // Eliminar el cable del pane
                pane.getChildren().remove(this.imagenSwitch); // Eliminar la imagen del switch del pane
                pane.getChildren().remove(circle); // Eliminar el círculo del switch del pane
                timeline.stop(); // Detener el monitoreo constante
            }
        });

        // Iniciar el monitoreo constante
        iniciarMonitoreo();
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
        circle = new Circle(midX, midY, 23, Color.RED);
        circle.setOnMouseClicked(this::manejarClickCirculo);
    
        // Añadir EventHandler para eliminar el switch al hacer clic en la imagen
        this.imagenSwitch.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Verificar si es clic izquierdo
                pane.getChildren().remove(this); // Eliminar el cable del pane
                pane.getChildren().remove(this.imagenSwitch); // Eliminar la imagen del switch del pane
                pane.getChildren().remove(circle); // Eliminar el círculo del switch del pane
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
            actualizarEncendido();
        } else if (estadoSwitch) {
            circle.setFill(Color.RED);
            estadoSwitch = false;
            actualizarApagado();
        }
    }

    private void actualizarEncendido() {
        if(filaFinal>=0 && filaFinal<5){
            for(int i = 0; i < 5; i++){
                if(matrizEnteros[filaInicial][columnaInicial] == -1) {
                    matrizEnteros[i][columnaFinal] = -1;
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 1; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
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
                    matrizEnteros[i][columnaFinal] = -1;
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 1; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                }
                else {
                    matrizEnteros[filaInicial][columnaInicial] = 0;
                    matrizEnteros[filaFinal][columnaFinal] = 0; 
                    cambiarColorCelda(filaInicial, columnaInicial, Color.BLACK);
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
            }
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
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.009), event -> monitorearEstado()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void monitorearEstado() {
        if (estadoSwitch == true){
            if (matrizEnteros[filaInicial][columnaInicial] == 1 || matrizEnteros[filaInicial][columnaInicial] == -1){

                if(filaFinal>=0 && filaFinal<5){
                    for(int i = 0; i < 5; i++){
                        if(matrizEnteros[filaInicial][columnaInicial] == -1) {
                            matrizEnteros[i][columnaFinal] = -1;
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                        else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                            matrizEnteros[i][columnaFinal] = 1; 
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
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
                            matrizEnteros[i][columnaFinal] = -1;
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLUE);
                        }
                        else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                            matrizEnteros[i][columnaFinal] = 1; 
                            cambiarColorCelda(filaFinal, columnaFinal, Color.RED);
                        }
                        else {
                            matrizEnteros[filaInicial][columnaInicial] = 0;
                            matrizEnteros[filaFinal][columnaFinal] = 0; 
                            cambiarColorCelda(filaInicial, columnaInicial, Color.BLACK);
                            cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                        }
                    }
                }
            } else {
                if(matrizEnteros[filaInicial][columnaInicial] == 0) {
                   if(filaFinal >= 0 && filaFinal <5){
                    for(int i = 0; i < 5; i++){
                        matrizEnteros[i][columnaFinal] = 0;
                        cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                    }
                   }
                }
                if(matrizEnteros[filaInicial][columnaInicial] == 0) {
                    if(filaFinal >= 5 && filaFinal <10){
                     for(int i = 5; i < 10; i++){
                         matrizEnteros[i][columnaFinal] = 0;
                         cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                     }
                    }
                 }
            }
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
        System.out.println(filaInicial);
        System.out.println(filaFinal);
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
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                nuevoPane.getChildren().remove(this); // Asegurar que el cable se elimine del nuevo pane
                nuevoPane.getChildren().remove(this.imagenSwitch);
                nuevoPane.getChildren().remove(circle);
                if (estadoSwitch) {
                    matrizEnteros[filaFinal][columnaFinal] = 0;
                    cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
                }
            }
        });
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