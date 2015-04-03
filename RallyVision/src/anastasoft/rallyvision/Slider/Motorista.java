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

    private int RELATIVE_STATE;

    public static final int RELATIVE_STATE_ADIANTADO = 1;
    public static final int RELATIVE_STATE_OK = 0;
    public static final int RELATIVE_STATE_ATRASADO = -1;

    private int ABSOLUTE_STATE;

    public static final int ABSOLUTE_STATE_DONE = 1;
    public static final int ABSOLUTE_STATE_RUNNING = 0;


    public Motorista(){
        percentualPercorrido = 0;
        this.RELATIVE_STATE = RELATIVE_STATE_OK;
        this.ABSOLUTE_STATE = ABSOLUTE_STATE_RUNNING;
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
     * @return inteiro com o estado do motorista. @see RELATIVE_STATE ...
     */
    public int getRelativeState(){
        return RELATIVE_STATE;
    }

    protected void setRelativeState(int aState){
        this.RELATIVE_STATE = aState;
    }

    public int getAbsoluteSate() { return ABSOLUTE_STATE;}

    protected void setAbsoluteState(int aState) {
        this.ABSOLUTE_STATE = aState;
    }


}


