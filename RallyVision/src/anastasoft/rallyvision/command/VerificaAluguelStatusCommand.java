package anastasoft.rallyvision.command;

import android.app.Application;

import anastasoft.rallyvision.controller.InAppBilling.AluguelChoreographer;

public class VerificaAluguelStatusCommand extends Command {

    /**
     * Objeto utilizado para verificar o status atual de assinatura do cliente.
     * Este objeto só será executado na versão aluguel do Rally Vision
     * @param controller
     */
    public VerificaAluguelStatusCommand(Application controller) {
        super(controller);
    }

    @Override
    public void Execute() {
        // TODO Auto-generated method stub
        AluguelChoreographer aAC = aController.getAluguelChoreographer();
        if(aAC != null){
            aAC.verificaAluguel();
        }
    }

}
