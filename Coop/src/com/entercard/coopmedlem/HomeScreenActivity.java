package com.entercard.coopmedlem;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.entercard.coopmedlem.fragment.CardsFragment;
import com.entercard.coopmedlem.fragment.TransactionsFragment;
import com.entercard.coopmedlem.fragment.TransferFundsFragment;
import com.entercard.coopmedlem.view.ParallexViewPager;

public class HomeScreenActivity extends BaseActivity implements ActionBar.TabListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ParallexViewPager parallexViewPager;
	private ActionBar actionBar;
	private ActivityFinishReceiver finishReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		init();

		regActivityLogoutReceiver();
		
		parallexViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
						switch (position) {
						case 0:
							actionBar.setTitle("Transactions");
							break;
						case 1:
							actionBar.setTitle("Credit Line Increase");
							break;
						case 2:
							actionBar.setTitle("Transfer Funds");
							break;
						default:
							actionBar.setTitle("Transactions");
							break;
						}
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	private void init() {
		actionBar = getSupportActionBar();
		actionBar.setTitle("Transactions");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		parallexViewPager = (ParallexViewPager) findViewById(R.id.pager);
        parallexViewPager.set_max_pages(3);
        parallexViewPager.setBackgroundAsset(R.drawable.back_three);
        parallexViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.pager_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.activity_chooser_view_content) {
//			//showDialog(null, null);
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		parallexViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return TransactionsFragment.newInstance(0);
				
			} else if (position == 1) {
				return CardsFragment.newInstance();
				
			} else if (position == 2) {
				return TransferFundsFragment.newInstance();
			}
			return new TransactionsFragment();
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tab_title_transaction).toUpperCase(l);
			case 1:
				return getString(R.string.tab_title_credit_increase).toUpperCase(l);
			case 2:
				return getString(R.string.tab_title_transfer).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * Reg activity logout receiver.
	 */
	private void regActivityLogoutReceiver() {
		finishReceiver = new ActivityFinishReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(getResources().getString(R.string.tag_act_finish));//ACTION.FINISH.LOGOUT
	    
	    registerReceiver(finishReceiver, intentFilter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null != finishReceiver) { 
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}
	/**
	 * RECEIVER for finishing the activity.
	 */
	private class ActivityFinishReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(getResources().getString(R.string.tag_act_finish))) {
				finish();
			}
		}
	}
}
