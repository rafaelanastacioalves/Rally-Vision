package anastasoft.rallyvision.Slider.motorista;

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
    public void setTrecho(Trecho trecho) {
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
