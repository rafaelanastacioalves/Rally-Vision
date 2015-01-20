package anastasoft.rallyvision.controller;

import java.util.Date;

public class TimeAdapter {

    private Date ti;
    private Date te;

    public TimeAdapter() {
        ti = new Date();
        te = new Date();
    }

    public void beginTimeCount() {
        ti.setTime(System.currentTimeMillis());
    }

    public void endTimeCount() {
        te.setTime(System.currentTimeMillis());
    }

    public long getDeltaT() {
        return te.getTime() - ti.getTime();
    }

}
