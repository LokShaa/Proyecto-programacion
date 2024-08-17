import javafx.scene.input.MouseEvent;//libreria para controlar los eventos del mouse
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line{
    private String tipo;//Atributo para saber si es cable positivo o negativo   

    public Cables(Pane pane){ //Constructor de la clase con un pane que ira sobre la imagen del protoboard para dibujar los cables sobre esta
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
 
    }

    

    
}
