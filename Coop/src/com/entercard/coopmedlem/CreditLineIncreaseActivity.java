package com.entercard.coopmedlem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class CreditLineIncreaseActivity extends BaseActivity {

	private ActivityFinishReceiver finishReceiver;
	private LinearLayout linearEmployment;
	private Button btnApplyCLI;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_creditline_increase);

		init();
		
		regActivityFinishReceiver();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

	}
	
	private void init() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * OnCLick Listener for the Numbers of Service Center
	 */
	OnClickListener viewOnCLickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.linearEmployment:
				
				Intent intent = new Intent(CreditLineIncreaseActivity.this, EmploymentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				break;
				
			case R.id.btnApplyCLI:
				break;
				
			default:
				break;
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, HomeScreenActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("COOP", "##### onDestroy CLI  #####");
		if(null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}
	/**
	 * Reg activity logout receiver.
	 */
	private void regActivityFinishReceiver() {
		finishReceiver = new ActivityFinishReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(getResources().getString(R.string.tag_act_finish));//ACTION.FINISH.LOGOUT
	    
	    registerReceiver(finishReceiver, intentFilter);
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
