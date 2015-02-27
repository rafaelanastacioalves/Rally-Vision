package anastasoft.rallyvision.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;

import anastasoft.rallyvision.Slider.Motorista;
import anastasoft.rallyvision.Slider.MotoristaIdeal;
import anastasoft.rallyvision.Slider.MotoristaUsuario;
import anastasoft.rallyvision.activity.customfontdemo.TypefaceManager;
import anastasoft.rallyvision.activity.dialog.ConfigureDialog;
import anastasoft.rallyvision.activity.dialog.EditDialog;
import anastasoft.rallyvision.activity.dialog.KeepRatioDialog;
import anastasoft.rallyvision.activity.dialog.TimePickerDialogFragment;
import anastasoft.rallyvision.command.CarregarArquivoCommand;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.StopAllCommand;
import anastasoft.rallyvision.command.VerificaAluguelStatusCommand;
import anastasoft.rallyvision.command.Zerar;
import anastasoft.rallyvision.command.startCommand;
import anastasoft.rallyvision.command.stopCommunicationCommand;
import anastasoft.rallyvision.controller.CarStatus;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Data.DBHelper;
import anastasoft.rallyvision.controller.Observable;
import anastasoft.rallyvisionaluguel.R;

@SuppressLint("NewApi")
public class MenuPrincipal extends ActionBarActivity {

    private static final int RESULT_SETTINGS = 1;

    private static final int CAR_STATUS = 0;


    private static final int MOTORISTA_USUARIO = 0;
    private static final int MOTORISTA_IDEAL = 1;

    private static final String TAG = "Menu Principal";

    private odometer mOdom;
    private AVRGvelocimeter mAVGveloc;
    private INSTvelocimeter mINSTveloc;

    private ToggleButton mConnect;
    private ConnectMediator aConnectMediator;

    protected Observable aObervable;

    Controller aController;

    private Command cmd;
    private TextView mStatusText;
    private Button mCheckLicenseButton;


    // Aplication State
    private static final int APLICATION_DISCONECTED = 0;
    private static final int APLICATION_CONNECTING = 1;
    private static final int APLICATION_CONNECTED =2;

    // Notification
    private static       int NOTIFICATION_ID = 10;


    //FileChooserRequest

    private static final int REQUEST_CHOOSER = 1234;

    //Menu Reference

    private boolean isSliderActive ;
    private boolean isAgendamentoInicioProvaSliderActive;

    private SliderMotorista mSLDMotUsr;
    private SliderMotorista mSLDMotIdeal;


//    slider
    /**
     * Usado para manter qual o layout será utilizado.
     */
    private static int layoutResID = R.layout.activity_menu_principal;
    private Menu aOptionsMenuPrincipal;


    /***
     * Chamado quando:
     * - O apliativo inicia;
     * - Quando a tela muda de orientação;
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceManager.initialize(this, R.xml.fonts);

        aController = (Controller) getApplicationContext();

        FragmentManager var = getSupportFragmentManager();
        try {
            aController.setup(this);
        } catch (DBHelper.AfericaoExistenteException e) {
        }
        setContentView(layoutResID);
        setupViews();







    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(layoutResID);
        setupViews();
        aConnectMediator.update();
        try {
            aObervable.Notify();
        } catch (DBHelper.AfericaoExistenteException e) {
            e.printStackTrace();
        }
    }



    /***
     * Chamado quando:
     * - Retomamos o aplicativo;
     * - Voltamos das opções;
     */
    @Override
    protected void onStart() {
        super.onStart();
        cancelNotification();
        if (aOptionsMenuPrincipal != null){
            onPrepareOptionsMenu(aOptionsMenuPrincipal);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.aOptionsMenuPrincipal = menu;
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            MenuItem aMenuItem = menu.findItem(R.id.action_slider_carregar_trecho);
            if(aMenuItem != null){
                aMenuItem.setVisible(isSliderActive);

            }

            aMenuItem = menu.findItem(R.id.action_slider_agendar_prova);
            if(aMenuItem != null){
//                    aMenuItem.setVisible(true);
                aMenuItem.setVisible(isAgendamentoInicioProvaSliderActive);

            }
            return super.onPrepareOptionsMenu(menu);



    }

    @Override
    protected void onResume() {

        super.onResume();
        cmd = new VerificaAluguelStatusCommand(aController);
        cmd.Execute();





    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    new CarregarArquivoCommand(aController,uri).Execute();
                }
                break;
        }





