package anastasoft.rallyvision.command;

import android.app.Application;

public class ConfigureRatioCommand extends Command {

    private int afericao;

    public ConfigureRatioCommand(Application controller, int afericao) {
        super(controller);
        this.afericao = afericao;
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        aController.configureRatio(afericao);
    }

}
