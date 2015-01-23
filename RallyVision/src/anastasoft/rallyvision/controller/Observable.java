package anastasoft.rallyvision.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import anastasoft.rallyvision.Slider.motorista.Motorista;
import anastasoft.rallyvision.activity.MenuPrincipal;
import anastasoft.rallyvision.controller.Controller.CounterAndConverter;

public class Observable {

    private static final String TAG = "Observable";
    private static final int INDEX_DISTANCE = 0;
    private static final int INDEX_INST_VEL = 1;
    private static final int INDEX_AVRG_VEL = 2;
    private static final int INDEX_RATIO = 3;
    private static final int INDEX_T_START = 4;
    private static final int INDEX_T_TOT = 5;
    private static float instantVel;
    private static float avrgVel;
    private static float deltaStot;
    private static Date tStart;
    private static long tTot;
    private static List<Object> aObservers;
    private static List<Float> listValues;
    private  Controller aController;
    float ratio;
    private ArrayList<Motorista> motoristasStatus;

    public Observable(Controller aController) {
        // TODO Auto-generated constructor stub

        this.aController = aController;
        instantVel = 0;
        avrgVel = 0;
        deltaStot = 0;
        aObservers = new ArrayList<Object>();

        // initializing values
        listValues = new ArrayList<Float>();

        listValues.add(deltaStot);
        listValues.add(instantVel);
        listValues.add(avrgVel);
        listValues.add(ratio);
        listValues.add((float) 0);
        listValues.add((float) 0);

        // timers
        tStart = new Date();
        tTot = 0;
    }

    /**
     * @return array<float>: [Distance, InstVelocity, AvarageVelocity, Ratio,
     * tStart, tLast]
     */
    public List<Float> getValues() {
        if ( aController.isTestOn())
            Log.e(TAG, " +++ getValues +++");

        listValues.set(INDEX_DISTANCE, deltaStot);
        listValues.set(INDEX_INST_VEL, instantVel);
        listValues.set(INDEX_AVRG_VEL, avrgVel);
        listValues.set(INDEX_RATIO, ratio);
        listValues.set(INDEX_T_START, (float) tStart.getTime());
        listValues.set(INDEX_T_TOT, (float) tTot);
        return listValues;
    }

    /**
     * @return array: [Distance, InstVelocity, AvarageVelocity ]
     */
    public void setValues(List<Float> values) {

        for (int i = 0; i < values.size(); i++) {

            switch (i) {
                case INDEX_DISTANCE:
                    deltaStot = values.get(i);
                    break;
                case INDEX_INST_VEL:
                    instantVel = values.get(i);
                    break;
                case INDEX_AVRG_VEL:
                    avrgVel = values.get(i);
                    break;
                case INDEX_RATIO:
                    ratio = values.get(INDEX_RATIO);

                default:
                    break;
            }

        }

        Notify();

    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float aRatio) {
        // TODO Auto-generated method stub
        this.ratio = aRatio;

        Notify();
    }

    public void Attach(Object object) {
        aObservers.add(object);
    }

    public void Detach(Object object) {
        aObservers.remove(object);
    }

    public void Notify() {

        for (int i = 0; i < aObservers.size(); i++) {
            Object array_element = aObservers.get(i);

            if (array_element != null) {
                if (array_element.getClass() == MenuPrincipal.class) {
                    ((MenuPrincipal) array_element).update();

                }

                if (array_element.getClass() == CounterAndConverter.class) {
                    ((CounterAndConverter) array_element).update();
                }

                if (array_element.getClass() == PreferencesAdapter.class) {
                    ((PreferencesAdapter) array_element).update();
                }
                if (array_element.getClass() == Clock.class) {
                    ((Clock) array_element).update();
                }
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

    public long getStartTime() {
        return tStart.getTime();
    }

    public void setValues(ArrayList<Float> values,
                          CounterAndConverter counterAndConverter) {
        // TODO Auto-generated method stub
        for (int i = 0; i < values.size(); i++) {

            switch (i) {
                case INDEX_DISTANCE:
                    deltaStot = values.get(i);
                    break;
                case INDEX_INST_VEL:
                    instantVel = values.get(i);
                    break;
                case INDEX_AVRG_VEL:
                    avrgVel = values.get(i);
                    break;
                default:
                    break;
            }
        }

        Notify(counterAndConverter);

    }

    public void zerar() {
        avrgVel = 0;
        deltaStot = 0;
        instantVel = 0;
        tTot = 0;
        tStart.setTime(System.currentTimeMillis());
        Notify();
    }

    public float getOdometer() {
        // TODO Auto-generated method stub
        return deltaStot;
    }

    public void setOdometer(int value) {
        deltaStot = value;
        Notify();
    }

    public void setValues(ArrayList<Motorista> motoristasStatus) {
        this.motoristasStatus = motoristasStatus;
        Notify(motoristasStatus);
    }

    private void Notify(ArrayList<Motorista> motoristasStatus) {
        for (int i = 0; i < aObservers.size(); i++) {
            Object array_element = aObservers.get(i);

            if (array_element != null) {
                if (array_element.getClass() == MenuPrincipal.class) {
                    ((MenuPrincipal) array_element).update(motoristasStatus);

                }

            }

        }
    }
}
