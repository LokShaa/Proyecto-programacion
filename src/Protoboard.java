import javafx.scene.layout.Pane;

public class Protoboard{
    //Declara matrix como atributo de la clase
    private Pane[][] matriz;
    
    public void inicializarMatrizCentral(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane) {
        matriz = new Pane[filas][columnas];
        for (int i = 0; i < filas; i++) {
            
            for (int j = 0; j < columnas; j++) {
                Pane cell = new Pane();
                cell.setPrefSize(cellAncho, cellAlt);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");

                // Calcular la posición de la celda
                double x = j * (cellAncho + padding1);
                double y = i * (cellAlt + padding2);
                
                if (i > 4) {
                    y +=  75; // Añadir espacio adicional
                }
                cell.setLayoutX(x);
                cell.setLayoutY(y);
                
                matrizPane.getChildren().add(cell);
                matriz[i][j] = cell;
            }
        }

        // Ajustar el tamaño del pane matrizPane
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                double ancho = columnas * (cellAncho + padding1);
                double alto = filas * (cellAlt + padding2);
                if (i > 4) {
                    alto += 75;
                    matrizPane.setPrefSize(ancho, alto);
                }
                else{
                    matrizPane.setPrefSize(ancho, alto);
                }
            }
        }
    }

    public void inicializarMatrizSupInf(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Pane cell = new Pane();
                cell.setPrefSize(cellAncho, cellAlt);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");

                // Calcular la posición de la celda
                double x = j * (cellAncho + padding1);
                double y = i * (cellAlt + padding2);
                
                matriz[i][j] = cell;
                matrizPane.getChildren().add(cell);
                cell.setLayoutX(x);
                cell.setLayoutY(y);
            }
        }
        // Ajustar el tamaño del pane matrizPane
        matrizPane.setPrefSize(columnas * (cellAncho + padding1), filas * (cellAlt + padding2));
    }
    public void inicializarMatrizCablesBateriaRojo(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Pane cell = new Pane();
                    cell.setPrefSize(cellAncho, cellAlt);
                    cell.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-background-color: red;");
            

                    // Calcular la posición de la celda
                    double x = j * (cellAncho + padding1);
                    double y = i * (cellAlt + padding2);
                    
                    matriz[i][j] = cell;
                    matrizPane.getChildren().add(cell);
                    cell.setLayoutX(x);
                    cell.setLayoutY(y);
                }
            }
        // Ajustar el tamaño del pane matrizPane
        matrizPane.setPrefSize(columnas * (cellAncho + padding1), filas * (cellAlt + padding2));
    }
    public void inicializarMatrizCablesBateriaAzul(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Pane cell = new Pane();
                    cell.setPrefSize(cellAncho, cellAlt);
                    cell.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-background-color: blue;");

                    // Calcular la posición de la celda
                    double x = j * (cellAncho + padding1);
                    double y = i * (cellAlt + padding2);
                    
                    matriz[i][j] = cell;
                    matrizPane.getChildren().add(cell);
                    cell.setLayoutX(x);
                    cell.setLayoutY(y);
                }
            }
        // Ajustar el tamaño del pane matrizPane
        matrizPane.setPrefSize(columnas * (cellAncho + padding1), filas * (cellAlt + padding2));
    }

    public boolean comprobarCuadrado(int fila, int columna, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane,double startX, double startY){
        // Obtener las coordenadas del click

        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
            
                // Obtener las coordenadas del cuadrado
                double squareX = matriz[i][j].getLayoutX();
                double squareY = matriz[i][j].getLayoutY();
                
                double largoX = squareX + cellAncho;
                double largoY = squareY + cellAlt;

                // Verificar si el click está dentro del cuadrado
                if (startX >= squareX && startX <= largoX && startY<=largoY && startY>= squareY) {
                    return true;
                }
            }
        }
        return false;
        
    }
    public Pane[][] getMatriz() {
        return matriz;
    }
}