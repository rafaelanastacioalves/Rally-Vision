package anastasoft.rallyvision.command;

import android.app.Application;
import android.text.Html;
import android.widget.Toast;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class ConfigureRatioCommand extends Command {

    private final String afericaoNome;
    private int afericaoValor;

    public ConfigureRatioCommand(Application controller, int afericao_valor, String afericao_nome) {
        super(controller);
        this.afericaoValor = afericao_valor;
        this.afericaoNome = afericao_nome;

    }


    @Override
    public void Execute() {
        Afericao afericaoTemp = new Afericao(afericaoNome, afericaoValor);
        if (aController.jaExiste(afericaoTemp)) {
            String text = aController.getResources()
                    .getString(R.string.invalido_jah_existe)+ " " +"<font color=#00DBFF>"+ afericaoTemp.getName()+ "</font>" ;
            Toast.makeText(aController.getApplicationContext(), Html.fromHtml(text), Toast.LENGTH_SHORT).show();
        } else {
            aController.configureRatio(afericaoValor, afericaoNome);

        }

    }
}
