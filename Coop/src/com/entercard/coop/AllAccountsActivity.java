package com.entercard.coop;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entercard.coop.adapters.AccountsAdapter;
import com.entercard.coop.entities.AccountsModel;
import com.entercard.coop.services.GetAccountsService;
import com.entercard.coop.services.GetAccountsService.GetAccountsListener;
import com.entercard.coop.utils.AlertHelper;

public class AllAccountsActivity extends BaseActivity implements GetAccountsListener {

	private GetAccountsService accountsService;
	private ListView accountsListView;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		init();
		
		//Get all the accounts from service
		accountsService = new GetAccountsService();
		accountsService.setAccountsListener(this);
		ApplicationEx.operationsQueue.execute(accountsService);
		showProgressDialog();
	}

	private void init() {
		
		actionBar = getSupportActionBar();
		actionBar.setTitle("Accounts");
		
		accountsListView = (ListView) findViewById(R.id.accountsListView);
		closeKeyBoard();
		
		accountsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AllAccountsActivity.this, HomeScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onGetAccountsFailed(final String errorMsg) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertHelper.Alert(errorMsg, AllAccountsActivity.this);
			}
		});
	}

	@Override
	public void onGetAccountsFinished(final ArrayList<AccountsModel> accountArrayList) {
		hideProgressDialog();
		if(null!=accountArrayList && accountArrayList.size()!=0) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, accountArrayList);
					accountsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			});
		}
//		showDeveloperLog("", "ACCOUNT 1::"+accountArrayList.get(0).getAccountNumber());
//		showDeveloperLog("", "ACCOUNT 2::"+accountArrayList.get(1).getAccountNumber());
	}
}
