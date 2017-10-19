package anastasoft.rallyvision.command;

import android.app.Activity;
import android.content.Context;

public class startCommand extends Command {

    int aOdomValue;
    private Activity currActivity;

    public startCommand(Context context, int oDometerValue) {
        super(context);
        currActivity = (Activity) context;
        this.aOdomValue = oDometerValue;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.startExecution(currActivity);

    }

}
