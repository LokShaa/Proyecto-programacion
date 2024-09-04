import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Led {

    private Circle selectedCircle = null;
    private boolean cableDibujadoIzq = false; // bandera para saber si el cable izquierdo ya fue dibujado
    private boolean cableDibujadoDer = false; // bandera para saber si el cable derecho ya fue dibujado


    public Circle circulosLed(double x, double y, double radius) {
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.BLACK);
        circle.setStroke(Color.BLACK);
        circle.setOnMouseClicked(event -> {
            selectedCircle = circle;
            Glow glow = new Glow(1);
            circle.setEffect(glow);
        });
        return circle;
    }
    public void brilloLed(ImageView imagenLed){
        imagenLed.setOnMouseEntered(enteredEvent -> { //Brillo para el led
            Glow glowLed = new Glow(1);
            imagenLed.setEffect(glowLed);
        });

        imagenLed.setOnMouseExited(exitEvent -> { //Se quita el brillo del led
            imagenLed.setEffect(null);
        });
    }

    private boolean movido = false; // bandera para que el pane del led no se vuelva a mover despues de soltar el mouse

    public void ledArrastrable(ImageView imagenLed, ImageView imagenLed2, Pane paneDibujo){

        imagenLed.setOnMouseClicked(clickedEvent -> { // Crear pane/imagen arrastrable
            movido = false; // Reiniciar la bandera para que el pane del switch se pueda mover nuevamente
            cableDibujadoDer = false; // Reiniciar la bandera para que el cable derecho  se pueda dibujar nuevamente
            cableDibujadoDer = false; // Reiniciar la bandera para que el cable derecho  se pueda dibujar nuevamente
            
            
            Pane nuevoPaneLed = new Pane();
            nuevoPaneLed.setPrefSize(30, 45); // Tamaño del pane switch
            nuevoPaneLed.setStyle("-fx-background-color: transparent;"); // Color transparente del pane
            nuevoPaneLed.setLayoutX(200); // Posición inicial X del nuevo Pane
            nuevoPaneLed.setLayoutY(200); // Posición inicial Y del nuevo Pane
            
            paneDibujo.getChildren().add(nuevoPaneLed);// Agregar el pane del switch al Pane principal

            ImageView nuevoLed = new ImageView(imagenLed2.getImage());
            nuevoLed.setX(0); // Posición inicial X
            nuevoLed.setY(0); // Posición inicial Y

            // Establecer el tamaño del led
            nuevoLed.setFitWidth(30); // Ancho
            nuevoLed.setFitHeight(44); // Alto

            nuevoPaneLed.getChildren().add(nuevoLed); //Agregar la imagen del led a nuevoPaneLed
            Circle circuloSwitchIzq = circulosLed(10, 42, 3);// Crea el círculo izquierdo del led
            Circle circuloSwitchDer = circulosLed(20, 42, 3);// Crea el círculo derecho del led
            
            nuevoPaneLed.getChildren().addAll(circuloSwitchIzq, circuloSwitchDer); // Agrega los panes de los circulos del led al Pane principal

            // Hace el pane del led arrastrable
            nuevoPaneLed.setOnMousePressed(pressEvent -> {
                nuevoPaneLed.setUserData(new double[]{pressEvent.getSceneX(), pressEvent.getSceneY(), nuevoPaneLed.getLayoutX(), nuevoPaneLed.getLayoutY()});
            });

            nuevoPaneLed.setOnMouseDragged(dragEvent -> {
                if(!movido){
                    double[] data = (double[]) nuevoPaneLed.getUserData();
                    double deltaX = dragEvent.getSceneX() - data[0];
                    double deltaY = dragEvent.getSceneY() - data[1];
                    nuevoPaneLed.setLayoutX(data[2] + deltaX);
                    nuevoPaneLed.setLayoutY(data[3] + deltaY);
                }
            });

            nuevoPaneLed.setOnMouseReleased(releaseEvent -> {
                if (!movido) {
                    movido = true; // Marcar que el pane ha sido movido después de soltar el mouse
                }
            });
        
        });
    }
    // Getters y Setters para los circulos generados
    public Circle getSelectedCircle() {
        return selectedCircle;
    }

    public void setSelectedCircle(Circle selectedCircle) {
        this.selectedCircle = selectedCircle;
    }

    public boolean isCableDibujado(Circle circle) {
        if (circle.getCenterX() == 10 && circle.getCenterY() == 42) {
            return cableDibujadoIzq;
        } else if (circle.getCenterX() == 20 && circle.getCenterY() == 42) {
            return cableDibujadoDer;
        }
        return false;
    }
    
    public void setCableDibujado(Circle circle, boolean dibujado) {
        if (circle.getCenterX() == 10 && circle.getCenterY() == 42) {
            cableDibujadoIzq = dibujado;
        } else if (circle.getCenterX() == 10 && circle.getCenterY() == 42) {
            cableDibujadoDer = dibujado;
        }
    }
}