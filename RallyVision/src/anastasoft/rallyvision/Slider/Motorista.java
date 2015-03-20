package anastasoft.rallyvision.Slider;

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

    private int STATE;

    public static final int STATE_ADIANTADO = 1;
    public static final int STATE_OK = 0;
    public static final int STATE_ATRASADO = -1;


    public Motorista(){
        percentualPercorrido = 0;
        this.STATE = STATE_OK;
    }

    public int getNumTrecho(){
        return trechoAtual.getNumTrecho();
    }

    protected abstract void zeraTrocandoTrecho();

    public String getTipoTrecho(){
        return  trechoAtual.getTipo() ;
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
    protected void setPercentualPercorrido(float percent){
        percentualPercorrido = percent;
    }

    protected abstract void setTrecho(Trecho trecho);

    /**
     * Retorna o Estado do motorista. Se for usuário, o estado dele em relação ao ideal.
     * Se o método for chamado para o motorista ideal, retorna o contrário do resultado do mot. usuário.
     * @return inteiro com o estado do motorista. @see STATE ...
     */
    public int getState(){
        return STATE;
    }

    protected void setState(int aState){
        this.STATE = aState;
    }

}


