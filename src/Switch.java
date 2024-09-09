import java.util.ArrayList;
import java.util.List;

import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;

public class Switch extends Line{
    //////////////////////////////////////////////////
private List<Line> cables = new ArrayList<>();
private List<ImageView> imagenesSwitch = new ArrayList<>();
private List<Circle> circulos = new ArrayList<>();
/////////////////////////////////////////////////////////

    public void brilloSwitch(ImageView imagenSwitch){

        imagenSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });
    
        imagenSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            imagenSwitch.setEffect(null);
        });

    }

    private String tipo; // Atributo para saber si es cable positivo o negativo
    private Pane pane; //Atributo para saber en que pane se dibujara el cable

    private ImageView imagenSwitch;
    private Circle circle;


    public Switch(Pane pane, Color color, double startX, double startY, ImageView imagenSwitch) {
        this.pane = pane;
        this.setStroke(color);
        this.setStrokeWidth(10);

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
                pane.getChildren().remove(this); // Eliminar el cable del pane
                pane.getChildren().remove(this.imagenSwitch); // Eliminar la imagen del switch del pane
                pane.getChildren().remove(circle); // Eliminar el círculo del switch del pane
            }
        });
    }

    private void crearImagenSwitch(ImageView imagenSwitch) {
        // Calcular el punto medio del cable
        double midX = (this.getStartX() + this.getEndX()) / 2;
        double midY = (this.getStartY() + this.getEndY()) / 2;

        // Crear la imagen
        this.imagenSwitch = new ImageView(imagenSwitch.getImage());
        this.imagenSwitch.setFitWidth(50); // Ancho
        this.imagenSwitch.setFitHeight(57); // Alto
        this.imagenSwitch.setX(midX-25);
        this.imagenSwitch.setY(midY-28);
  
        

        // Crear el círculo clickeable
        circle = new Circle(midX, midY, 14, Color.RED);
        circle.setOnMouseClicked(event -> {
            if (circle.getFill().equals(Color.RED)) {
                circle.setFill(Color.GREEN);
            } else {
                circle.setFill(Color.RED);
            }
        });

        // Añadir la imagen y el círculo al pane
        pane.getChildren().addAll(this.imagenSwitch, circle);
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
            }
        });

        crearImagenSwitch(imagenSwitch); // Actualizar la imagen y el círculo cuando se actualiza el pane
    }

    // Método para asignar el tipo de cable
    public void setTipo(Color color) {
        if (color.equals(Color.RED)) {
            this.tipo = "Positivo";
        } else if (color.equals(Color.BLUE)) {
            this.tipo = "Negativo";
        }
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