import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cables extends Line{
    private String tipo;//Atributo para saber si es cable positivo o negativo
    private Pane pane; //Atributo para saber en que pane se dibujara el cable

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
        pane.getChildren().add(this); //Añadimos el cable al pane

        // Agregar EventHandler para detectar clic derecho
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {//Veriicar si es clic derecho
                pane.getChildren().remove(this); //Eliminar el cable del pane
            }
        });
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
}
