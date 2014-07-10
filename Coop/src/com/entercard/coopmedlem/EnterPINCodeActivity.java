package com.entercard.coopmedlem;

import java.util.Date;

import org.kobjects.base64.Base64;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.Controller;
import com.encapsecurity.encap.android.client.api.FinishAuthenticationResult;
import com.encapsecurity.encap.android.client.api.StartAuthenticationResult;
import com.encapsecurity.encap.android.client.api.exception.AuthenticationFailedException;
import com.encapsecurity.encap.android.client.api.exception.InputFormatException;
import com.encapsecurity.encap.android.client.api.exception.LockedException;
import com.entercard.coopmedlem.services.FundsTransferService;
import com.entercard.coopmedlem.services.FundsTransferService.FundsTransferListener;
import com.entercard.coopmedlem.services.InitiateDisputeService;
import com.entercard.coopmedlem.services.InitiateDisputeService.InitiateDisputeListener;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.NetworkHelper;
import com.entercard.coopmedlem.utils.PreferenceHelper;
import com.entercard.coopmedlem.utils.Utils;

public class EnterPINCodeActivity extends BaseActivity implements FundsTransferListener, InitiateDisputeListener{

	private EditText pin1EditText;
	private EditText pin2EditText;
	private EditText pin3EditText;
	private EditText pin4EditText;
	
	private LinearLayout layoutPinContainer;
	private String newPIN = null;
	private Controller controller;
	private EditText dummyEditText;
	private String clientDate = null;
	private int ACTIVITY_RESULT_STATE;
	private StringBuilder stringBuilder;//Will be used in JB and Up devices that will act for an softKeyboard from a TextWatcher
	
	private FundsTransferService fundsTransferService;
	private InitiateDisputeService initiateDisputeService;
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("", "---onResume--");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		init();
		
		ACTIVITY_RESULT_STATE = getIntent().getExtras().getInt(getResources().getString(R.string.pref_verify_pin));
		
		getFocusToDummyEditText();
	
