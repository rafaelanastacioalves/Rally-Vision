package anastasoft.rallyvision.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.ConfigureRatioCommand;
import anastasoft.rallyvision.controller.Controller;

public class ConfigureDialog extends android.support.v4.app.DialogFragment {

    View OKButton;
    private Controller aController;
    private Resources res;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        aController = (Controller) (getActivity()).getApplicationContext();
        res = getResources();
        LayoutInflater liAdd = LayoutInflater.from(getActivity());
        View promptsView = liAdd.inflate(R.layout.config, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userAfericaoInput = (EditText) promptsView
                .findViewById(R.id.config);
        final EditText userAfericaoName = (EditText)  promptsView
                .findViewById(R.id.config_name);

        userAfericaoInput.requestFocus();

        userAfericaoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String strEnteredVal = userAfericaoInput.getText().toString();
//				if (OKButton == null) {
//					OKButton = getOkButton();
//				}
                if (strEnteredVal.equals("")) {

//					OKButton.setEnabled(false);

                } else {

//					OKButton.setEnabled(true);

                    int num = Integer.parseInt(strEnteredVal);

                    if (num < 1) {
                        userAfericaoInput.setText("");
                    }

                }

            }
        });

        alertDialogBuilder.setCancelable(true).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        String afericao_valor = String.valueOf(userAfericaoInput.getText());
                        String afericao_nome = String.valueOf(userAfericaoName.getText());
                        try {

                            if (afericao_valor.equals("")) {
                                Field field = dialog.getClass().getSuperclass()
                                        .getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                                Toast.makeText(aController,
                                        res.getString(R.string.not_valid),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Command cmd = new ConfigureRatioCommand(
                                        aController, Integer.parseInt(afericao_valor), afericao_nome);
                                cmd.Execute();
                            }

                        } catch (NumberFormatException e) {
                            Toast.makeText(aController.getApplicationContext(),
                                    res.getString(R.string.not_valid),
                                    Toast.LENGTH_SHORT).show();
                        } catch (NoSuchFieldException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setTitle(res.getString(R.string.config_ratio));
        // forcing showing soft input
        AlertDialog aDialog = alertDialogBuilder.create();
        aDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return aDialog;

    }

    /**
     * Especific por this Dialog Fragment!
     *
     * @return The Button OK or null if not found
     */
//	protected View getOkButton() {
//		
//		ViewGroup v = (ViewGroup) getDialog().getCurrentFocus().getParent()
//				.getParent().getParent().getParent();
//		
//		View v2 = ((ViewGroup) ((ViewGroup) v.getChildAt(3))
//				.getChildAt(0)).getChildAt(2);
//		if (v2.getClass() == Button.class) {
//			if (((Button) v2).getText().equals(res.getString(R.string.OK))) {
//				return v2;
//			}
//		}
//		
//		return null;
//		
//	}
}
