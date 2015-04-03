package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

import java.util.Calendar;

public class SetarInicioProvaSliderCommand extends Command {

    private  int hora;
    private  int minuto;

    public SetarInicioProvaSliderCommand(Application controller) {
        super(controller);
        // TODO Auto-generated constructor stub
    }

    public SetarInicioProvaSliderCommand(Context context) {
        super(context);
    }

    public SetarInicioProvaSliderCommand(Context aController, int hora, int minuto){
        super(aController);
        this.hora = hora;

        this.minuto = minuto;
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,hora);
            cal.set(Calendar.MINUTE, minuto);
            cal.set(Calendar.SECOND, 00);
            aController.getSliderChoreographer().setInicioProvaSlider(cal);
    }

}
