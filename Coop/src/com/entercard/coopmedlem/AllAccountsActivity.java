package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.entercard.coopmedlem.adapters.AccountsAdapter;
import com.entercard.coopmedlem.entities.AccountsModel;
import com.entercard.coopmedlem.services.GetAccountsService;
import com.entercard.coopmedlem.services.GetAccountsService.GetAccountsListener;
import com.entercard.coopmedlem.utils.AlertHelper;

public class AllAccountsActivity extends BaseActivity implements GetAccountsListener {

	private GetAccountsService accountsService;
	private ListView accountsListView;
	private TextView textViewServerErrorMsg;
//	private Button btnTryAgain;
	private com.entercard.coopmedlem.AllAccountsActivity.ActivityFinishReceiver finishReceiver;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		init();
		
		regActivityFinishReceiver();
		
		if(null!=ApplicationEx.getInstance().getAccountsArrayList()
				&& !ApplicationEx.getInstance().getAccountsArrayList().isEmpty()) {
			
			AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, ApplicationEx.getInstance().getAccountsArrayList());
			accountsListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
		} else {
			callGetAccountsService();
		}
	}

	private void init() {
		
		actionBar = getSupportActionBar();
		actionBar.setTitle("Accounts");
		
		textViewServerErrorMsg = (TextView) findViewById(R.id.lblServerErrorMsg);
		//btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
		
		accountsListView = (ListView) findViewById(R.id.listViewAccounts);
		closeKeyBoard();
		
		accountsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				String openToBuy =  ApplicationEx.getInstance().getAccountsArrayList().get(arg2).getOpenToBuy();
				String spent = ApplicationEx.getInstance().getAccountsArrayList().get(arg2).getSpent();
				int count = Integer.parseInt(ApplicationEx.getInstance().getAccountsArrayList().get(arg2).getTransactionsCount());
				
				//Set these temporarily in BaseActivity all Class level
				setOpenToBuy(openToBuy);
				setSpent(spent);
				setAccountPosition(arg2);
				setTransactionsCount(count);
				
				/*showDeveloperLog(">>openToBuy>>"+openToBuy);
				showDeveloperLog(">>spent>>"+spent);
				showDeveloperLog(">>Account number>>"+ ApplicationEx.applicationEx.getAccountsArrayList().get(arg2).getAccountNumber());
				showDeveloperLog(">>count>>"+count);*/
				
				Intent intent = new Intent(AllAccountsActivity.this, HomeScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
	}

	private void callGetAccountsService() {
		// Get all the accounts from service
		showProgressDialog();
		accountsService = new GetAccountsService();
		accountsService.setAccountsListener(this);
		ApplicationEx.operationsQueue.execute(accountsService);
		
	}

	@Override
	public void onGetAccountsFailed(final String errorMsg) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertHelper.Alert(errorMsg, AllAccountsActivity.this);
				accountsListView.setVisibility(View.GONE);
				textViewServerErrorMsg.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onGetAccountsFinished(final ArrayList<AccountsModel> accountArrayList) {
		hideProgressDialog();
		
		if(null!=accountArrayList && accountArrayList.size()!=0) {
			
			/*If Just one account then directly move the user to the transactions screen*/
			if(accountArrayList.size()>1) {
				ApplicationEx.getInstance().setAccountsArrayList(accountArrayList);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, ApplicationEx.getInstance().getAccountsArrayList());
						accountsListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				});
			} else {
				String openToBuy =  ApplicationEx.getInstance().getAccountsArrayList().get(0).getOpenToBuy();
				String spent = ApplicationEx.getInstance().getAccountsArrayList().get(0).getSpent();
				int count = Integer.parseInt(ApplicationEx.getInstance().getAccountsArrayList().get(0).getTransactionsCount());
				
				//Set these temporarily in BaseActivity all Class level
				setOpenToBuy(openToBuy);
				setSpent(spent);
				setAccountPosition(0);
				setTransactionsCount(count);
				
				Intent intent = new Intent(AllAccountsActivity.this, HomeScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		} else {
			ApplicationEx.getInstance().setAccountsArrayList(null);
		}
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