package anastasoft.rallyvision.command;

import anastasoft.rallyvision.Slider.MotoristaUsuario;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;
import anastasoft.rallyvision.Slider.SliderCore;
import anastasoft.rallyvision.controller.CarStatus;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Observable;
import anastasoft.rallyvision.controller.Relogio;
import anastasoft.rallyvision.controller.SliderCoreographer.SliderChoreographer;

/**
 * Created by rafaelanastacioalves on 02/04/15.
 */
public class EditaPosicaoSliderCommand extends Command {

    private SliderChoreographer aSliderChoreographer;
    public EditaPosicaoSliderCommand(Controller aController, Trecho aTrecho, float dSlocal) {
        super(aController);
        aSliderChoreographer = aController.getSliderChoreographer();

        SliderCore aSliderCore =  aSliderChoreographer.getSliderCore();

        ////////// Atualizando o hodometro
        MotoristaUsuario aMotoristaUsuario=  aSliderCore.getStatusMotoristaUsuario();
        float dSMotoristaAntigo = aSliderCore.getdSUntil(aMotoristaUsuario.getTrecho(), aMotoristaUsuario.getdSpercorrido());
        float dSMotoristaNovo   = aSliderCore.getdSUntil(aTrecho,dSlocal);
        float hodomedtroDs = dSMotoristaNovo - dSMotoristaAntigo;
        CarStatus aCar =  aSliderChoreographer.getCarStatus();
        float deltaStotNovo = aCar.getDeltaStot() + hodomedtroDs;
        if(deltaStotNovo < 0){
            deltaStotNovo = 0;
        }
        aCar.setDeltaStot(deltaStotNovo);



        //////////// Atualizando o Slider
        aSliderCore.zerarContagem();
        Relogio aRelogio = aSliderChoreographer.getaRelogio();
        aRelogio.reSetarSlider();
        aSliderCore.update(0,dSMotoristaNovo);

        Observable aObservable =  aSliderChoreographer.getOBservable();
        aObservable.Notify();

    }
}
