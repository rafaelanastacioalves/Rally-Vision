package anastasoft.rallyvision.Slider.Prova_Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 21/01/15.
 */
public class TrechoN extends Trecho {

    private float       dTTrecho; // em milisegundos


    public TrechoN(int numero, float ki, float kf, float dTTrecho, float ti){

        this.numero = numero;
        this.ki = ki;
        this.kf = kf;
        this.ti = ti;
        this.dTTrecho = dTTrecho;
    }


    public float getdTTrecho() {
        return dTTrecho;
    }

    @Override
    public String getTipo() {
        return "N";
    }
}
