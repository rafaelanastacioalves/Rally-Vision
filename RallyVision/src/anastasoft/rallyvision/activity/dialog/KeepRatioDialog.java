package anastasoft.rallyvision.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import anastasoft.rallyvisionaluguel.R;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.ResetRatioCommand;
import anastasoft.rallyvision.controller.Controller;

public class KeepRatioDialog extends android.support.v4.app.DialogFragment {

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
                .getString(R.string.dialog_Keep_ratio_ask_text));

        alertDialogBuilder.setCancelable(true).setPositiveButton(
                res.getString(R.string.sim),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialogBuilder.setNegativeButton(res.getString(R.string.nao),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Command cmd = new ResetRatioCommand(aController);
                        cmd.Execute();
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setTitle(res
                .getString(R.string.dialog_keep_ratio_title));

        // forcing showing soft input
        AlertDialog aDialog = alertDialogBuilder.create();
        return aDialog;

    }

    /**
     * Especific por this Dialog Fragment!
     *
     * @return The Button OK or null if not found
     */
    protected View getOkButton() {

        ViewGroup v = (ViewGroup) getDialog().getCurrentFocus().getParent()
                .getParent().getParent().getParent();

        View v2 = ((ViewGroup) ((ViewGroup) v.getChildAt(3))
                .getChildAt(0)).getChildAt(2);
        if (v2.getClass() == Button.class) {
            if (((Button) v2).getText().equals(res.getString(R.string.OK))) {
                return v2;
            }
        }

        return null;

    }
}
