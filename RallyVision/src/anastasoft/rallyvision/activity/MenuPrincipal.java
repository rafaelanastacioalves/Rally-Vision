package anastasoft.rallyvision.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ipaulpro.afilechooser.FileChooserActivity;

import java.util.List;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.activity.customfontdemo.TypefaceManager;
import anastasoft.rallyvision.activity.dialog.ConfigureDialog;
import anastasoft.rallyvision.activity.dialog.EditDialog;
import anastasoft.rallyvision.activity.dialog.KeepRatioDialog;
import anastasoft.rallyvision.activity.dialog.NotConfigureDialog;
import anastasoft.rallyvision.command.CarregarArquivoCommand;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.Zerar;
import anastasoft.rallyvision.command.startCommand;
import anastasoft.rallyvision.command.stopCommunicationCommand;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Observable;

@SuppressLint("NewApi")
public class MenuPrincipal extends ActionBarActivity {

    private static final int RESULT_SETTINGS = 1;

    private static final int DISTANCE = 0;
    private static final int INST_VEL = 1;
    private static final int AVRG_VEL = 2;

    private static final String TAG = "Menu Principal";

    private odometer mOdom;
    private AVRGvelocimeter mAVGveloc;
    private INSTvelocimeter mINSTveloc;
    private ToggleButton mConnect;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceManager.initialize(this, R.xml.fonts);

        aController = (Controller) getApplicationContext();

        setContentView(R.layout.activity_menu_principal);


        setupViews();
        aController.setup(this);





    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cancelNotification();

    }

    public void warnNotCalibrated() {
        NotConfigureDialog dFrag = new NotConfigureDialog();
        dFrag.show(getSupportFragmentManager(), "not_calibrated");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
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

        aController.actionAfterBlueTooth(requestCode, resultCode, data);
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
            Intent i = new Intent(this, FileChooserActivity.class);
            startActivityForResult(i, REQUEST_CHOOSER);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(aController.shouldCreateNotification()){
            createNotification();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Command cmd = new stopCommunicationCommand(aController);
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
                        .setOngoing(true).setTicker(getString(R.string.notification_ticker));

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    private void cancelNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }



    public void update() {

        List<Float> lstValues;
        lstValues = aObervable.getValues();
        float a, b, c;
        a = lstValues.get(DISTANCE);
        b = lstValues.get(INST_VEL);
        c = lstValues.get(AVRG_VEL);
        updateDistance((int) a);
        updateInstVel((int) b);
        updateAvrgVel((int) c);

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

