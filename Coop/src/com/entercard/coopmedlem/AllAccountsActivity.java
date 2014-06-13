package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.content.Intent;
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
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		init();
		
		callGetAccountsService();
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
				
				//showDeveloperLog("COOP", ">>Account Position>>"+arg3);
				//showDeveloperLog("COOP", ">>Account Position>>"+arg2);
				
				String openToBuy =  ApplicationEx.applicationEx.getAccountsArrayList().get(arg2).getOpenToBuy();
				String spent =  ApplicationEx.applicationEx.getAccountsArrayList().get(arg2).getSpent();
				
				Intent intent = new Intent(AllAccountsActivity.this, HomeScreenActivity.class);
				intent.putExtra(getResources().getString(R.string.tag_position), arg2);
				intent.putExtra(getResources().getString(R.string.tag_open_to_buy), openToBuy);
				intent.putExtra(getResources().getString(R.string.tag_spent), spent);
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
				accountsListView.setVisibility(View.GONE);
				textViewServerErrorMsg.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onGetAccountsFinished(final ArrayList<AccountsModel> accountArrayList) {
		hideProgressDialog();
		if(null!=accountArrayList && accountArrayList.size()!=0) {
			
			ApplicationEx.applicationEx.setAccountsArrayList(accountArrayList);
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AccountsAdapter adapter = new AccountsAdapter(AllAccountsActivity.this, 0, ApplicationEx.applicationEx.getAccountsArrayList());
					accountsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			});
		} else {
			ApplicationEx.applicationEx.setAccountsArrayList(null);
		}
//		showDeveloperLog("", "ACCOUNT 1::"+accountArrayList.get(0).getAccountNumber());
	}
}
