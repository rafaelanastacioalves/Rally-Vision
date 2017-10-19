package anastasoft.rallyvision.command;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import anastasoft.rallyvision.activity.MenuPrincipal;

public class ResetarApplicationCommand extends Command {

    private  int hora;
    private  int minuto;

    public ResetarApplicationCommand(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    public ResetarApplicationCommand(Context context) {
        super(context);
    }



    @Override
    public void Execute() {



        Intent mStartActivity = new Intent(aController, MenuPrincipal.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(aController, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)aController.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
