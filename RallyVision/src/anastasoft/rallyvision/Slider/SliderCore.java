package anastasoft.rallyvision.Slider;

import android.database.Cursor;

import java.util.ArrayList;

import anastasoft.rallyvision.Slider.Prova_Trecho.Prova;
import anastasoft.rallyvision.Slider.motorista.Motorista;
import anastasoft.rallyvision.Slider.motorista.MotoristaIdeal;
import anastasoft.rallyvision.Slider.motorista.MotoristaUsuario;

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
    }

    public MotoristaIdeal getStatusMotoristaIdeal(){
        return aMotoristaIdeal;
    }

    public MotoristaUsuario getStatusMotoristaUsuario(){
        return aMotoristaUsuario;
    }

    public void setPercentPercorrido(){


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

        dSTrechoRestante = aMotoristaUsuario.getDSTrechoRestante();

        if(dSTrechoRestante <= dS){
            dSFinal = dS - dSTrechoRestante;
            mudaTrecho(aMotoristaUsuario);
        }
        else{
            dSFinal = dS;
        }

        aMotoristaUsuario.atualizaDS(dSFinal);

        //atualizando status motorista ideal

        dTRemanescente = aMotoristaIdeal.getDTRemanescente();

        if(dT >= dTRemanescente){
            dTfinal = dT - dTRemanescente;
            mudaTrecho(aMotoristaIdeal);
        }
        else {
            dTfinal = dT;
        }

        aMotoristaIdeal.incrementaDTacumuladoTrecho(dTfinal);

        percentual = aMotoristaIdeal.getDTacumuladoTrecho()/aMotoristaIdeal.getDTtotalTrecho();
        aMotoristaIdeal.setPercentualPercorrido(percentual);



    }

    private void mudaTrecho(Motorista aMotorista) {
        aMotorista.setTrecho(
                aProva.getTrecho(
                        aMotorista.getNumTrecho() + 1)
        );

    }


}
