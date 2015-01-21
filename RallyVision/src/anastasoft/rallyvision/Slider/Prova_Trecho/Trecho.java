package anastasoft.rallyvision.Slider.Prova_Trecho;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class Trecho {

    private String      tipo;
    private int         numero;
    private float       ki;
    private float       kf;
    private float       deltaTProva;


    public float getDeltaStrecho(){
        return (kf - ki)*1000;
    }


    public int getNumTrecho(){
        return numero;
    }

    public String getTipo(){
        return tipo;
    }





}


