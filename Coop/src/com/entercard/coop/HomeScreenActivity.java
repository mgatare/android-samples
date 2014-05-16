package com.entercard.coop;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.entercard.coop.fragment.NavigationDrawerFragment;
import com.entercard.coop.fragment.TransactionsFragment;

public class HomeScreenActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment navigationDrawerFragment;
	//public static String accountName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		//Get Account Number
		//accountName = getIntent().getExtras().getString("account");
		
		if (null == savedInstanceState) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.navgationContainer, new TransactionsFragment(),
							"home_screen").commit();
		}

		navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		navigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

		ActionBar actionBar = getSupportActionBar();

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle("Transactions");

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
	}

	public void onSectionAttached(int number) {
		// TODO
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!navigationDrawerFragment.isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_home_actions, menu);
		
//		// Associate searchable configuration with the SearchView
//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//		Log.i("", "SearchView-->>" + searchView);
//		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			// TODO search action
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
