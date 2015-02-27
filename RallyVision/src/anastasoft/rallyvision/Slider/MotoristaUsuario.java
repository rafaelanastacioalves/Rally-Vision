package anastasoft.rallyvision.Slider;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;

public class MotoristaUsuario extends Motorista {


    private float deltaSTrechoTotal;
    private float deltaSTrechoPercorrido;
    public MotoristaUsuario(){
        super();
        deltaSTrechoPercorrido =0;
    }


    protected void zeraTrocandoTrecho() {
        deltaSTrechoPercorrido = 0;
        percentualPercorrido = 0;
    }

    @Override
    protected void setTrecho(Trecho trecho) {
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


}
