package anastasoft.rallyvision.Slider.Prova_Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class TrechoD extends Trecho {
    private float       dTTrecho; // em milisegundos


        public TrechoD(int numero, float ki, float kf,float dTTrecho, float ti ){

        this.numero = numero;
        this.ki = ki;
        this.kf = kf;
        this.ti = ti;
        this.dTTrecho = dTTrecho;
    }

    @Override
    public float getdTTrecho() {
        return dTTrecho;
    }
}


