package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entercard.coopmedlem.EmploymentActivity.EmploymentTypeListener;
import com.entercard.coopmedlem.entities.SingletonUserDataModel;
import com.entercard.coopmedlem.fragment.AcceptTermsAndConditionDialogFragment;
import com.entercard.coopmedlem.utils.AlertHelper;

public class CreditLineIncreaseActivity extends BaseActivity implements EmploymentTypeListener {

	private ActivityFinishReceiver finishReceiver;
	
	private LinearLayout linearEmployment;
	private EditText txtYearlyIncome;
	private EditText txtMortgage;
	private EditText txtOtherLoans;
	private TextView lblEmploymentType;
	private TextView lblCreditAmountApplied;
	private Button btnApplyCLI;
	private Button btnPlusCreditLimit;
	private Button btnSubstractCreditLimit;
	
	private static String employmentTxt;
	private int originalCredit;
	private int adjustedCreditLimit;
	private int maxCreditLimit = 150000;
	private int RESULT_CODE = 102;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_creditline_increase);

		init();
		
		regActivityFinishReceiver();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Credit Line Increase");
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

	}
	
	private void init() {
		linearEmployment = (LinearLayout) findViewById(R.id.linearEmployment);
		txtYearlyIncome = (EditText) findViewById(R.id.txtYearlyIncome);
		txtMortgage = (EditText) findViewById(R.id.txtMortgage);
		txtOtherLoans = (EditText) findViewById(R.id.txtOtherLoans);
		lblEmploymentType = (TextView) findViewById(R.id.lblEmploymentType);
		lblCreditAmountApplied = (TextView) findViewById(R.id.lblCreditAmountApplied);
		btnApplyCLI = (Button) findViewById(R.id.btnApplyCLI);
		btnPlusCreditLimit = (Button) findViewById(R.id.btnPlusCreditLimit);
		btnSubstractCreditLimit= (Button) findViewById(R.id.btnSubstractCreditLimit);

		//max limit will be 150K - Credit Limit for that ACCOUNT
		//originalCredit = Integer.parseInt(ApplicationEx.getInstance().getAccountsArrayList().get(getAccountPosition()).getCreditLimit());
		originalCredit = 13000;
		adjustedCreditLimit = 150000 - originalCredit;
		lblCreditAmountApplied.setText("5000");
		
		linearEmployment.setOnClickListener(viewOnCLickListener);
		btnApplyCLI.setOnClickListener(viewOnCLickListener);
		btnPlusCreditLimit.setOnClickListener(viewOnCLickListener);
		btnSubstractCreditLimit.setOnClickListener(viewOnCLickListener);
	}

	/**
	 * OnCLick Listener for the Numbers of Service Center
	 */
	OnClickListener viewOnCLickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Log.e("COOP", "MAX CREDIT LIMIT IS ->>"+adjustedCreditLimit);
			int creditAmountAplied = Integer.parseInt(lblCreditAmountApplied.getText().toString());
			
			switch (v.getId()) {
			case R.id.btnPlusCreditLimit:
				incrementCreditLimit(creditAmountAplied);
				break;

			case R.id.btnSubstractCreditLimit:
				/*if (creditAmountAplied == adjustedCreditLimit) {
					int amount = creditAmountAplied % 5000;
				} else {
					decrementCreditLimit(creditAmountAplied);
				}*/
				decrementCreditLimit(creditAmountAplied);

				break;
			
			case R.id.linearEmployment:
				
				Intent intent = new Intent(CreditLineIncreaseActivity.this, EmploymentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
				
			case R.id.btnApplyCLI:
				
				String yearlyIncomeTxt = txtYearlyIncome.getText().toString();
				String mortgageTxt = txtMortgage.getText().toString();
				String otherLoansTxt = txtOtherLoans.getText().toString();
				String empTypeTxt = lblEmploymentType.getText().toString();
				
				if (TextUtils.isEmpty(yearlyIncomeTxt)
						&& TextUtils.isEmpty(mortgageTxt)
						&& TextUtils.isEmpty(otherLoansTxt)
						&& (TextUtils.isEmpty(empTypeTxt) || empTypeTxt.equalsIgnoreCase("None"))) {
					AlertHelper.Alert(getResources().getString(R.string.credit_line_details_missing), CreditLineIncreaseActivity.this);
					return;
				}
				
				if(TextUtils.isEmpty(yearlyIncomeTxt)) {
					AlertHelper.Alert(getResources().getString(R.string.yearly_income_missing), CreditLineIncreaseActivity.this);
					return;
				}
				if(TextUtils.isEmpty(mortgageTxt)) {
					AlertHelper.Alert(getResources().getString(R.string.mortgage_missing), CreditLineIncreaseActivity.this);
					return;
				}
				if(TextUtils.isEmpty(otherLoansTxt)) {
					AlertHelper.Alert(getResources().getString(R.string.other_loans_missing), CreditLineIncreaseActivity.this);
					return;
				}
				if(TextUtils.isEmpty(empTypeTxt) && empTypeTxt.equalsIgnoreCase("None")) {
					AlertHelper.Alert(getResources().getString(R.string.employment_is_missing), CreditLineIncreaseActivity.this);
					return;
				}
				
				/*
				 * MAKE WS CALLS HERE
				 */
				ArrayList<SingletonUserDataModel> arrayList = new ArrayList<SingletonUserDataModel>();
				SingletonUserDataModel userDataModel = new SingletonUserDataModel();
				
				userDataModel.setYearlyIncome(Integer.parseInt(yearlyIncomeTxt));
				userDataModel.setMortgage(Integer.parseInt(mortgageTxt));
				userDataModel.setOtherLoans(Integer.parseInt(otherLoansTxt));
				userDataModel.setAmountApplied(creditAmountAplied);
				userDataModel.setEmployment(empTypeTxt);
				arrayList.add(userDataModel);
				
				/*Clear old contents*/
				if(null!=BaseActivity.getSingletonUserDataModelArrayList())
					BaseActivity.getSingletonUserDataModelArrayList().clear();
				
				BaseActivity.setSingletonUserDataModelList(arrayList);
				
				DialogFragment termsDialogFragment = AcceptTermsAndConditionDialogFragment.newInstance(1);
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				termsDialogFragment.show(fragmentTransaction,"dailog_terms_and_condition_cli");
				
				break;
				
			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @param amount
	 */
	private void incrementCreditLimit(int amount) {
		if(amount == adjustedCreditLimit)
			return;
		int incresedAmount = amount + 5000;
		if (incresedAmount <= maxCreditLimit) {
			if(incresedAmount > adjustedCreditLimit) {
				lblCreditAmountApplied.setText("" + adjustedCreditLimit);
			} else {
				lblCreditAmountApplied.setText("" + incresedAmount);
			}
		}
	}
	/**
	 * 
	 * @param amount
	 */
	private void decrementCreditLimit(int amount) {
		int incresedAmount = amount - 5000;
		if (incresedAmount >= 5000) {
			lblCreditAmountApplied.setText("" + incresedAmount);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == RESULT_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.d("", " CLI MILAAAAAAAA");
				showDialog();
			}
	        if (resultCode == Activity.RESULT_CANCELED) {
	            AlertHelper.Alert(getResources().getString(R.string.exception_general), CreditLineIncreaseActivity.this);
	        }
	    }
	}
	
	private void showDialog() {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(CreditLineIncreaseActivity.this);
		builder.setTitle(getResources().getString(R.string.application_been_sent))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								txtYearlyIncome.setText(null);
								txtMortgage.setText(null);
								txtOtherLoans.setText(null);
								lblEmploymentType.setText("None");
								finish();
							}
						}).show();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, HomeScreenActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("COOP", "##### onDestroy CLI  #####");
		employmentTxt = null;
		if(null != finishReceiver) {
			unregisterReceiver(finishReceiver);
			finishReceiver = null;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("COOP", "### onResume() ##"+employmentTxt);
		if (!TextUtils.isEmpty(employmentTxt))
			lblEmploymentType.setText(employmentTxt);
		else
			lblEmploymentType.setText("None");
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
	
	@Override
	public void onReturnEmploymentType(final String type) {
		employmentTxt = type;
		Log.d("COOP", "###employmentTxt##"+employmentTxt);
	}
}
