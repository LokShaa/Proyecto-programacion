import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line{
    private String tipo;//Atributo para saber si es cable positivo o negativo
    private Pane pane; //Atributo para saber en que pane se dibujara el cable
    private static final int CELL_SIZE = 20;
    int filaInicial;
    int columnaInicial;
    int filaFinal;
    int columnaFinal;
    public Cables(){//Constructor de la clase
    }

    public Cables(Pane pane, Color color, double startX, double startY) { // Constructor de la clase con un pane que ira sobre la imagen del protoboard para dibujar los cables sobre esta
        
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(10);

        // Inicializamos las coordenadas del cable
        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX); //Inicialmente el final es el mismo que el inicio para solucionar el bug de la linea
        this.setEndY(startY);

        this.setMouseTransparent(false);
        pane.getChildren().add(this); // Añadimos el cable al pane

        // Agregar EventHandler para detectar clic derecho
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {//Verificar si es clic derecho
                double xLocalInicial = this.getStartX();
                double yLocalInicial = this.getStartY();
                double xLocalFinal = this.getEndX();
                double yLocalFinal = this.getEndY();
                filaInicial = (int) (yLocalInicial / CELL_SIZE);
                columnaInicial = (int) (xLocalInicial / CELL_SIZE);
                filaFinal = (int) (yLocalFinal / CELL_SIZE);
                columnaFinal = (int) (xLocalFinal / CELL_SIZE);
                
                filaInicial = ajustarFila(filaInicial);
                columnaInicial = ajustarColumna(columnaInicial);
                filaFinal = ajustarFila(filaFinal);
                columnaFinal = ajustarColumna(columnaFinal);

                Main.matrizCentralProtoboard.setMatrizCables(filaInicial, columnaInicial, 0);
                Main.matrizCentralProtoboard.setMatrizCables(filaFinal, columnaFinal, 0);
                
                pane.getChildren().remove(this); //Eliminar el cable del pane
            }
        });
    }
    public int getFilaInicial(){
        return filaInicial;
    }
    public int getColumnaInicial(){
        return columnaInicial;
    }
    public int getFilaFinal(){
        return filaFinal;
    }
    public int getColumnaFinal(){
        return columnaFinal;
    }
    // Método para ajustar la fila según las reglas específicas
    private int ajustarFila(int fila) {
        fila -= (fila / 2);
        if (fila >= 7){
            fila -=2;
        }
        
        return fila;
    }

    // Método para ajustar la columna según las reglas específicas
    private int ajustarColumna(int columna) {
        columna -= (columna / 2);
        if (columna > 20 ){
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
    }
    
    public void actualizarPane(Pane nuevoPane) {
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
            }
        });
    }

    //metodo para asignar el tipo de cable
    public void setTipo(Color color) {
        if (color.equals(Color.RED)) {
            this.tipo = "Positivo";
        } else if (color.equals(Color.BLUE)) {
            this.tipo = "Negativo";
        }
    }

    public Pane getPane(){
        return pane;
    }

    public double getXInicial(){
        return this.getStartX();
    }

    public double getYInicial(){
        return this.getStartY();
    }
}