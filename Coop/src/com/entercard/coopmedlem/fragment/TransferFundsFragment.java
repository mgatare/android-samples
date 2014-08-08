package com.entercard.coopmedlem.fragment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.entercard.coopmedlem.BaseActivity;
import com.entercard.coopmedlem.EnterPINCodeActivity;
import com.entercard.coopmedlem.HomeScreenActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.SingletonWebservicesDataModel;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.StringUtils;
import com.entercard.coopmedlem.utils.Utils;

public class TransferFundsFragment extends Fragment {

	private TextView lblHeading;
	private Button btnFundsTransfer;
	private EditText txtMessage;
	private EditText txtReceiversName;
	private EditText txtAccountNumber;
	private EditText txtAmount;
	private HomeScreenActivity parentActivity;
	
	public static TransferFundsFragment newInstance() {
		TransferFundsFragment fragment = new TransferFundsFragment();
		return fragment;
	}

	public TransferFundsFragment() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_transfer_funds, container, false);
		
		init(parentView);
		
		txtAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	        	
	        	String amountTxt = txtAmount.getText().toString();
	        	
				if (!TextUtils.isEmpty(amountTxt)) {
					if (hasFocus) {
						amountTxt = StringUtils.removeCurrencyFormat(amountTxt);
						txtAmount.setText(amountTxt);
						txtAmount.setSelection(txtAmount.getText().length());
					} else {
						amountTxt = StringUtils.formatStringCurrencyAddNorwegianCode(amountTxt);
						txtAmount.setText(amountTxt);
						txtAmount.setSelection(txtAmount.getText().length());
					}
				}
	        }
	    });
		
		btnFundsTransfer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int amount = 0;
				double otbAmount = Double.parseDouble(parentActivity.getOpenToBuy());
				String receiverNameTxt = txtReceiversName.getText().toString();
				String accountNumberTxt = txtAccountNumber.getText().toString();
				String messageTxt = txtMessage.getText().toString();
				
				if(!TextUtils.isEmpty(StringUtils.removeCurrencyFormat(txtAmount.getText().toString())))
					amount = Integer.parseInt(StringUtils.removeCurrencyFormat(txtAmount.getText().toString()));
				
				if (accountNumberTxt.length() < 11 && amount <= 500
						&& TextUtils.isEmpty(receiverNameTxt)) {
					AlertHelper.Alert(getResources().getString(R.string.transfer_funds_validation), parentActivity);
					return;
				}
				if (TextUtils.isEmpty(receiverNameTxt)) {
					AlertHelper.Alert(getResources().getString(R.string.receiver_name_missing), parentActivity);
					return;
				}
				if (accountNumberTxt.length() < 11) {
					AlertHelper.Alert(getResources().getString(R.string.account_number_length), parentActivity);
					return;
				}
				if (amount < 500) {
					AlertHelper.Alert(getResources().getString(R.string.amount_incorrect), parentActivity);
					return;
				}
				/**
				 * EXCEPTION caused for Larger value. Check this.
				 */
				BigInteger bigIntegerOTB = new BigDecimal(otbAmount).toBigInteger();
				BigInteger bigInt = new BigInteger(String.valueOf(amount));
				if (bigInt.intValue() > bigIntegerOTB.intValue()) {
					AlertHelper.Alert(getResources().getString(R.string.amount_cannot_be_higher_than_otb), parentActivity);
					return;
				}
				
				//Toast.makeText(getActivity(), "ALL MILAAAAAAAAAA", Toast.LENGTH_LONG).show();
				
				ArrayList<SingletonWebservicesDataModel> arrayList = new ArrayList<SingletonWebservicesDataModel>();
				SingletonWebservicesDataModel userDataModel = new SingletonWebservicesDataModel();
				userDataModel.setBenificiaryName(receiverNameTxt);
				userDataModel.setFundsAccNumer(accountNumberTxt);
				userDataModel.setFundsAmount(amount);
				userDataModel.setFundsMessage(messageTxt);
				arrayList.add(userDataModel);
				
				/* Clear old contents */
				if(null!=BaseActivity.getSingletonUserDataModelArrayList())
					BaseActivity.getSingletonUserDataModelArrayList().clear();
				
				BaseActivity.setSingletonUserDataModelList(arrayList);
				
				Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(parentActivity.getResources().getString(R.string.pref_verify_pin), BaseActivity.TRANSFER_FUNDS);
				startActivityForResult(intent, 1);
			}
		});
		return parentView;
	}
	
	/**
	 * @param view
	 */
	private void init(View view) {

		parentActivity = (HomeScreenActivity) getActivity();
		lblHeading = (TextView) view.findViewById(R.id.lblHeadingText);
		btnFundsTransfer = (Button) view.findViewById(R.id.btnFundsTransfer);
		txtReceiversName = (EditText) view.findViewById(R.id.txtReceiversName);
		txtAccountNumber = (EditText) view.findViewById(R.id.txtAccountNumber);
		txtAmount = (EditText) view.findViewById(R.id.txtAmount);
		txtMessage = (EditText) view.findViewById(R.id.txtMessage);
		
		lblHeading.setText(StringUtils.getStyledTextFromHtml(getResources().getString(R.string.transfer_funds_title)));
		lblHeading.setMovementMethod(LinkMovementMethod.getInstance());
		lblHeading.setLinkTextColor(getResources().getColor(R.color.text_body));
		
		/*Disable Copy Paste of Edittexts*/
		Utils.disableViewContextMenuOptions(txtAccountNumber);
		Utils.disableViewContextMenuOptions(txtAmount);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				AlertHelper.Alert(getResources().getString(R.string.funds_transfer_success), parentActivity);
				txtReceiversName.setText(null);
				txtAccountNumber.setText(null);
				txtAmount.setText(null);
				txtMessage.setText(null);
			}
	        if (resultCode == Activity.RESULT_CANCELED) {
	            AlertHelper.Alert(getResources().getString(R.string.exception_general), parentActivity);
	        }
	    }
	}
}
