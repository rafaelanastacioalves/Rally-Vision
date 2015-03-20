package anastasoft.rallyvision.Slider;

import android.database.Cursor;

import java.util.ArrayList;

import anastasoft.rallyvision.Slider.Prova_Trecho.Prova;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class SliderCore {

    private MotoristaUsuario    aMotoristaUsuario;
    private MotoristaIdeal      aMotoristaIdeal;
    private Prova               aProva;

    // para alguns calculos rotineiros
    private float                   dSTrechoRestante;
    private float                   dSFinal;
    private float                   dTRemanescente;
    private float                   dTfinal;
    private float                   percentual;
    private  ArrayList<Motorista>    listaMotorista = new ArrayList<Motorista>(2);


    public void setProva(Prova aProva){
        this.aProva = aProva;
        for( Motorista aMotorista : listaMotorista){
            aMotorista.setTrecho(aProva.getTrecho(1));
        };
    }

    public MotoristaIdeal getStatusMotoristaIdeal(){
        return aMotoristaIdeal;
    }

    public MotoristaUsuario getStatusMotoristaUsuario(){
        return aMotoristaUsuario;
    }

    public void setPercentPercorrido(){


    }

    public void reset(){

    }

    public SliderCore (){

        aMotoristaIdeal = new MotoristaIdeal();
        aMotoristaUsuario = new MotoristaUsuario();

        // deixando logo para sempre nesse array para não ter que ficar atribuindo eles na hora de exportar os dois objetos...
        listaMotorista.add(aMotoristaUsuario);
        listaMotorista.add(aMotoristaIdeal);

    }

    public ArrayList<Motorista> getStatus(){
        return listaMotorista;
    }

    public void carregarProva(Cursor c){
        setProva(new Prova(c));
    }

    //
    public void update(float dT, float dS){

        //atualizando status motorista usuario

        updateMotoristaUsuario(dS);

        //atualizando status motorista ideal

        updateMotoristaIdeal(dT);






    }

    /**
     * Atualiza o STATE do motorista ideal.
     * Supõe-se que as mudanças sempre sejam positivas, para a frente.
     */
    private void updateStateMotIdeal() {
            /*
            * se os dois motoristas estavam ok, e o mot. ideal mudou de estado, é obvio que este passa
            * a estar adiantado...
            * */

            if(aMotoristaIdeal.getState() == Motorista.STATE_OK){
                aMotoristaIdeal.setState(Motorista.STATE_ADIANTADO);
                aMotoristaUsuario.setState(Motorista.STATE_ATRASADO);
            }
            /*
            * caso contrario, se o mot. ideal está atrasado e os trechos coincidem, é obvio que ele
            * agora está ok com o outro motorista
             */
            else if(aMotoristaIdeal.getState() == Motorista.STATE_ATRASADO && aMotoristaIdeal.getNumTrecho() == aMotoristaUsuario.getNumTrecho()){

                    aMotoristaIdeal.setState(Motorista.STATE_OK);
                    aMotoristaUsuario.setState(Motorista.STATE_OK);
                }
            /*
            *Caso contrario, o estado continua do jeito que está: ou o mot. ideal está adiantado ou está atrasado
            * Nada se faz
             */

    }

    /**
     * Atualiza o STATE do motorista ideal.
     * Supõe-se que as mudanças sempre sejam positivas, para a frente.
     */
    private void updateStateMotUsuario() {
            /*
            * se os dois motoristas estavam ok, e o mot. usuario mudou de estado, é obvio que este passa
            * a estar adiantado...
            * */

        if(aMotoristaUsuario.getState() == Motorista.STATE_OK){
            aMotoristaUsuario.setState(Motorista.STATE_ADIANTADO);
            aMotoristaIdeal.setState(Motorista.STATE_ATRASADO);
        }
            /*
            * caso contrario, se o mot. usuario está atrasado e os trechos coincidem, é obvio que ele
            * agora está ok com o outro motorista
             */
        else if(aMotoristaUsuario.getState() == Motorista.STATE_ATRASADO && aMotoristaUsuario.getNumTrecho() == aMotoristaIdeal.getNumTrecho()){

            aMotoristaUsuario.setState(Motorista.STATE_OK);
            aMotoristaIdeal.setState(Motorista.STATE_OK);
        }
            /*
            *Caso contrario, o estado continua do jeito que está: ou o mot. usuario está adiantado ou está atrasado
            * Nada se faz
             */

    }

    private void updateMotoristaIdeal(float dT) {
        dTRemanescente = aMotoristaIdeal.getDTRemanescente();

        while (dT >= dTRemanescente){
            dT = dT - dTRemanescente;
            mudaTrecho(aMotoristaIdeal);
            updateStateMotIdeal();
            dTRemanescente = aMotoristaIdeal.getDTRemanescente();
        }


        aMotoristaIdeal.incrementaDTacumuladoTrecho(dT);

        percentual = aMotoristaIdeal.getDTacumuladoTrecho()/aMotoristaIdeal.getDTtotalTrecho();
        aMotoristaIdeal.setPercentualPercorrido(percentual);
    }

    private void updateMotoristaUsuario(float dS) {
        dSTrechoRestante = aMotoristaUsuario.getDSTrechoRestante();

        while (dSTrechoRestante <= dS){
            dS = dS - dSTrechoRestante;
            mudaTrecho(aMotoristaUsuario);
            updateStateMotUsuario();
            dSTrechoRestante =aMotoristaUsuario.getDSTrechoRestante();
        }


        aMotoristaUsuario.atualizaDS(dS);
    }

    private void mudaTrecho(Motorista aMotorista) {
        aMotorista.setTrecho(
                aProva.getTrecho(
                        aMotorista.getNumTrecho() + 1)
        );

    }


}
