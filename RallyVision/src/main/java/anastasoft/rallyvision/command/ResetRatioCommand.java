package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

public class ResetRatioCommand extends Command {

    public ResetRatioCommand(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ResetRatioCommand(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.resetRatio();
    }

}
