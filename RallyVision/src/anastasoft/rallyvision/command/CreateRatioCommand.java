package anastasoft.rallyvision.command;

import android.app.Application;
import android.widget.Toast;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class CreateRatioCommand extends Command {

    private final String aFericaoNome;
    private float aRatio;

    public CreateRatioCommand(Application controller, float aRatio, String aFericaoName) {
        super(controller);
        // TODO Auto-generated constructor stub
        this.aRatio = aRatio;
        this.aFericaoNome = aFericaoName;
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        Afericao afericaoTemp = new Afericao(aFericaoNome,aRatio);
        if(aController.jaExiste(afericaoTemp)){
            Toast.makeText(aController.getApplicationContext(), aController.getResources()
            .getString(R.string.invalido_jah_existe) + afericaoTemp.getName(), Toast.LENGTH_SHORT).show();
        }
        else {
            aController.setRatio(aRatio, aFericaoNome);

        }
    }

}
