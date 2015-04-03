package anastasoft.rallyvision.Slider.Prova_Trecho;

import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    /**
     *
     *Retorna o trecho referente à numero fornecido
     *@param numTrecho >= 1
     */

    public Trecho getTrecho(int numTrecho) throws FimDeProvaException {

        try{


            return listaDeTrechos.get(numTrecho -1);
        }catch (RuntimeException e){
            if(numTrecho - 1> listaDeTrechos.size() -1 ){
                throw new FimDeProvaException();
            }
            return listaDeTrechos.get(listaDeTrechos.size() -1);

        }


    }
    public ArrayList<Trecho> toList(){
        return listaDeTrechos;
    }

    public Prova(Cursor c){
        DateFormat fmtSemMilisegundos = new SimpleDateFormat("HH:mm:ss");
        DateFormat fmtComMilisegundos = new SimpleDateFormat("HH:mm:ss.SSS");
        Date aDate1;
        Date aDate2;
        Date aDateBase;

        listaDeTrechos = new ArrayList<Trecho>();

        String tipo;

        c.moveToFirst();

        try {
            Trecho aTrecho = null;
            aDateBase = fmtSemMilisegundos.parse("00:00:00");// nao mexer, usado somente para calcular diferenças



            do {
                tipo = c.getString(c.getColumnIndex(KEY_TIPO));

                if (tipo.equalsIgnoreCase("d")) {

                    aDate1 =  fmtSemMilisegundos.parse(c.getString(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)));
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));

                    aTrecho = new TrechoD(c.getInt(c.getColumnIndex(KEY_NUM)), 1000* c.getFloat(c.getColumnIndex(KEY_KI)), 1000* c.getFloat(c.getColumnIndex(KEY_KF)),(float)(aDate1.getTime() - aDateBase.getTime())   ,  (float)(aDate2.getTime()- aDateBase.getTime()));

                    listaDeTrechos.add(aTrecho);

                }

                if (tipo.equalsIgnoreCase("n")) {
                    aDate1 =  fmtSemMilisegundos.parse(c.getString(c.getColumnIndex(KEY_DTTRECHO_VMEDIO)));
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));
                    aTrecho = new TrechoN(c.getInt(c.getColumnIndex(KEY_NUM)),1000* c.getFloat(c.getColumnIndex(KEY_KI)), 1000* c.getFloat(c.getColumnIndex(KEY_KF)),(float)(aDate1.getTime() - aDateBase.getTime())   ,  (float)(aDate2.getTime()- aDateBase.getTime()));

                    listaDeTrechos.add(aTrecho);

                }
                if (tipo.equalsIgnoreCase("v")) {
                    aDate2 =  fmtComMilisegundos.parse(c.getString(c.getColumnIndex(KEY_TI)));

                    aTrecho = new TrechoV(c.getInt(c.getColumnIndex(KEY_NUM)), 1000*c.getFloat(c.getColumnIndex(KEY_KI)), 1000*c.getFloat(c.getColumnIndex(KEY_KF)), ( (float) 1/3600) *c.getFloat(c.getColumnIndex(KEY_DTTRECHO_VMEDIO))  ,  (float)(aDate2.getTime()- aDateBase.getTime()));
                    listaDeTrechos.add(aTrecho);

                }


            } while (c.moveToNext());

        }catch (Exception erro){
            if (true) {
                Log.e("PROVA", erro.getMessage());
            }
            
        }

    }


}
