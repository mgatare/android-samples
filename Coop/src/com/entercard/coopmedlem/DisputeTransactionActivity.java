package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.entercard.coopmedlem.entities.SingletonWebservicesDataModel;
import com.entercard.coopmedlem.fragment.AcceptTermsAndConditionDialogFragment;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.StringUtils;

public class DisputeTransactionActivity extends BaseActivity {

	private ToggleButton toogleTransaction;
	private EditText txtReasonDispute;
	private EditText txtEmail;
	private EditText txtPhone;
	private Button btnDisputeTransc;
	private ActionBar actionBar;

	private String amount;
	private String date;
	private String description;
	private String transID;
	private int RESULT_CODE = 101;

	private ActivityFinishReceiver finishReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispute_tranasction);

		init();

		regActivityLogoutReceiver();

		btnDisputeTransc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				closeKeyBoard();

				String reason = txtReasonDispute.getText().toString();
				String email = txtEmail.getText().toString();
				String phone = txtPhone.getText().toString();

				if (TextUtils.isEmpty(reason) && TextUtils.isEmpty(email)&& TextUtils.isEmpty(phone)) {
					AlertHelper.Alert(getResources().getString(R.string.dispute_validation),DisputeTransactionActivity.this);
					return;
				}

				if (TextUtils.isEmpty(reason)) {
					AlertHelper.Alert(getResources().getString(R.string.reason_is_missing),DisputeTransactionActivity.this);
					return;
				}

				if (TextUtils.isEmpty(email)) {
					AlertHelper.Alert(getResources().getString(R.string.email_is_missing),DisputeTransactionActivity.this);
					return;
				} else {
					if (!StringUtils.isValidEmail(email)) {
						AlertHelper.Alert(getResources().getString(R.string.email_is_invalid),DisputeTransactionActivity.this);
						return;
					}
				}

				if (TextUtils.isEmpty(phone)) {
					AlertHelper.Alert(getResources().getString(R.string.phone_is_missing),DisputeTransactionActivity.this);
					return;
				}

				Log.d("COOP", "::amount::" + amount + "::transID::" + transID);
				
				Log.i("", "btnIncreaseCreditLimit>>>>"+ApplicationEx.getInstance().getCookie());
				Log.i("", "btnIncreaseCreditLimit>>>>>"+ApplicationEx.getInstance().getUUID());

				// MAKE WS CALL HERE
				ArrayList<SingletonWebservicesDataModel> arrayList = new ArrayList<SingletonWebservicesDataModel>();
				SingletonWebservicesDataModel userDataModel = new SingletonWebservicesDataModel();
				userDataModel.setFundsAccNumer(ApplicationEx.getInstance()
						.getAccountsArrayList().get(getAccountPosition())
						.getAccountNumber());// NEED TO CHANGE THIS LOGIC
				userDataModel.setDisputebillingAmount(amount);
				userDataModel.setDisputedescription(description);
				userDataModel.setDisputetransactionDate(date);
				userDataModel.setDisputeEmail(email);
				userDataModel.setKnownTransaction(isTransactionKnown());
				userDataModel.setDisputetransactionId(transID);
				userDataModel.setDisputePhone(phone);
				userDataModel.setDisputeReason(reason);

				//Toast.makeText(DisputeTransactionActivity.this, ""+isTransactionKnown(), Toast.LENGTH_LONG).show();
				
				// Clear old contents
				if (null != BaseActivity.getSingletonUserDataModelArrayList())
					BaseActivity.getSingletonUserDataModelArrayList().clear();

				arrayList.add(userDataModel);
				BaseActivity.setSingletonUserDataModelList(arrayList);

				DialogFragment termsDialogFragment = AcceptTermsAndConditionDialogFragment.newInstance(0);
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				termsDialogFragment.show(fragmentTransaction,"dailog_terms_and_condition");
			}
		});
	}

	private void init() {

		actionBar = getSupportActionBar();
		actionBar.setTitle(getResources().getString(R.string.dispute));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setIcon(R.drawable.icon_coop);

		amount = getIntent().getExtras().getString("amount");
		description = getIntent().getExtras().getString("desc");
		date = getIntent().getExtras().getString("date");
		transID = getIntent().getExtras().getString("id");

		toogleTransaction = (ToggleButton) findViewById(R.id.toogleTransaction);
		txtReasonDispute = (EditText) findViewById(R.id.txtReasonDispute);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		btnDisputeTransc = (Button) findViewById(R.id.btnDisputeTransc);
		
		toogleTransaction.setChecked(true);

	}

	private boolean isTransactionKnown() {
		return toogleTransaction.isChecked();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		txtReasonDispute.requestFocus();
		if (requestCode == RESULT_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.d("", "---RESULT_OK----");
				txtReasonDispute.setText(null);
				txtEmail.setText(null); 
				txtPhone.setText(null);
				
				showDialog();
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				Log.d("", "---RESULT_CANCELED----");
				//longToast(getResources().getString(R.string.exception_general));
			}
		} else {
			Log.d("", "---RESULT_NOT FOUND----");
			//longToast(getResources().getString(R.string.exception_general));
		}
	}
	
	private void showDialog() {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(DisputeTransactionActivity.this);
		builder.setMessage(getResources().getString(R.string.you_will_hear_from_us))
				.setTitle(getResources().getString(R.string.dispute_sent))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								finish();
							}
						}).show();
	}

	/**
	 * Reg activity logout receiver.
	 */
	private void regActivityLogoutReceiver() {
		finishReceiver = new ActivityFinishReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(getResources()
				.getString(R.string.tag_act_finish));// ACTION.LOGOUT

		registerReceiver(finishReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
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
	@Override
	protected void onResume() {
		//updateLocalTimeoutStamp();
		super.onResume();
	}
}