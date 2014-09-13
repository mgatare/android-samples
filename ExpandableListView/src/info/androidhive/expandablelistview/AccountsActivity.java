package info.androidhive.expandablelistview;

import info.androidhive.expandablelistview.LoadMoreExpandableListView.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AccountsActivity extends FragmentActivity {

	ExpandableListAdapter listAdapter;
	LoadMoreExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	boolean isClicked = false;
	
	private DisplayMetrics metrics;
	int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		// get the listview
		expListView = (LoadMoreExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		expListView.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));
		
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();

				ImageView imgDropDown = (ImageView) v
						.findViewById(R.id.imgExpanded);
				if (!isClicked) {
					turn(180f, imgDropDown);
					// imgDropDown.setImageResource(R.drawable.arrow_down);
					isClicked = true;
				} else {
					turn(0f, imgDropDown);
					// imgDropDown.setImageResource(R.drawable.arrow_up);
					isClicked = false;
				}
				// Log.d("", "imgDropDown--->>"+imgDropDown);
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				int len = listAdapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						expListView.collapseGroup(i);
					}
				}
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		
		expListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				
				new BackgroundTask().execute("");
				
				//expListView.onLoadMoreComplete();
			}
		});
	}

	private class BackgroundTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			expListView.onLoadMoreComplete();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	
	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Coop:Black");
		listDataHeader.add("Coop:Silver");
		listDataHeader.add("Coop:Gold");
		
		listDataHeader.add("Coop:Black2");
		listDataHeader.add("Coop:Silver2");
		listDataHeader.add("Coop:Gold2");
		
		listDataHeader.add("Coop:Black3");
		listDataHeader.add("Coop:Silver3");
		listDataHeader.add("Coop:Gold3");
		
		listDataHeader.add("Coop:Black4");
		listDataHeader.add("Coop:Silver4");
		listDataHeader.add("Coop:Gold4");
		
		listDataHeader.add("Coop:Black5");
		listDataHeader.add("Coop:Silver5");
		listDataHeader.add("Coop:Gold5");
		
		listDataHeader.add("Coop:Black6");
		listDataHeader.add("Coop:Silver6");
		listDataHeader.add("Coop:Gold6");
		
		listDataHeader.add("Coop:Black6");
		listDataHeader.add("Coop:Silver6");
		listDataHeader.add("Coop:Gold6");
		
		listDataHeader.add("Coop:Black7");
		listDataHeader.add("Coop:Silver7");
		listDataHeader.add("Coop:Gold7");

		// Adding child data
		List<String> cardOne = new ArrayList<String>();
		cardOne.add("xxxx xxxx xxxx 2123");

		List<String> cardTwo = new ArrayList<String>();
		cardTwo.add("xxxx xxxx xxxx 0012");

		List<String> cardThree = new ArrayList<String>();
		cardThree.add("xxxx xxxx xxxx 8042");

		listDataChild.put(listDataHeader.get(0), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(1), cardTwo);
		listDataChild.put(listDataHeader.get(2), cardThree);
		
		listDataChild.put(listDataHeader.get(3), cardThree);
		
		listDataChild.put(listDataHeader.get(4), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(5), cardTwo);
		listDataChild.put(listDataHeader.get(6), cardThree);
		
		
		listDataChild.put(listDataHeader.get(7), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(8), cardTwo);
		listDataChild.put(listDataHeader.get(9), cardThree);
		
		
		listDataChild.put(listDataHeader.get(10), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(11), cardTwo);
		listDataChild.put(listDataHeader.get(12), cardThree);
		
		
		listDataChild.put(listDataHeader.get(13), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(14), cardTwo);
		listDataChild.put(listDataHeader.get(15), cardThree);
		
		
		listDataChild.put(listDataHeader.get(16), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(17), cardTwo);
		listDataChild.put(listDataHeader.get(18), cardThree);
		
		listDataChild.put(listDataHeader.get(19), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(20), cardTwo);
		listDataChild.put(listDataHeader.get(21), cardThree);
		
		listDataChild.put(listDataHeader.get(22), cardOne); // Header, Child data
		listDataChild.put(listDataHeader.get(23), cardTwo);
		
		
	}

	/**
	 * 
	 * @param currentRotation
	 * @param view
	 */

	public void turn(float degree, ImageView view) {
		final RotateAnimation anim = new RotateAnimation(0.0f, degree,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(300);
		anim.setFillEnabled(true);

		anim.setFillAfter(true);
		view.startAnimation(anim);
	}

	public int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}
}
