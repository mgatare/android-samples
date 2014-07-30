package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.entercard.coopmedlem.adapters.AccountsAdapter;
import com.entercard.coopmedlem.entities.AccountsModel;
import com.entercard.coopmedlem.services.GetAccountsService;
import com.entercard.coopmedlem.services.GetAccountsService.GetAccountsListener;
import com.entercard.coopmedlem.utils.AlertHelper;

public class AllAccountsActivity extends BaseActivity implements
		GetAccountsListener {

	private GetAccountsService accountsService;
	private ListView accountsListView;
	private TextView textViewServerErrorMsg;
	private Button btnTryAgain;
	private ActivityFinishReceiver finishReceiver;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		init();

		regActivityFinishReceiver();

		// Log.e("", "ARRAYLIST INSTANCE###"+ApplicationEx.getInstance().getAccountsArrayList());

		if (null != ApplicationEx.getInstance().getAccountsArrayList()
				&& !ApplicationEx.getInstance().getAccountsArrayList()
						.isEmpty()) {

			AccountsAdapter adapter = new AccountsAdapter(
					AllAccountsActivity.this, 0, ApplicationEx.getInstance()
							.getAccountsArrayList());
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
		btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

		accountsListView = (ListView) findViewById(R.id.listViewAccounts);
		closeKeyBoard();

		btnTryAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnTryAgain.setVisibility(View.GONE);
				textViewServerErrorMsg.setVisibility(View.GONE);
				callGetAccountsService();
			}
		});

		accountsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String openToBuy = ApplicationEx.getInstance()
						.getAccountsArrayList().get(arg2).getOpenToBuy();
				String spent = ApplicationEx.getInstance()
						.getAccountsArrayList().get(arg2).getSpent();
				int count = Integer.parseInt(ApplicationEx.getInstance()
						.getAccountsArrayList().get(arg2)
						.getTransactionsCount());

				// Set these temporarily in BaseActivity all Class level
				setOpenToBuy(openToBuy);
				setSpent(spent);
				setAccountPosition(arg2);
				setTransactionsCount(count);

				/*
				 * showDeveloperLog(">>openToBuy>>"+openToBuy);
				 * showDeveloperLog(">>spent>>"+spent);
				 * showDeveloperLog(">>Account number>>"+
				 * ApplicationEx.applicationEx
				 * .getAccountsArrayList().get(arg2).getAccountNumber());
				 * showDeveloperLog(">>count>>"+count);
				 */

				Intent intent = new Intent(AllAccountsActivity.this,
						HomeScreenActivity.class);
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
				btnTryAgain.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onGetAccountsFinished(
			final ArrayList<AccountsModel> accountArrayList) {
		hideProgressDialog();
		Log.e("COOP", "accountArrayList.size()-->>"
				+ ApplicationEx.getInstance().getAccountsArrayList().size());

		if (null != accountArrayList && accountArrayList.size() != 0) {

			/*
			 * If Just one account then directly move the user to the
			 * transactions screen, else update the APAPTER
			 */
			if (accountArrayList.size() > 1) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AccountsAdapter adapter = new AccountsAdapter(
								AllAccountsActivity.this, 0, accountArrayList);
						accountsListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				});
			} else {
				String openToBuy = accountArrayList.get(0).getOpenToBuy();
				String spent = accountArrayList.get(0).getSpent();
				int count = Integer.parseInt(accountArrayList.get(0)
						.getTransactionsCount());

				// Set these temporarily
				setOpenToBuy(openToBuy);
				setSpent(spent);
				setAccountPosition(0);
				setTransactionsCount(count);

				Intent intent = new Intent(AllAccountsActivity.this,HomeScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();// Need to finish the ACCOUNTS screen as there is only one account
			}
		} else {
			ApplicationEx.getInstance().setAccountsArrayList(null);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("COOP", "################onDestroy ALL ACCOUNTS###########");
		if (null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("COOP", "#######ON BACK PRESSED FOR ALL ACCOUNTS###########");
		ApplicationEx.getInstance().clearGlobalContents();
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