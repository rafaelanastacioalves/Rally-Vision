package anastasoft.rallyvision.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class VelocityEng {

    private static final int DELTA_S = 0;
    private static final int INSTANT_VEL = 1;
    private static final int AVRG_VEL = 2;
    private static final int RATIO = 3;
    private static final int T_START = 4;
    private static final int T_TOT = 5;
    static float instantVel;
    static float avrgVel;
    static float deltaStot;
    static float deltaS;
    private static Clock aClock;
    float ratio;
    Date tStart;
    Long deltaTTot;
    private List<Float> valuesList;

    public VelocityEng(float ratio, Clock aClock) {
        // TODO Auto-generated constructor stub
        this.ratio = ratio;

        valuesList = new ArrayList<Float>();
        valuesList.add((float) 0);
        valuesList.add((float) 0);
        valuesList.add((float) 0);
        valuesList.add((float) 0);
        valuesList.add((float) 0);
        valuesList.add((float) 0);

        deltaTTot = (long) 0;
        instantVel = 0;
        avrgVel = 0;
        deltaStot = 0;
        deltaS = 0;

        tStart = new Date(System.currentTimeMillis());

        this.aClock = aClock;
    }

    protected int getAvrgVel() {

        return (int) avrgVel;

    }

    protected int getDistance() {
        return (int) deltaStot;
    }

    protected void updateEnd(int pulse) {

        // updating Spaces
        deltaS = convert(pulse);
        deltaStot += deltaS;
        deltaTTot = aClock.getDeltaTTot();
        // updating times

        // updating velocities
        instantVel = (float) (3.6 * deltaS / (((float) (aClock.getDeltaT())) / ((float) 1000)));
        avrgVel    = (float) (3.6 * deltaStot / (((float) (deltaTTot)) / ((float) 1000)));
    }

    protected int getInstVel() {

        return (int) instantVel;
    }

    public ArrayList<Float> getValues() {
        valuesList.set(DELTA_S, deltaStot);
        valuesList.set(INSTANT_VEL, instantVel);
        valuesList.set(AVRG_VEL, avrgVel);
        valuesList.set(RATIO, ratio);
        valuesList.set(T_START, (float) tStart.getTime());
        valuesList.set(T_TOT, (float) deltaTTot);
        return (ArrayList<Float>) valuesList;

    }

    public void setValues(List<Float> list) {
        deltaStot = list.get(DELTA_S);
        instantVel = list.get(INSTANT_VEL);
        avrgVel = list.get(AVRG_VEL);
        ratio = list.get(RATIO);

        float t = list.get(T_START);
        tStart.setTime((long) t);

        t = list.get(T_TOT);

        deltaTTot = (long) t;
    }

    protected void zerar() {
        deltaS = 0;
        instantVel = 0;
        avrgVel = 0;
        deltaStot = 0;
        deltaTTot = (long) 0;
        tStart.setTime((System.currentTimeMillis()));

    }

    private float convert(int pulse) {
        return pulse * ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    protected void setDeltaSTotal(int value) {
        // TODO Auto-generated method stub
        this.deltaStot = value;
    }
}
