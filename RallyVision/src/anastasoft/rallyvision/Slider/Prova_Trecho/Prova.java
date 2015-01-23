package anastasoft.rallyvision.Slider.Prova_Trecho;

import android.database.Cursor;

import java.util.ArrayList;

import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.TrechoD;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.TrechoN;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.TrechoV;

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

        listaDeTrechos = new ArrayList<Trecho>();

        String tipo;
        tipo = c.getString(c.getColumnIndex(KEY_TIPO));

        try {
            Trecho aTrecho = null;



            do {
                if (tipo.equals("d")) {
                    aTrecho = new TrechoD(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)), c.getFloat(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)), c.getFloat(c.getColumnIndex(KEY_TI)));
                }

                if (tipo.equals("n")) {
                    aTrecho = new TrechoN(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)), c.getFloat(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)), c.getFloat(c.getColumnIndex(KEY_TI)));
                }
                if (tipo.equals("v")) {
                    aTrecho = new TrechoV(c.getInt(c.getColumnIndex(KEY_NUM)), c.getFloat(c.getColumnIndex(KEY_KI)), c.getFloat(c.getColumnIndex(KEY_KF)), c.getFloat(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)), c.getFloat(c.getColumnIndex(KEY_TI)));
                }

                listaDeTrechos.add(aTrecho);

            } while (c.moveToNext());

        }catch (Exception error){
            
        }


    }

}
