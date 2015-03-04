package anastasoft.rallyvision.controller.SliderCoreographer;

import android.net.Uri;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.Calendar;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.Slider.SliderCore;
import anastasoft.rallyvision.activity.MenuPrincipal;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Observable;

/**
 * Created by rafaelanastacioalves on 22/01/15.
 */
public class SliderChoreographer {

    private Controller aController;
    private MenuPrincipal aMenuPrincipal;
    private Controller.CounterAndConverter aCounterandConverter;
    private Observable aObservable;
    private SliderCore aSliderCore;
    private FileParser aFileParser;
    private Calendar inicioProvaSlider;

    public  SliderChoreographer(Controller aController, MenuPrincipal aMenuPrincipal, Controller.CounterAndConverter aCounterandConverter, Observable aObservable){


        this.aController = aController;
        this.aMenuPrincipal = aMenuPrincipal;
        setupAConuntAndConverter(aCounterandConverter);
        this.aObservable = aObservable;
        this.aSliderCore = new SliderCore();
        aFileParser = new FileParser();

        aCounterandConverter.setSliderCore(aSliderCore);

        this.aMenuPrincipal.setUpSliders(true);




        this.aObservable.Attach(this);





    }

    public void setupAConuntAndConverter(Controller.CounterAndConverter aCounterandConverter) {
        this.aCounterandConverter = aCounterandConverter;
    }

    public void setaSliderCore(){
        if(aCounterandConverter != null)
            aCounterandConverter.setSliderCore(aSliderCore);
    }

    public void carregarArquivoProva(Uri aUri){
        // Get the File path from the Uri
        String path = FileUtils.getPath(aMenuPrincipal , aUri);

        try{
            // Alternatively, use FileUtils.getFile(Context, Uri)
            if (path != null && FileUtils.isLocal(path)) {
                aSliderCore.carregarProva(aFileParser.getProvaCursorFrom(path));

            }
        }catch(Exception erro){
            Toast.makeText(aController, aController.getResources().getString(R.string.slider_carregar_arquivo_invalido), Toast.LENGTH_SHORT).show();
        }
        try{
            aMenuPrincipal.setAgendarInicioProvaSlider(true);
        }catch (NullPointerException e){
            if (aController.isTestOn()) {
                Toast.makeText(aController.getApplicationContext(), "Erro em setAgendarInicioProvaSlider: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }



    }

    public void desactivate(){

    }

    public void setInicioProvaSlider(Calendar inicioProvaSlider) {
        aObservable.getRelogio().setarAlarme(inicioProvaSlider, aSliderCore);

    }

    /**
     * Inicia a contagem da prova usando sliders.
     * Necessita que o CountAndConverter esteja iniciado.
     */
    public void startProvaSlider(){
        this.aCounterandConverter.setSliderCore(aSliderCore);

    }

}
