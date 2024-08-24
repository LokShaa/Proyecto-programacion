import javafx.scene.layout.Pane;

 
public class Protoboard {
    public void inicializarMatriz(int rows, int cols, double cellAncho, double cellAlt, double padding1, double padding2, Pane matrizPane, Pane[][] matrix){
    matrix = new Pane[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            Pane cell = new Pane();
            cell.setPrefSize(cellAncho, cellAlt);
            cell.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");//Establece el color de fondo a negro
            // Calcular la posición de la celda
            double x = j * (cellAncho + padding1);
            double y = i * (cellAlt + padding2);
            cell.setLayoutX(x);
            cell.setLayoutY(y);
            matrizPane.getChildren().add(cell);
            matrix[i][j] = cell;
            // Añadir evento a cada celda
            cell.setOnMouseClicked(event -> {
                //AQUI IRAN LOS METODOS PARA HACER LAS INTERACCIONES DE LA MATRIZ
                
            });
        }
    }
     // Ajustar el tamaño del pane matrizPane
     matrizPane.setPrefSize(cols * (cellAncho + padding1), rows * (cellAlt + padding2));
    }

}

