package anastasoft.rallyvision.Slider;

import android.database.Cursor;

import java.util.ArrayList;

import anastasoft.rallyvision.Slider.Prova_Trecho.FimDeProvaException;
import anastasoft.rallyvision.Slider.Prova_Trecho.Prova;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;

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
    private ArrayList<Trecho> prova;


    public void setProva(Prova aProva){
        this.aProva = aProva;
        for( Motorista aMotorista : listaMotorista){
            try {
                aMotorista.setTrecho(aProva.getTrecho(1));
            } catch (FimDeProvaException e) {
                e.printStackTrace();
            }
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

    /**
     * Reseta o status dos motoristas. Mantém a Prova.
     * Atualmente utilizado quando queremos setar uma nova posição para o motorista usuário.
     * Requer reconfiguração externa do dS e dT desde o começo...
     */
    public void zerarContagem(){
        aMotoristaIdeal = new MotoristaIdeal();
        aMotoristaUsuario = new MotoristaUsuario();


        listaMotorista.clear();
        listaMotorista.add(aMotoristaUsuario);
        listaMotorista.add(aMotoristaIdeal);

        setProva(aProva);
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

        if(aMotoristaUsuario.getAbsoluteSate() == Motorista.ABSOLUTE_STATE_RUNNING) {
            updateMotoristaUsuario(dS);
        }

        //atualizando status motorista ideal

        if (aMotoristaIdeal.getAbsoluteSate() == Motorista.ABSOLUTE_STATE_RUNNING) {
            updateMotoristaIdeal(dT);
        }






    }

    public float getdSUntil(Trecho aTrecho, float dSLocal) {

        float dS = 0;

        for(Trecho aT : aProva.toList()){
            if(aT.equals(aTrecho)){
                dS = dS + dSLocal;
                break;
            }
            dS = dS + aT.getDeltaStrecho();
        }

        return dS;


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

        if(aMotoristaIdeal.getRelativeState() == Motorista.RELATIVE_STATE_OK){
            aMotoristaIdeal.setRelativeState(Motorista.RELATIVE_STATE_ADIANTADO);
            aMotoristaUsuario.setRelativeState(Motorista.RELATIVE_STATE_ATRASADO);
        }
            /*
            * caso contrario, se o mot. ideal está atrasado e os trechos coincidem, é obvio que ele
            * agora está ok com o outro motorista
             */
        else if(aMotoristaIdeal.getRelativeState() == Motorista.RELATIVE_STATE_ATRASADO && aMotoristaIdeal.getNumTrecho() == aMotoristaUsuario.getNumTrecho()){

            aMotoristaIdeal.setRelativeState(Motorista.RELATIVE_STATE_OK);
            aMotoristaUsuario.setRelativeState(Motorista.RELATIVE_STATE_OK);
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

        if(aMotoristaUsuario.getRelativeState() == Motorista.RELATIVE_STATE_OK){
            aMotoristaUsuario.setRelativeState(Motorista.RELATIVE_STATE_ADIANTADO);
            aMotoristaIdeal.setRelativeState(Motorista.RELATIVE_STATE_ATRASADO);
        }
            /*
            * caso contrario, se o mot. usuario está atrasado e os trechos coincidem, é obvio que ele
            * agora está ok com o outro motorista
             */
        else if(aMotoristaUsuario.getRelativeState() == Motorista.RELATIVE_STATE_ATRASADO && aMotoristaUsuario.getNumTrecho() == aMotoristaIdeal.getNumTrecho()){

            aMotoristaIdeal.setRelativeState(Motorista.RELATIVE_STATE_OK);
            aMotoristaUsuario.setRelativeState(Motorista.RELATIVE_STATE_OK);
        }
            /*
            *Caso contrario, o estado continua do jeito que está: ou o mot. usuario está adiantado ou está atrasado
            * Nada se faz
             */

    }


    private void updateMotoristaIdeal(float dT) {
        dTRemanescente = aMotoristaIdeal.getDTRemanescente();

        try {


            while (dT >= dTRemanescente) {
                dT = dT - dTRemanescente;
                mudaTrecho(aMotoristaIdeal);
                updateStateMotIdeal();
                dTRemanescente = aMotoristaIdeal.getDTRemanescente();
            }



        aMotoristaIdeal.incrementaDTacumuladoTrecho(dT);

        percentual = aMotoristaIdeal.getDTacumuladoTrecho()/aMotoristaIdeal.getDTtotalTrecho();
        } catch (FimDeProvaException e) {
            aMotoristaIdeal.setAbsoluteState(Motorista.ABSOLUTE_STATE_DONE);
            percentual = 1;
        }
        aMotoristaIdeal.setPercentualPercorrido(percentual);
    }

    private void updateMotoristaUsuario(float dS) {
        dSTrechoRestante = aMotoristaUsuario.getDSTrechoRestante();

        try{
        while (dS>=  dSTrechoRestante ) {
            dS = dS - dSTrechoRestante;
            mudaTrecho(aMotoristaUsuario);
            updateStateMotUsuario();
            dSTrechoRestante = aMotoristaUsuario.getDSTrechoRestante();
        }


        } catch (FimDeProvaException e) {
            aMotoristaUsuario.setAbsoluteState(Motorista.ABSOLUTE_STATE_DONE);
            dS = aMotoristaUsuario.getDSTrechoRestante();
        }
            aMotoristaUsuario.atualizaDS(dS);

    }

    private void mudaTrecho(Motorista aMotorista) throws FimDeProvaException {

            aMotorista.setTrecho(
                    aProva.getTrecho(
                            aMotorista.getNumTrecho() + 1)
            );


    }


    public Prova getProva() {
        return aProva;
    }



}
