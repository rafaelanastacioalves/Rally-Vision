package anastasoft.rallyvision.Slider.Prova_Trecho;

import java.util.ArrayList;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class Prova {

    private ArrayList<Trecho> listaDeTrechos;

    public Trecho getTrecho(int numTrecho){
        return listaDeTrechos.get(numTrecho -1);
    }
}
