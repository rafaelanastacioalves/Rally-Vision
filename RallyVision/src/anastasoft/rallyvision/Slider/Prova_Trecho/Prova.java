package anastasoft.rallyvision.Slider.Prova_Trecho;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.TrechoD;

/**
 * Created by rafaelanastacioalves on 20/01/15.
 */
public class Prova {

    private static final String KEY_TIPO = "tipo";
    private static final String KEY_NUM = "numero";
    private static final String KEY_KI = "ki";
    private static final String KEY_KF = "kf";
    private static final String KEY_DTTRECHO_VMEDIO = "dttrecho_vmmedio";
    private static final String KEY_TI = "ti";
    private ArrayList<Trecho> listaDeTrechos;


    public Trecho getTrecho(int numTrecho){
        return listaDeTrechos.get(numTrecho -1);
    }

    public Prova(Cursor c){
        DateFormat fmtSemMilisegundos = new SimpleDateFormat("hh:mm:ss");
        DateFormat fmtComMilisegundos = new SimpleDateFormat("hh:mm:ss.SS");
        Date aDate1;
        Date aDate2;

        listaDeTrechos = new ArrayList<Trecho>();

        String tipo;

        c.moveToFirst();
        tipo = c.getString(c.getColumnIndex(KEY_TIPO));

        try {
            Trecho aTrecho = null;



            do {
                if (tipo.equalsIgnoreCase("d")) {

                    aDate1 =  fmtSemMilisegundos.parse(c.getString(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)));
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));

                    aTrecho = new TrechoD(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)),(float)aDate1.getTime()   ,  (float)aDate2.getTime());

                    listaDeTrechos.add(aTrecho);

                }

                if (tipo.equalsIgnoreCase("n")) {
                    aDate1 =  fmtSemMilisegundos.parse(c.getString(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)));
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));
                    aTrecho = new TrechoD(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)),(float)aDate1.getTime()   ,  (float)aDate2.getTime());

                    listaDeTrechos.add(aTrecho);

                }
                if (tipo.equalsIgnoreCase("v")) {
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));

                    aTrecho = new TrechoD(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)),c.getFloat(c.getColumnIndex(KEY_DTTRECHO_VMEDIO))  ,  (float)aDate2.getTime());
                    listaDeTrechos.add(aTrecho);

                }


            } while (c.moveToNext());

        }catch (Exception error){
            
        }

    }


}
