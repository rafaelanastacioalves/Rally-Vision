package anastasoft.rallyvision.controller.InAppBilling;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import anastasoft.rallyvision.activity.EscolheAluguel;
import anastasoft.rallyvision.activity.MenuPrincipal;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.R;

/**
 * Created by rafaelanastacioalves on 20/02/15.
 * Serve para intermediar a interação entre as partes de forma a prover o serviço de alguel. Qualquer interação com a API do App Billing
 * deve ser feita no AluguelAdapter
 * @see AluguelAdapter
 */
public class AluguelChoreographer {



    private static final String TAG = "AluguelChoreographer" ;
    /**
           Não pode coincidir com nenhum das outras constantes do Controller!
     */
    private static final int REQUEST_ESCOLHER_ALUGUEL = 3;
    private Controller aController;
    private static AluguelAdapter aAluguelAdapter;


    private static int STATE;
    private static final int  STATE_INICIALIZANDO = -1;
    private static final int  STATE_SEM_ALUGUEL = 0;
    private static final int  STATE_ESCOLHENDO = 1;
    private static final int  STATE_COM_ALUGUEL = 2;

    private static String BASE64_PUBLIC_KEY_ALUGUEL ;
    private static ArrayList<String> listaDeCompras;
    private EscolheAluguel aEscolheAluguelActivity;


    public AluguelChoreographer(Controller aController, String public_key){


        this.aController = aController;
        this.BASE64_PUBLIC_KEY_ALUGUEL = public_key;
        aAluguelAdapter = new AluguelAdapter(aController,this);

        setState(STATE_INICIALIZANDO);
        inicializar();


    }

    public static ArrayList<String> getListaDeCompras() {
        return aAluguelAdapter.getListadeComprasSKU();
    }

    public void encerrar(){

    }
    public void inicializar(){


        aAluguelAdapter.inicializar(this.BASE64_PUBLIC_KEY_ALUGUEL);

    }

    private void mostrarTelaBemVindo() {

        try{
            MenuPrincipal aMenuPrincipal = (MenuPrincipal)aController.getCurrentActiviy();


            Intent serverIntent = new Intent(aMenuPrincipal, EscolheAluguel.class);



            // finishing
            aMenuPrincipal.startActivityForResult(serverIntent, REQUEST_ESCOLHER_ALUGUEL);
        }catch (Exception e){

        }


    }


    public boolean aluguelOn(){
        return true;
    }


    public void complain(String s) {

        try{
            Toast.makeText(aController.getApplicationContext(),"Erro: " + s,Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }

    }

    public void inicializacaoTerminada() {

    }

    private void setState(int state){

        this.STATE = state;
        verificaAluguel();
    }

    private int getSTATE(){
        return this.STATE;
    }

    public void recupInventorioTerminado(String inventorio) {

        if (aController.isTestOn()) {
            Toast.makeText(aController.getApplicationContext(), "Inventorio: " + inventorio, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verifica o aluguel e toma ação caso não tenha nenhum registro
     * Caso já tenha alguel, no momento da verificação, encerra a atividade
     * relacionada a aluguel.
     * Note que a cada mudança de estado da verificação precisa ser registrada e
     * esta função precisa ser chamada em seguida para poder tomar a devida ação.
     */
    public void verificaAluguel() {

        switch (STATE){

            case STATE_SEM_ALUGUEL:
                    mostrarTelaBemVindo();
//                    aEscolheAluguelActivity.mostraParaEscolher();


                break;


            case STATE_COM_ALUGUEL:
                try{
                    Toast.makeText(aController.getApplicationContext(), aController.getResources().getString(R.string.obrigado_utilizar)+"!" ,Toast.LENGTH_SHORT).show();

                    aEscolheAluguelActivity.finish();
                }catch(NullPointerException e){

                }
                break;


        }

    }

    /**
     * Desativa apropriadamente as partes envolvidas no Aluguel
     */
    public void onDestroy(){
        aAluguelAdapter.encerrarAtividades();

    }

    /**
     * Solicita o Google PLay para realizar uma transação proveniente do index da lista de compras escolhido
     * @param aEscolheAluguel - a atividade qu deveria estar requisistando a compra. Por enquanto,
     *                        somente a Escolhe Aluguel
     * @param index - representa o index da lista de compras recuperadas pelo sistema
     */

    public void comprar(Activity aEscolheAluguel, int index) {
        aAluguelAdapter.realizarTransacaoUsando(aEscolheAluguel,index);

    }

    public void temAluguel(boolean temAluguel) {
        if(temAluguel){
            setState(STATE_COM_ALUGUEL);

        }else{
            setState(STATE_SEM_ALUGUEL);

        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        aAluguelAdapter.handleActivityResult(requestCode,resultCode, data);
    }

    /**
     * Olha o estado atual do aluguel e retorna true se já tiver confirmado que já tem uma assinatura.
     * Note que ele olha o estado atual. Não fazer nenhuma nova query...
     * @return
     */
    public boolean temAluguel() {

        if (STATE == STATE_COM_ALUGUEL){
            return true;
        }
        return false;
    }

    public void setActivity(EscolheAluguel activity) {
        this.aEscolheAluguelActivity = activity;
    }

    public EscolheAluguel getaEscolheAluguelActivity() {
        return aEscolheAluguelActivity;
    }

    public void setaEscolheAluguelActivity(EscolheAluguel aEscolheAluguelActivity) {
        this.aEscolheAluguelActivity = aEscolheAluguelActivity;
    }

    public boolean statusSemAluguelConfirmado() {
        if(STATE == STATE_SEM_ALUGUEL){
            return true;
        }
        return false;
    }
}
