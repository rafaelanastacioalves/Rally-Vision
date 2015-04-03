package anastasoft.rallyvision.controller;

/**
 * Created by rafaelanastacioalves on 16/09/14.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import anastasoft.rallyvision.Slider.SliderCore;


public class Relogio  {
    private Date tInicialBasico;
    private long clockBasico;


    private Controller aController;
    private BroadcastReceiver br;

    private AlarmManager am;


    private Calendar horarioInicioSlider;
    private float    dTTotalSlider;
    private CountDownTimer aCountDownTimerSlider;
    private SliderCore aSliderCore;
    private Calendar sliderTimeStamp;
    private Calendar sliderTimeStampTemp;
    private long deltaTSliderTemp;
    private Observable aObservable;
    private boolean sliderRodando;


    public Relogio(long clockBasico, Controller aController) {
        this.clockBasico = clockBasico;
        this.aController = aController;
        tInicialBasico = new Date();
        sliderTimeStampTemp = Calendar.getInstance();
        sliderRodando = false;
    }


    /**
     * Usado somente quando resetamos hodômetro e velocímetros
     * ou quando utilizamos eles pela primeira vez no CountAndConverter.run()
     */
    public void beginBasicTimeCount() {
        tInicialBasico.setTime(System.currentTimeMillis());
    }




    public long getDeltaT() {
        return this.clockBasico;
    }

    /**
     * Usado internamente para ficar dando incrementos de dT corrigidos desde o momento referente
     * ao horário de início da prova
     * @return long referente ao incremento de dT a cada clock
     */
    public long getDeltaTslider(){

        sliderTimeStampTemp.setTimeInMillis(System.currentTimeMillis());
        deltaTSliderTemp = sliderTimeStampTemp.getTimeInMillis() - sliderTimeStamp.getTimeInMillis();

        
        this.sliderTimeStamp.setTime( sliderTimeStampTemp.getTime());
        return deltaTSliderTemp;
    }

    public long getDeltaTTotBasico() {
        return System.currentTimeMillis() - tInicialBasico.getTime();
    }

    public boolean isReady() {
        return (getDeltaT() >= clockBasico);
    }

    public boolean isSliderRunning(){
        return sliderRodando;
    }

    public void resetBasico() {
        beginBasicTimeCount();
    }

    public long getClockBasico() {
        return clockBasico;
    }

    public void setarAlarmeSlider(Calendar horarioInicioSlider, final SliderCore aSliderCore){
        this.horarioInicioSlider = (Calendar)horarioInicioSlider.clone();
        this.sliderTimeStamp = (Calendar) horarioInicioSlider.clone();
        this.aSliderCore = aSliderCore;

            // primeiro verificamos se essa hora já começou
        if(horarioInicioSlider.getTimeInMillis() < System.currentTimeMillis()){
            comecarFuncionamentoComSliders(aSliderCore);

        }else {
            // agenda para começar na hora marcada
            PendingIntent pi;
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context c, Intent i) {
                    aController.getSliderChoreographer().startProvaSlider();
                    if (aController.isTestOn()) {
                        Toast.makeText(aController.getApplicationContext(), "Relogio funcionando", Toast.LENGTH_SHORT).show();
                    }
                    comecarFuncionamentoComSliders(aSliderCore);

                }
            };
            aController.registerReceiver(br, new IntentFilter("anastasoft.rallyvision"));
            pi = PendingIntent.getBroadcast(aController.getCurrentActiviy(), 0, new Intent("anastasoft.rallyvision"),
                    0);
            am = (AlarmManager)(aController.getSystemService( Context.ALARM_SERVICE ));
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
                am.set(AlarmManager.RTC,horarioInicioSlider.getTimeInMillis(),pi);
            }
            else{
                am.setExact( AlarmManager.RTC, horarioInicioSlider.getTimeInMillis(), pi );
            }
        }





    }

    /**
     * Usado para quando editamos a posição no slider
     */
    public void reSetarSlider(){
        aCountDownTimerSlider.cancel();
        Calendar temp = horarioInicioSlider;
        sliderTimeStampTemp = Calendar.getInstance();
        setarAlarmeSlider(temp, aSliderCore);
    }

    private void comecarFuncionamentoComSliders(final SliderCore aSliderCore) {
        aCountDownTimerSlider =  new CountDownTimer(2*24*3600*1000, clockBasico) {

            public void onTick(long millisUntilFinished) {
                aSliderCore.update(getDeltaTslider(),0);
                aObservable.Notify(aSliderCore.getStatus());

            }

            public void onFinish() {
                aController.stopCommunication();
                sliderRodando = false;
            }
        }.start();
        this.sliderRodando = true;

    }



    public void setaObservable(Observable aObservable) {
        this.aObservable = aObservable;
    }
}
