package anastasoft.rallyvision.controller;

import com.google.android.vending.licensing.LicenseCheckerCallback;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.activity.MenuPrincipal;

public class MylicenseChekerCallBack implements LicenseCheckerCallback {
    private final Controller controller;

    public MylicenseChekerCallBack(Controller controller) {
        this.controller = controller;
    }


        public void allow(int reason) {
            if (((MenuPrincipal) controller.getCurrentActiviy()).isFinishing()) {
                // Don't setState UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            ((MenuPrincipal) controller.getCurrentActiviy()).displayResult(controller.getString(R.string.allow));
        }

        public void dontAllow(int reason) {
            if (((MenuPrincipal) controller.getCurrentActiviy()).isFinishing()) {
                // Don't setState UI if Activity is finishing.
                return;
            }
            ((MenuPrincipal) controller.getCurrentActiviy()).displayResult(controller.getString(R.string.dont_allow));
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
            // ((MenuPrincipal)getCurrentActiviy()).displayDialog(reason == Policy.RETRY);
        }

        public void applicationError(int errorCode) {
            if (((MenuPrincipal) controller.getCurrentActiviy()).isFinishing()) {
                // Don't setState UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = String.format(controller.getString(R.string.application_error), errorCode);
            ((MenuPrincipal) controller.getCurrentActiviy()).displayResult(result);
        }
    }
