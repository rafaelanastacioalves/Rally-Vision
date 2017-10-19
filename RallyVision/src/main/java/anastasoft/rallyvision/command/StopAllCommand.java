package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

/**
 * Created by rafaelanastacioalves on 26/01/15.
 */
public class StopAllCommand extends Command {

    public StopAllCommand(Application controller) {
        super(controller);
    }

    public StopAllCommand(Context menuPrincipal) {
        super(menuPrincipal);
    }

    @Override
    public void Execute(){
        aController.stopAll();
    }
}
