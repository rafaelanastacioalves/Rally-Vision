package anastasoft.rallyvision.controller;

/**
 * Created by rafaelanastacioalves on 16/09/14.
 */

import android.os.CountDownTimer;

import java.util.Date;


public class Clock extends CountDownTimer {
    private Date ti;
    private Date te;
    private long timer;
    private Observable aObservable;
    private Controller.CounterAndConverter aCC;


    public Clock(long timer, Controller.CounterAndConverter aCC, Observable aObservable) {
        super(timer, timer);
        this.timer = timer;
        this.aCC = aCC;
        ti = new Date();
        te = new Date();
        this.aObservable = aObservable;
    }


    @Override
    public void onTick(long value) {

    }

    @Override
    public void onFinish() {
//        aCC.CLOCK_STATE = aCC.CLOCK_STATE_READY;
    }

    public void beginTimeCount() {
        ti.setTime(System.currentTimeMillis());
    }

    public void endTimeCount() {
        te.setTime(System.currentTimeMillis());
    }

    public void update() {
        ti.setTime(aObservable.getStartTime());
    }

    public long getDeltaT() {
        return this.timer;
    }

    public long getDeltaTTot() {
        return System.currentTimeMillis() - ti.getTime();
    }

    public boolean isReady() {
        return (getDeltaT() >= timer);
    }

    public void reset() {
        beginTimeCount();
    }
}
