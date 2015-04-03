package anastasoft.rallyvision.Slider.Prova_Trecho.Trecho;

/**
 * Created by rafaelanastacioalves on 21/01/15.
 */
public abstract class Trecho {
    protected int         numero;
    protected float       ki;   // em metros
    protected float       kf;   // em metros
    protected float       ti;   // em milisegundos




    public float getDeltaStrecho(){
        return (kf - ki);
    }


    public float getTi(){
        return ti;
    }
    public abstract float getdTTrecho();

    public int getNumTrecho() {
        return numero;
    }

    /**
     * Fornece a quilometragem inicial do referido trecho, em metros
     * @return
     */
    public float getKi(){
        return ki;
    }

    /**
     * Fornece a quilometragem final do referido trecho, em metros
     * @return
     */
    public float getKf(){
        return kf;
    }

    public abstract String getTipo();

    @Override
    public boolean equals(Object aTrecho){
        if((this.getClass() == aTrecho.getClass()) ){
            if(this.getNumTrecho() == ((Trecho)aTrecho).getNumTrecho()){
                return true;
            }
        }

        return false;

    }
}
