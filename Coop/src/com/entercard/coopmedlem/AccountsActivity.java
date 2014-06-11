package com.entercard.coopmedlem;
//package com.entercard.coop;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.ExpandableListView.OnGroupClickListener;
//import android.widget.ExpandableListView.OnGroupCollapseListener;
//import android.widget.ExpandableListView.OnGroupExpandListener;
//import android.widget.ImageView;
//
//import com.entercard.coop.adapters.ExpandableListAdapter;
//
//public class AccountsActivity extends BaseActivity {
//
//	ExpandableListAdapter listAdapter;
//	ExpandableListView expListView;
//	List<String> listDataHeader;
//	HashMap<String, List<String>> listDataChild;
//	private DisplayMetrics metrics;
//	int width;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_accounts);
//
//		expListView = (ExpandableListView) findViewById(R.id.lvExp);
//		prepareListData();
//
//		ActionBar actionBar = getSupportActionBar();
//
//		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setDisplayShowHomeEnabled(true);
//		actionBar.setDisplayShowTitleEnabled(true);
//		
//		actionBar.setTitle("Accounts");
//		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//		
//		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
//		expListView.setAdapter(listAdapter);
//		metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		width = metrics.widthPixels;
//		expListView.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));
//		expListView.setOnGroupClickListener(new OnGroupClickListener() {
//
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v,
//					int groupPosition, long id) {
//				
//				return false;
//			}
//		});
//
//		// Listview Group expanded listener
//		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
//			@Override
//			public void onGroupExpand(int groupPosition) {
//				int len = listAdapter.getGroupCount();
//				for (int i = 0; i < len; i++) {
//					if (i != groupPosition) {
//						expListView.collapseGroup(i);
//					}
//				}
//				//Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Expanded",Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		// Listview Group collasped listener
//		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
//			@Override
//			public void onGroupCollapse(int groupPosition) {
//				//Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Collapsed",Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		// Listview on child click listener
//		expListView.setOnChildClickListener(new OnChildClickListener() {
//
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				// TODO Auto-generated method stub
////				Toast.makeText(
////						getApplicationContext(),
////						listDataHeader.get(groupPosition)
////								+ " : "
////								+ listDataChild.get(
////										listDataHeader.get(groupPosition)).get(
////										childPosition), Toast.LENGTH_SHORT)
////						.show();
//				
//				Intent intent = new Intent(AccountsActivity.this, HomeScreenActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("account", listDataHeader.get(groupPosition));
//				startActivity(intent);
//				
//				return false;
//			}
//		});
//	}
//
//	/*
//	 * Preparing the list data
//	 */
//	private void prepareListData() {
//		listDataHeader = new ArrayList<String>();
//		listDataChild = new HashMap<String, List<String>>();
//
//		// Adding child data
//		listDataHeader.add("Coop:Black");
//		listDataHeader.add("Coop:Silver");
//		listDataHeader.add("Coop:Gold");
//
//		// Adding child data
//		List<String> cardOne = new ArrayList<String>();
//		cardOne.add("xxxx xxxx xxxx 2123");
//
//		List<String> cardTwo = new ArrayList<String>();
//		cardTwo.add("xxxx xxxx xxxx 0012");
//		
//		List<String> cardThree = new ArrayList<String>();
//		cardThree.add("xxxx xxxx xxxx 8042");
//
//		listDataChild.put(listDataHeader.get(0), cardOne); // Header, Child data
//		listDataChild.put(listDataHeader.get(1), cardTwo);
//		listDataChild.put(listDataHeader.get(2), cardThree);
//	}
//
//	/**
//	 * 
//	 * @param currentRotation
//	 * @param view
//	 */
//	
//	public void turn(float degree, ImageView view) {
//		final RotateAnimation anim = new RotateAnimation(0.0f, degree,
//	            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//	            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//
//		anim.setInterpolator(new LinearInterpolator());
//		anim.setDuration(300);
//		anim.setFillEnabled(true);
//
//		anim.setFillAfter(true);
//		view.startAnimation(anim);
//	}
//	/**
//	 * @param pixels
//	 * @return
//	 */
//	public int GetDipsFromPixel(float pixels) {
//		final float scale = getResources().getDisplayMetrics().density;
//		return (int) (pixels * scale + 0.5f);
//	}
//}
