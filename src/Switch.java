import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Switch{
    private Circle selectedCircle = null;

    /* 
    //metodo q crea un circulo con las coordenadas y radio que se le pasan y brilla cuando se pasa el mouse por encima
    public Circle circulosSwitch(int x, int y, int r){

        Circle circuloSwitch = new Circle(x,y,r);// Crea un círculo para el switch
        circuloSwitch.setStyle("-fx-fill: black;"); // Color del circulo
        circuloSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            circuloSwitch.setStyle("-fx-fill: yellow;"); // Color del circulo
            circuloSwitch.setEffect(glowSwitch);
        });
        circuloSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            circuloSwitch.setEffect(null);
            circuloSwitch.setStyle("-fx-fill: black;"); // Color del circulo
        });
        return circuloSwitch;
    }
    */
    public Circle circulosSwitch(double x, double y, double radius) {
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setOnMouseClicked(event -> {
            selectedCircle = circle;
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

    public void switchArrastrable(ImageView imagenSwitch, Pane paneDibujo, Protoboard matrizCentral){

        imagenSwitch.setOnMouseClicked(clickedEvent -> { // Crear pane/imagen arrastrable
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
        
        });


    };
    public Circle getSelectedCircle() {
        return selectedCircle;
    }

    public void setSelectedCircle(Circle selectedCircle) {
        this.selectedCircle = selectedCircle;
    }

}