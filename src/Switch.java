import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Switch {
    //metodo q crea un circulo con las coordenadas y radio que se le pasan y brilla cuando se pasa el mouse por encima
    public Circle circulosSwitch(int x, int y, int r){

        Circle circuloSwitch = new Circle(x,y,r);// Crea un círculo para el switch
        circuloSwitch.setStyle("-fx-fill: black;"); // Color del circulo
        circuloSwitch.setStyle("-fx-fill: black;"); // Color del circulo
        circuloSwitch.setOnMouseEntered(enteredEvent -> { // Brillo para el switch
            Glow glowSwitch = new Glow(1);
            circuloSwitch.setStyle("-fx-fill: black;"); // Color del circulo
            circuloSwitch.setEffect(glowSwitch);
        });
        circuloSwitch.setOnMouseExited(exitEvent -> { // Se quita el brillo del switch
            circuloSwitch.setEffect(null);
        });
        return circuloSwitch;
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

    public boolean comprobarCirculo (Circle circulo, double startX, double startY, Pane paneSwitch) {
        double centerX = circulo.getLayoutX() + circulo.getRadius();
        double centerY = circulo.getLayoutY() + circulo.getRadius();
        double radius = circulo.getRadius();
    
        // Verificar si el clic está dentro del círculo
        double distance = Math.sqrt(Math.pow(startX - centerX, 2) + Math.pow(startY - centerY, 2));
        if (distance <= radius) {
            // Dibujar el cable desde el círculo hasta el paneMatriz
            Line cable = new Line(centerX, centerY, startX, startY);
            paneSwitch.getChildren().add(cable);
            return true; // El clic está dentro del círculo
        }
        return false; // El clic no está dentro del círculo
    }

    public void dibujarCablesSwitch(boolean comprobarCirculo,Circle circulo, Pane paneSwitch, Runnable onComplete, Cables cableActual, Color colorActual) {
        circulo.setOnMouseClicked(mouseClickedEvent -> {
            final Cables[] cableActualEspecial = {cableActual};//solo pq no está en el main es necesario esto
            double x = mouseClickedEvent.getX();
            double y = mouseClickedEvent.getY();
            boolean cableIniciado = false;

            if (cableActual == null) {
                // Iniciar el dibujo del cable
                if (comprobarCirculo(circulo,x,y,paneSwitch)) {
                    cableActualEspecial[0] = new Cables(paneSwitch, colorActual, x, y);
                    cableActualEspecial[0].iniciarDibujoCable(x, y);
                    cableIniciado = true;
                }
            } else {
                // Finalizar el dibujo del cable
                if (comprobarCirculo(circulo,x,y,paneSwitch)) {
                    cableActualEspecial[0].finalizarDibujoCable(x, y);
                    cableActualEspecial[0] = null; // Finalizamos el dibujo del cable haciendo que sea null otra vez
                    //botonCableAzul1.toFront();
                    //botonCableAzul2.toFront();
                    //botonCableRojo1.toFront();
                    //botonCableRojo2.toFront();
                    onComplete.run();
                }
            }

            if (!cableIniciado && cableActual != null) {
                // Si no se inició el cable y cableActual no es null, reiniciar cableActual
                cableActualEspecial[0] = null;
            }
        });
    }

    // Método para desactivar los eventos de dibujo
    private void desactivarDibujoSwitch(Pane paneSwitch) {
        paneSwitch.setOnMouseClicked(null);
    }

    public void switchArrastrable(ImageView imagenSwitch, Pane paneDibujo){

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
                double[] data = (double[]) nuevoPaneSwitch.getUserData();
                double deltaX = dragEvent.getSceneX() - data[0];
                double deltaY = dragEvent.getSceneY() - data[1];
                nuevoPaneSwitch.setLayoutX(data[2] + deltaX);
                nuevoPaneSwitch.setLayoutY(data[3] + deltaY);
                
            });
        });
    };
}