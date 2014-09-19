package com.no.entercard.coopmedlem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.no.entercard.coopmedlem.fragment.CardsFragment;
import com.no.entercard.coopmedlem.fragment.TransactionsFragment;
import com.no.entercard.coopmedlem.fragment.TransferFundsFragment;

public class HomeScreenActivity extends BaseActivity implements ActionBar.TabListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private ActivityFinishReceiver finishReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		init();

		regActivityLogoutReceiver();
		
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	private void init() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setIcon(R.drawable.icon_coop);
		actionBar.setTitle(null);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_home_actions, menu);
		
		int accountsCount = ApplicationEx.getInstance().getAccountsArrayList().size();
		
		//Currently Language option is set to INVISIBLE need to 14/09/14
		MenuItem menuitemLanguage = menu.findItem(R.id.action_language);
		menuitemLanguage.setVisible(false);
		
		if(accountsCount==1) {
			MenuItem menuitemAccount = menu.findItem(R.id.action_account);
			menuitemAccount.setVisible(false);
		}
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_account:
			Intent intent = new Intent(HomeScreenActivity.this,AllAccountsActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
			//Toggle States to NOT allow animation again
			BaseActivity.setFirstVisit(true);
			return true;
			
		case R.id.action_language:
			//Log.e("COOP", ">>HOMESCREEN In action_language");
			
			Intent languageIntent = new Intent(HomeScreenActivity.this, LanguageSelectorActivity.class);
			startActivity(languageIntent);
			
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
		
		/*TextView textView = (TextView) tab.getCustomView().findViewById(R.id.title);
		textView.setTextColor(Color.RED);
		textView.setTypeface(null, Typeface.BOLD);*/
		closeKeyBoard();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
				return TransactionsFragment.newInstance();
				
			} else if (position == 1) {
				return TransferFundsFragment.newInstance();
				
			} else if (position == 2) {
				return CardsFragment.newInstance();
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
			//Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tab_title_transaction);
			case 1:
				return getString(R.string.tab_title_transfer);
			case 2:
				return getString(R.string.tab_title_cards);
			}
			return null;
		}
	}
	
	@Override
	public void onBackPressed() {
		int accountsCount = ApplicationEx.getInstance().getAccountsArrayList().size();
		if (accountsCount == 1)
			ApplicationEx.getInstance().clearGlobalContents();

		// Toggle States to NOT allow animation again
		BaseActivity.setFirstVisit(true);
		super.onBackPressed();
	}
	
	/**
	 * Reg activity logout receiver.
	 */
	private void regActivityLogoutReceiver() {
		finishReceiver = new ActivityFinishReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(getResources().getString(R.string.tag_act_finish));//ACTION.LOGOUT
	    
	    registerReceiver(finishReceiver, intentFilter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.e("COOP", ">>HOMESCREEN In Method: onDestroy()"+finishReceiver);
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
