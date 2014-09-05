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
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.entercard.coopmedlem.entities.SingletonWebservicesDataModel;
import com.entercard.coopmedlem.fragment.AcceptTermsAndConditionDialogFragment;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.DateUtils;
import com.entercard.coopmedlem.utils.StringUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

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

				if(TextUtils.isEmpty(email)&& TextUtils.isEmpty(phone)) {
					AlertHelper.Alert(getResources().getString(R.string.email_is_missing)
							+"\n"+getResources().getString(R.string.phone_is_missing)
							,DisputeTransactionActivity.this);
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

				//Log.d("COOP", "::amount::" + amount + "::transID::" + transID);
				
				//Log.i("", "btnIncreaseCreditLimit>>>>"+ApplicationEx.getInstance().getCookie());
				//Log.i("", "btnIncreaseCreditLimit>>>>>"+ApplicationEx.getInstance().getUUID());

				// MAKE WS CALL HERE
				ArrayList<SingletonWebservicesDataModel> arrayList = new ArrayList<SingletonWebservicesDataModel>();
				SingletonWebservicesDataModel userDataModel = new SingletonWebservicesDataModel();
				userDataModel.setFundsAccNumer(ApplicationEx.getInstance()
						.getAccountsArrayList().get(getAccountPosition())
						.getAccountNumber());// NEED TO CHANGE THIS LOGIC
				userDataModel.setDisputebillingAmount(amount);
				userDataModel.setDisputedescription(description);
				userDataModel.setDisputetransactionDate(DateUtils.getTransactionTimeStamp(date));
				userDataModel.setDisputeEmail(email);
				userDataModel.setKnownTransaction(isTransactionKnown());
				userDataModel.setDisputetransactionId(transID);
				userDataModel.setDisputePhone(phone);
				userDataModel.setDisputeReason(reason);

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
		actionBar.setDisplayShowHomeEnabled(true);
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
		
		txtReasonDispute.setFilters(new InputFilter[]{StringUtils.getAplhabetsNumbersSpaceInputFilter()});
		
		txtPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String phoneNumbertxt = txtPhone.getText().toString();
				if (!TextUtils.isEmpty(phoneNumbertxt)) {
					if (!hasFocus && phoneNumbertxt.length()==8) {
						// Use the library’s functions
						PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
						PhoneNumber phNumberProto = null;
						try {
							phNumberProto = phoneUtil.parse(phoneNumbertxt, "NO");
	
						} catch (NumberParseException e) {
							Log.e("COOP","NumberParseException was thrown:"+ e.toString());
						}
						// check if the number is valid
						boolean isValid = phoneUtil.isValidNumberForRegion(phNumberProto, "NO");
						Log.d("", "::::::::::isValid::::::::::"+isValid);
						if (isValid) {
							//Log.d("", "INTERNATIONAL--->>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.INTERNATIONAL));
							//Log.d("", "E164--------->>>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.E164));
							//Log.d("", "NATIONAL--->>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL));
							String fomatedPhoneTxt = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL);;
							txtPhone.setText(fomatedPhoneTxt);
						} 
					} else {
						txtPhone.setText(StringUtils.removeBlankSpaces(phoneNumbertxt));
					}
				}
			}
		});
		
//		txtPhone.addTextChangedListener(new TextWatcher() {
//			private boolean mFormatting;
//			private int mAfter;
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				mAfter = after;
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if (!mFormatting) {
//					mFormatting = true;
//					if (mAfter != 0) {
//						// TODO Mayur
//						String phoneNumber = StringUtils.removeBlankSpaces(s.toString());
//						//phoneNumber = StringUtils.removeBlankSpaces(phoneNumber);
//
//						if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length()==8) {
//							
//							// Use the library’s functions
//							PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//							PhoneNumber phNumberProto = null;
//		
//							try {
//								// You can find your country code here
//								// http://www.iso.org/iso/country_names_and_code_elements
//								phNumberProto = phoneUtil.parse(phoneNumber, "NO");
//		
//							} catch (NumberParseException e) {
//								// if there’s any error
//								System.err.println("NumberParseException was thrown:"+ e.toString());
//							}
//		
//							// check if the number is valid
//							boolean isValid = phoneUtil.isValidNumberForRegion(phNumberProto, "NO");
//							
//							Log.d("", "::::::::::isValid::::::::::"+isValid);
//		
//							if (isValid) {
//								// get the valid number’s international format
//								//String internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.INTERNATIONAL);
//								//Toast.makeText(getBaseContext(),"Phone number VALID:" + internationalFormat,Toast.LENGTH_SHORT).show();
//								
//								Log.d("", "INTERNATIONAL--->>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.INTERNATIONAL));
//								Log.d("", "E164--------->>>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.E164));
//								Log.d("", "NATIONAL--->>"+phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL));
//		
//							} else {
//								// prompt the user when the number is invalid
//								Toast.makeText(getBaseContext(),"Phone number is INVALID:" + phoneNumber,Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//					mFormatting = false;
//				}
//			}
//		});

	}

	private boolean isTransactionKnown() {
		return toogleTransaction.isChecked();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.d("COOP", "### android.R.id.home CLI:####");
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        menu.clear();
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
			}
		} else {
			Log.d("", "---RESULT_NOT FOUND----");
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
}