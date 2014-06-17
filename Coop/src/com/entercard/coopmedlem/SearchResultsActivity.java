//package com.entercard.coopmedlem;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.ActionBar;
//import android.app.SearchManager;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.widget.TextView;
//
//public class SearchResultsActivity extends FragmentActivity {
//
//	private TextView queryTextView;
//
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@SuppressLint("NewApi")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_search_results);
//
//		// get the action bar
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		queryTextView = (TextView) findViewById(R.id.queryTextView);
//		handleIntent(getIntent());
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		setIntent(intent);
//		handleIntent(intent);
//	}
//
//	/**
//	 * Handling intent data
//	 */
//	private void handleIntent(Intent intent) {
//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			String query = intent.getStringExtra(SearchManager.QUERY);
//			queryTextView.setText("Search : " + query);
//		}
//	}
//}
