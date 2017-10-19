package anastasoft.rallyvision.controller.SliderCoreographer;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rafaelanastacioalves on 22/01/15.
 */
public class FileParser {



            // colunas para cursor a ser usado no cursor de entrada para construir a prova no slider
            private static final String KEY_TIPO = "tipo";
            private static final String KEY_NUM = "numero";
            private static final String KEY_KI = "ki";
            private static final String KEY_KF = "kf";
            private static final String KEY_DTTRECHO_VMEDIO = "dttrecho_vmmedio";
            private static final String KEY_TI = "ti";

            private String[] listaColunas = {KEY_TIPO, KEY_NUM, KEY_KI, KEY_KF, KEY_DTTRECHO_VMEDIO, KEY_TI};

            private String pathAccessed;


            private ArrayList<String[]> finalList;



    public FileParser(){

            finalList = new ArrayList<String[]>();

            }


            public Cursor getProvaCursorFrom(String path){
                return convertToCursor(
                            loadFromPath(path)
                                            );
            }

            private ArrayList<String[]> loadFromPath( String path) {
                this.pathAccessed = path;
                try{
                    loadWords();

                }catch (IOException e){
                    throw new RuntimeException(e);
                }
//                new Thread(new Runnable() {
//
//
//                    public void run() {
//                        try {
//                            loadWords();
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }).start();

                return finalList;

            }


            private void loadWords() throws IOException {
                //Find the directory for the SD Card using the API
                //*Don't* hardcode "/sdcard"
                File sdcard = Environment.getExternalStorageDirectory();

                finalList.clear();
                //Get the text file
                File file = new File(pathAccessed);
                BufferedReader reader = new BufferedReader(new FileReader(file));



                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        String[] strings = line.split("\\s+");
                        if (estaTudoNosConformes(strings)){
                            strings =separaTipoNumero(strings);
                            finalList.add(strings);

                        }

                    }
                } finally {
                    reader.close();
                }

            }

    private String[] separaTipoNumero(String[] strings) {
        String[] finalString = new String[strings.length + 1];
        finalString[0] = strings[0].substring(0,1); // fornecendo o tipo, que eh a primeira letra
        finalString[1] = strings[0].substring(1); // fornecendo o numero do trecho, que é o restante a partir do segundo elemento do array
        for(int i = 1; i< strings.length; i++ ){
            finalString[i+1] = strings[i];
        }

        return finalString;
    }



    private boolean estaTudoNosConformes(String[] strings) {
            String sTest = new String(strings[0].substring(0,1).trim());

           if(sTest.contains("D") || sTest.contains("N") || sTest.contains("V")){
               return true;
           }


            return false;
    }

    private MatrixCursor convertToCursor(ArrayList<String[]> string){
        MatrixCursor c = new MatrixCursor(listaColunas);

        for (String[] tempString : string){
            c.addRow(tempString);
        }
        return c;
    }






}