		if (NetworkHelper.isOnline(this)) {
			startAuthentication();
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_internet_connection), this);
		}
	}

	private void init() {
		
		controller =  ((ApplicationEx) getApplication()).getController();
		stringBuilder = new StringBuilder();
		
		pin1EditText = (EditText) findViewById(R.id.txtpinOne);
		pin2EditText = (EditText) findViewById(R.id.txtpinTwo);
		pin3EditText = (EditText) findViewById(R.id.txtpinThree);
		pin4EditText = (EditText) findViewById(R.id.txtpinFour);
		dummyEditText = (EditText) findViewById(R.id.txtDummy);
		layoutPinContainer = (LinearLayout) findViewById(R.id.layoutPinContainer);
		
	}

	private void getFocusToDummyEditText() {
		dummyEditText.requestFocus();
		dummyEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		/**
		 * For JellyBean and above devices we are not able to get the KeyEvents,
		 * so for this we need to have a TextWatcher that will act in place of
		 * keyevents.
		 * http://developer.android.com/reference/android/view/KeyEvent.html
		 */
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
			dummyEditText.addTextChangedListener(new PINTextWatcher());
		} 
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 * Wont give KeyEvents for Number Keys from JelyBean and above
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		Log.i("", "---keyCode--"+keyCode);
	    switch (keyCode) {
	    
	        case KeyEvent.KEYCODE_0:
	        	setTextFromSoftKeyboard("0");
	            return true;
	            
	        case KeyEvent.KEYCODE_1:
	        	setTextFromSoftKeyboard("1");
	            return true;
	            
	        case KeyEvent.KEYCODE_2:
	        	setTextFromSoftKeyboard("2");
	            return true;
	            
	        case KeyEvent.KEYCODE_3:
	        	setTextFromSoftKeyboard("3");
	            return true;
	            
	        case KeyEvent.KEYCODE_4:
	        	setTextFromSoftKeyboard("4");
	            return true;
	            
	        case KeyEvent.KEYCODE_5:
	        	setTextFromSoftKeyboard("5");
	            return true;
	            
	        case KeyEvent.KEYCODE_6:
	        	setTextFromSoftKeyboard("6");
	            return true;
	            
	        case KeyEvent.KEYCODE_7:
	        	setTextFromSoftKeyboard("7");
	            return true;
	            
	        case KeyEvent.KEYCODE_8:
	        	setTextFromSoftKeyboard("8");
	            return true;
	            
	        case KeyEvent.KEYCODE_9:
	        	setTextFromSoftKeyboard("9");
	            return true;
	    
	        case KeyEvent.KEYCODE_DEL:
	        	deleteTextForEditText();
	            return true;
	            
	        case KeyEvent.KEYCODE_BACK:
	        	Log.d("", "------------------BACK PRESSED-------------------");
	    		finish();
	            return true;
	           
	        case KeyEvent.KEYCODE_MENU:
				/*This works but still not a solution to stop the keyboard from hiding. return false not working*/
				showKeyBoard(dummyEditText);
				return true;

	        default:
	            return super.onKeyUp(keyCode, event);
	    }
	}

	/**
	 * TextWatcher for all the PIN editTexts which will be involved for JB 4.1.x
	 * and above devices
	 */
	private class PINTextWatcher implements TextWatcher {

		private PINTextWatcher() {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			//Log.i("COOP", "===afterTextChanged===");
			if(stringBuilder.length() == 0) {
				
				pin1EditText.setText(text);
				pin1EditText.setBackgroundColor(Color.GREEN);
				stringBuilder.append(text);
				
			} else if(stringBuilder.length() == 1) {
				
				pin2EditText.setText(text.substring(1));
				pin2EditText.setBackgroundColor(Color.GREEN);
				stringBuilder.append(text.substring(1));
				
			} else if(stringBuilder.length() == 2) {
				
				pin3EditText.setText(text.substring(2));
				pin3EditText.setBackgroundColor(Color.GREEN);
				stringBuilder.append(text.substring(2));
				
			} else if(stringBuilder.length() == 3) {
				
				pin4EditText.setText(text.substring(3));
				pin4EditText.setBackgroundColor(Color.GREEN);
				stringBuilder.append(text.substring(3));
				
				newPIN = pin1EditText.getText().toString()
						+ pin2EditText.getText().toString()
						+ pin3EditText.getText().toString()
						+ pin4EditText.getText().toString();
				
				Log.i("", "------afterTextChanged--PIN CODE IS----" + newPIN);
				
				//Finish the Authentication process
				finishAuthentication(newPIN);
			} else {
				Log.i("COOP", "===IN ELSEEEE stringBuilder.length()=="+stringBuilder.length());
			}
		}
	}
	/**
	 * 
	 * @param text
	 */
	public void setTextFromSoftKeyboard(String text) {
		
		if(pin1EditText.getText().length()==0) {
			pin1EditText.setText(text);
			pin1EditText.setBackgroundColor(Color.GREEN);
			stringBuilder.append(text);
			
		}else if(pin2EditText.getText().length()==0) {
			pin2EditText.setText(text);
			pin2EditText.setBackgroundColor(Color.GREEN);
			stringBuilder.append(text);
			
		}else if(pin3EditText.getText().length()==0) {
			pin3EditText.setText(text);
			pin3EditText.setBackgroundColor(Color.GREEN);
			stringBuilder.append(text);
			
		}else if(pin4EditText.getText().length()==0) {
			pin4EditText.setText(text);
			pin4EditText.setBackgroundColor(Color.GREEN);
			stringBuilder.append(text);
			
			newPIN = pin1EditText.getText().toString()
					+ pin2EditText.getText().toString()
					+ pin3EditText.getText().toString()
					+ pin4EditText.getText().toString();
			
			Log.i("", "-------setTextFromSoftKeyboard-PIN CODE IS----" + newPIN);
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Finish the Authentication process
			finishAuthentication(newPIN);
		} 
	}

	public void deleteTextForEditText() {
		
		if(pin4EditText.getText().length()>0) {
			pin4EditText.setText("");
			pin4EditText.setBackgroundResource(android.R.drawable.edit_text);
			stringBuilder.deleteCharAt(3).trimToSize();
			
		} else if(pin3EditText.getText().length()>0) {
			pin3EditText.setText("");
			pin3EditText.setBackgroundResource(android.R.drawable.edit_text);
			stringBuilder.deleteCharAt(2).trimToSize();
			
		} else if(pin2EditText.getText().length()>0) {
			pin2EditText.setText("");
			pin2EditText.setBackgroundResource(android.R.drawable.edit_text);
			stringBuilder.deleteCharAt(1).trimToSize();
			
		} else if(pin1EditText.getText().length()>0) {
			stringBuilder.deleteCharAt(0).trimToSize();
			//Log.i("", "---deleteCharAt(0)---"+stringBuilder.toString());
			
			resetPINFields();
			
		} else {
			//TODO
		}
	}
	
	/**
	 * To reset the PIN fields
	 */
	private void resetPINFields() {
		
		pin1EditText.setText(null);
		pin2EditText.setText(null);
		pin3EditText.setText(null);
		pin4EditText.setText(null);
		dummyEditText.setText(null);
		
		pin1EditText.setBackgroundResource(android.R.drawable.edit_text);
		pin2EditText.setBackgroundResource(android.R.drawable.edit_text);
		pin3EditText.setBackgroundResource(android.R.drawable.edit_text);
		pin4EditText.setBackgroundResource(android.R.drawable.edit_text);
		
		stringBuilder = new StringBuilder();
	}
	
	//Shake funcation  for the PIN layout to be shaked if the user enters some wrong PIN
	private void shakePINLayout() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		layoutPinContainer.startAnimation(shake);
		
		resetPINFields();
	}
	
	/**
	 * Start the Authenticatio of the applciation. This process will be done
	 * each time the user starts the app
	 */
	protected void startAuthentication() {
		
		showProgressDialog();
		/**
		 * IMP !!
		 * Need to set the Client only true as the Entercard Authentication is based on Client side setting true for all app authentication
		 * as suggested by the support@encapsecurity.com
		 */
		controller.setClientOnly(true);
		controller.startAuthentication(getClientDate(), new AsyncCallback<StartAuthenticationResult>() {
					public void onFailure(final Throwable throwable) {
						hideProgressDialog();
						Log.i("COOP", ">>>>startAuthentication onFailure>>"+ throwable);
						
						if (throwable instanceof LockedException) {
							retryErrorDialog(getResources().getString(
									R.string.encap_authentication_error));
						} else {
							AlertHelper.Alert(getResources().getString(
											R.string.encap_error),EnterPINCodeActivity.this);
						}
					}
					public void onSuccess(final StartAuthenticationResult result) {
						Log.i("COOP", ">>>startAuthentication onSuccess>>"+ result);
						hideProgressDialog();
					}
				});
		
		/**
		 * 07-01 16:40:14.997: I/COOP(15162): >>>>startAuthentication
		 * onFailure>>
		 * com.encapsecurity.encap.android.client.api.exception.LockedException
		 * [message=Device registration is under lock-down.,
		 * errorCode=serverErrorLockedPinVerificationFailed,
		 * cause=com.encapsecurity.da[message=Device registration is under
		 * lock-down., errorCode=lockedPinVerificationFailed,
		 * localizationKey=server.error.lockedPinVerificationFailed,
		 * localizationArguments=[], com.encapsecurity.dd[fatal=true,
		 * remainingAttempts=0]]]
		 **/
	}
	
	/**
	 * 
	 * @param code
	 */
	protected void finishAuthentication(String code) {
		showProgressDialog();
		controller.finishAuthentication(code,
				new AsyncCallback<FinishAuthenticationResult>() {
					public void onFailure(final Throwable throwable) {

						hideProgressDialog();
						resetPINFields();

						if (throwable instanceof AuthenticationFailedException) {
							
							final int remainingAttempts = ((AuthenticationFailedException) throwable).getRemainingAttempts();
							Log.i("COOP", throwable.getMessage()+ ">>>AuthenticationFailedException>>"+ throwable);
							Log.i("COOP", throwable.getMessage()+ ">>>Attempt Remaining::: "+ remainingAttempts);

							shakePINLayout();

						} else if (throwable instanceof InputFormatException) {
							
							Log.i("COOP", ">>InputFormatException>>" + throwable);
							AlertHelper.Alert(throwable.getLocalizedMessage(),EnterPINCodeActivity.this);
							shakePINLayout();
							
						} else if (throwable instanceof LockedException) {
							Log.i("COOP",">>LockedException>>"+ throwable);
							retryErrorDialog(getResources().getString(R.string.encap_authentication_error));
						} else {
							// Authentication error, cannot retry.
							Log.i("COOP",">>Authentication error, cannot retry>>"+ throwable);
							AlertHelper.Alert(getResources().getString(R.string.encap_error),EnterPINCodeActivity.this);
						}
					}

					public void onSuccess(final FinishAuthenticationResult result) {
						Log.d("COOP", ">>>finishAuthentication onSuccess"+ result);
						hideProgressDialog();
						if (result.hasResponseContent()) {

							String samlData = Base64.encode(result.getResponseContentAsBytes()).replaceAll("(\\r|\\n|\\t)", "");
							Log.i("","-----SAML Length-----" + samlData.length());
							closeKeyBoard();
							/*
							 * Add to TXT file for testing purposes. Remove on
							 * Staging/Deployment Create the SAML token here by
							 * encoding the response to Base64
							 */
							if (ApplicationEx.getInstance().isdeveloperMode) {
								Utils.writeToTextFile(samlData,EnterPINCodeActivity.this, "dump.tmp");
							}
							/*
							 * Store the SAML data as required for the rest of the WS calls and move to the next screen
							 */
							ApplicationEx.getInstance().setSAMLTxt(samlData);
							selectNextActivity(ACTIVITY_RESULT_STATE);
							
						} else {
							AlertHelper.Alert("SAML data not found.",EnterPINCodeActivity.this);
						}
					}
				});
	}
	
	/*
	private void returnResult() {
		setResult(RESULT_OK);
		finish();
	}*/
	
    /**
     * 
     * @param samlData
     * @param activityStatus
     */
	private void selectNextActivity(int activityStatus) {
		
		Log.i("", "---activityStatus-->>>>"+activityStatus);
		String accountNumberTxt = null;
		String messageTxt= null;
		int amountTxt = 0;
		String benificiaryNameTxt = null;
		String transactionID = null;
		String billingAmount = null;
		String description = null;
		String email = null;
		boolean knownTransaction = false;
		String mobile = null;
		String reason = null;
		String transactionDate = null;
		
		if (null != BaseActivity.getSingletonUserDataModelArrayList()
				&& !BaseActivity.getSingletonUserDataModelArrayList().isEmpty()) {

			accountNumberTxt = BaseActivity
					.getSingletonUserDataModelArrayList().get(0)
					.getFundsAccNumer();
			messageTxt = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getFundsMessage();
			amountTxt = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getFundsAmount();
			benificiaryNameTxt = BaseActivity
					.getSingletonUserDataModelArrayList().get(0)
					.getBenificiaryName();
			transactionID = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getDisputetransactionId();
			billingAmount = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getDisputebillingAmount();
			description = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getDisputedescription();
			email = BaseActivity.getSingletonUserDataModelArrayList().get(0)
					.getDisputeEmail();
			knownTransaction = BaseActivity
					.getSingletonUserDataModelArrayList().get(0)
					.isKnownTransaction();
			mobile = BaseActivity.getSingletonUserDataModelArrayList().get(0)
					.getDisputePhone();
			reason = BaseActivity.getSingletonUserDataModelArrayList().get(0)
					.getDisputeReason();
			transactionDate = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getDisputetransactionDate();
		}
		
		switch (activityStatus) {
		case BaseActivity.NO_STATE:
			
			/* Start the PIN code Activity */
			Intent intent = new Intent(EnterPINCodeActivity.this,AllAccountsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			finish();
			break;

		case BaseActivity.DISPUTE:
			
			showProgressDialog();
			initiateDisputeService = new InitiateDisputeService(ApplicationEx.getInstance().getUUID(), 
					ApplicationEx.getInstance().getSAMLTxt(), ApplicationEx.getInstance().getSessionID(), 
					accountNumberTxt, transactionID, billingAmount, description, email, knownTransaction, mobile, reason, transactionDate);
			
			initiateDisputeService.setInitiateDisputeListener(EnterPINCodeActivity.this);
			ApplicationEx.operationsQueue.execute(initiateDisputeService);
			break;
			
		case BaseActivity.TRANSFER_FUNDS:
			/**
			 * Make Webservice call here
			 */
			showProgressDialog();
			fundsTransferService = new FundsTransferService(ApplicationEx.getInstance().getUUID(), 
					ApplicationEx.getInstance().getCookie(), 
					ApplicationEx.getInstance().getSAMLTxt(), 
					ApplicationEx.getInstance().getAccountsArrayList().get(getAccountPosition()).getAccountNumber(), 
					accountNumberTxt, messageTxt, 
					amountTxt, benificiaryNameTxt);
			
			fundsTransferService.setTransactionListener(EnterPINCodeActivity.this);
			ApplicationEx.operationsQueue.execute(fundsTransferService);
			break;

		case BaseActivity.CLI:
			//TODO:
			break;
			
		default:
			break;
		}
	}
	/**
	 * 
	 * @param msg
	 */
	private void retryErrorDialog(String msg) {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setTitle(getResources().getString(R.string.alert_title))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								//Clear the shared preference of all Activation related FLAGS
								PreferenceHelper helper = new PreferenceHelper(EnterPINCodeActivity.this);
								helper.clear();
								
								//Set the Client only to False again to Activate the app
								controller.setClientOnly(false);
								
								dialog.dismiss();
								finish();
								
								Intent intent = new Intent(EnterPINCodeActivity.this, ActivateAppActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						}).show();
	}
	
	/**
	 * @return
	 */
	protected String getClientDate() {
		if (!controller.isOperationInProgress()) {
			clientDate = new Date().toString();
		}
		return clientDate == null ? clientDate : Base64.encode(clientDate.getBytes());
	}
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Log.d("COOP", "-----onOptionsItemSelected-----");
//		return super.onOptionsItemSelected(item);
//	}
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		Log.d("COOP", "-----onCreateOptionsMenu-----");
//		return super.onCreateOptionsMenu(menu);
//	}

	@Override
	public void onFundsTransferSuccess(String resp) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----success-----"+RESULT_OK);
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	@Override
	public void onFundsTransferFailed(final String error) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----error-----"+error);
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	public void onInitiateDisputeFinished(String resp) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----success-----"+RESULT_OK);
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	@Override
	public void onInitiateDisputeFailed(final String error) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----error-----"+error);
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
