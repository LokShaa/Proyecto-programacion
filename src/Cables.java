import javafx.scene.input.MouseEvent;//libreria para controlar los eventos del mouse
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line{
    private String tipo;//Atributo para saber si es cable positivo o negativo   
    private Pane pane;//Atributo para saber en que pane se dibujara el cable


    /*  public Cables(Pane pane){ //Constructor de la clase con un pane que ira sobre la imagen del protoboard para dibujar los cables sobre esta
        this.setStroke(Color.RED);//Color del cable
        this.setStrokeWidth(3);//Grosor del cable

        pane.getChildren().add(this);//Agregamos el cable al pane
        
        pane.setOnMousePressed((MouseEvent event) ->{
            this.setStartX(event.getX());//Obtenemos la posicion en x donde se hizo click
            this.setStartY(event.getY());//Obtenemos la posicion en y donde se hizo click
        });

        pane.setOnMouseReleased((MouseEvent event) ->{
            this.setEndX(event.getX());//Obtenemos la posicion en x donde se hizo click
            this.setEndY(event.getY());//Obtenemos la posicion en y donde se hizo click
        });
        */

    public Cables(Pane pane,Color color, double startX, double startY){ //Constructor de la clase con un pane que ira sobre la imagen del protoboard para dibujar los cables sobre esta
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(4);

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
