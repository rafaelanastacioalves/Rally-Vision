package anastasoft.rallyvision.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import anastasoft.rallyvisionaluguel.R;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.EditCommand;
import anastasoft.rallyvision.controller.Controller;

public class EditDialog extends android.support.v4.app.DialogFragment {

    private Controller aController;
    private Resources res;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        res = getResources();
        aController = (Controller) (getActivity()).getApplicationContext();
        LayoutInflater liAdd = LayoutInflater.from(getActivity());
        View promptsView = liAdd.inflate(R.layout.change, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.change);
        userInput.requestFocus();
        alertDialogBuilder.setTitle(res.getString(R.string.editar));
        alertDialogBuilder.setMessage(res
                .getString(R.string.editar_odometro_message));
        alertDialogBuilder.setCancelable(true).setPositiveButton(
                res.getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            String result = String.valueOf(userInput.getText());
                            Command cmd = new EditCommand(aController, Integer
                                    .parseInt(result));
                            cmd.Execute();
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(aController.getApplicationContext(),
                                    res.getString(R.string.not_valid),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(res.getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // forcing showing soft input
        AlertDialog aDialog = alertDialogBuilder.create();
        aDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return aDialog;

    }
}
