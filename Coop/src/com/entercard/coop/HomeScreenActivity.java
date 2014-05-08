package com.entercard.coop;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.entercard.coop.adapters.TransactionsAdapter;
import com.entercard.coop.fragment.NavigationDrawerFragment;

import dcom.entercard.coop.model.DataModel;

public class HomeScreenActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment navigationDrawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		// if (null == savedInstanceState) {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new TransactionsFragment(), "home_screen")
				.commit();
		// }

		navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		navigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// Log.i("", "DRAWER ITEM CLICKED :::::Position:::::" + position);
	}

	public void onSectionAttached(int number) {
		// TODO
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Transactions");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!navigationDrawerFragment.isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class TransactionsFragment extends Fragment {
		private ListView listView;

		public static TransactionsFragment newInstance(int sectionNumber) {
			TransactionsFragment fragment = new TransactionsFragment();
			return fragment;
		}

		public TransactionsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_transactions,
					container, false);

			listView = (ListView) rootView.findViewById(R.id.listView);

			setData();
			return rootView;
		}

		private void setData() {

			ArrayList<DataModel> arrayList = new ArrayList<DataModel>();

			for (int i = 0; i < 20; i++) {
				DataModel dataModel = new DataModel();
				dataModel.setId("00" + i);
				dataModel.setDate(i + "/01/2014");
				dataModel
						.setName("Lorem Ipsum is simply dummy text of the printing");
				dataModel.setPrice(i + "0000");
				arrayList.add(dataModel);
			}

			TransactionsAdapter transactionsAdapter = new TransactionsAdapter(
					getActivity(), 0, arrayList);
			listView.setAdapter(transactionsAdapter);
			transactionsAdapter.notifyDataSetChanged();
		}
	}
}
