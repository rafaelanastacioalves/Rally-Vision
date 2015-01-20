package anastasoft.rallyvision.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import anastasoft.rallyvision.controller.Controller;

/**
 * Created by rafaelanastacioalves on 09/01/15.
 */
public class ConnectMediator {

    // application states
    private static final int APLICATION_DISCONECTED = 0;
    private static final int APLICATION_CONNECTING = 1;
    private static final int APLICATION_CONNECTED =2;

    private Controller aController;

    public ConnectMediator(Controller aController){
        this.aController = aController;
    }

    public void getState(){

    }

    public void update(Controller aController){
        int appState;
        Activity aActivity = aController.getCurrentActiviy();
        if(aActivity.getClass() == MenuPrincipal.class) {


            appState = aController.getAppState();
            switch (appState) {
                case APLICATION_DISCONECTED:
                    ((MenuPrincipal)aActivity).makeConnectBRed();
                    break;
                case APLICATION_CONNECTING:
                    break;
                case APLICATION_CONNECTED:
                    ((MenuPrincipal)aActivity).makeConnectBGreen();
                    Vibrator vb = (Vibrator) aController.getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(1000);

                    break;
            }
        }

    }
}
