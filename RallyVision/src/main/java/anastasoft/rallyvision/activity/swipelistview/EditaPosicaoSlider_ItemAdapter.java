package anastasoft.rallyvision.activity.swipelistview;


import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.Slider.Prova_Trecho.Trecho.Trecho;
import anastasoft.rallyvision.command.Command;
import anastasoft.rallyvision.command.EditaPosicaoSliderCommand;
import anastasoft.rallyvision.controller.Controller;

public class EditaPosicaoSlider_ItemAdapter extends ArrayAdapter<Trecho> {

	List<Trecho> data;
	Context contextActivity;
	int layoutResID;
	Controller aController;
	private Trecho itemdata;
    SwipeListView aSwipeListView;
    Vibrator vb;

	public EditaPosicaoSlider_ItemAdapter(Context context, int layoutResourceId,
                                          List<Trecho> data, SwipeListView sListView) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.contextActivity = context;
		this.layoutResID = layoutResourceId;
        this.aSwipeListView = sListView;
		aController = (Controller) context.getApplicationContext();
        vb = (Vibrator) aController.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		final NewsHolder holder;// =null;
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) contextActivity).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new NewsHolder();

			holder.metragem = (EditText) row.findViewById(R.id.sp_swipe_et_valor_metragem);
			holder.metragem.setTextAppearance(contextActivity, R.style.MyListTitle);
            holder.buttonConfirma = (Button) row.findViewById(R.id.sp_swipe_confirma);
            holder.trecho = (TextView) row.findViewById(R.id.sp_swipe_trecho);
            holder.Ki = (TextView) row.findViewById(R.id.sp_swipe_ki);
            holder.Kf = (TextView) row.findViewById(R.id.sp_swipe_kf);
			row.setTag(holder);
		} else {
			holder = (NewsHolder) row.getTag();
		}


		itemdata = data.get(position);


        holder.buttonConfirma.setTag(position);


        holder.trecho.setText(itemdata.getTipo()+itemdata.getNumTrecho());

        holder.Ki.setText(String.valueOf((int)itemdata.getKi()));
        holder.Kf.setText(String.valueOf((int)itemdata.getKf()));



        holder.buttonConfirma.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                View vParent = (View) v.getParent();
                EditText etValorMetragem = (EditText) vParent.findViewById(R.id.sp_swipe_et_valor_metragem);
                int position = (Integer)v.getTag();
                aSwipeListView.closeAnimate(position);

                float dSlocal;
                if(etValorMetragem.getText().length() == 0){
                    dSlocal = 0;
                }else {
                    dSlocal =  Float.parseFloat(etValorMetragem.getText().toString());

                }


                vb.vibrate(1000);
                Command cmd = new EditaPosicaoSliderCommand(aController, data.get(position), dSlocal );
                cmd.Execute();
                ((Activity) contextActivity).finish();




            }
        });









		return row;



	}






	static class NewsHolder {


        //front

        TextView trecho;
        TextView Ki;
        TextView Kf;



        // back
		EditText metragem;
        Button buttonConfirma;

	}

}
