package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

public class GeneralResetCommand extends Command {

    public GeneralResetCommand(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public GeneralResetCommand(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.resetGeral();
    }

}
