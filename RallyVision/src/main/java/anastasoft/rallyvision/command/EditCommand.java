package anastasoft.rallyvision.command;

import android.content.Context;

public class EditCommand extends Command {
    private int odomValue;

    public EditCommand(Context context, int oDomValue) {
        super(context);
        this.odomValue = oDomValue;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.setOdometer(odomValue);

    }

}
