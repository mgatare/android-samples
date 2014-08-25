package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entercard.coopmedlem.adapters.AccountsAdapter;
import com.entercard.coopmedlem.entities.AccountsModel;
import com.entercard.coopmedlem.services.GetAccountsService;
import com.entercard.coopmedlem.services.GetAccountsService.GetAccountsListener;

public class AllAccountsActivity extends BaseActivity implements
		GetAccountsListener {

	private GetAccountsService accountsService;
	private ListView accountsListView;
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
					AllAccountsActivity.this, 0, ApplicationEx.getInstance().getAccountsArrayList());
			accountsListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} else {
			callGetAccountsService();
		}
	}

	private void init() {

		accountsListView = (ListView) findViewById(R.id.listViewAccounts);
		closeKeyBoard();

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

				Intent intent = new Intent(AllAccountsActivity.this,HomeScreenActivity.class);
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
				showErrorDialog(errorMsg, AllAccountsActivity.this);
			}
		});
	}

	@Override
	public void onGetAccountsFinished(final ArrayList<AccountsModel> accountArrayList) {
		hideProgressDialog();
		Log.e("COOP", "accountArrayList.size()-->>"+ accountArrayList.size());
		if (null != accountArrayList && accountArrayList.size() != 0) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					/*
					 * If Just one account then directly move the user to the
					 * transactions screen, else update the APAPTER
					 */
					if(accountArrayList.size() == 1) {
						String openToBuy = accountArrayList.get(0).getOpenToBuy();
						String spent = accountArrayList.get(0).getSpent();
						int count = Integer.parseInt(accountArrayList.get(0).getTransactionsCount());

						// Set these temporarily
						setOpenToBuy(openToBuy);
						setSpent(spent);
						setAccountPosition(0);
						setTransactionsCount(count);

						// Move to HomeScreen directly without showing Acounts
						// screen
						Intent intent = new Intent(AllAccountsActivity.this,HomeScreenActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
						finish();// Need to finish the ACCOUNTS screen as there is only one account
						
					} else {
						actionBar = getSupportActionBar();
						actionBar.setTitle(getResources().getString(R.string.accounts));
						actionBar.setIcon(R.drawable.icon_coop);
						
						AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, accountArrayList);
						accountsListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
					
//					if (accountArrayList.size() > 1) {
//
//						actionBar = getSupportActionBar();
//						actionBar.setTitle(getResources().getString(R.string.accounts));
//						
//						AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, accountArrayList);
//						accountsListView.setAdapter(adapter);
//						adapter.notifyDataSetChanged();
//
//					} else {
//						String openToBuy = accountArrayList.get(0).getOpenToBuy();
//						String spent = accountArrayList.get(0).getSpent();
//						int count = Integer.parseInt(accountArrayList.get(0).getTransactionsCount());
//
//						// Set these temporarily
//						setOpenToBuy(openToBuy);
//						setSpent(spent);
//						setAccountPosition(0);
//						setTransactionsCount(count);
//
//						// Move to HomeScreen directly without showing Acounts
//						// screen
//						Intent intent = new Intent(AllAccountsActivity.this,HomeScreenActivity.class);
//						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//						
//						finish();// Need to finish the ACCOUNTS screen as there is only one account
//					}
				}
			});
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

	private void showErrorDialog(String msg, Context context) {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				.setTitle(context.getResources().getString(R.string.alert_title))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								
								Intent intent = new Intent(AllAccountsActivity.this, EnterPINCodeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
								startActivity(intent);
								
								finish();
							}
						}).show();
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