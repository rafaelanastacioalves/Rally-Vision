package anastasoft.rallyvision.Slider.motorista;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public abstract class Motorista {
    protected   Trecho  trechoAtual;
//    protected float   deltaSTrechoPercorrido;
      protected float   percentualPercorrido;
//    protected float   deltaSTrechoTotal;
//    protected float   deltaTacumuladoTrecho;

    public Motorista(){
        percentualPercorrido = 0;
    }

    public int getNumTrecho(){
        return trechoAtual.getNumTrecho();
    }

    protected abstract void zeraTrocandoTrecho();

    public String getTipoTrechoAtual(){
        return  trechoAtual.getTipo();
    }
    /***
     * @param
     * @return float >= 0 ou <=1
     */

    public float getPercentPercorrido(){
        return percentualPercorrido;
    }

    /**
     * @param percent 0<= percent <=1
     */
    public void setPercentualPercorrido(float percent){
        percentualPercorrido = percent;
    }

    public abstract void setTrecho(Trecho trecho);

}


