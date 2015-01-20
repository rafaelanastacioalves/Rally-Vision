package anastasoft.rallyvision.command;

import android.app.Application;

public class SetRatioCommand extends Command {

    private float aRatio;

    public SetRatioCommand(Application controller, float aRatio) {
        super(controller);
        // TODO Auto-generated constructor stub
        this.aRatio = aRatio;
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.setRatio(aRatio);
    }

}
