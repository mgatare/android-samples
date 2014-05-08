package com.example.actiont;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeScreenActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private Button btnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		init();
	}

	private void init() {
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 startActivity(new Intent(HomeScreenActivity.this, SecondActivity.class));
				// FragmentManager fragmentManager =
				// getSupportFragmentManager();
				// fragmentManager.beginTransaction()
				// .replace(R.id.container, new MapHolderFragment())
				// .addToBackStack("map_view").commit();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.popBackStackImmediate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	/**
	 * Restore action bar.
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Home");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings_one) {
			return true;
		} else if (id == R.id.action_settings_two) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
			return rootView;
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		//Log.i("", "position::::>>>>>"+position);
	}
}
