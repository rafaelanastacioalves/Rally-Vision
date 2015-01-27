package anastasoft.rallyvision.command;

import android.app.Application;
import android.net.Uri;

import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.SliderCoreographer.SliderChoreographer;

/**
 * Created by rafaelanastacioalves on 23/01/15.
 */
public class CarregarArquivoCommand extends Command {

    private Uri aUri;
    private SliderChoreographer aSliderChoreographer;

    public CarregarArquivoCommand(Application aController, Uri aUri){
        super(aController);
        this.aSliderChoreographer =((Controller) aController).getChoreographer();
        this.aUri = aUri;
    }


    @Override
    public void Execute() {
        aSliderChoreographer.carregarArquivoProva(aUri);

    }
}
