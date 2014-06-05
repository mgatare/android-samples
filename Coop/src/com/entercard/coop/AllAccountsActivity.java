package com.entercard.coop;

import java.util.ArrayList;

import android.os.Bundle;

import com.entercard.coop.helpers.AlertHelper;
import com.entercard.coop.model.AccountsModel;
import com.entercard.coop.services.GetAccountsService;
import com.entercard.coop.services.GetAccountsService.GetAccountsListener;

public class AllAccountsActivity extends BaseActivity implements GetAccountsListener {

	private GetAccountsService accountsService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		accountsService = new GetAccountsService();
		accountsService.setAccountsListener(this);
		ApplicationEx.operationsQueue.execute(accountsService);
		showProgressDialog();
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
	public void onGetAccountsFinished(ArrayList<AccountsModel> accountArrayList) {
		// TODO Auto-generated method stub
		hideProgressDialog();
		
	}
}
