import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseButton;

public class Switch{
    private Circle selectedCircle = null;
    private boolean cableDibujadoIzq1 = false; // bandera para saber si el cable izquierdo 1 ya fue dibujado
    private boolean cableDibujadoDer1 = false; // bandera para saber si el cable derecho 1 ya fue dibujado
    private boolean cableDibujadoIzq2 = false; // bandera para saber si el cable izquierdo 2 ya fue dibujado
    private boolean cableDibujadoDer2 = false; // bandera para saber si el cable derecho 2 ya fue dibujado

    public Circle circulosSwitch(double x, double y, double radius) {
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setOnMouseClicked(event -> {
            selectedCircle = circle;
            Glow glow = new Glow(1);
            circle.setEffect(glow);
        });
        return circle;
    }


    public void brilloSwitch(ImageView imagenSwitch){

        imagenSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });
    
        imagenSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            imagenSwitch.setEffect(null);
        });

    }

    private boolean movido = false; // bandera para que el pane del switch no se vuelva a mover despues de soltar el mouse

    public void switchArrastrable(ImageView imagenSwitch, Pane paneDibujo){

        imagenSwitch.setOnMouseClicked(clickedEvent -> { // Crear pane/imagen arrastrable
            movido = false; // Reiniciar la bandera para que el pane del switch se pueda mover nuevamente
            cableDibujadoDer1 = false; // Reiniciar la bandera para que el cable derecho 1 se pueda dibujar nuevamente
            cableDibujadoDer2 = false; // Reiniciar la bandera para que el cable derecho 2 se pueda dibujar nuevamente
            cableDibujadoIzq1 = false; // Reiniciar la bandera para que el cable izquierdo 1 se pueda dibujar nuevamente
            cableDibujadoIzq2 = false; // Reiniciar la bandera para que el cable izquierdo 2 se pueda dibujar nuevamente
            
            Pane nuevoPaneSwitch = new Pane();
            nuevoPaneSwitch.setPrefSize(75, 95); // Tamaño del pane switch
            nuevoPaneSwitch.setStyle("-fx-background-color: transparent;"); // Color transparente del pane
            nuevoPaneSwitch.setLayoutX(200); // Posición inicial X del nuevo Pane
            nuevoPaneSwitch.setLayoutY(200); // Posición inicial Y del nuevo Pane
            
            paneDibujo.getChildren().add(nuevoPaneSwitch);// Agregar el pane del switch al Pane principal

            ImageView nuevoSwitch = new ImageView(imagenSwitch.getImage());
            nuevoSwitch.setX(0); // Posición inicial X
            nuevoSwitch.setY(0); // Posición inicial Y

            // Establecer el tamaño del switch
            nuevoSwitch.setFitWidth(75); // Ancho
            nuevoSwitch.setFitHeight(94); // Alto

            nuevoPaneSwitch.getChildren().add(nuevoSwitch); //Agregar la imagen del Switch a nuevoPaneSwitch
            Circle circuloSwitchIzq1 = circulosSwitch(8, 5, 5);// Crea el círculo izquierdo superior del switch
            Circle circuloSwitchDer1 = circulosSwitch(67, 5, 5);// Crea el círculo derecho superior del switch
            Circle circuloSwitchIzq2 = circulosSwitch(8, 90, 5);// Crea el círculo izquierdo inferior del switch
            Circle circuloSwitchDer2 = circulosSwitch(67, 90, 5);// Crea el círculo derecho inferior del switch
            nuevoPaneSwitch.getChildren().addAll(circuloSwitchIzq1, circuloSwitchDer1, circuloSwitchIzq2, circuloSwitchDer2); // Agrega los panes de los circulos del switch al Pane principal

            //Circulo grande central para que al hacer click en el cambie el color inficando que esta activo o inactivo
            Circle circuloSwitchCentral = new Circle(38, 47, 20);
            circuloSwitchCentral.setFill(Color.RED);
            circuloSwitchCentral.setStroke(Color.BLACK);
            
            circuloSwitchCentral.setOnMouseClicked(event -> {
                if (circuloSwitchCentral.getFill() == Color.RED) {
                    circuloSwitchCentral.setFill(Color.GREEN);
                } else {
                    circuloSwitchCentral.setFill(Color.RED);
                }
            });
            nuevoPaneSwitch.getChildren().add(circuloSwitchCentral);



            // Hace el pane del switch arrastrable
            nuevoPaneSwitch.setOnMousePressed(pressEvent -> {
                nuevoPaneSwitch.setUserData(new double[]{pressEvent.getSceneX(), pressEvent.getSceneY(), nuevoPaneSwitch.getLayoutX(), nuevoPaneSwitch.getLayoutY()});
            });

            nuevoSwitch.setOnMouseDragged(dragEvent -> {
                if(!movido){
                    double[] data = (double[]) nuevoPaneSwitch.getUserData();
                    double deltaX = dragEvent.getSceneX() - data[0];
                    double deltaY = dragEvent.getSceneY() - data[1];
                    nuevoPaneSwitch.setLayoutX(data[2] + deltaX);
                    nuevoPaneSwitch.setLayoutY(data[3] + deltaY);
                }
            });

            nuevoPaneSwitch.setOnMouseReleased(releaseEvent -> {
                if (!movido) {
                    movido = true; // Marcar que el pane ha sido movido después de soltar el mouse
                }
            });
            nuevoPaneSwitch.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Verificar si el clic es con el botón derecho
                    paneDibujo.getChildren().remove(nuevoPaneSwitch); // Eliminar el pane del switch
                }
            });
        });
        
    };

    // Getters y Setters para los circulos generados
    public Circle getSelectedCircle() {
        return selectedCircle;
    }

    public void setSelectedCircle(Circle selectedCircle) {
        this.selectedCircle = selectedCircle;
    }

    public boolean isCableDibujado(Circle circle) {
        if (circle.getCenterX() == 8 && circle.getCenterY() == 5) {
            return cableDibujadoIzq1;
        } else if (circle.getCenterX() == 67 && circle.getCenterY() == 5) {
            return cableDibujadoDer1;
        } else if (circle.getCenterX() == 8 && circle.getCenterY() == 90) {
            return cableDibujadoIzq2;
        } else if (circle.getCenterX() == 67 && circle.getCenterY() == 90) {
            return cableDibujadoDer2;
        }
        return false;
    }

    public void setCableDibujado(Circle circle, boolean dibujado) {
        if (circle.getCenterX() == 8 && circle.getCenterY() == 5) {
            cableDibujadoIzq1 = dibujado;
        } else if (circle.getCenterX() == 67 && circle.getCenterY() == 5) {
            cableDibujadoDer1 = dibujado;
        } else if (circle.getCenterX() == 8 && circle.getCenterY() == 90) {
            cableDibujadoIzq2 = dibujado;
        } else if (circle.getCenterX() == 67 && circle.getCenterY() == 90) {
            cableDibujadoDer2 = dibujado;
        }
    }

}