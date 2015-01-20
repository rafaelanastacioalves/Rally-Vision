package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

public class stopCommunicationCommand extends Command {

    public stopCommunicationCommand(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public stopCommunicationCommand(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.stopCommunication();
    }

}
