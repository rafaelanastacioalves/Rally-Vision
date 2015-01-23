package anastasoft.rallyvision.controller;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.ubertesters.common.models.LockingMode;
import com.ubertesters.sdk.Ubertesters;

import anastasoft.rallyvision.BTManager.BTManager;
import anastasoft.rallyvision.R;
import anastasoft.rallyvision.Slider.SliderCore;
import anastasoft.rallyvision.activity.ConnectMediator;
import anastasoft.rallyvision.activity.DeviceListActivity;
import anastasoft.rallyvision.activity.MenuPrincipal;

// licensing

public class Controller extends Application {


    //checking license purposes

    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwsUvY54xrzoeuyTlFg8gVvY6/Uu5HyIQnK4lKiPwC2dQC34D0wQ8JkiXClovkgY4xmxV8jtTufgAV971yqHESLB7KP68sYNM1eMm7JAl1aL4hiw2qakBWsosbUbOnS0NyYMu5Rkt5m2irVdVGdsqnfXaoRChEh7auqhik5ZOxRZKaml2g2pYUx5Nw3cGA2wM0EbXlb0pVuMXuKcK0mo9YXGODm9TfA7NhvzAaVBSqjR5cyFJ9ZWiqIgBX+843auM6TYYXrKE8pEZSr+TVs4g3gfi40E6aWfzD5xhDyZ0eIBvfCVE0VeV55h4v9B4imQWUTwYO6YXre+y+9NsipD5qwIDAQAB";

