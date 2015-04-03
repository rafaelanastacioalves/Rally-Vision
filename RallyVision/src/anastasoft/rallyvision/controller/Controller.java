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
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.ubertesters.common.models.LockingMode;
import com.ubertesters.sdk.Ubertesters;

import java.util.ArrayList;
import java.util.List;

import anastasoft.rallyvision.BTManager.BTManager;
import anastasoft.rallyvision.R;
import anastasoft.rallyvision.Slider.MotoristaUsuario;
import anastasoft.rallyvision.Slider.SliderCore;
import anastasoft.rallyvision.activity.ConnectMediator;
import anastasoft.rallyvision.activity.DeviceListActivity;
import anastasoft.rallyvision.activity.MenuPrincipal;
import anastasoft.rallyvision.controller.Data.DBHelper;
import anastasoft.rallyvision.controller.Data.model.Afericao;
import anastasoft.rallyvision.controller.InAppBilling.AluguelChoreographer;
import anastasoft.rallyvision.controller.SliderCoreographer.SliderChoreographer;

// licensing

public class Controller extends Application {


    //checking license purposes

    private MylicenseChekerCallBack mLicenseCheckerCallBack;
    private LicenseChecker mChecker;

    private static final String BASE64_PUBLIC_KEY =         "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwsUvY54xrzoeuyTlFg8gVvY6/Uu5HyIQnK4lKiPwC2dQC34D0wQ8JkiXClovkgY4xmxV8jtTufgAV971yqHESLB7KP68sYNM1eMm7JAl1aL4hiw2qakBWsosbUbOnS0NyYMu5Rkt5m2irVdVGdsqnfXaoRChEh7auqhik5ZOxRZKaml2g2pYUx5Nw3cGA2wM0EbXlb0pVuMXuKcK0mo9YXGODm9TfA7NhvzAaVBSqjR5cyFJ9ZWiqIgBX+843auM6TYYXrKE8pEZSr+TVs4g3gfi40E6aWfzD5xhDyZ0eIBvfCVE0VeV55h4v9B4imQWUTwYO6YXre+y+9NsipD5qwIDAQAB";
    private static final String BASE64_PUBLIC_KEY_ALUGUEL = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/D1dXUtGrVIjidkGnpx990SWKZ5J0Gh+7p7vwozEAIer+KV5FUokUdPAGiKd/qGzue2xf+8FTcisjdc3hnWGagWF+CaG4M7nQ+Ets5gEmSd6jhmWcijCCnWYl3RkC4WoMnUire3IJDPOjGM/QFz+jzB8hbRjx7G0QTrIVkaKhw7PVWY44QzjIWAlz3G1RIA3c1VkdFSZLPkxZXk5YEAgYUcDUw/pniDvLORRzsJ595oIEQCuI9YF3YuqzhPy/zKpWVJ4e0Xb4eZyHDsWt+Ooakbu3NAKDnTWaRNfAuyczPP6uM/zglmCwNxHPHj+/WUMGT0yvIosn4N5nvJtR/CBwIDAQAB";
    // Generate your own 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[]{
            -46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64,
            89
    };

    // Configuration swith switch

    private boolean testON = true;

    private boolean uberON = false;

    private boolean licenseCheckON = false;

    private boolean sliderON  = true;


    private static final int  clockBasico = 500;

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
    private static Observable aObservable;


    private static SliderChoreographer aSliderChoreographer;

    private SharedPreferences preferences;
    private static DBHelper dbHelper;

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
                           if(testON){
                               Toast.makeText(getApplicationContext() ,"Erro: " + e.toString(), Toast.LENGTH_SHORT).show();
                           }
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
    private AluguelChoreographer aAluguelChoreographer;

    public DBHelper getDbHelper() {
        return dbHelper;
    }


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

            setupCountAndConverter();
        }

        // if (isRatioDefault(aObservable.getRatio())){
        // if(currentAct.getClass() == MenuPrincipal.class){
        // ((MenuPrincipal)currentAct).warnNotCalibrated();
        // }
        // }

        try {

                aCountConv.start();

            }catch (Exception e) {

            if (isTestOn()) {
                Toast.makeText(getApplicationContext(), "Erro em startCommunication: " + e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
        if (isTestOn())
            Log.e(TAG, " +++ START +++");
        try{
            setAppState(APLICATION_CONECTED);

        }catch (SecurityException e){
            if (isTestOn()) {
                Toast.makeText(getApplicationContext(), "Erro em startCommunication: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean shouldCreateNotification() {
        if (getAppState() == APLICATION_CONECTED) {
            return true;
        } else
            return false;
    }


    public void setup(Activity currentAct) throws DBHelper.AfericaoExistenteException {
        if (isTestOn())
            Log.e(TAG, "+++ setup  +++");
        aResource = getResources();
        this.currentAct = currentAct;

        if(aAluguelChoreographer == null){
            if(getApplicationContext().getPackageName().endsWith("rallyvisionaluguel")){
                    aAluguelChoreographer = new AluguelChoreographer(this, BASE64_PUBLIC_KEY_ALUGUEL);

                }
            }



        // ------------ubertesters      -------------//

        if (uberON) {
            Ubertesters.initialize(this,
                    LockingMode.LockApplication);
        }

        // ------------checking license -------------//

        if(licenseCheckON){
            // Try to use more data here. ANDROID_ID is a single point of attack.
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            // Construct the LicenseCheckerCallback. The library calls this when done.
            mLicenseCheckerCallBack = new  MylicenseChekerCallBack(this);


            // Construct the LicenseChecker with a Policy.
            mChecker = new LicenseChecker(
                    getCurrentActiviy(), new ServerManagedPolicy(getCurrentActiviy(),
                    new AESObfuscator(SALT, getPackageName(), deviceId)),
                    BASE64_PUBLIC_KEY  // Your public licensing key.
            );

            doCheck();

        }

        //Relogio

        Relogio aRelogio = new Relogio(clockBasico,this);

        // -------- other components --------//
        if (aObservable == null) {
            aObservable = new Observable(this, aRelogio);
            aObservable.Attach(aRelogio);
            aRelogio.setaObservable(aObservable);


        }


                //setup DBHelper

        if (dbHelper == null) {
            dbHelper = new DBHelper( getApplicationContext(), aObservable);
            aObservable.Attach(dbHelper);
            dbHelper.createAfericao(new Afericao(
                    getResources().getString(R.string.pref_default_ratio_name), Float.parseFloat(getString(R.string.pref_default_ratio_number))));
        }

        if (aPrefAdapter == null) {
            aPrefAdapter = new PreferencesAdapter(this, aObservable);

        }



        aObservable.Attach(aPrefAdapter);

        if (aBTManager == null) {
            aBTManager = new BTManager(this, mHandler);

        }
        aBTManager.setupBT();



        Afericao aFericao = aPrefAdapter.getAfericao();
        aObservable.setAfericao(aFericao);


        setupCountAndConverter();




        // setup Menu Principal
        try {
            ((MenuPrincipal) currentAct).setObservable(aObservable);
        } catch (Exception erro) {

        }


        // ConnectMediator
        if (mConnMediator == null) {
            mConnMediator = new ConnectMediator(this);
            try {
                ((MenuPrincipal) currentAct).setaConnectMediator(mConnMediator);
            }catch (ClassCastException e){
                if (isTestOn()) {
                    Toast.makeText(getApplicationContext(), "Erro em setup: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }
//        mConnMediator.setRelativeState(this);

        // activating Slider

        if(aSliderChoreographer == null ){
            if(sliderON) {
                try {
                    aSliderChoreographer = new SliderChoreographer(this, (MenuPrincipal) getCurrentActiviy(), aCountConv, aObservable);

                } catch (Exception erro) {

                }
            }
        }

        if (FIRST_RUN == false) {
            aObservable.Notify();
        }
        FIRST_RUN = false;

    }

    /**
     * Precisa configurar o método KeepScreenOn
     * @param act
     */
    public void decideScreenOn(MenuPrincipal act) {
        act.keepScreenOn(aPrefAdapter.getScreenOnStatus());
    }

    public boolean jaExiste(Afericao aFer){
        List<Afericao> afericaoListTemp = dbHelper.getTodasAfericoes();
        for (Afericao afericaoTemp : afericaoListTemp ){
            if (afericaoTemp.getName().equals(aFer.getName())){
                return true;
            }

        }
        return false;
    }

    private void setupCountAndConverter() {
        // setup Count and Converter

        if (aCountConv == null) {

            aCountConv = new CounterAndConverter(this, aObservable);
            aObservable.Attach(aCountConv);
            if(aSliderChoreographer != null){
                aSliderChoreographer.setupAConuntAndConverter(aCountConv);
            }
        }
    }

    public int getAppState() {
        return appState;
    }

    public void setAppState(int appState) {
        this.appState = appState;
        notifY();
    }

    private void notifY() {
        mConnMediator.setState(this);
    }




    private void doCheck() {
        try{
            mChecker.checkAccess( mLicenseCheckerCallBack);
        }catch (NullPointerException erro){

        }
    }

    public void checkAccess() {
        try{
            mChecker.checkAccess( mLicenseCheckerCallBack);

        }catch (NullPointerException erro){

        }

    }

    public void resetRatio() {


        try {
            aObservable.setAfericao(dbHelper.getAfericaoByName(aResource.getString(R.string.pref_default_ratio_name)));
        } catch (DBHelper.AfericaoExistenteException e) {

        }
    }

    public void resetGeral() {
        resetRatio();
        zerar();
    }

    public void setOdometer(int value) {
        aObservable.setOdometer(value);
    }

    public void zerar() {
        aObservable.zerar();
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
    public boolean isUberTestOn(){
        return uberON;
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

    public void handleActivityResult(int requestCode, int resultCode,
                                     Intent data) {

        if (isTestOn())
            Log.d(TAG, "handleActivityResult" + resultCode);
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

    public void setRatio(float ratio, String nomeAfericao) {
        Toast.makeText(currentAct, String.valueOf(ratio), Toast.LENGTH_SHORT)
                .show();

            Afericao aFerTemp = new Afericao(nomeAfericao, ratio);

                aObservable.setAfericao(aFerTemp);

    }

    /**
     * Usar esse método somente quando tiver certeza de que a aplicação inteira irá encerrar
     */
    public void stopAll(){
        stopCommunication();

        if(aAluguelChoreographer!= null){
            aAluguelChoreographer.onDestroy();
        }
    }

    public void stopCommunication() {
        // Stop the Bluetooth chat services
        if (aBTManager != null)
            aBTManager.stop();
        if (testON)
            Log.e(TAG, "--- stopCommunication ---");
        if (aCountConv != null)
            destroyACountAndConverterSafely();
            setupCountAndConverter();

        setAppState(APLICATION_DISCONECTED);

        try{
            mChecker.onDestroy();
        }catch (NullPointerException erro){

        }


    }

    /**
     * Usar somente quando você encerra a conexão com o Bluetooth
     */
    private void destroyACountAndConverterSafely() {
        if(aCountConv != null && aObservable !=null){
            aCountConv.cancel();

            aObservable.Detach(aCountConv);
            aCountConv = null;
        }
    }

    public void configureRatio(int deltaSAfericao, String afericaoNome) throws DBHelper.AfericaoExistenteException {
        if (isTestOn())
            Log.e(TAG, " +++ configureRatio +++");
        // TODO Auto-generated method stub

        try {
            float deltaSSistema;
            float ratioTemp;
            float ratioSis;

            CarStatus carStatusTemp = aObservable.getCarStatus();

            // retrieving deltaS sistem
            deltaSSistema = carStatusTemp.getDeltaStot();

            // retrieving ratio sistem
            ratioSis = carStatusTemp.getAfericao().getRatio();

            // temp Ratio
            ratioTemp = deltaSAfericao / deltaSSistema;

            // math

            ratioSis = ratioSis * ratioTemp;

            try{
                aObservable.setAfericao(new Afericao(afericaoNome, ratioSis));

            }catch (DBHelper.AfericaoExistenteException e){
                Toast.makeText(getApplicationContext(), aResource.getString(R.string.invalido) + " : " + e.getMessage(), Toast.LENGTH_SHORT);
            }

            aObservable.zerar();
        } catch (ArithmeticException e) {
            Toast.makeText(getApplicationContext(),
                    aResource.getString(R.string.config_ratio_zero),
                    Toast.LENGTH_LONG).show();
        }


    }

    public SliderChoreographer getSliderChoreographer() {
        return aSliderChoreographer;
    }

    public List<Afericao> getListaAfericoes() {
        if(dbHelper == null){
            dbHelper = new DBHelper(getApplicationContext(), aObservable);
        }
        return dbHelper.getTodasAfericoes();
    }

    public Afericao getAfericao() {
        CarStatus carStatusTemp = aObservable.getCarStatus();
        return carStatusTemp.getAfericao();
    }

    public int getIndexDeAfericao(Afericao afericao, List<Afericao> afericaoList) {
        Afericao afericaoTemp;
        for(int i =0; i< afericaoList.size() ; i++){
            if (afericao.getName().equals(afericaoList.get(i).getName()))
                return i;
        }

        return -1;
    }

    /**
     * Utilizado para fornecer a referencia do Aluguel Choreographer
     * @return null se a versão do aplicativo não é para aluguel ou a referência do choreographer.
     */
    public AluguelChoreographer getAluguelChoreographer() {
        if(aAluguelChoreographer != null){
            return aAluguelChoreographer;

        }
        else{
            return null;
        }
    }

    public void deleteAfericao(Afericao afericao) {
        if(afericao.getName().equals(getString(R.string.pref_default_ratio_name))){
            Toast.makeText(this, getString(R.string.afericao_padrao_nao_pode_deletar),Toast.LENGTH_SHORT).show();
        }
        dbHelper.deleteActionBox(afericao.getId());
    }

    public List<Afericao> getListaAfericoesUsuario() {

        return  dbHelper.getAfericoesUsuario();
    }

    public void persist(ArrayList<Afericao> itemData) {
        for (Afericao af:itemData){
            dbHelper.updateActionBox(af);

        };
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
        int distance;
        Controller aController;
        private VelocityEng aVelEng;
        private Relogio aRelogioTemp;
        private Observable aObservable;
        private SliderCore aSliderCore;

        // para quando precisar fazer consultas rapidas...
        private MotoristaUsuario aMotoristaUsuarioTemp;

        public CounterAndConverter(Controller ac, Observable aObservable) {

            aController = ac;

            this.aObservable = aObservable;

            //this clock can change the variable CLOCK_STATE;
            aVelEng = new VelocityEng(aObservable.getCarStatus(), aRelogioTemp);
            // TODO Auto-generated constructor stub

            aSliderCore = null;
        }



//        public void resetState() {
//            if (isTestOn())
//                Log.e(TAG, " +++ RESET_STATE +++");
//
//            aRelogioTemp.start();
//
//            // redundant
//            aRelogioTemp.beginBasicTimeCount();
//            this.STATE = STATE_READY;
//            CLOCK_STATE = CLOCK_STATE_COUNTING;
//
//        }

        @Override
        public void run() {
            aRelogioTemp = aObservable.getRelogio();
            long tempClockBasico = aRelogioTemp.getClockBasico();
            // redundant
            aRelogioTemp.beginBasicTimeCount();

            aVelEng.setValues(aObservable.getValues());
            try{
                sleep(tempClockBasico);

            }catch (InterruptedException e){

            }
            STATE = STATE_READY;

            while (STATE != STATE_STOPPED) {
                switch (STATE) {
                    case STATE_READY:

                            aController.query();
                            STATE = STATE_WAITING;
                            try {
                                sleep(tempClockBasico);

                            } catch (InterruptedException e) {
                                if (testON) {
                                    Toast.makeText(getApplicationContext(), "Erro em sleep: " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (isTestOn())
                                Log.e(TAG, " +++ STATE_QUERY +++");

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
                try{


                    // essa verificacao gasta muito em processamnto...
                    aMotoristaUsuarioTemp= aSliderCore.getStatusMotoristaUsuario();
                    if(aMotoristaUsuarioTemp.estaEmNovoTrechoKiZero() ){
                        aController.zerar();
                        aController.setOdometer((int)aMotoristaUsuarioTemp.getdSpercorrido());


                    }
                    aSliderCore.update(0, aVelEng.getDeltaS());
                }catch (NullPointerException e){
                    if(testON){
                        Toast.makeText(getApplicationContext(), "Erro em " + "SliderCoreUpdate: " + e.toString(),Toast.LENGTH_SHORT ).show();
                        throw  e;
                    }

                }

            }

            aObservable.setValues(aVelEng.getValues(), this);

            STATE = STATE_READY;
        }

        protected void cancel() {
            STATE = STATE_STOPPED;

        }

        public void  setSliderCore(SliderCore aSliderCore){
            this.aSliderCore = aSliderCore;
        }

        public void update() {
            aVelEng.setValues(aObservable.getValues());
        }






    }


}




