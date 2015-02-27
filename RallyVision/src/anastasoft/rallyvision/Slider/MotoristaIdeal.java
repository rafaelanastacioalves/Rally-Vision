package anastasoft.rallyvision.Slider;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class MotoristaIdeal extends Motorista {
    private float deltaTacumuladoTrecho;
    private float deltaTTrechoTotal;

    public MotoristaIdeal(){
        super();
        deltaTacumuladoTrecho = 0;
    }

    @Override
    protected void zeraTrocandoTrecho() {
        percentualPercorrido = 0;
        deltaTacumuladoTrecho = 0;
    }

    @Override
    protected void setTrecho(Trecho trecho) {
        trechoAtual = trecho;
        zeraTrocandoTrecho();

        deltaTTrechoTotal = trechoAtual.getdTTrecho();
    }


    public float getDTRemanescente() {
        return deltaTTrechoTotal - deltaTacumuladoTrecho;
    }

    public void incrementaDTacumuladoTrecho(float dTfinal) {
        deltaTacumuladoTrecho += dTfinal;
    }

    public float getDTacumuladoTrecho() {
        return deltaTacumuladoTrecho;
    }


    public float getDTtotalTrecho() {
         return  trechoAtual.getdTTrecho();
    }


}
