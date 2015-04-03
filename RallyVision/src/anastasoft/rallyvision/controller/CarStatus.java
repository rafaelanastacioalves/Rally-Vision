package anastasoft.rallyvision.controller;

import anastasoft.rallyvision.controller.Data.model.Afericao;

/**
 * Created by rafaelanastacioalves on 17/02/15.
 */
public class CarStatus {

    private static float instantVel;
    private static float avrgVel;

    /**
     * em metros
     */
    private static float deltaStot;
    private static Afericao afericao;

    public static float getInstantVel() {
        return instantVel;
    }

    static void setInstantVel(float instantVel) {
        CarStatus.instantVel = instantVel;
    }

    public static float getAvrgVel() {
        return avrgVel;
    }

    static void setAvrgVel(float avrgVel) {
        CarStatus.avrgVel = avrgVel;
    }

    /**
     * em metros
     * @return
     */
    public static float getDeltaStot() {
        return deltaStot;
    }

     public void setDeltaStot(float deltaStot) {
        CarStatus.deltaStot = deltaStot;
    }

    public static Afericao getAfericao() {
        return afericao;
    }

    static void setAfericao(Afericao afericao) {
        CarStatus.afericao = afericao;
    }

    static void incrementaDeltaStot(float dS){
        deltaStot += dS;
    }
}
