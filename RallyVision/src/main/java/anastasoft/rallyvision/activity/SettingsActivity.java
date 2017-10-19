package anastasoft.rallyvision.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.List;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.ResetarApplicationCommand;
import anastasoft.rallyvision.command.SetRatioCommand;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Data.model.Afericao;
import anastasoft.rallyvision.controller.PreferencesAdapter;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference
                        .setSummary(index >= 0 ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    private Controller aController;
    private Command cmd;
    private Resources res;

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference
                .setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(
                        preference.getContext()).getString(preference.getKey(),
                        ""));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        aController = (Controller) getApplicationContext();
        res = getResources();
        setupSimplePreferencesScreen();

        loadAfericaoList();

        initSummary(getPreferenceScreen());


    }

    private void loadAfericaoList() {
        ListPreference lPref = (ListPreference) findPreference(getResources().getString(R.string.ratio_key_list));
        List<Afericao> afericaoList = aController.getListaAfericoes();

        CharSequence entries[] = new String[afericaoList.size()];
        CharSequence entryValues[] = new String[afericaoList.size()];
        int i = 0;
        for (Afericao afericao : afericaoList) {
            entries[i] = afericao.getName();
            entryValues[i] = (String.valueOf(afericao.getRatio()));
            i++;
        }
        lPref.setEntries(entries);
        lPref.setEntryValues(entryValues);
        if (afericaoList.size() == 1){
            lPref.setValueIndex(0);
        }
        else{
            Afericao afericao;
            afericao =  aController.getAfericao();
            int index;
            index = aController.getIndexDeAfericao(afericao,afericaoList);
            lPref.setValueIndex(index);
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils. navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // /** {@inheritDoc} */
    // @Override
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // public void onBuildHeaders(List<Header> target) {
    // if (!isSimplePreferences(this)) {
    // loadHeadersFromResource(R.xml.pref_headers, target);
    // }
    // }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().contains("assword")) {
                p.setSummary("******");
            } else {
                p.setSummary(editTextPref.getText());
            }
        }
//		if (p instanceof MultiSelectListPreference) {
//			EditTextPreference editTextPref = (EditTextPreference) p;
//			p.setSummary(editTextPref.getText());
//		}
    }

    // /**
    // * This fragment shows data and sync preferences only. It is used when the
    // * activity is showing a two-pane settings UI.
    // */
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // public static class DataSyncPreferenceFragment extends PreferenceFragment
    // {
    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // addPreferencesFromResource(R.xml.pref_data_sync);
    //
    // // Bind the summaries of EditText/List/Dialog/Ringtone preferences
    // // to their values. When their values change, their summaries are
    // // updated to reflect the new value, per the Android Design
    // // guidelines.
    // bindPreferenceSummaryToValue(findPreference("sync_frequency"));
    // }
    // }
    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        PreferenceManager.setDefaultValues(aController, R.xml.pref_general,
                false);



    }
    @Override
    protected void onRestart(){
        super.onRestart();

    }

    // /**
    // * This fragment shows notification preferences only. It is used when the
    // * activity is showing a two-pane settings UI.
    // */
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // public static class NotificationPreferenceFragment extends
    // PreferenceFragment {
    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // addPreferencesFromResource(R.xml.pref_notification);
    //
    // // Bind the summaries of EditText/List/Dialog/Ringtone preferences
    // // to their values. When their values change, their summaries are
    // // updated to reflect the new value, per the Android Design
    // // guidelines.
    // bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
    // }
    // }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        float ratio;



        if (key.equals(getResources().getString(R.string.descanso_key))){
            CheckBoxPreference pref = (CheckBoxPreference)findPreference(key);
            if(pref.isChecked()){
                pref.setChecked(true);

            }
            else{
                pref.setChecked(false);
            }
        }
        if(key.equals(res.getString((R.string.ratio_key_list)))){
            ListPreference aListPref = (ListPreference) findPreference(key);

            updateRatioSummary(sharedPreferences, "example_ratio");
            ratio = Float.parseFloat(sharedPreferences.getString(key,
                    res.getString(R.string.pref_default_ratio_number)));
            updatePrefSummary(findPreference(res.getString(R.string.ratio_key_list)));
            cmd = new SetRatioCommand(aController, Float.parseFloat(aListPref.getValue()), String.valueOf(aListPref.getEntry()) );
            cmd.Execute();



        }
        if(key.equals(res.getString(R.string.sliders_key))) {

                final CheckBoxPreference pref = (CheckBoxPreference) findPreference(key);

                AlertDialog alert = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_set_slider_certeza));
                builder.setMessage(getString(R.string.dialog_set_slider_necessario_reiniciar));
                final AlertDialog finalAlert = alert;
                builder.setNegativeButton(getString(R.string.cancelar),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Cancel was clicked; do something

                                PreferencesAdapter aPA = aController.getPrefAdapter();

                                pref.setChecked(!pref.isChecked());
                                finish();


                            }
                        });
                builder.setPositiveButton(getString(R.string.reiniciar), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK was clicked; do something
                        // Close Activity
                        Command c = new ResetarApplicationCommand(aController);
                        c.Execute();
                    }
                });
                alert = builder.create();

                // Show the dialog
                    alert.show();
        }



    }

    private void updateRatioSummary(SharedPreferences sharedPreferences, String key) {

        Preference pref = findPreference(key);
        // Set summary to be the user-description for the selected value

        try {
            pref.setSummary(sharedPreferences.getString(key,
                    res.getString(R.string.pref_default_ratio_number)));



        } catch (Exception e) {

        }
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }
    }
}
