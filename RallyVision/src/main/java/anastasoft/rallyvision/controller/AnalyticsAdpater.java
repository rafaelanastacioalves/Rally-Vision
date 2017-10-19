package anastasoft.rallyvision.controller;

import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import anastasoft.rallyvision.R;

/**
 * Created by rafaelanastacioalves on 02/01/15.
 */
public class AnalyticsAdpater {
    private Controller aController;
    public AnalyticsAdpater(Controller aController){
        this.aController = aController;
    }
    public static enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
    private static final String TAG = "Analytics Adapter";

    private final String EXECUTANDO = "Executando";

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    public static final String PROPERTY_ID = "UA-57987159-1";



    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(aController);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker):null;
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public void sendScreen(){
        // analytics

        // Get tracker.
        Tracker t = getTracker(
                TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(EXECUTANDO);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        if(aController.isTestOn()){
            Log.e(TAG, " +++ tracker.send +++");
        }

    }



}