        aController.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_config) {
            ConfigureDialog dFrag = new ConfigureDialog();
            dFrag.show(getSupportFragmentManager(), "change");
            return true;
        }

        if (id == R.id.action_slider_carregar_trecho){
            // Use the GET_CONTENT intent from the utility class
            Intent target = FileUtils.createGetContentIntent();
            // Create the chooser Intent
            Intent intent = Intent.createChooser(
                    target, getString(R.string.slider_carregar_prova_titulo));
            try {
                startActivityForResult(intent, REQUEST_CHOOSER);
            } catch (ActivityNotFoundException e) {
                // The reason for the existence of aFileChooser
            }
            return true;
        }

        if(id == R.id.action_slider_agendar_prova){
            TimePickerDialogFragment newFragment = new TimePickerDialogFragment();
            newFragment.show(getSupportFragmentManager(),"timePicker");
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /***
     * Chamado quando:
     * - muda a orientação da tela;
     * - quando aperta ESC;
     * - quando chamamos as opções;
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(aController.shouldCreateNotification()){
            createNotification();

        }
    }

    /***
     * Chamado quando:
     * - Mudamos a orientação da tela;
     * - Fechamos o aplicativo;
     *TODO Se houver outro momento em que esta funcao eh chamada, anotar!
     */
    @Override
    public void onDestroy() {
        cancelNotification();

        super.onDestroy();

        Command cmd = new StopAllCommand(aController);
        cmd.Execute();

    }

    @Override
    public void onBackPressed() {

        if(aController.shouldCreateNotification()){
            createNotification();

        }

        moveTaskToBack(true);

    }

    private void createNotification() {

        Intent resultIntent = new Intent(this, MenuPrincipal.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notify)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .setContentInfo(getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setOngoing(true).setTicker(getString(R.string.notification_ticker));

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to setState the notification later on.
        Notification notification = mBuilder.build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
    private void cancelNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }



    public void update() {

        ArrayList<Object> lstValues;
        lstValues = aObervable.getValues();
        CarStatus carStatusTemp = (CarStatus)lstValues.get(CAR_STATUS);
        float a, b, c;

        updateDistance((int) carStatusTemp.getDeltaStot());
        updateInstVel((int)  carStatusTemp.getInstantVel());
        updateAvrgVel((int)  carStatusTemp.getAvrgVel());

    }


    public void keepScreenOn(boolean on){
        if (on){
            if (on)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            else
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }



    public void updateDistance(int distance) {
        if (aController.isTestOn()) {
            Log.e(TAG, " +++ updateDistance +++ ");
        }

        String number = String.valueOf(distance);

        switch (number.length()) {
            case 1:
                number = "0000" + number;
                break;
            case 2:
                number = "000" + number;
                break;
            case 3:
                number = "00" + number;
                break;
            case 4:
                number = "0" + number;
                break;
            default:
                break;
        }
        char[] digit = number.toCharArray();

        mOdom.TVdig1.setText(String.valueOf(digit[4]));
        mOdom.TVdig2.setText(String.valueOf(digit[3]));
        mOdom.TVdig3.setText(String.valueOf(digit[2]));
        mOdom.TVdig4.setText(String.valueOf(digit[1]));
        mOdom.TVdig5.setText(String.valueOf(digit[0]));

    }

    public void updateAvrgVel(Integer av) {
        if (aController.isTestOn()) {
            Log.e(TAG, " +++ updateAvrgVel +++ ");
        }

        String number = String.valueOf(av);

        switch (number.length()) {
            case 1:
                number = "00" + number;
                break;
            case 2:
                number = "0" + number;
                break;
            default:
                break;
        }
        char[] digit = number.toCharArray();

        mAVGveloc.TVdig1.setText(String.valueOf(digit[2]));
        mAVGveloc.TVdig2.setText(String.valueOf(digit[1]));
        mAVGveloc.TVdig3.setText(String.valueOf(digit[0]));
    }

    public void updateInstVel(Integer iv) {
        if (aController.isTestOn()) {
            Log.e(TAG, " +++ updateInstVel +++ ");
        }

        String number = String.valueOf(iv);

        switch (number.length()) {
            case 1:
                number = "00" + number;
                break;
            case 2:
                number = "0" + number;
                break;
            default:
                break;
        }
        char[] digit = number.toCharArray();

        mINSTveloc.TVdig1.setText(String.valueOf(digit[2]));
        mINSTveloc.TVdig2.setText(String.valueOf(digit[1]));
        mINSTveloc.TVdig3.setText(String.valueOf(digit[0]));
    }

    public void updateTrechoMotorista(SliderMotorista aSlider, String tipoTrecho ,int numTrecho){

        if (aController.isTestOn()) {
            Log.e(TAG, " +++ updateInstVel +++ ");
        }

        String number = String.valueOf(numTrecho);

        switch (number.length()) {
            case 1:
                number = "00" + number;
                break;
            case 2:
                number = "0" + number;
                break;
            default:
                break;
        }
        char[] digit = number.toCharArray();

        aSlider.TVSliderNumTrechodig1.setText(String.valueOf(digit[2]));
        aSlider.TVSliderNumTrechodig2.setText(String.valueOf(digit[1]));
        aSlider.TVSliderNumTrechodig3.setText(String.valueOf(digit[0]));

        aSlider.TVSliderTipoTrecho.setText(tipoTrecho);
    }

    public void setObservable(Observable observable) {

        this.aObervable = observable;

        aObervable.Attach(this);
    }

    public void startProcedure(View v) {
            cmd = new startCommand(this, getOdometerValue());
            cmd.Execute();


    }

    public void stopCom(View v){
        cmd = new stopCommunicationCommand(this);
        cmd.Execute();
    }

    public void zerar(View v) {
        cmd = new Zerar(aController);
        cmd.Execute();
    }

    public void editar(View v) {
        EditDialog dFrag = new EditDialog();
        dFrag.show(getSupportFragmentManager(), "change");
    }

    public int getOdometerValue() {
        String sValue = "";
        sValue += mOdom.TVdig5.getText();
        sValue += mOdom.TVdig4.getText();
        sValue += mOdom.TVdig3.getText();
        sValue += mOdom.TVdig2.getText();
        sValue += mOdom.TVdig1.getText();

        int intValue = Integer.parseInt(sValue);
        return intValue;
    }
    public Button getConnectButton(){
        mConnect = (ToggleButton) findViewById(R.id.executar);

        return mConnect;
    }

    public void makeConnectBGreen(){
        LinearLayout mConnectLayout = (LinearLayout)findViewById(R.id.ConnecLayout);

        mConnectLayout.removeAllViews();

        // Create new LayoutInflater - this has to be done this way, as you can't directly inflate an XML without creating an inflater object first
        LayoutInflater inflater = getLayoutInflater();
        mConnectLayout.addView(inflater.inflate(R.layout.connect_button_green, null));

    }
    public void makeConnectBOrange(){
        LinearLayout mConnectLayout = (LinearLayout)findViewById(R.id.ConnecLayout);

        mConnectLayout.removeAllViews();

        // Create new LayoutInflater - this has to be done this way, as you can't directly inflate an XML without creating an inflater object first
        LayoutInflater inflater = getLayoutInflater();
        mConnectLayout.addView(inflater.inflate(R.layout.connect_button_orange, null));

    }
    public void makeConnectBRed(){
       LinearLayout mConnectLayout = (LinearLayout)findViewById(R.id.ConnecLayout);
        mConnectLayout.removeAllViews();

        // Create new LayoutInflater - this has to be done this way, as you can't directly inflate an XML without creating an inflater object first
        LayoutInflater inflater = getLayoutInflater();
        mConnectLayout.addView(inflater.inflate(R.layout.connect_button_red, null));


    }

    private void setupViews() {



            mOdom = new odometer();
            mINSTveloc = new INSTvelocimeter();
            mAVGveloc = new AVRGvelocimeter();

            mSLDMotIdeal  = new SliderMotorista();
            mSLDMotUsr = new SliderMotorista();


            // odometer
            mOdom.TVdig1 = (TextView) findViewById(R.id.ODdig01);
            mOdom.TVdig2 = (TextView) findViewById(R.id.ODdig02);
            mOdom.TVdig3 = (TextView) findViewById(R.id.ODdig03);
            mOdom.TVdig4 = (TextView) findViewById(R.id.ODdig04);
            mOdom.TVdig5 = (TextView) findViewById(R.id.ODdig05);

            // instant velocimeter

            mINSTveloc.TVdig1 = (TextView) findViewById(R.id.InstVelDig01);
            mINSTveloc.TVdig2 = (TextView) findViewById(R.id.InstVelDig02);
            mINSTveloc.TVdig3 = (TextView) findViewById(R.id.InstVelDig03);

            // average velocimeter
            mAVGveloc.TVdig1 = (TextView) findViewById(R.id.AVRGVelDig01);
            mAVGveloc.TVdig2 = (TextView) findViewById(R.id.AVRGVelDig02);
            mAVGveloc.TVdig3 = (TextView) findViewById(R.id.AVRGVelDig03);

            // Sliders
            mSLDMotUsr.TVSliderTipoTrecho =     (TextView) findViewById(R.id.tipoTrechoMotoristaUsuario);
            mSLDMotUsr.TVSliderNumTrechodig1 =  (TextView) findViewById(R.id.numTrechoMotoristaUsuarioDig1);
            mSLDMotUsr.TVSliderNumTrechodig2 =  (TextView) findViewById(R.id.numTrechoMotoristaUsuarioDig2);
            mSLDMotUsr.TVSliderNumTrechodig3 =  (TextView) findViewById(R.id.numTrechoMotoristaUsuarioDig3);
            mSLDMotUsr.PBSliderPercent = (ProgressBar) findViewById(R.id.progressBarMotoristaUsuário);

            mSLDMotIdeal.TVSliderTipoTrecho = (TextView) findViewById(R.id.tipoTrechoMotoristaIdeal);
            mSLDMotIdeal.TVSliderNumTrechodig1 = (TextView) findViewById(R.id.numTrechoMotoristaIdealDig1);
            mSLDMotIdeal.TVSliderNumTrechodig2 = (TextView) findViewById(R.id.numTrechoMotoristaIdealDig2);
            mSLDMotIdeal.TVSliderNumTrechodig3 = (TextView) findViewById(R.id.numTrechoMotoristaIdealDig3);


        mSLDMotIdeal.PBSliderPercent = (ProgressBar) findViewById(R.id.progressBarMotoristaIdeal);




        // edit button

        Button edButton = (Button) findViewById(R.id.editar);
        edButton.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                editar(v);
                return true;
            }
        });
        Button zeraButton = (Button) findViewById(R.id.zera);
        zeraButton.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                zerar(v);
                return true;
            }
        });






    }

    public void askKeepRatio() {
        // TODO Auto-generated method stub
        KeepRatioDialog dFrag = new KeepRatioDialog();
        dFrag.show(getSupportFragmentManager(), "keep");
    }

    private void hideActionBar() {
        if (Build.VERSION.SDK_INT < 16) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

        } else {


            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            actionBar.hide();
        }
    }

    public void displayResult(final String result) {
        aController.getHandler().post(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "LICENSA: " + result, Toast.LENGTH_SHORT);
//                toast.show();
            }
        });
    }

    public void displayDialog(final boolean showRetry) {
        aController.getHandler().post(new Runnable() {
            public void run() {
                setProgressBarIndeterminateVisibility(false);
                showDialog(showRetry ? 1 : 0);
                mCheckLicenseButton.setEnabled(true);
            }
        });
    }

