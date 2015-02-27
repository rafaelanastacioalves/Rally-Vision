package anastasoft.rallyvision.controller.InAppBilling;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.InAppBilling.util.IabHelper;
import anastasoft.rallyvision.controller.InAppBilling.util.IabResult;
import anastasoft.rallyvision.controller.InAppBilling.util.Inventory;
import anastasoft.rallyvision.controller.InAppBilling.util.Purchase;
import anastasoft.rallyvisionaluguel.R;

/**
 * Created by rafaelanastacioalves on 21/02/15.
 * Servirá como uma interface simplificada das chamadas básicas necessárias para as regras de negócio
 */
public class AluguelAdapter {

    private Controller aController;
    private AluguelChoreographer aAluguelChoreographer;
    private IabHelper mHelper;
    private static final String TAG = "AluguelAdapter";

    private ArrayList<String> listadeComprasSKU;
    private ArrayList<String> listadeComprasNOMES;


    private String ALUGUEL_MENSAL_NOME = "Assinatura Mensal";
    private static final String ALUGUEL_MENSAL_SKU = "aluguel_mensal";
    private int ALUGUEL_MENSAL_INDEX = 0;

    private String COMRA_TESTE_NOME = "Compra Teste";
    private static final String COMPRA_TESTE_SKU = "android.test.purchased";
    // (arbitrary) request code for the purchase flow
    private int COMPRA_TESTE_INDEX = 1;


    static final int RC_REQUEST = 10001;


    private Inventory aInventory;


    public AluguelAdapter(Controller aController, AluguelChoreographer aAluguelChoreographer) {

        this.aController = aController;

        this.aAluguelChoreographer = aAluguelChoreographer;

        this.listadeComprasSKU = new ArrayList<String>();
        this.listadeComprasNOMES = new ArrayList<String>();

        listadeComprasSKU.add(ALUGUEL_MENSAL_SKU);
        listadeComprasNOMES.add(ALUGUEL_MENSAL_NOME);

        if (aController.isTestOn() || aController.isUberTestOn()) {
            listadeComprasSKU.add(COMPRA_TESTE_SKU);
            listadeComprasNOMES.add(COMRA_TESTE_NOME);
        }
    }


    /**
     * Deixa carregando o Helper do App Billing e deixa a aplicação sem poder mexer enquando não retornar da inicialização
     * promovida pelo Helper...
     * Quando o Helper retorna ele tira do bloqueio e exibe as opções disponíveis.
     */
    public void inicializar(String base64EncodedPublicKey) {
        if (aController.isTestOn()) {
            Log.i(TAG, "INICIALIZAR");
        }
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(aController.getApplicationContext(), base64EncodedPublicKey);

//        enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(aController.isTestOn());

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (aController.isTestOn()) {
                    Log.i(TAG, "Setup finished");
                }


                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    aAluguelChoreographer.complain(aController.getResources().getString(R.string.aluguel_inicializar_problema) +" "+ result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                if (aController.isTestOn()) {
                    Log.i(TAG, "Setup successful. Querying inventory.");
                }

                mHelper.queryInventoryAsync(mGotInventoryListener);

            }
        });


    }

    /**
     * Realiza a transação baseado no index fornecido,
     * que é de uso interno, dado a lista de compras recuperada
     *
     * @param aEscolheAluguel
     * @param index
     */
    public void realizarTransacaoUsando(Activity aEscolheAluguel, int index) {
        String nomeSKU = listadeComprasSKU.get(index);
        if (!mHelper.subscriptionsSupported()) {
            aAluguelChoreographer.complain(aController.getString(R.string.aluguel_assinatura_nao_compativel));
            return;
        }

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        if (aController.isTestOn()) {
            Log.i(TAG, "Launching purchase flow for infinite gas subscription.");
        }

        if (nomeSKU.equals(COMPRA_TESTE_SKU)) {
            mHelper.launchPurchaseFlow(aEscolheAluguel,
                    nomeSKU, IabHelper.ITEM_TYPE_INAPP,
                    RC_REQUEST, mPurchaseFinishedListener, payload);
        }
        if (nomeSKU.equals(ALUGUEL_MENSAL_SKU)) {
            mHelper.launchPurchaseFlow(aEscolheAluguel,
                    nomeSKU, IabHelper.ITEM_TYPE_SUBS,
                    RC_REQUEST, mPurchaseFinishedListener, payload);

        }

    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            /* have a backup of the invetory for future retrievement. @see recupInventorioTerminado */
            setInventory(inventory);


            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                aAluguelChoreographer.complain(aController.getString(R.string.aluguel_assinatura_impossivel_carregar_opcoes_compra) +" " +  result);
                return;
            }/**
             * Retorna os itens disponíveis para compra.
             * Depois o ítem escolhido servirá de referencia para poder chamar a atividade responsável por confirmar o pagamento em outro momento.
             */

            if (aController.isTestOn()) {
                Log.i(TAG, "Query inventory was successful.");
            }
            

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            boolean temAssinaturaMensal;
            Purchase assinaturaMensal = inventory.getPurchase(ALUGUEL_MENSAL_SKU);
            temAssinaturaMensal = (assinaturaMensal != null &&
                    verifyDeveloperPayload(assinaturaMensal));

            boolean temProdutoTeste = false;
            if(aController.isTestOn() || aController.isUberTestOn()) {
                Purchase produtoTeste = inventory.getPurchase(COMPRA_TESTE_SKU);
                temProdutoTeste = (produtoTeste != null &&
                        verifyDeveloperPayload(produtoTeste));
            }
            /*
            no fim de toda a verificação, vamos avisar ao choreographer
            se o usuário tem aluguel ou não
            */
            aAluguelChoreographer.temAluguel(temAssinaturaMensal || temProdutoTeste);

            if (aController.isTestOn()) {
                Log.i(TAG, "User " + (temAssinaturaMensal ? "HAS" : "DOES NOT HAVE")
                        + " aluguel_mensal_teste.");
                aAluguelChoreographer.recupInventorioTerminado(String.valueOf(temAssinaturaMensal));
                Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            }


