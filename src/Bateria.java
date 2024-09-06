import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Bateria{
    private int voltaje = 5; //Atributo para saber el voltaje de la bateria
    public static boolean banderaBateria = false;

    @FXML
    public void botonConectadoDesconectado(ImageView luzRoja,ImageView luzVerde, ImageView bateriaCortada,ImageView bateriaCompleta,ImageView portaBaterias) { //Metodo para hacer aparecer y desaparecer la bateria completa y la bateria cortada, ademas las luces roja y verde
        luzRoja.setVisible(!luzRoja.isVisible()); //Se hace invisible la luz roja
        luzVerde.setVisible(luzVerde.isVisible()); //Se hace visible la luz verde
        portaBaterias.setVisible(!portaBaterias.isVisible()); //Se hace invisible el porta baterias
        bateriaCompleta.setVisible(!bateriaCompleta.isVisible()); //Se hace invisible la bateria completa
        bateriaCortada.setVisible(bateriaCortada.isVisible()); //Se hace visible la bateria cortada
        if (banderaBateria == false){
            banderaBateria = true;
        }
        else{
            banderaBateria = false;
        }
        System.out.println("Bateria: " + banderaBateria);
    }
}