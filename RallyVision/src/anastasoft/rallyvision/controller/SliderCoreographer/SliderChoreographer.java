package anastasoft.rallyvision.controller.SliderCoreographer;

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

    public SliderChoreographer(Controller aController, MenuPrincipal aMenuPrincipal, Controller.CounterAndConverter aCounterandConverter, Observable aObservable){


        this.aController = aController;
        this.aMenuPrincipal = aMenuPrincipal;
        this.aCounterandConverter = aCounterandConverter;
        this.aObservable = aObservable;
        this.aSliderCore = new SliderCore();


        aCounterandConverter.setSliderCore(aSliderCore);





    }
}