//    public void mostrarTelaBemVindo() {
//
//        BemVindoAluguelDialog bemVindoAluguelDialog = new BemVindoAluguelDialog();
//
//
//        bemVindoAluguelDialog.show(getSupportFragmentManager(), "change");
//    }




    public void update(ArrayList<Motorista> motoristasStatus) {

        try{

            int progress;

        updateTrechoMotorista(mSLDMotIdeal, (motoristasStatus.get(MOTORISTA_IDEAL))
                                            .getTipoTrecho(),(motoristasStatus.get(MOTORISTA_IDEAL))
                                                             .getNumTrecho());

        updateTrechoMotorista(mSLDMotUsr, (motoristasStatus.get(MOTORISTA_USUARIO))
                    .getTipoTrecho(),(motoristasStatus.get(MOTORISTA_USUARIO))
                    .getNumTrecho());


        progress =  (int)  (((float)(((MotoristaUsuario)motoristasStatus.get(MOTORISTA_USUARIO)).
                getPercentPercorrido()))*100.0);
        mSLDMotUsr.PBSliderPercent.setProgress(progress);


        progress = (int)  (((float)(((MotoristaIdeal)motoristasStatus.get(MOTORISTA_IDEAL)).
                getPercentPercorrido()))*100.0);
            mSLDMotIdeal.PBSliderPercent.setProgress(progress);
        }catch (Exception erro){
            if (aController.isTestOn()) {
                Log.e(TAG, erro.getMessage());
            }
        }

    }



    public void setUpSliders(boolean on) {
        if(on){
            isSliderActive =true;
            layoutResID = R.layout.activity_menu_principal_sliders;
        }else{
            isSliderActive = false;
            layoutResID = R.layout.activity_menu_principal;
        }

    }
    public void setAgendarInicioProvaSlider(boolean on){
        if(on){
            isAgendamentoInicioProvaSliderActive = true;

        }else {
            isAgendamentoInicioProvaSliderActive = false;
        }
        ;
    }


    public void setaConnectMediator(ConnectMediator aCM){
        this.aConnectMediator = aCM;
    }



}


class odometer {

    TextView TVdig1;
    TextView TVdig2;
    TextView TVdig3;
    TextView TVdig4;
    TextView TVdig5;

}

class AVRGvelocimeter {

    TextView TVdig1;
    TextView TVdig2;
    TextView TVdig3;

}

class INSTvelocimeter {

    TextView TVdig1;
    TextView TVdig2;
    TextView TVdig3;
}

class SliderMotorista {

    TextView            TVSliderTipoTrecho;
    TextView            TVSliderNumTrechodig1;
    TextView            TVSliderNumTrechodig2;
    TextView            TVSliderNumTrechodig3;
    ProgressBar         PBSliderPercent;
}


