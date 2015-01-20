package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

public class Zerar extends Command {

    public Zerar(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    public Zerar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.zerar();
    }

}
