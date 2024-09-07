import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;

public class Protoboard{
    //Declara matriz como atributo de la clase
    private Pane[][] matriz;
    private int [][] matrizEnteros;
    private Integer valorTemporal = null;

    private Protoboard matrizOrigen = null; // Almacenar la referencia de la matriz origen
    
    //metodo para iniciar la matriz central de panes
    public void inicializarMatrizCentral(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane) {
        matriz = new Pane[filas][columnas];
        matrizEnteros = new int[filas][columnas];

        for (int i = 0; i < filas; i++){

            for (int j = 0; j < columnas; j++){
                matrizEnteros[i][j] = 0; //inicializar matriz con 0
                Pane cell = new Pane();
                cell.setPrefSize(cellAncho, cellAlt);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");

                //Calcular la posición de la celda
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

    public int obtenerValorMatrizEnteros(MouseEvent event) {
        Pane celdaClickeada = (Pane) event.getSource();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == celdaClickeada) {
                    return matrizEnteros[i][j];
                }
            }
        }
        return -1;
    }

    /*public void manejadorDeClick(Protoboard matrizActual) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                final int fila = i;
                final int columna = j;
                matriz[i][j].setOnMouseClicked(event -> {
                    if (valorTemporal == null) {
                        // Primer clic: almacenar el valor y la referencia de la matriz origen
                        valorTemporal = obtenerValorMatrizEnteros(event);
                        matrizOrigen = matrizActual; // Guardamos la referencia de la matriz donde ocurrió el primer clic
                        System.out.println("Valor temporal almacenado: " + valorTemporal + " desde la matriz origen.");
                    } else {
                        // Segundo clic: aplicar el valor en la celda de la matriz actual
                        if (matrizOrigen != null) {
                            manejarClickMatrizCentral(fila, columna, valorTemporal);
                            matriz[fila][columna].setStyle("-fx-background-color: yellow;");
                            System.out.println("Valor aplicado en [" + fila + "][" + columna + "] desde la matriz origen.");
                            
                            // Resetear el valor temporal y la matriz origen
                            valorTemporal = null;
                            matrizOrigen = null;
                            
                        }
                    }
                });
            }
        }
    }*/
    
    //METODO QUE SE USA SOLO PARA LA MATRIZ CENTRAL
   /*  public void manejarClickMatrizCentral(int fila, int columna,int energia){
        if (fila >=0 && fila < 5){
            //Cambiar la columna completa (1-5) a 1 y a amarillo
            for (int i = 0; i < 5; i++) {
                matrizEnteros[i][columna] = energia;
                matriz[i][columna].setStyle("-fx-background-color: yellow;");
            }

        } 
        if(fila>= 5  && fila<=10){
            //Cambiar la columna completa (6-10) a 1 y a amarillo
            for (int i = 5; i < 10; i++) {
                matrizEnteros[i][columna] = energia;
                matriz[i][columna].setStyle("-fx-background-color: yellow;");
            }
        }
       
    }*/

    public void manejarClickMatrizSupInf(int fila, int columna, int energia){
        for (int col = 0; col < 30; col++) {
            if(energia == 1 ){
                matrizEnteros[fila][col] = 1; 
                matriz[fila][col].setStyle("-fx-background-color: red ;");
            }
            if(energia == -1 ){
                matrizEnteros[fila][col] = -1;
                matriz[fila][col].setStyle("-fx-background-color: blue ;");
            }
        }
        imprimirMatrizEnteros();
    }

    //Método para configurar los eventos del click en la matriz central para no llamarlo de el metodo de inicializarMatrizCentral
    public void configurarManejadoresDeEventosSupInf(int energia){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++){
                final int fila = i;
                final int columna = j;
                matriz[i][j].setOnMouseClicked(event -> manejarClickMatrizSupInf(fila, columna, energia));
            }
        }
    }
    
    public void inicializarMatrizSupInf(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        matrizEnteros = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++){
                matrizEnteros[i][j] = 1; //INICIALIZAMOS LA MATRIZ DE ENTEROS SOLO CON 0
                Pane cell = new Pane();
                cell.setPrefSize(cellAncho, cellAlt);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");

                //Calcular la posición de la celda
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

                    /*final int fila = i; // Necesario para usar dentro del lambda
                    cell.setOnMouseClicked(event -> {
                        // Cambiar el color de toda la columna
                        for (int col = 0; col < columnas; col++) {
                            matriz[fila][col].setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: yellow;"); // Cambiar color a azul
                        }
                    });*/
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
   
    public void imprimirMatrizEnteros(){
        for (int i = 0; i < matrizEnteros.length; i++){
            if(i == 5){
                System.out.println("------------------------------------------------------");
            }
            for (int j = 0; j < matrizEnteros[i].length; j++) {
                System.out.print(matrizEnteros[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------");
    }
    
    public Pane[][] getMatriz(){
        return matriz;
    }
   
    //Método para obtener la matriz de enteros
    public int[][] getMatrizEnteros() {
        return matrizEnteros;
    }

    public void desactivarEventosDeDibujo(){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setOnMouseClicked(null);
                matriz[i][j].setOnMouseEntered(null);
                matriz[i][j].setOnMouseExited(null);
            }
        }
    }
}
