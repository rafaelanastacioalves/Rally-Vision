package anastasoft.rallyvision.controller;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import anastasoft.rallyvision.R;

public class PreferencesAdapter {

    private static String RATIO;
    private static String SCREEN;
    private static int INDEX_RATIO = 3;
    private static SharedPreferences sPreferences;
    private static String TAG;
    private static Resources res;
    private Controller aController;
    private Editor editor;
    private Observable aObservable;

    public PreferencesAdapter(Controller c, Observable aObervable) {
        aController = c;
        aObservable = aObervable;
        res = aController.getResources();
        TAG = getClass().getName();

        RATIO = res.getString(R.string.ratio_key);
        SCREEN = res.getString(R.string.descanso_key);
    }

    public float getRatio() {

        sPreferences = PreferenceManager
                .getDefaultSharedPreferences(aController);
        float ratio = Float.parseFloat((sPreferences.getString(RATIO,
                res.getString(R.string.pref_default_ratio_number))));
//		Toast.makeText(aController.getCurrentActiviy(),
//				"ratio = " + String.valueOf(ratio), Toast.LENGTH_SHORT).show();
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
            Log.e(TAG, " +++ setState +++");
        ArrayList<Float> tempArray = (ArrayList<Float>) aObservable.getValues();
        setRatio((tempArray.get(INDEX_RATIO)));
    }

    public boolean getScreenOnStatus(){
        sPreferences = PreferenceManager.getDefaultSharedPreferences(aController);
        return sPreferences.getBoolean(SCREEN, false);

    }

}
