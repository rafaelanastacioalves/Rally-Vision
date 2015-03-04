package anastasoft.rallyvision.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Controller;


public class BemVindoAluguelDialog extends DialogFragment {

	private Controller aController;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		aController = (Controller) (getActivity()).getApplicationContext();
		LayoutInflater liAdd = LayoutInflater.from(getActivity());
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());
		builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle(getResources().getString(
				R.string.dialog_bemvindo_aluguel));





		return builderSingle.create();

	}
}
