import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line{
    private String tipo;//Atributo para saber si es cable positivo o negativo   
    private Pane pane;//Atributo para saber en que pane se dibujara el cable

    public Cables(){
    }

    public Cables(Pane pane,Color color, double startX, double startY){ //Constructor de la clase con un pane que ira sobre la imagen del protoboard para dibujar los cables sobre esta
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(10);

        // Inicializamos las coordenadas del cable
        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(startX);  // Inicialmente el final es el mismo que el inicio para solucionar el bug de la linea 
        this.setEndY(startY);

        pane.getChildren().add(this); // Añadimos el cable al pane

    }    
    public void iniciarDibujoCable(double startX, double startY){
        this.setStartX(startX);
        this.setStartY(startY);
    }
    public void finalizarDibujoCable(double endX,double endY){
        this.setEndX(endX);
        this.setEndY(endY);
    }
}
