import javafx.scene.layout.Pane;

public class Protoboard{
    //Declara matriz como atributo de la clase
    private Pane[][] matriz;
    private int [][] matrizEnteros;
    private int [][] matrizCortoCircuito;
    private int energiaRoja=0,energiaAzul=0;
    private int filaRoja=-1,filaAzul=-1;
    private int columnaRoja=-1,columnaAzul=-1;
    private int[][] matrizCables;
    
    //metodo para iniciar la matriz central de panes
    public void inicializarMatrizCentral(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane) {
        matriz = new Pane[filas][columnas];
        matrizEnteros = new int[filas][columnas];
        matrizCables = new int[filas][columnas];
        matrizCortoCircuito = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizCables[i][j] = 0;
                matrizEnteros[i][j] = 0; // inicializar matriz con 0
                matrizCortoCircuito[i][j] = 0; 
                Pane cell = new Pane();
                cell.setPrefSize(cellAncho, cellAlt);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");
    
                // Calcular la posición de la celda
                double x = j * (cellAncho + padding1);
                double y = i * (cellAlt + padding2);

                if (i > 4) {
                    y += 75; // Añadir espacio adicional
                }
                cell.setLayoutX(x);
                cell.setLayoutY(y);

                matrizPane.getChildren().add(cell);
                matriz[i][j] = cell;
            }
        }
    
        // Ajustar el tamaño del pane matrizPane
        double ancho = columnas * (cellAncho + padding1) - padding1; // Ajustar el ancho total
        double alto = filas * (cellAlt + padding2) - padding2; // Ajustar el alto total
        if (filas > 4) {
            alto += 75;
        }
        matrizPane.setPrefSize(ancho, alto);
    }

    //Método para manejar el click en la matriz central
    public void manejarClickMatrizSupInf(int fila, int columna, int energia){
        for (int col = 0; col < 30; col++) {
            if(energia == 1){
                filaRoja = fila;
                
                if (Bateria.banderaBateria == true){
                    matrizEnteros[filaRoja][col] = 1; 
                    matriz[filaRoja][col].setStyle("-fx-background-color: red ;");
                }
                energiaRoja = 1;
            }
            if(energia == -1){
                filaAzul = fila;
                
                if(Bateria.banderaBateria == true){
                    matrizEnteros[filaAzul][col] = -1;
                    matriz[filaAzul][col].setStyle("-fx-background-color: blue ;");
                }
                energiaAzul = -1;
            }
        }
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

    public void actualizarEstadoLuz(boolean banderaBateria) {
        if (banderaBateria) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (energiaRoja == 1 && i == filaRoja) {
                        matriz[filaRoja][j].setStyle("-fx-background-color: red;");
                        matrizEnteros[filaRoja][j] = 1;
                    } else if (energiaAzul == -1 && i == filaAzul) {
                        matriz[filaAzul][j].setStyle("-fx-background-color: blue;");
                        matrizEnteros[filaAzul][j] = -1;
                    } else if (matrizEnteros[i][j] == 0) {
                        matriz[i][j].setStyle("-fx-background-color: black;");
                    }
                }
            }
        } else {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    matrizEnteros[i][j] = 0;
                    matriz[i][j].setStyle("-fx-background-color: black;");
                }
            }
        }
        //imprimirMatrizEnteros();
    }
    //Método para manejar el click en la matriz central
    public void manejarClickMatrizCentral(int fila, int columna, int energia){
        if (fila >= 0 && fila <5){
            for(int fil = 0; fil < 5; fil++){
                if(energia == 1){
                    columnaRoja = columna;
                    filaRoja = fila;
                    if (Bateria.banderaBateria == true){
                        matrizEnteros[fil][columnaRoja] = 1; 
                        matriz[fil][columnaRoja].setStyle("-fx-background-color: red ;");
                    }
                    energiaRoja = 1;
                }
                if(energia == -1){
                    columnaAzul = columna;
                    filaAzul = fila;
                    if(Bateria.banderaBateria == true){
                        matrizEnteros[fil][columnaAzul] = -1;
                        matriz[fil][columnaAzul].setStyle("-fx-background-color: blue ;");
                    }
                    energiaAzul = -1;
                }

            }
        }
        if (fila >= 5 && fila <10){
            for(int fil = 5; fil < 10; fil++){
                if(energia == 1){
                    columnaRoja = columna;
                    filaRoja = fila;
                    if (Bateria.banderaBateria == true){
                        matrizEnteros[fil][columnaRoja] = 1; 
                        matriz[fil][columnaRoja].setStyle("-fx-background-color: red ;");
                    }
                    energiaRoja = 1;
                }
                if(energia == -1){
                    columnaAzul = columna;
                    filaAzul = fila;
                    if(Bateria.banderaBateria == true){
                        matrizEnteros[fil][columnaAzul] = -1;
                        matriz[fil][columnaAzul].setStyle("-fx-background-color: blue ;");
                    }
                    energiaAzul = -1;
                }
            }
        }
    }
    
    public void configurarManejadoresDeEventosCentral(int energia){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++){
                final int fila = i;
                final int columna = j;
                matriz[i][j].setOnMouseClicked(event -> manejarClickMatrizCentral(fila, columna, energia));
            }
        }
    }

    public void actualizarEstadoLuzCentral(boolean banderaBateria) {
        if (banderaBateria) {
            if (filaRoja >= 0 && filaRoja < 5) {
                for (int i = 0; i < 5; i++) {
                    actualizarCelda(i, columnaRoja, energiaRoja);
                }
            }
            if (filaRoja >= 5 && filaRoja < 10) {
                for (int i = 5; i < 10; i++) {
                    actualizarCelda(i, columnaRoja, energiaRoja);
                }
            }
            if (filaAzul >= 0 && filaAzul < 5) {
                for (int i = 0; i < 5; i++) {
                    actualizarCelda(i, columnaAzul, energiaAzul);
                }
            }
            if (filaAzul >= 5 && filaAzul < 10) {
                for (int i = 5; i < 10; i++) {
                    actualizarCelda(i, columnaAzul, energiaAzul);
                }
            }
        } else {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    matrizEnteros[i][j] = 0;
                    matriz[i][j].setStyle("-fx-background-color: black;");
                }
            }
        }
    }
    
    private void actualizarCelda(int fila, int columna, int energia) {
        if (energia == 1) {
            matriz[fila][columna].setStyle("-fx-background-color: red;");
            matrizEnteros[fila][columna] = 1;
        } else if (energia == -1) {
            matriz[fila][columna].setStyle("-fx-background-color: blue;");
            matrizEnteros[fila][columna] = -1;
        }
    }

    //Método para inicializar la matriz de la parte superior e inferior
    public void inicializarMatrizSupInf(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane){
        matriz = new Pane[filas][columnas];
        matrizEnteros = new int[filas][columnas];
        matrizCortoCircuito = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++){
                matrizEnteros[i][j] = 0; //INICIALIZAMOS LA MATRIZ DE ENTEROS SOLO CON 0
                matrizCortoCircuito[i][j] = 0; //INICIALIZAMOS LA MATRIZ DE CORTO CIRCUITO SOLO CON 0
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
    
    public boolean comprobarCuadrado(int filas, int columnas, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane, double startX, double startY) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Calcular las coordenadas del cuadrado
                double squareX = j * (cellAncho + padding1);
                double squareY = i * (cellAlt + padding2);
                
                if (i > 4) {
                    squareY += 75; // Añadir espacio adicional
                }
    
                double largoX = squareX + cellAncho;
                double largoY = squareY + cellAlt;
    
                // Verificar si el clic está dentro del cuadrado
                if (startX >= squareX && startX <= largoX && startY >= squareY && startY <= largoY) {
                    return true;
                }
            }
        }
        return false;
    }
   
    public Pane[][] getMatriz() {
        return matriz;
    }

    public int[][] getMatrizEnteros() {
        return matrizEnteros;
    }

    public int[][] getMatrizCables() {
        return matrizCables;
    }

    public int[][] getMatrizCortoCircuito() {
        return matrizCortoCircuito;
    }

    public void setMatrizCortoCircuito(int fila ,int columna, int valor){ 
        if (fila >= 0 && fila <5){
            for(int fil = 0; fil < 5; fil++){
                this.matrizCortoCircuito[fil][columna] = valor;
            }
        }
        if (fila >= 5 && fila <10){
            for(int fil = 5; fil < 10; fil++){
                this.matrizCortoCircuito[fil][columna] = valor;
            }
        }
    }
    public void setMatrizCortoCircuitoSupInf(int fila ,int columna, int valor){ 
        if (fila == 0){
            for(int col = 0; col < 30; col++){
                this.matrizCortoCircuito[0][col] = valor;
            }
        }
        else if (fila == 1){
            for(int col = 0; col < 30; col++){
                this.matrizCortoCircuito[1][col] = valor;
            }
        }
    }
    
    public void setMatrizCables(int fila ,int columna, int valor){ 
        this.matrizCables[fila][columna] = valor;
    }
    public void setMatrizEnteros(int fila ,int columna, int valor){ 
        this.matrizEnteros[fila][columna] = valor;
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

