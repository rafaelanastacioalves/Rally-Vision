package anastasoft.rallyvision.Slider.motorista;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class Motorista {
    private float   deltaSPercorrido;
    private float   percentualPercorrido;
    private float   deltaSTrechoTotal;
    private Trecho  trechoAtual;



    public int getNumTrecho(){
        return trechoAtual.getNumTrecho();
    }

    public String getTipo(){
        return trechoAtual.getTipo();
    }

    public  void setDeltaSPercorrido(){

    }

    public void setPercentualPercorrido(float percent){
        percentualPercorrido = percent;
    }
}


