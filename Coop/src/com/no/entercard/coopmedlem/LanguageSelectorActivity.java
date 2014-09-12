package com.no.entercard.coopmedlem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.no.entercard.coopmedlem.adapters.EmploymentDetailsAdapter;

public class LanguageSelectorActivity extends BaseActivity {

	private ActivityFinishReceiver finishReceiver;
	private ArrayList<String> employmentArrayList;
	private ListView listViewEmployment;
	private EmploymentDetailsAdapter employmentAdapter;
	// private EmploymentTypeListener employmentTypeListener;
	//private String employmentTxt = null;

	// protected interface EmploymentTypeListener {
	// public void onReturnEmploymentType(String type);
	// }

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rc_listview);

		init();

		regActivityFinishReceiver();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.employment));
		actionBar.setIcon(R.drawable.icon_coop);

		listViewEmployment.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listViewEmployment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView txtLabel = (TextView) arg1.findViewById(R.id.lblEmploymentName);
				String languageTxt = txtLabel.getText().toString();
				Log.i("", "Language selected is---"+languageTxt);
				employmentAdapter.setPosition(arg2);
				employmentAdapter.notifyDataSetChanged();
				
				Log.d("", "LOCALE--"+Locale.getDefault());
				//Configuration config = getBaseContext().getResources().getConfiguration();
				switch (arg2) {
				
				case 0:
					setLanguage(ApplicationEx.getInstance(), "en_US");
					//Locale localeEn = new Locale("en");
					/*Locale.setDefault(localeEn);
					
					config.locale = localeEn;
					// updating locale
					getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics()); */
					break;
				case 1:
					//Locale localeNo = new Locale("no");
					setLanguage(ApplicationEx.getInstance(), "nb_NO");
					break;
					
				case 2:
					//Locale localeSv = new Locale("sv");
					setLanguage(ApplicationEx.getInstance(), "sv_SE");
					break;

				default:
					break;
				}
			}
		});
	}

	public static void setLanguage(Context context, String languageToLoad) {
		Log.d(TAG, "setting language");
		Locale locale = new Locale(languageToLoad); // e.g "sv"
		Locale systemLocale = Locale.getDefault();
		if (systemLocale != null && systemLocale.equals(locale)) {
			Log.d(TAG, "Already correct language set");
			return;
		}
		Locale.setDefault(locale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
		Log.d(TAG, "Language set");
	}
	
	
	private void init() {

		listViewEmployment = (ListView) findViewById(R.id.singleListView);
		String[] arrays = getResources()
				.getStringArray(R.array.array_languages);

		employmentArrayList = new ArrayList<String>(arrays.length);
		Collections.addAll(employmentArrayList, arrays);

		employmentAdapter = new EmploymentDetailsAdapter(
				LanguageSelectorActivity.this, 0, employmentArrayList);
		listViewEmployment.setAdapter(employmentAdapter);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		finish();
		startActivity(getIntent());
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * OnCLick Listener for the Numbers of Service Center
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.d("COOP", "### android.R.id.home:####");
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("COOP", "##### onDestroy EmploymentActivity  #####");
		if (null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/**
	 * Reg activity logout receiver.
	 */
	private void regActivityFinishReceiver() {
		finishReceiver = new ActivityFinishReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(getResources()
				.getString(R.string.tag_act_finish));// ACTION.FINISH.LOGOUT

		registerReceiver(finishReceiver, intentFilter);
	}

	/**
	 * RECEIVER for finishing the activity.
	 */
	private class ActivityFinishReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					getResources().getString(R.string.tag_act_finish))) {
				finish();
			}
		}
	}
}
