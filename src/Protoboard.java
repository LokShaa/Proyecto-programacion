import javafx.scene.layout.Pane;

public class Protoboard{
    //Declara matrix como atributo de la clase
    private Pane[][] matriz;
    private int [][] matrizEnteros;
    //metodo para iniciar la matriz central de panes
    public void inicializarMatrizCentral(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane) {
        matriz = new Pane[filas][columnas];
        matrizEnteros = new int[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++){
                matrizEnteros[i][j] = 0; //inicializar matriz con 0
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
                
                final int fila = i; 
                final int columna = j;
                cell.setOnMouseClicked(event -> {
                    //CAMBIAMOS LA MATRIZ DE ENTEROS AL VALOR 1
                    matrizEnteros[fila][columna] = 1;
                    //CAMBIAMOS LA CELDA A AMARILLO PARA VER SI FUNCIONA EL CLICK 
                    cell.setStyle("-fx-background-color: yellow;");//CAMBIAR EL COLOR PARA PROBAR EL CLICK
                    //METODO PARA IMPRIMIR LA MATRIZ EN CONSOLA Y VER QUE SE ACTUALICEN LOS VALORES
                    imprimirMatrizEnteros();
                });
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
        matrizEnteros = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++){
                matrizEnteros[i][j] = 0; //inicializar matriz con 0
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

                final int fila = i; //usamos solo la fila ya que en la matriz de 2x30 se cambia solo la fila

                cell.setOnMouseClicked(event -> {
                    // Cambiar toda la fila en la matriz de enteros a 1
                for (int col = 0; col < columnas; col++) {
                    matrizEnteros[fila][col] = 1;
                    matriz[fila][col].setStyle("-fx-background-color: yellow;");
                }
                    //METODO PARA IMPRIMIR LA MATRIZ EN CONSOLA Y VER QUE SE ACTUALICEN LOS VALORES
                    imprimirMatrizEnteros();
                });
                
            }
        }
        // Ajustar el tamaño del pane matrizPane
        matrizPane.setPrefSize(columnas * (cellAncho + padding1), filas * (cellAlt + padding2));
    }

    public void inicializarMatrizCablesBateria(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        if(filas == 1){
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Pane cell = new Pane();
                    cell.setPrefSize(cellAncho, cellAlt);
                    if (j == 0) {
                        cell.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-background-color: red;");
                    }
                    else{
                        cell.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-background-color: blue;");
                    }

                    // Calcular la posición de la celda
                    double x = j * (cellAncho + padding1);
                    double y = i * (cellAlt + padding2);
                    
                    matriz[i][j] = cell;
                    matrizPane.getChildren().add(cell);
                    cell.setLayoutX(x);
                    cell.setLayoutY(y);
                    
                    /*final int fila = i; // Necesario para usar dentro del lambda
                    cell.setOnMouseClicked(event -> {
                        // Cambiar el color de toda la columna
                        for (int col = 0; col < columnas; col++) {
                            matriz[fila][col].setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: yellow;"); // Cambiar color a azul
                        }
                    });*/
                }
            }
        }
        if (filas == 2) {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Pane cell = new Pane();
                    cell.setPrefSize(cellAncho, cellAlt);
                    if (i == 0) {
                        cell.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-background-color: red;");
                    }
                    else{
                        cell.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-background-color: blue;");
                    }

                    // Calcular la posición de la celda
                    double x = j * (cellAncho + padding1);
                    double y = i * (cellAlt + padding2);
                    
                    matriz[i][j] = cell;
                    matrizPane.getChildren().add(cell);
                    cell.setLayoutX(x);
                    cell.setLayoutY(y);
                    
                }
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
   
    public void imprimirMatrizEnteros() {
        for (int i = 0; i < matrizEnteros.length; i++) {
            for (int j = 0; j < matrizEnteros[i].length; j++) {
                System.out.print(matrizEnteros[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("---------");
    }

    public Pane[][] getMatriz() {
        return matriz;
    }
}