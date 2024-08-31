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
        this.setMouseTransparent(true);
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
        double xLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getX();//SceneToLocal convierte las coordenadas globales a las locales!!
        double yLocalesIniciales = nuevoPane.sceneToLocal(xGlobalesIniciales, yGlobalesIniciales).getY();
        double xLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getX();
        double yLocalesFinales = nuevoPane.sceneToLocal(xGlobalesFinales, yGlobalesFinales).getY();

        // Actualizar las coordenadas del cable
        this.setStartX(xLocalesIniciales);
        this.setStartY(yLocalesIniciales);
        this.setEndX(xLocalesFinales);
        this.setEndY(yLocalesFinales);
        
        pane.toFront(); //Traer el cable al frente
    }

    public Pane getPane(){
        return pane;
    }
}
