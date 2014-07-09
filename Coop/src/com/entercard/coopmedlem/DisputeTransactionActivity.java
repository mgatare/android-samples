package com.entercard.coopmedlem;

import java.util.ArrayList;

import com.entercard.coopmedlem.entities.SingletonUserDataModel;
import com.entercard.coopmedlem.utils.AlertHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class DisputeTransactionActivity extends BaseActivity {

	private ToggleButton toogleTransaction;
	private EditText txtReasonDispute;
	private EditText txtEmail;
	private EditText txtPhone;
	private Button btnIncreaseCreditLimit;
	private ActionBar actionBar;
	
	private String amount;
	private String date;
	private String description;
	private String transID;
	private int RESULT_CODE = 101;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispute_tranasction);

		init();
		
		btnIncreaseCreditLimit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String reason = txtReasonDispute.getText().toString();
				String email = txtEmail.getText().toString();
				String phone = txtPhone.getText().toString();
				
				if(TextUtils.isEmpty(reason) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
					AlertHelper.Alert(getResources().getString(R.string.dispute_validation), DisputeTransactionActivity.this);
					return;
				}
				
				if(TextUtils.isEmpty(reason)) {
					AlertHelper.Alert(getResources().getString(R.string.reason_is_missing), DisputeTransactionActivity.this);
					return;
				}
				
				if(TextUtils.isEmpty(email)) {
					AlertHelper.Alert(getResources().getString(R.string.email_is_missing), DisputeTransactionActivity.this);
					return;
				}
				
				if(TextUtils.isEmpty(phone)) {
					AlertHelper.Alert(getResources().getString(R.string.phone_is_missing), DisputeTransactionActivity.this);
					return;
				}
				
				/**{
			    "dispute": {
			        "billingAmount": Number,
			        "description": String,
			        "email": String,
			        "knownTransaction": boolean,
			        "mobile": String,
			        "reason": String,
			        "transactionDate": String
			    }
			}**/
				
				/*MAKE WS CALL HERE*/
				ArrayList<SingletonUserDataModel> arrayList = new ArrayList<SingletonUserDataModel>();
				SingletonUserDataModel userDataModel = new SingletonUserDataModel();
				userDataModel.setDisputebillingAmount(amount);
				userDataModel.setDisputedescription(description);
				userDataModel.setDisputetransactionDate(date);
				userDataModel.setDisputeEmail(email);
				userDataModel.setKnownTransaction(isTransactionKnown());
				userDataModel.setDisputePhone(phone);
				userDataModel.setDisputeReason(reason);
				
				
				/*Clear old contents*/
				if(null!=BaseActivity.getSingletonUserDataModelArrayList())
					BaseActivity.getSingletonUserDataModelArrayList().clear();
				
				BaseActivity.setSingletonUserDataModelList(arrayList);
				
				Intent intent = new Intent(DisputeTransactionActivity.this, EnterPINCodeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.DISPUTE);
				startActivityForResult(intent, RESULT_CODE);
			}
		});
	}

	private void init() {

		actionBar = getSupportActionBar();
		actionBar.setTitle("Dispute transaction");
		amount = getIntent().getExtras().getString("amount");
		description = getIntent().getExtras().getString("desc");
		date = getIntent().getExtras().getString("date");
		transID = getIntent().getExtras().getString("id");
		
		toogleTransaction = (ToggleButton) findViewById(R.id.toogleTransaction);
		txtReasonDispute = (EditText) findViewById(R.id.txtReasonDispute);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		btnIncreaseCreditLimit = (Button) findViewById(R.id.btnIncreaseCreditLimit);

	}
	
	private boolean isTransactionKnown() {
		return toogleTransaction.isChecked();
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == RESULT_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.d("", "---RESULT_OK----");
				
			}
	        if (resultCode == Activity.RESULT_CANCELED) {
	           // AlertHelper.Alert(getResources().getString(R.string.exception_general), parentActivity);
	        	Log.d("", "---RESULT_CANCELED----");
	        }
	    } else {
	    	Log.d("", "---RESULT_NOT FOUND----");
	    }
	}
}