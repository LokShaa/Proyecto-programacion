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

    public Switch(Pane pane, Color color, double startX, double startY, ImageView imagenSwitch, int[][] matrizEnteros, Pane matrizPane) {
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
    private int ajustarColumna(int columna) {
        columna -= (columna / 2);
        if (columna > 20) {
            columna += 1;
        }
        return columna;
    }

    private void crearImagenSwitch(ImageView imagenSwitch) {
        // Calcular el punto medio del cable
        double midX = (this.getStartX() + this.getEndX()) / 2;
        double midY = (this.getStartY() + this.getEndY()) / 2;

        // Crear la imagen
        this.imagenSwitch = new ImageView(imagenSwitch.getImage());
        this.imagenSwitch.setFitWidth(50); // Ancho
        this.imagenSwitch.setFitHeight(60); // Alto
        this.imagenSwitch.setX(midX - 25);
        this.imagenSwitch.setY(midY - 28);

        // Crear el círculo clickeable
        circle = new Circle(midX, midY, 14, Color.RED);
        circle.setOnMouseClicked(this::manejarClickCirculo);

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
                    cambiarColorCelda(filaFinal, columnaFinal, Color.YELLOW);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 1; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.YELLOW);
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
                    cambiarColorCelda(filaFinal, columnaFinal, Color.YELLOW);
                }
                else if (matrizEnteros[filaInicial][columnaInicial] == 1) {
                    matrizEnteros[i][columnaFinal] = 1; 
                    cambiarColorCelda(filaFinal, columnaFinal, Color.YELLOW);
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
    }
    
    // Método auxiliar para cambiar el color de una celda en la matriz
    private void cambiarColorCelda(int fila, int columna, Color color) {
        Pane targetCell = (Pane) matrizPane.getChildren().get(fila * matrizEnteros[fila].length + columna);
        
        if (fila >=0 && fila <5){
            for(int i = 0; i < 5; i++){
                if (color == color.YELLOW){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: yellow;");
                }
                else if (color == color.BLACK){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: black;");
                }
            }
        }
        if(fila >=5 && fila <10){
            for(int i = 5; i < 10; i++){
                if (color == color.YELLOW){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: yellow;");
                }
                else if (color == color.BLACK){
                    targetCell = (Pane) matrizPane.getChildren().get(i * matrizEnteros[fila].length + columna);
                    targetCell.setStyle("-fx-background-color: black;");
                }
            }
        }
        
    }

    private void iniciarMonitoreo() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> monitorearEstado()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void monitorearEstado() {
        if (estadoSwitch) {
            if (matrizEnteros[filaInicial][columnaInicial] == 1 || matrizEnteros[filaInicial][columnaInicial] == -1) {
                matrizEnteros[filaFinal][columnaFinal] = matrizEnteros[filaInicial][columnaInicial];
                cambiarColorCelda(filaFinal, columnaFinal, Color.YELLOW);
            } else {
                matrizEnteros[filaFinal][columnaFinal] = 0;
                cambiarColorCelda(filaFinal, columnaFinal, Color.BLACK);
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
        crearImagenSwitch(imagenSwitch); // Actualizar la imagen y el círculo cuando se finaliza el dibujo del cable
    }

    public void actualizarPane(Pane nuevoPane, ImageView imagenSwitch) {
        // Guardar las coordenadas globales del cable
        double xGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getX();
        double yGlobalesIniciales = pane.localToScene(this.getStartX(), this.getStartY()).getY();
        double xGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getX();
        double yGlobalesFinales = pane.localToScene(this.getEndX(), this.getEndY()).getY();

        // Remover el cable del pane actual
        this.pane.getChildren().remove(this);

        // Actualizar el pane
        this.pane = nuevoPane;

        // Añadir el cable al nuevo pane
        nuevoPane.getChildren().add(this);

        // Convertir las coordenadas globales a las coordenadas locales del nuevo pane
        double xLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getX();
        double yLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getY();
        double xLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getX();
        double yLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getY();

        // Actualizar las coordenadas del cable
        this.setStartX(xLocalesIniciales);
        this.setStartY(yLocalesIniciales);
        this.setEndX(xLocalesFinales);
        this.setEndY(yLocalesFinales);

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
        crearImagenSwitch(imagenSwitch); // Actualizar la imagen y el círculo cuando se actualiza el pane
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