package anastasoft.rallyvision.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anastasoft.rallyvision.Slider.Motorista;
import anastasoft.rallyvision.Slider.MotoristaUsuario;
import anastasoft.rallyvision.Slider.SliderCore;
import anastasoft.rallyvision.activity.MenuPrincipal;
import anastasoft.rallyvision.controller.Controller.CounterAndConverter;
import anastasoft.rallyvision.controller.Data.DBHelper;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class Observable {

    private static final String TAG = "Observable";
    private static final int INDEX_CAR_STATUS = 0;
    private static final int INDEX_RELOGIO = 1;
    private static CarStatus aCarStatus;

    private static List<Object> aObservers;
    private static ArrayList<Object> listValues;
    private  Controller aController;
    private ArrayList<Motorista> motoristasStatus;

    public Observable(Controller aController, Relogio aRelogio) {
        // TODO Auto-generated constructor stub

        this.aController = aController;
        aCarStatus = new CarStatus();
        aCarStatus.setInstantVel(0);
        aCarStatus.setAvrgVel(0);
        aCarStatus.setDeltaStot(0);
        aObservers = new ArrayList<Object>();

        // initializing values
        listValues = new ArrayList<Object>();

        listValues.add(aCarStatus);
        listValues.add(aRelogio);


    }

    /**
     * @return array<float>: [Distance, InstVelocity, AvarageVelocity, Ratio,
     * tStart, tLast]
     */
    public ArrayList<Object> getValues() {
        if ( aController.isTestOn())
            Log.e(TAG, " +++ getValues +++");


        return listValues;
    }

    /**
     * @return array: [Distance, InstVelocity, AvarageVelocity ]
     * @param values
     */
    public void setValues(List<Object> values)  {

        this.listValues = (ArrayList<Object>)values;

        try {
            Notify();
        } catch (DBHelper.AfericaoExistenteException e) {

        }

    }


    public void setAfericao(Afericao afericao) {
        // TODO Auto-generated method stub

        aCarStatus = (CarStatus) listValues.get(INDEX_CAR_STATUS);
        aCarStatus.setAfericao(afericao);
        listValues.set(INDEX_CAR_STATUS, aCarStatus);
        try {
            Notify();
        } catch (DBHelper.AfericaoExistenteException e) {
            throw e;
        }
    }


    public void Attach(Object object) {
        aObservers.add(object);
    }

    public void Detach(Object object) {
        aObservers.remove(object);
    }

    public void Notify() throws DBHelper.AfericaoExistenteException {

        for (int i = 0; i < aObservers.size(); i++) {
            Object array_element = aObservers.get(i);

            if (array_element != null) {
                if (array_element.getClass() == MenuPrincipal.class) {
                    ((MenuPrincipal) array_element).update();
                    Notify(motoristasStatus);

                }

                if (array_element.getClass() == CounterAndConverter.class) {
                    ((CounterAndConverter) array_element).update();
                }

                if (array_element.getClass() == PreferencesAdapter.class) {
                    ((PreferencesAdapter) array_element).update();
                }

                if(array_element.getClass() == DBHelper.class)
                    ((DBHelper) array_element).update();
            }


        }
    }

    public void Notify(CounterAndConverter aCounterAndConverter) {
        for (int i = 0; i < aObservers.size(); i++) {
            Object array_element = aObservers.get(i);

            if (array_element.getClass() == MenuPrincipal.class) {
                ((MenuPrincipal) array_element).update();

            }

        }
    }



    public void setValues(ArrayList<Object> values,
                          CounterAndConverter counterAndConverter) {
        this.listValues = values;

        Notify(counterAndConverter);

    }

    public void zerar() {
        aCarStatus = (CarStatus)listValues.get(INDEX_CAR_STATUS);
        aCarStatus.setAvrgVel(0);
        aCarStatus.setDeltaStot(0);
        aCarStatus.setInstantVel(0);

        Relogio aRelogio = (Relogio)listValues.get(INDEX_RELOGIO);
        aRelogio.resetBasico();
        listValues.set(INDEX_CAR_STATUS, aCarStatus);
        listValues.set(INDEX_RELOGIO,aRelogio);
            Notify();

    }



    public void setOdometer(int value) {
        aCarStatus = (CarStatus) listValues.get(INDEX_CAR_STATUS);
        aCarStatus.setDeltaStot(value);
        listValues.set(INDEX_CAR_STATUS , aCarStatus);
        try {

            Notify();
        } catch (DBHelper.AfericaoExistenteException e) {
        }
    }

    public void setValues(ArrayList<Motorista> motoristasStatus) {
        this.motoristasStatus = motoristasStatus;
        Notify(motoristasStatus);
    }

    public void Notify(ArrayList<Motorista> motoristasStatus) {
        Object array_element;

        for (int i = 0; i < aObservers.size(); i++) {
            array_element = aObservers.get(i);

            if (array_element != null) {
                if (array_element.getClass() == MenuPrincipal.class) {
                    ((MenuPrincipal) array_element).update(motoristasStatus);

                }

            }

        }
    }

    public CarStatus getCarStatus() {
        return (CarStatus)listValues.get(INDEX_CAR_STATUS);
    }


    public Relogio getRelogio() {
        return (Relogio)listValues.get(INDEX_RELOGIO);
    }

    public MotoristaUsuario getMotoristaUsuario() {
        return (MotoristaUsuario) motoristasStatus.get(SliderCore.INDEX_MOT_USUARIO);
    }
}
