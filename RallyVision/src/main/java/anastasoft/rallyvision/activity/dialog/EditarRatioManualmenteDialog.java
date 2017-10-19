package anastasoft.rallyvision.activity.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.ResetRatioCommand;
import anastasoft.rallyvision.controller.Controller;

public class EditarRatioManualmenteDialog extends DialogPreference {

    private Controller aController;
    private Resources res;
    private Command cmd;

    public EditarRatioManualmenteDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        aController = (Controller) context.getApplicationContext();
        setDialogLayoutResource(R.layout.config);

    }

    @Override
    protected void onBindDialogView(View view) {
        // TODO Auto-generated method stub
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // TODO Auto-generated method stub
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            cmd = new ResetRatioCommand(aController);
            cmd.Execute();
        }
    }


}