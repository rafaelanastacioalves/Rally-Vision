package anastasoft.rallyvision.activity.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.SetarInicioProvaSliderCommand;
import anastasoft.rallyvision.controller.Controller;

/**
 * Created by rafaelanastacioalves on 19/02/15.
 */
public  class TimePickerDialogFragment_old extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    private Controller aController;

    public Dialog onCreateDialog(Bundle savedInstanceState, Controller aController) {
        this.aController = aController;
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
//        LayoutInflater liAdd = LayoutInflater.from(getActivity());
//        View promptsView = liAdd.inflate(R.layout, null);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        Command cmd = new SetarInicioProvaSliderCommand(aController, hourOfDay, minute  );
        cmd.Execute();
    }
}