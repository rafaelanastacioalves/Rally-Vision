package anastasoft.rallyvision.command;

import android.app.Application;

public class SetRatioCommand extends Command {

    private final String aFericaoNome;
    private float aRatio;

    public SetRatioCommand(Application controller, float aRatio, String aFericaoName) {
        super(controller);
        // TODO Auto-generated constructor stub
        this.aRatio = aRatio;
        this.aFericaoNome = aFericaoName;
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.setRatio(aRatio, aFericaoNome);
    }

}
