package anastasoft.rallyvision.Slider.Prova_Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 21/01/15.
 */
public class TrechoV extends Trecho {

    private float vMedio; // em metros/milisegundos

    public TrechoV(int numero, float ki, float kf, float vMedio, float ti ){

        this.numero = numero;
        this.ki = ki;
        this.kf = kf;
        this.ti = ti;
        this.vMedio = vMedio;
    }


    public float getvMedio() {
        return vMedio;
    }

    public float getdTTrecho() {
        return getDeltaStrecho()/vMedio;
    }
}
