package anastasoft.rallyvision.command;

import android.net.Uri;

import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.SliderCoreographer.SliderChoreographer;

/**
 * Created by rafaelanastacioalves on 23/01/15.
 */
public class CarregarArquivoCommand extends Command {

    private Uri aUri;
    private SliderChoreographer aSliderChoreographer;

    public CarregarArquivoCommand(Controller aController, Uri aUri){
        super(aController);
        this.aSliderChoreographer = aController.getChoreographer();
        this.aUri = aUri;
    }


    @Override
    public void Execute() {
        aSliderChoreographer.carregarArquivoProva(aUri);

    }
}
