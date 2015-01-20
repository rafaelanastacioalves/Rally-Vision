package anastasoft.rallyvision.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Controller;

public class NotConfigureDialog extends android.support.v4.app.DialogFragment {

    View OKButton;
    private Controller aController;
    private Resources res;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        aController = (Controller) (getActivity()).getApplicationContext();
        res = getResources();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder

        alertDialogBuilder.setMessage(res
                .getString(R.string.dialog_not_configured_message));

        alertDialogBuilder.setTitle(res
                .getString(R.string.dialog_not_configured_title));

        // forcing showing soft input
        AlertDialog aDialog = alertDialogBuilder.create();
        return aDialog;

    }

}
