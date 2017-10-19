package anastasoft.rallyvision.controller;

import java.util.ArrayList;

class VelocityEng {

    private static final int CAR_STATUS = 0;
    private static final int RELOGIO = 1;
    private static CarStatus carStatusTemp;
    private static float deltaS;

    /**
     * Utilziado localmente para evitar acessar instancias da lista...
     */
    private static Relogio aRelogio;
    Long deltaTTot;
    private ArrayList<Object> valuesList;

    public VelocityEng(CarStatus carStatus, Relogio aRelogio) {
        // TODO Auto-generated constructor stub
        this.carStatusTemp = carStatus;

        valuesList = new ArrayList<Object>();



        deltaS = 0;

        valuesList.add(carStatus);
        valuesList.add((aRelogio));


    }




    protected void updateEnd(int pulse) {
        aRelogio = ((Relogio)valuesList.get(RELOGIO));
        carStatusTemp = (CarStatus)valuesList.get(CAR_STATUS);
        // updating Spaces
        deltaS = convert(pulse);
        carStatusTemp.incrementaDeltaStot(deltaS);
        deltaTTot = aRelogio.getDeltaTTotBasico();
        // updating times

        // updating velocities
        carStatusTemp.setInstantVel(
                (float) (3.6 * deltaS / (((float) (aRelogio.getDeltaT())) / ((float) 1000)))
        );
        carStatusTemp.setAvrgVel(
                (float) (3.6 * carStatusTemp.getDeltaStot() / (((float) (deltaTTot)) / ((float) 1000)))
        );
    }



    public ArrayList<Object> getValues() {

        return (ArrayList<Object>) valuesList;

    }

    public void setValues(ArrayList<Object> list) {
        this.valuesList = list;
    }






    private float convert(int pulse) {
        return pulse *
                        (
                            ((CarStatus)valuesList.get(CAR_STATUS))
                                .getAfericao()
                                    .getRatio()
                        );
    }




    public float getDeltaS() {
        return deltaS;
    }
}
