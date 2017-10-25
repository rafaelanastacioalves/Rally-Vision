package anastasoft.rallyvision.activity.swipelistview;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fortysevendeg.android.swipelistview.SwipeListView;

import java.util.List;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class ListaAfericoes_ItemAdapter extends ArrayAdapter<Afericao> {

	List<Afericao> data;
	Context contextActivity;
	int layoutResID;
	Controller aController;
	private Afericao itemdata;
    SwipeListView aSwipeListView;

	public ListaAfericoes_ItemAdapter(Context context, int layoutResourceId,
                                      List<Afericao> data, SwipeListView sListView) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.contextActivity = context;
		this.layoutResID = layoutResourceId;
        this.aSwipeListView = sListView;
		aController = (Controller) context.getApplicationContext();
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		final NewsHolder holder;// =null;
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) contextActivity).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new NewsHolder();

			holder.nomeAferição = (EditText) row
					.findViewById(R.id.swipe_nome_afericao);
//            holder.ratio = (TextView) row.findViewById(R.id.swipe_ratio);
			holder.nomeAferição.setTextAppearance(contextActivity, R.style.MyListTitle);
            holder.buttonOK = (Button) row.findViewById(R.id.swipe_ok);
			holder.buttonDelete = (ImageButton) row.findViewById(R.id.swipe_delete);
			holder.buttonEdit = (ImageButton) row.findViewById(R.id.swipe_edit);
			row.setTag(holder);
		} else {
			holder = (NewsHolder) row.getTag();
		}


		itemdata = data.get(position);

		holder.buttonDelete.setTag(position);
		holder.buttonEdit.setTag(position);

        holder.buttonOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                View vParent = (View) v.getParent();
                EditText etAfericaoNome = (EditText) vParent.findViewById(R.id.swipe_nome_afericao);

                data.get(position).setName(etAfericaoNome.getText().toString());


                etAfericaoNome.setEnabled(false);


                etAfericaoNome.clearFocus();


                etAfericaoNome.setCursorVisible(false);


                InputMethodManager imm = (InputMethodManager) ((Activity)contextActivity).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(etAfericaoNome.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                v.setVisibility(View.INVISIBLE);
                aSwipeListView.closeAnimate(position);


            }
        });

		holder.nomeAferição.setText(itemdata.getName());
//        holder.ratio.setText(String.valueOf(itemdata.getRatio())
//        );


		// holder.icon.setImageDrawable(itemdata.getIcon());

		holder.buttonDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Toast.makeText(contextActivity, "Button 1 Clicked", Toast.LENGTH_SHORT)
//						.show();
                int position = (Integer) v.getTag();
                aSwipeListView.closeAnimate(position);

                aController.deleteAfericao(data.get(position));
                data.remove(position);
                notifyDataSetChanged();


            }
        });

		holder.buttonEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Toast.makeText(contextActivity, "Button 2 Clicked", Toast.LENGTH_SHORT)
//						.show();
                int position = (Integer) v.getTag();
                View parentView = (View) ((ViewParent)v.getParent()).getParent();


                Button bOK = (Button) parentView.findViewById(R.id.swipe_ok);


                EditText etAfericaoNome = (EditText) parentView.findViewById(R.id.swipe_nome_afericao);

                aSwipeListView.closeAnimate(position);

                bOK.setVisibility(View.VISIBLE);
                etAfericaoNome.clearFocus();

                etAfericaoNome.setEnabled(true);
                etAfericaoNome.setFocusable(true);
                etAfericaoNome.requestFocus();

                etAfericaoNome.extendSelection(etAfericaoNome.getText().length());
                InputMethodManager imm = (InputMethodManager) ((Activity)contextActivity).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etAfericaoNome, InputMethodManager.SHOW_IMPLICIT);


//                ((Activity)contextActivity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




        };
    });


		return row;



	}



	static class NewsHolder {

		EditText nomeAferição;
//        TextView ratio;
        Button buttonOK;
		ImageButton buttonDelete;
		ImageButton buttonEdit;

	}

}
