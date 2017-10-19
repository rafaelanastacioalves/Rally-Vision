package anastasoft.rallyvision.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.VerificaAluguelStatusCommand;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.InAppBilling.AluguelChoreographer;
import anastasoft.rallyvision.R;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by rafaelanastacioalves on 21/02/15.
 */
public class EscolheAluguel extends Activity {


    private ArrayAdapter<String> listaAlugueisAdapter;
    private Controller aController;
    private AluguelChoreographer aAluguelChoreographer;

    public Activity aEscolheAluguel;

    private ListView listaDeAlugueis;
    private OnItemClickListener mAluguelClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            aAluguelChoreographer.comprar(aEscolheAluguel,arg2);
        }
    };
    private TextView aTVEstamosCarregando;
    private TextView aTVEscolhaUmaOpcao;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Setup the window
        aController = (Controller)getApplicationContext();
        aAluguelChoreographer = aController.getAluguelChoreographer();
        aAluguelChoreographer.setActivity(this);

        this.aEscolheAluguel = this;

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.escolha_aluguel);
        setupViews();




    }

    private void setupViews() {



        aTVEstamosCarregando = (TextView) findViewById(R.id.aluguel_estamos_carregando_mensagem_text_view);
        aTVEscolhaUmaOpcao = (TextView) findViewById(R.id.aluguel_mensagem__escolha_text_view);
    }

    @Override
    protected void onStart() {
        if(aAluguelChoreographer.statusSemAluguelConfirmado()){
            mostraParaEscolher();
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void configuraListaDeAlugueis() {

        listaAlugueisAdapter = new ArrayAdapter<String>(this, R.layout.texto_lista_simples);

        ArrayList<String> listaDeCompras = AluguelChoreographer.getListaDeCompras();
        for(String nome : listaDeCompras){
            listaAlugueisAdapter.add(nome);

        }
        listaDeAlugueis = (ListView) findViewById(R.id.aluguel_lista_opcao);
        listaDeAlugueis.setAdapter(listaAlugueisAdapter);
        listaDeAlugueis.setOnItemClickListener(mAluguelClickListener);
        listaDeAlugueis.setVisibility(View.VISIBLE);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        aAluguelChoreographer.handleActivityResult(requestCode,resultCode,data);

        super.onActivityResult(requestCode, resultCode, data);
        Command cmd = new VerificaAluguelStatusCommand(aController);
        cmd.Execute();
    }

    public void complain(String message) {
        alert("Error: " + message);
    }


    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }


    public void mostraParaEscolher() {

        try{

            aTVEstamosCarregando.setVisibility(View.GONE);
            aTVEscolhaUmaOpcao.setVisibility(View.VISIBLE);

            configuraListaDeAlugueis();



        }catch (NullPointerException e){
            mostraParaEscolher();
        }

    }
}