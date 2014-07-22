package com.entercard.coopmedlem;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.entercard.coopmedlem.adapters.EmploymentDetailsAdapter;

public class EmploymentActivity extends BaseActivity {

	private ActivityFinishReceiver finishReceiver;
	private ArrayList<String> employmentArrayList;
	private ListView listViewEmployment;
	private EmploymentDetailsAdapter employmentAdapter;
	private EmploymentTypeListener employmentTypeListener;
	private String employmentTxt = null;
	
	protected interface EmploymentTypeListener {
		public void onReturnEmploymentType(String type);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rc_listview);

		init();
		
		regActivityFinishReceiver();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle("Employment");
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		listViewEmployment.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listViewEmployment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				TextView txtLabel = (TextView) arg1.findViewById(R.id.lblEmploymentName);
				employmentTxt = txtLabel.getText().toString();
				employmentAdapter.setPosition(arg2);
				employmentAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void init() {
		
		listViewEmployment = (ListView) findViewById(R.id.singleListView);
		String[] arrays = getResources().getStringArray(R.array.array_employment);
		
		employmentArrayList = new ArrayList<String>(arrays.length);
		Collections.addAll(employmentArrayList, arrays);
		
		employmentAdapter = new EmploymentDetailsAdapter(EmploymentActivity.this, 0, employmentArrayList);
		listViewEmployment.setAdapter(employmentAdapter);
		
        try {
        	employmentTypeListener = (EmploymentTypeListener) new CreditLineIncreaseActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
	}

	/**
	 * OnCLick Listener for the Numbers of Service Center
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			/*Intent upIntent = new Intent(this, CreditLineIncreaseActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
				//TaskStackBuilder.create(this).addNextIntent(upIntent).startActivities();
				setResult(RESULT_OK);
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
				setResult(RESULT_OK);
			}*/
//			Intent upIntent = new Intent();
//			upIntent.putExtra("ID", "MAYURRRR");
//			setResult(RESULT_OK, upIntent);
			
			if(null!=employmentTypeListener) {
				employmentTypeListener.onReturnEmploymentType(employmentTxt);
			}
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
		if(null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(null!=employmentTypeListener) {
			employmentTypeListener.onReturnEmploymentType(employmentTxt);
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