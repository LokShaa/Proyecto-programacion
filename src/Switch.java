import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Switch {
    public void metodosSwitch(ImageView imagenSwitch, Pane paneDibujo){
        imagenSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            imagenSwitch.setEffect(glowSwitch);
        });

        imagenSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            imagenSwitch.setEffect(null);
        });

        imagenSwitch.setOnMouseClicked(clickedEvent -> { // Crear pane/imagen arrastrable
            Pane nuevoPaneSwitch = new Pane();
            nuevoPaneSwitch.setPrefSize(75, 95); // Tamaño del pane switch
            nuevoPaneSwitch.setStyle("-fx-background-color: green;"); // Fondo transparente
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

            Circle circuloSwitchIzq1 = new Circle(8, 5, 5);// Crea el círculo izquierdo superior del switch
            circuloSwitchIzq1.setStyle("-fx-fill: black;"); // Color del circulo

            Circle circuloSwitchDer1 = new Circle(8, 0, 5);// Crea el círculo derecho superior del switch
            circuloSwitchDer1.setStyle("-fx-fill: black;"); // Color del circulo

            Circle circuloSwitchIzq2 = new Circle(67, 5, 5);// Crea el círculo izquierdo inferior del switch
            circuloSwitchIzq2.setStyle("-fx-fill: black;"); // Color del circulo

            Circle circuloSwitchDer2 = new Circle(37.5, 0, 5); // Crea el círculo derecho inferior del switch
            circuloSwitchDer2.setStyle("-fx-fill: black;"); // Color del circulo

            nuevoPaneSwitch.getChildren().add(circuloSwitchIzq1);// Agregar el pane del switch al Pane principal
            //nuevoPaneSwitch.getChildren().add(circuloSwitchDer1);// Agregar el pane del switch al Pane principal
            nuevoPaneSwitch.getChildren().add(circuloSwitchIzq2);// Agregar el pane del switch al Pane principal
            //nuevoPaneSwitch.getChildren().add(circuloSwitchDer2);// Agregar el pane del switch al Pane principal



            // Hace el pane del switch arrastrable
            nuevoPaneSwitch.setOnMousePressed(pressEvent -> {
                nuevoPaneSwitch.setUserData(new double[]{pressEvent.getSceneX(), pressEvent.getSceneY(), nuevoPaneSwitch.getLayoutX(), nuevoPaneSwitch.getLayoutY()});
            });

            nuevoSwitch.setOnMouseDragged(dragEvent -> {
                double[] data = (double[]) nuevoPaneSwitch.getUserData();
                double deltaX = dragEvent.getSceneX() - data[0];
                double deltaY = dragEvent.getSceneY() - data[1];
                nuevoPaneSwitch.setLayoutX(data[2] + deltaX);
                nuevoPaneSwitch.setLayoutY(data[3] + deltaY);
            });
        
        });
    
    }
}
