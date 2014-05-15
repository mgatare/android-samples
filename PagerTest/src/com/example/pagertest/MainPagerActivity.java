package com.example.pagertest;

import java.util.Locale;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fragments.CardsFragment;
import com.example.fragments.TransactionsFragment;
import com.example.fragments.TransferFundsFragment;

public class MainPagerActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	//private ViewPager mViewPager;
	private ParallexViewPager parallexViewPager;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pager);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Demo App");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		parallexViewPager = (ParallexViewPager) findViewById(R.id.pager);
        parallexViewPager.set_max_pages(3);
        parallexViewPager.setBackgroundAsset(R.drawable.back_four);
        parallexViewPager.setAdapter(mSectionsPagerAdapter);
		//mViewPager = (ViewPager) findViewById(R.id.pager);
		//mViewPager.setAdapter(mSectionsPagerAdapter);

        parallexViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pager_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//showDialog(null, null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
			//Log.i("", "*************getItem is called::"+position);
			if(position == 0) {
				return TransactionsFragment.newInstance();
			}else if(position == 1) {
				return CardsFragment.newInstance();
			}else if(position ==2) {
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
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * @param title
	 * @param message
	 */
//	public void showDialog(String title, String message) {
//
//		progressDialog = new Dialog(MainPagerActivity.this);
//		View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog_layout, null);
//		progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		progressDialog.setContentView(dialogView);
//
//		progressDialog.setCancelable(true);
//
//		ImageView progressSpinner = (ImageView) dialogView.findViewById(R.id.progress);
//
//		RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		anim.setRepeatCount(Animation.INFINITE);
//		anim.setDuration(1000);
//		anim.setInterpolator(new LinearInterpolator());
//		progressSpinner.startAnimation(anim);
//
//		progressDialog.show();
//		progressDialog.getWindow().setLayout(100, 100);
//
//	}
	
//	public void hideDialog() {
//		try {
//			if (null != progressDialog  && progressDialog.isShowing()) {
//				progressDialog.dismiss();
//				progressDialog.cancel();
//			}
//			progressDialog = null;
//		} catch (Exception e) {
//		}
//	}
}