//
//            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase gasPurchase = inventory.getPurchase("aluguel_mensal_teste");
//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
//                return;
//            }
//
//            updateUi();
//            setWaitScreen(false);



        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                aAluguelChoreographer.complain(aController.getString(R.string.aluguel_assinatura_erro_realizar_compra) +" "+ result);

                // conferencia a ser utilizado somente em static responses
                try {
                    if (aController.isTestOn() && purchase.getSku().equals(COMRA_TESTE_NOME)) {
                        aAluguelChoreographer.temAluguel(true);

                    }
                } catch (NullPointerException e) {

                }
                return;

            }


            if (!verifyDeveloperPayload(purchase)) {
                aAluguelChoreographer.complain(aController.getString(R.string.aluguel_assinatura_erro_realizar_compra_autenticidade));
                return;
            }

            if (aController.isTestOn()) {
                Log.i(TAG, "Purchase successful.");
            }


            if (aController.isTestOn() || aController.isUberTestOn()) {
                if (purchase.getSku().equals(COMPRA_TESTE_SKU)) {
                    // bought the infinite gas subscription
                    Log.d(TAG, "Infinite gas subscription purchased.");
                    aAluguelChoreographer.temAluguel(true);

                }
            }

            if (purchase.getSku().equals(ALUGUEL_MENSAL_SKU)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                aAluguelChoreographer.temAluguel(true);


            }

            return;
        }
    };






        boolean verifyDeveloperPayload(Purchase p) {
            String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

            return true;
        }

        private void setInventory(Inventory aInventory) {
            this.aInventory = aInventory;
        }

        /**
         * Retorna os itens disponíveis para compra.
         * Depois o ítem escolhido servirá de referencia para poder chamar a atividade responsável por confirmar o pagamento em outro momento.
         */
        public ArrayList<String> getListadeComprasSKU() {


            return listadeComprasNOMES;
        }

        public void handleActivityResult(int requestCode, int resultCode, Intent data) {
            if (aController.isTestOn()) {
                Log.i(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
            }

            if (mHelper == null) return;

            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...
            } else {
                if(aController.isTestOn()){
                    Log.d(TAG, "onActivityResult handled by IABUtil.");

                }
            }
        }

        public void encerrarAtividades() {
            if (mHelper != null) {
                mHelper.dispose();
                mHelper = null;
            }
        }

}
