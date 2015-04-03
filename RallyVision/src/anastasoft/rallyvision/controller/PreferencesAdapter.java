package anastasoft.rallyvision.controller;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Data.DBHelper;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class PreferencesAdapter {

    private static String RATIO;
    private static String SCREEN;
    private static int INDEX_CARSTATUS = 0;
    private static SharedPreferences sPreferences;
    private static String TAG;
    private static Resources res;
    private Controller aController;
    private Editor editor;
    private Observable aObservable;
    private DBHelper dbHelper;

    /**
     * Interface que interage com as partes da aplicação de forma a fornecer informações de preferencia do usuario
     * @param c
     * @param aObervable
     */
    public PreferencesAdapter(Controller c, Observable aObervable) {
        aController = c;
        aObservable = aObervable;
        res = aController.getResources();
        TAG = getClass().getName();

        RATIO = res.getString(R.string.ratio_key);
        SCREEN = res.getString(R.string.descanso_key);

        this.dbHelper = aController.getDbHelper();

        
    }



    public float getRatio() {
        sPreferences = PreferenceManager
                .getDefaultSharedPreferences(aController);

        float ratio = Float.parseFloat((sPreferences.getString(RATIO,
                res.getString(R.string.pref_default_ratio_number))));
//		Toast.makeText(aController.getCurrentActiviy(),
//				"ratio = " + String.valueOf(ratio), Toast.LENGTH_SHORT).show();

        if(dbHelper == null){
            this.dbHelper = aController.getDbHelper();
        }
        try{
            float ratioAfericao;

            if(dbHelper.getAfericaoCount() >1){
                ratioAfericao = Float.parseFloat((sPreferences.getString(RATIO,
                        res.getString(R.string.pref_default_ratio_number))));
                if(ratio == ratioAfericao){
                    return ratioAfericao;
                }
            }
            else {
                Afericao aFericao;
                aFericao = dbHelper.getAfericaoByName(res.getString(R.string.pref_default_ratio_name));
                ratioAfericao = aFericao.getRatio();
                if (aFericao.getRatio() == ratio){
                    return aFericao.getRatio();
            }


            }

        }catch (NullPointerException e){

        }

        return ratio;




    }

    public void setRatio(float ratio) {
        editor = sPreferences.edit();
        float currentRatio = getRatio();

        if (currentRatio != ratio) {
            editor.putString(RATIO, String.valueOf(ratio));

            editor.commit();
        }

    }

    public void update() {
        if (aController.isTestOn())
            Log.e(TAG, " +++ setRelativeState +++");
        ArrayList<Object> tempArray =  aObservable.getValues();
        CarStatus carStatusTemp = (CarStatus)tempArray.get(INDEX_CARSTATUS);
        setRatio(carStatusTemp.getAfericao().getRatio());
    }

    public boolean getScreenOnStatus(){
        sPreferences = PreferenceManager.getDefaultSharedPreferences(aController);
        return sPreferences.getBoolean(SCREEN, false);

    }

    /**
     * Retorna a afericao padrão. Caso o usuário deseje uma aferição personalizada, ele deverá buscar em configurações.
     * Futuramente essa função  poderá carregar da memória a ultima aferição escolhida.
     * @return aferição padrão
     */
    public Afericao getAfericao() {
        sPreferences = PreferenceManager
                .getDefaultSharedPreferences(aController);
        Afericao aFericao;




        try{


                aFericao = dbHelper.getAfericaoByName(res.getString(R.string.pref_default_ratio_name));


        }catch (NullPointerException e){
            aFericao = null;

        }

        return aFericao;
    }
}
