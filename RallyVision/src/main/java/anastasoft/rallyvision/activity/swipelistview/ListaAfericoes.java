package anastasoft.rallyvision.activity.swipelistview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;

import anastasoft.rallyvision.R;
import anastasoft.rallyvision.controller.Controller;
import anastasoft.rallyvision.controller.Data.model.Afericao;

public class ListaAfericoes extends Activity {

	public static SwipeListView swipelistview;
	Controller aController;
    private ArrayList<Afericao> itemData;
    private ListaAfericoes_ItemAdapter itAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);

		aController = (Controller) getApplicationContext();
		setSwipeListView();


		// Controller.LoadActionBox();
		// aController.populaActionBoxTeste();
		// Toast.makeText(this , String.valueOf(aController.getId() ),
		// Toast.LENGTH_SHORT).show();

        itemData = (ArrayList<Afericao>) aController.getListaAfericoesUsuario();
        if(itemData.isEmpty()){
            Toast.makeText(this,getString(R.string.lista_afericao_vazia)+"! " + getString(R.string.realize_afericoes),Toast.LENGTH_SHORT).show();
            this.finish();
        }
        itAdapter = new ListaAfericoes_ItemAdapter(this, R.layout.custom_row_edita_lista_afericoes, itemData, swipelistview);
        swipelistview.setAdapter(itAdapter);
        itAdapter.notifyDataSetChanged();

	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;


		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onStop() {
		super.onStop();
		aController.persist(itemData);
	}



	public void setSwipeListView() {
		swipelistview = (SwipeListView) findViewById(R.id.example_swipe_lv_list);

		swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public void onMove(int position, float x) {
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				Log.d("swipe", String.format("onStartOpen %d - action %d",
                        position, action));
			}

			@Override
			public void onStartClose(int position, boolean right) {
				Log.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position) {
				Log.d("swipe", String.format("onClickFrontView %d", position));

				// swipelistview.openAnimate(position); //when you touch front
				// view it will open

			}

			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));

				swipelistview.closeAnimate(position);// when you touch back view
														// it will close
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {

			}

		});

		// These are the swipe listview settings. you can change these
		// setting as your requirement
		swipelistview.setSwipeMode(SwipeListView.SWIPE_ACTION_NONE); // there
																		// are
																		// five
																		// swiping
																		// modes
		swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_MODE_LEFT); // there
																			// are
																			// four
																			// swipe
																			// actions
		swipelistview.setSwipeActionRight(SwipeListView.SWIPE_MODE_NONE);
		swipelistview.setOffsetLeft(convertDpToPixel(80f)); // left side offset
		swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
		swipelistview.setAnimationTime(64); // Animation time
		swipelistview.setSwipeOpenOnLongPress(true); // enable or disable
														// SwipeOpenOnLongPress

	}
}