    // Generate your own 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[]{
            -46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64,
            89
    };

    // Test switch

    private boolean testON = false;

    private boolean uberON = false;


    // Aplication States

    private static final int APLICATION_DISCONECTED = 0;
    private static final int APLICATION_CONECTING = 1;
    private static final int APLICATION_CONECTED = 2;
    private static int appState = APLICATION_DISCONECTED;

    // Google Analytics

    protected AnalyticsAdpater analyticsAdpater = new AnalyticsAdpater(this);


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final String TIME = "time";

    // Counter and Converter intelligence
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    // Current Activity
    public static final int MESSAGE_READ = 2;

    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_TIME = 6;

    private static final String TAG = "Controller";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static boolean FIRST_RUN = true;
    private static Resources aResource;
    private static String RATIO = "example_ratio";
    private static PreferencesAdapter aPrefAdapter;
    private static Observable aObervable;

    // Resources
    BTManager aBTManager;

    // Preferences
    private volatile CounterAndConverter aCountConv;
    private Activity currentAct;

    // Contain all values that change
    // Name of the connected device
    private String mConnectedDeviceName = null;

    private static ConnectMediator mConnMediator;
    private final Handler mHandler = new Handler() {

        long deltaT;
        int i;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (isTestOn())
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BTManager.STATE_CONNECTED:
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_CONNECTED +++");
                            startCommunication();

                            break;
                        case BTManager.STATE_CONNECTING:
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_CONNECTING +++");
                            break;
                        case BTManager.STATE_LISTEN:
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_LISTEN +++");
                            break;
                        case BTManager.STATE_NONE:
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_NONE +++");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    if (isTestOn())
                        Log.e(TAG, " +++ MESSAGE_WRITE +++");
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct
                    // a
                    // string
                    // from
                    // the
                    // buffer
                    String writeMessage = new String(writeBuf);

                    break;
                case MESSAGE_READ:
                    if (isTestOn())
                        Log.e(TAG, " +++ MESSAGE_READ +++");

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct
                    // a
                    // string
                    // from
                    // the
                    // valid
                    // bytes
                    // in
                    // the
                    // buffer

                    try {
                        // String readMessage = new String(readBuf, 0,
                        // msg.arg1);
                        //deltaT = msg.getData().getLong(TIME);

                        // just one byte is sent... that's why the 0 index is
                        // constant
                        i = (readBuf[0] & 0xff);
                        aCountConv.update(i);
                    } catch (Exception e) {

                    }

                    break;
                case MESSAGE_DEVICE_NAME:
                    if (isTestOn())
                        Log.e(TAG, " +++ MESSAGE_DEVICE_NAME +++");
                    // save
                    // the
                    // connected
                    // device's
                    // name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(
                            getApplicationContext(),
                            aResource.getString(R.string.message_connected_to)
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    if (isTestOn())
                        Log.e(TAG, " +++ MESSAGE_TOAST +++");
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    setAppState(APLICATION_DISCONECTED);
                    break;
            }
        }
    };

    private SharedPreferences preferences;

    private int toInt(byte[] data, int length) {
        int res = 0x0;
        // if (data == null || data.length != 4) return 0x0;
        // ----------

        if (length > 3) {
            res = (0xff & data[3]) << 0;
        }
        if (length > 2) {
            res = res | (0xff & data[2]) << (8 * (length - 2) - 1);
        }

        if (length > 1) {
            res = res | (0xff & data[1]) << (8 * (length - 1) - 1);
        }

        if (length > 0) {
            res = res | ((0xff & data[0]) << (8 * (length - 0) - 1));
        }

        return res;

        // return (int)( // NOTE: type cast not necessary for int
        // (0xff & data[0]) << 24 |
        // (0xff & data[1]) << 16 |
        // (0xff & data[2]) << 8 |
        // (0xff & data[3]) << 0
        // );
    }


    public void startCommunication() {
        if (aCountConv == null) {

            aCountConv = new CounterAndConverter(this, aObervable);
        }

        // if (isRatioDefault(aObervable.getRatio())){
        // if(currentAct.getClass() == MenuPrincipal.class){
        // ((MenuPrincipal)currentAct).warnNotCalibrated();
        // }
        // }

        try {
            if (aCountConv.isAlive()) {
                aCountConv.resetState();

            }
            aCountConv.start();
            if (isTestOn())
                Log.e(TAG, " +++ START +++");
            setAppState(APLICATION_CONECTED);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public boolean shouldCreateNotification() {
        if (getAppState() == APLICATION_CONECTED) {
            return true;
        } else
            return false;
    }


    public void setup(Activity currentAct) {
        if (isTestOn())
            Log.e(TAG, "+++ setup  +++");
        aResource = getResources();
        this.currentAct = currentAct;

        // ------------ubertesters      -------------//

        if (uberON) {
            Ubertesters.initialize(this,
                    LockingMode.LockApplication);
        }

        // ------------checking license -------------//
        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Construct the LicenseCheckerCallback. The library calls this when done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();


        // Construct the LicenseChecker with a Policy.
        mChecker = new LicenseChecker(
                getCurrentActiviy(), new ServerManagedPolicy(getCurrentActiviy(),
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY  // Your public licensing key.
        );

        doCheck();


        // -------- other components --------//
        if (aObervable == null) {
            aObervable = new Observable(this);

        }

        if (aPrefAdapter == null) {
            aPrefAdapter = new PreferencesAdapter(this, aObervable);

        }

        ((MenuPrincipal) getCurrentActiviy()).keepScreenOn(aPrefAdapter.getScreenOnStatus());

        aObervable.Attach(aPrefAdapter);

        if (aBTManager == null) {
            aBTManager = new BTManager(this, mHandler);

        }
        aBTManager.setupBT();

        // If the adapter is null, then Bluetooth is not supported
//		if (!aBTManager.hasBTDevide()) {
//			Toast.makeText(this,
//					aResource.getString(R.string.message_device_has_no_BT),
//					Toast.LENGTH_LONG).show();
//			currentAct.finish();
//			return;
//		}
//		
        // setting the Ratio
        float ratio = aPrefAdapter.getRatio();
        aObervable.setRatio(ratio);

        // setup Count and Converter

        if (aCountConv == null) {

            aCountConv = new CounterAndConverter(this, aObervable);
            aObervable.Attach(aCountConv);
        }


        // setup Menu Principal
        try {
            ((MenuPrincipal) currentAct).setObservable(aObervable);
        } catch (Exception erro) {

        }


        // ConnectMediator
        if (mConnMediator == null) {
            mConnMediator = new ConnectMediator(this);
        }
        mConnMediator.update(this);


        if (FIRST_RUN == false) {
            aObervable.Notify();
        }
        FIRST_RUN = false;

    }

    public int getAppState() {
        return appState;
    }

    public void setAppState(int appState) {
        this.appState = appState;
        notifY();
    }

    private void notifY() {
        mConnMediator.update(this);
    }


    private boolean isRatioDefault(float ratio2) {
        // TODO Auto-generated method stub
        if (aObervable.getRatio() == (float) 1.0) {
            return true;
        } else {
            return false;
        }
    }

    private void doCheck() {
        mChecker.checkAccess(mLicenseCheckerCallback);
    }

    public void checkAccess() {
        mChecker.checkAccess(mLicenseCheckerCallback);

    }

    public void resetRatio() {
        aObervable.setRatio(Float.parseFloat(aResource
                .getString(R.string.pref_default_ratio_number)));
    }

    public void resetGeral() {
        resetRatio();
        zerar();
    }

    public void setOdometer(int value) {
        aObervable.setOdometer(value);
    }

    public void zerar() {
        aObervable.zerar();
    }

    public void enableBT(Activity currentAct) {
        this.currentAct = currentAct;
        if (isTestOn())
            Log.e(TAG, "++ enableBT ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

        if (!aBTManager.isBTEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.currentAct.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }

    }

    protected void query() {
        if (isTestOn())
            Log.e(TAG, " +++ QUERY +++");
        String message = "Q";
        // Check that we're actually connected before trying anything
        if (aBTManager.getState() != BTManager.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            if (isTestOn())
                Log.e(TAG, " +++ NOT_CONNECTED +++");
            return;
        }

        byte[] send = message.getBytes();
        aBTManager.write(send);

    }

    public boolean isTestOn() {
        return testON;
    }

    public void startExecution(Activity currentActivity) {
        if (isTestOn())
            Log.e(TAG, "++ scanDevices ++");
        this.currentAct = currentActivity;
        setAppState(APLICATION_CONECTING);

        if (!aBTManager.isBTEnabled()) {

            enableBT(currentAct);
            return;
        }

        Intent serverIntent = new Intent(currentAct, DeviceListActivity.class);

        // analytics
        analyticsAdpater.sendScreen();

        // finishing
        currentAct.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public Activity getCurrentActiviy() {
        return currentAct;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void actionAfterBlueTooth(int requestCode, int resultCode,
                                     Intent data) {

        if (isTestOn())
            Log.d(TAG, "actionAfterBlueTooth" + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {

                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    aBTManager.connectToDeviceWith(address);
                } else {
                    stopCommunication();
                    setAppState(APLICATION_DISCONECTED);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled
                    startExecution(currentAct);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    stopCommunication();
                    setAppState(APLICATION_DISCONECTED);
                    Toast.makeText(this, R.string.title_BT_not_connected,
                            Toast.LENGTH_SHORT).show();


                }
        }
    }

    public void setupOdometro() {

    }

    public void setRatio(float ratio) {
        Toast.makeText(currentAct, String.valueOf(ratio), Toast.LENGTH_SHORT)
                .show();
        aObervable.setRatio(ratio);

    }


    public void stopCommunication() {
        // Stop the Bluetooth chat services
        if (aBTManager != null)
            aBTManager.stop();
        if (testON)
            Log.e(TAG, "--- stopCommunication ---");
        if (aCountConv != null)
            aCountConv.cancel();
        aCountConv = null;
        setAppState(APLICATION_DISCONECTED);

        mChecker.onDestroy();


    }

    public void configureRatio(int deltaSAfericao) {
        if (isTestOn())
            Log.e(TAG, " +++ configureRatio +++");
        // TODO Auto-generated method stub

        try {
            float deltaSSistema;
            float ratioTemp;
            float ratioSis;

            // retrieving deltaS sistem
            deltaSSistema = aObervable.getValues().get(0);

            // retrieving ratio sistem
            ratioSis = aObervable.getValues().get(3);

            // temp Ratio
            ratioTemp = deltaSAfericao / deltaSSistema;

            // math

            ratioSis = ratioSis * ratioTemp;

            aObervable.setRatio(ratioSis);
            aObervable.zerar();
        } catch (ArithmeticException e) {
            Toast.makeText(getApplicationContext(),
                    aResource.getString(R.string.config_ratio_zero),
                    Toast.LENGTH_LONG).show();
        }


    }


    public class CounterAndConverter extends Thread {

        protected static final int STATE_WAITING = 1;
        protected static final int STATE_READY = 2;
        protected static final int STATE_STOPPED = 3;
        protected static final int CLOCK_STATE_READY = 1;
        protected static final int CLOCK_STATE_COUNTING = 2;
        protected String TAG = "CounterAndConverter";
        protected int STATE;
        protected int CLOCK_STATE;
        float ratio;
        int distance;
        Controller aController;
        private VelocityEng aVelEng;
        private Clock aClock;
        private Observable aObservable;
        private SliderCore aSliderCore;

        public CounterAndConverter(Controller ac, Observable aObservable) {

            aController = ac;

            this.aObservable = aObservable;

            ratio = aObervable.getRatio();


            //this clock can change the variable CLOCK_STATE;
            aClock = new Clock(300, this, aObservable);
            aObservable.Attach(aClock);
            aVelEng = new VelocityEng(ratio, aClock);
            // TODO Auto-generated constructor stub
        }

        public void setOdometer(int value) {
            // TODO Auto-generated method stub
            aVelEng.setDeltaSTotal(value);
        }

        public void resetState() {
            if (isTestOn())
                Log.e(TAG, " +++ RESET_STATE +++");

            aClock.start();

            // redundant
            aClock.beginTimeCount();
            this.STATE = STATE_READY;
            CLOCK_STATE = CLOCK_STATE_COUNTING;

        }

        @Override
        public void run() {
            aClock.start();

            // redundant
            aClock.beginTimeCount();

            STATE = STATE_READY;
            CLOCK_STATE = CLOCK_STATE_COUNTING;
            aVelEng.setValues(aObservable.getValues());
            while (STATE != STATE_STOPPED) {
                switch (STATE) {
                    case STATE_READY:
                        if (CLOCK_STATE == CLOCK_STATE_READY) {
                            aClock.start();
                            aController.query();
                            STATE = STATE_WAITING;
                            CLOCK_STATE = CLOCK_STATE_COUNTING;
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_QUERY +++");
                        }
                        ;

                        break;
                    case STATE_WAITING:

                        break;

                    default:
                        break;

                }
            }

        }


        protected void update(int newPulse) {

            aVelEng.updateEnd(newPulse); // are this  synchronous? I think so...

            // for slider
            if (aSliderCore !=null){
                aSliderCore.update(aVelEng.getDeltaT(), aVelEng.getDeltaS());
                aObervable.setValues(aSliderCore.getStatus());
            }

            aObservable.setValues(aVelEng.getValues(), this);

            STATE = STATE_READY;
        }

        protected void cancel() {
            STATE = STATE_STOPPED;
            aClock.cancel();
        }

        public void  setSliderCore(SliderCore aSliderCore){
            this.aSliderCore = aSliderCore;
        }

        public void update() {
            aVelEng.setValues(aObervable.getValues());
        }

        protected void setRatio(float ratio) {
            aVelEng.setRatio(ratio);
        }

        public void zerar() {
            aVelEng.zerar();
            aObservable.setValues(aVelEng.getValues(), this);

        }


    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int reason) {
            if (((MenuPrincipal) getCurrentActiviy()).isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            ((MenuPrincipal) getCurrentActiviy()).displayResult(getString(R.string.allow));
        }

        public void dontAllow(int reason) {
            if (((MenuPrincipal) getCurrentActiviy()).isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            ((MenuPrincipal) getCurrentActiviy()).displayResult(getString(R.string.dont_allow));
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
            // ((MenuPrincipal)getCurrentActiviy()).displayDialog(reason == Policy.RETRY);
        }

        public void applicationError(int errorCode) {
            if (((MenuPrincipal) getCurrentActiviy()).isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = String.format(getString(R.string.application_error), errorCode);
            ((MenuPrincipal) getCurrentActiviy()).displayResult(result);
        }
    }


}




