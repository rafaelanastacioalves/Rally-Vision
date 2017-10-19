package anastasoft.rallyvision.Slider;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;

public class MotoristaUsuario extends Motorista {


    private float deltaSTrechoTotal;
    private float deltaSTrechoPercorrido;
    private float dSpercorrido;

    /**
     * usado em conjunto com a funcao estaEmNovoTrechoKiZero()
     */
    private boolean flag_novo_trecho_Ki_zero;


    public MotoristaUsuario(){
        super();
        deltaSTrechoPercorrido =0;
        flag_novo_trecho_Ki_zero = true;
    }


    protected void zeraTrocandoTrecho() {
        deltaSTrechoPercorrido = 0;
        percentualPercorrido = 0;
    }

    @Override
    protected void setTrecho(Trecho trecho) {

        // verificando se estah em um trecho com Ki igual a zero
        if(trecho.equals(trechoAtual)){
            flag_novo_trecho_Ki_zero = false;
        }else{
            if (trecho.getKi() == 0)
            flag_novo_trecho_Ki_zero = true;
        }




        trechoAtual = trecho;
        zeraTrocandoTrecho();

        deltaSTrechoTotal = trecho.getDeltaStrecho();
    }

    public float getDSTrechoRestante() {
            return deltaSTrechoTotal - deltaSTrechoPercorrido;
    }

    public void atualizaDS(float dSFinal) {

        deltaSTrechoPercorrido = deltaSTrechoPercorrido + dSFinal;
        setPercentualPercorrido(deltaSTrechoPercorrido/deltaSTrechoTotal);
        
    }


    public Trecho getTrecho() {
        return trechoAtual;
    }

    /**
     * Retorna true se estiver em novo trecho.
     * Note que só serve para uma consulta. Caso consulte novamente em seguida e retorna false.
     * Ele muda para true a cada mudança de trecho.
     */
    public boolean estaEmNovoTrechoKiZero(){
        if(flag_novo_trecho_Ki_zero == true) {
            flag_novo_trecho_Ki_zero = false;
            return true;
        }
        return flag_novo_trecho_Ki_zero;
    }

    public float getdSpercorrido() {
        return deltaSTrechoPercorrido;
    }
}
