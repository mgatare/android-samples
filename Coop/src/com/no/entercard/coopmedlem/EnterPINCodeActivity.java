package com.no.entercard.coopmedlem;

import java.math.BigInteger;
import java.util.Date;

import org.kobjects.base64.Base64;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.encapsecurity.encap.android.client.api.exception.UnknownRegistrationException;
import com.no.entercard.coopmedlem.services.CreditLineIncreaseService;
import com.no.entercard.coopmedlem.services.CreditLineIncreaseService.CreditLineIncreaseListener;
import com.no.entercard.coopmedlem.services.FundsTransferService;
import com.no.entercard.coopmedlem.services.FundsTransferService.FundsTransferListener;
import com.no.entercard.coopmedlem.services.InitiateDisputeService;
import com.no.entercard.coopmedlem.services.InitiateDisputeService.InitiateDisputeListener;
import com.no.entercard.coopmedlem.utils.CompatibilityUtils;
import com.no.entercard.coopmedlem.utils.DateUtils;
import com.no.entercard.coopmedlem.utils.NetworkHelper;
import com.no.entercard.coopmedlem.utils.PreferenceHelper;
import com.no.entercard.coopmedlem.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class EnterPINCodeActivity.
 */
public class EnterPINCodeActivity extends BaseActivity implements FundsTransferListener, InitiateDisputeListener, CreditLineIncreaseListener{

	/** The pin1 edit text. */
	private EditText pin1EditText;
	
	/** The pin2 edit text. */
	private EditText pin2EditText;
	
	/** The pin3 edit text. */
	private EditText pin3EditText;
	
	/** The pin4 edit text. */
	private EditText pin4EditText;
	
	/** The layout pin container. */
	private LinearLayout layoutPinContainer;
	
	/** The new pin. */
	private String newPIN = null;
	
	/** The controller. */
	private Controller controller;
	
	/** The dummy edit text. */
	private EditText dummyEditText;
	
	/** The client date. */
	private String clientDate = null;
	
	/** The activity state. */
	private int ACTIVITY_STATE;
	
	/** The string builder. */
	private StringBuilder stringBuilder;//Will be used in JB 4.1.X and Up devices that will act for an softKeyboard from a TextWatcher
	
	/** The funds transfer service. */
	private FundsTransferService fundsTransferService;
	
	/** The initiate dispute service. */
	private InitiateDisputeService initiateDisputeService;
	
	/** The credit line increase service. */
	private CreditLineIncreaseService creditLineIncreaseService;
	
	/** The action bar. */
	private ActionBar actionBar;
	
	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		init();
		
		ACTIVITY_STATE = getIntent().getExtras().getInt(getResources().getString(R.string.pref_verify_pin));
		
		getFocusToDummyEditText();
	
		if (NetworkHelper.isOnline(this)) {
			startAuthentication();
		} else {
//			AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong),
//					getResources().getString(R.string.no_internet_connection), this);
			getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
					getResources().getString(R.string.no_internet_connection), this);
			
		}
	}

	/**
	 * Inits the.
	 */
	private void init() {
		
		controller =  ((ApplicationEx) getApplication()).getController();
		stringBuilder = new StringBuilder();
		
		pin1EditText = (EditText) findViewById(R.id.txtpinOne);
		pin2EditText = (EditText) findViewById(R.id.txtpinTwo);
		pin3EditText = (EditText) findViewById(R.id.txtpinThree);
		pin4EditText = (EditText) findViewById(R.id.txtpinFour);
		dummyEditText = (EditText) findViewById(R.id.txtDummy);
		layoutPinContainer = (LinearLayout) findViewById(R.id.layoutPinContainer);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(getResources().getString(R.string.enter_pin_code));
		actionBar.setIcon(R.drawable.icon_coop);
		
		/**
		 * IMP!! INITIALIZE WATCHER ONLY ONCE 
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

	/**
	 * Gets the focus to dummy edit text.
	 *
	 * @return the focus to dummy edit text
	 */
	private void getFocusToDummyEditText() {
		dummyEditText.requestFocus();
		dummyEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
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
				return false;

	        default:
	            return super.onKeyUp(keyCode, event);
	    }
	}

	/**
	 * TextWatcher for all the PIN editTexts which will be involved for JB 4.1.x
	 * and above devices
	 */
	private class PINTextWatcher implements TextWatcher {

		/**
		 * Instantiates a new PIN text watcher.
		 */
		private PINTextWatcher() {
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
		 */
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		 */
		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			
			if(stringBuilder.length() == 0) {
				
				pin1EditText.setText(text);
				pin1EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
				stringBuilder.append(text);
				
			} else if(stringBuilder.length() == 1) {
				
				pin2EditText.setText(text.substring(1));
				pin2EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
				stringBuilder.append(text.substring(1));
				
			} else if(stringBuilder.length() == 2) {
				
				pin3EditText.setText(text.substring(2));
				pin3EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
				stringBuilder.append(text.substring(2));
				
			} else if(stringBuilder.length() == 3) {
				
				pin4EditText.setText(text.substring(3));
				pin4EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
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
	 * Sets the text from soft keyboard.
	 *
	 * @param text the new text from soft keyboard
	 */
	public void setTextFromSoftKeyboard(String text) {
		
		if(pin1EditText.getText().length()==0) {
			pin1EditText.setText(text);
			pin1EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
			stringBuilder.append(text);
			
		}else if(pin2EditText.getText().length()==0) {
			pin2EditText.setText(text);
			pin2EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
			stringBuilder.append(text);
			
		}else if(pin3EditText.getText().length()==0) {
			pin3EditText.setText(text);
			pin3EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
			stringBuilder.append(text);
			
		}else if(pin4EditText.getText().length()==0) {
			pin4EditText.setText(text);
			pin4EditText.setBackgroundResource(R.drawable.view_circular_background_filled_green);
			stringBuilder.append(text);
			
			newPIN = pin1EditText.getText().toString()
					+ pin2EditText.getText().toString()
					+ pin3EditText.getText().toString()
					+ pin4EditText.getText().toString();
			
			Log.i("", "-------setTextFromSoftKeyboard-PIN CODE IS----" + newPIN);
			
			//Finish the Authentication process
			finishAuthentication(newPIN);
		} 
	}

	/**
	 * Delete text for edit text.
	 */
	public void deleteTextForEditText() {
		
		if(pin4EditText.getText().length()>0) {
			pin4EditText.setText("");
			pin4EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
			stringBuilder.deleteCharAt(3).trimToSize();
			
		} else if(pin3EditText.getText().length()>0) {
			pin3EditText.setText("");
			pin3EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
			stringBuilder.deleteCharAt(2).trimToSize();
			
		} else if(pin2EditText.getText().length()>0) {
			pin2EditText.setText("");
			pin2EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
			stringBuilder.deleteCharAt(1).trimToSize();
			
		} else if(pin1EditText.getText().length()>0) {
			stringBuilder.deleteCharAt(0).trimToSize();
			resetPINFields();
			
		} 
	}
	
	/**
	 * To reset the PIN fields.
	 */
	private void resetPINFields() {
		
		pin1EditText.setText(null);
		pin2EditText.setText(null);
		pin3EditText.setText(null);
		pin4EditText.setText(null);
		dummyEditText.setText(null);
		
		pin1EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
		pin2EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
		pin3EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
		pin4EditText.setBackgroundResource(R.drawable.view_circular_background_transparent);
		
		stringBuilder = new StringBuilder();
	}
	
	//Shake function  for the PIN layout to be shaked if the user enters some wrong PIN
	/**
	 * Shake pin layout.
	 */
	private void shakePINLayout() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		layoutPinContainer.startAnimation(shake);
		
		resetPINFields();
		
		getFocusToDummyEditText();
	}
	
	/**
	 * Start the Authentication of the application. This process will be done
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
		controller.startAuthentication(getClientDate(),
				new AsyncCallback<StartAuthenticationResult>() {
					public void onFailure(final Throwable throwable) {
						hideProgressDialog();
						Log.i("COOP", ">>>>startAuthentication onFailure>>"+ throwable);
						
						if (throwable instanceof LockedException) {
							
							retryErrorDialog(getResources().getString(R.string.encap_authentication_error));
							
						} else if (throwable instanceof UnknownRegistrationException) {
							/*retryErrorDialog(getResources().getString(
									R.string.encap_error));*/
							getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error), EnterPINCodeActivity.this);
						} else {
							/*AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error)
									,EnterPINCodeActivity.this);*/
							getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error), EnterPINCodeActivity.this);
						}
					}
					public void onSuccess(final StartAuthenticationResult result) {
						Log.i("COOP", ">>>startAuthentication onSuccess>>"+ result);
						hideProgressDialog();
						
						if(ACTIVITY_STATE != BaseActivity.TYPE_NONE) {
							PreferenceHelper preferenceHelper = new PreferenceHelper(EnterPINCodeActivity.this);
							boolean isDeviceSesnExpired = Utils.isDeviceSessionExpired(
									preferenceHelper.getString(getResources().getString(R.string.pref_device_session))
									,DateUtils.getCurrentTimeStamp());
							//Logout App Dialog
							if(isDeviceSesnExpired)
								logoutAppDialog();
						} 
					}
				});
	}
	
	/**
	 * ---------------- DEFINE THIS CALL------------------.
	 */
	private void logoutAppDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(EnterPINCodeActivity.this);
		builder.setMessage(getResources().getString(R.string.encap_error))
				.setTitle(getResources().getString(R.string.encap_something_went_wrong))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@SuppressLint("InlinedApi") 
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								//Restart the activity
								finish();
								//startActivity(getIntent());
								
								ApplicationEx.getInstance().clearGlobalContents();
								
								Log.d("", ">>>>>>>>>>>>>>>>>>>>>APP LEVEL TIMEOUTT HAPENNED APP WILL LOGOUT >>>>>>>>>>>");
								
								if(CompatibilityUtils.getSdkVersion() < 11) {
									Intent intent = new Intent(getApplicationContext(), CustomerServiceScreen.class);
									ComponentName cn = intent.getComponent();
									Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
									startActivity(mainIntent);
								} else {
									Intent intent = new Intent(EnterPINCodeActivity.this, CustomerServiceScreen.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							}
						}).show();
	}
	
	/**
	 * Finish authentication.
	 *
	 * @param code the code
	 */
	protected void finishAuthentication(String code) {
		showProgressDialog();
		//closeKeyBoard(); Dont do this...coz this will never show the keyboard again
		
		controller.finishAuthentication(code,
				new AsyncCallback<FinishAuthenticationResult>() {
					public void onFailure(final Throwable throwable) {

						hideProgressDialog();
						resetPINFields();

						if (throwable instanceof AuthenticationFailedException) {
							
							int remainingAttempts = ((AuthenticationFailedException) throwable).getRemainingAttempts();
							Log.i("COOP", throwable.getMessage()+ ">>>AuthenticationFailedException>>"+ throwable);
							Log.i("COOP", throwable.getMessage()+ ">>>Attempt Remaining::: "+ remainingAttempts);

							shakePINLayout();

						} else if (throwable instanceof InputFormatException) {
							
							Log.i("COOP", ">>InputFormatException>>" + throwable);
							//AlertHelper.Alert(throwable.getLocalizedMessage(),EnterPINCodeActivity.this);
							getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error), EnterPINCodeActivity.this);
							
							shakePINLayout();
							
						} else if (throwable instanceof LockedException) {
							Log.i("COOP",">>LockedException>>"+ throwable);
							retryErrorDialog(getResources().getString(R.string.encap_authentication_error));
						} else {
							// Authentication error, cannot retry.
							Log.i("COOP",">>Authentication error, cannot retry>>"+ throwable);
//							AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong),
//									getResources().getString(R.string.encap_error),EnterPINCodeActivity.this);
							getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error), EnterPINCodeActivity.this);
						}
					}

					public void onSuccess(final FinishAuthenticationResult result) {
						Log.d("COOP", ">>>finishAuthentication onSuccess"+ result);
						hideProgressDialog();
						if (result.hasResponseContent()) {

							String samlData = Base64.encode(result.getResponseContentAsBytes()).replaceAll("(\\r|\\n|\\t)", "");
							Log.i("","-----SAML Length-----" + samlData.length());
							closeKeyBoard();
							/**
							 * Add to TXT file for testing purposes. Remove on
							 * Staging/Deployment Create the SAML token here by
							 * encoding the response to Base64
							 */
							// if (ApplicationEx.getInstance().isdeveloperMode)
							// {
							// Utils.writeToTextFile(samlData,EnterPINCodeActivity.this,
							// "dump.tmp");
							// }
							/*SET the SAML data as required for the rest of the WS calls and move to the next screen*/
							ApplicationEx.getInstance().setSAMLTxt(samlData);
							selectNextActivity(ACTIVITY_STATE);
							
							/**UPDATE THE TIME-STAMP TO MAIN DEVICE SESSION**/
							PreferenceHelper preferenceHelper = new PreferenceHelper(EnterPINCodeActivity.this);
							preferenceHelper.addString(getResources().getString(R.string.pref_device_session), DateUtils.getCurrentTimeStamp());
							Log.i("", ">>>>>>>>>>>>>>>>>>>>DATE TIME SESSION UPPPDATEEEEED>>>>>>>>>>>>>>>>>>>>>");
							
						} else {
							getToCustomerScreenDialog(getResources().getString(R.string.encap_something_went_wrong),
									getResources().getString(R.string.encap_error), EnterPINCodeActivity.this);
						}
					}
				});
	}
    
    /**
     * Select next activity.
     *
     * @param activityStatus the activity status
     */
	private void selectNextActivity(int activityStatus) {
		
		Log.i("", "---activityStatus-->>>>"+activityStatus);
		String fundsTransferAccountNoTxt = null;
		String messageTxt= null;
		BigInteger amount = null;
		String benificiaryNameTxt = null;
		String transactionID = null;
		String billingAmount = null;
		String description = null;
		String email = null;
		boolean knownTransaction = false;
		String mobile = null;
		String reason = null;
		String transactionDate = null;
		
		BigInteger yearlyIncome = null;
		BigInteger mortgage = null;
		BigInteger otherLoans = null;
		BigInteger creditAmountAplied = null;
		String employmentType = null;
		
		if (null != BaseActivity.getSingletonUserDataModelArrayList()
				&& !BaseActivity.getSingletonUserDataModelArrayList().isEmpty()) {

			fundsTransferAccountNoTxt = BaseActivity
					.getSingletonUserDataModelArrayList().get(0)
					.getFundsAccNumer();
			messageTxt = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getFundsMessage();
			amount = BaseActivity.getSingletonUserDataModelArrayList()
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
			
			yearlyIncome = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getYearlyIncome();
			mortgage = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getMortgage();
			otherLoans = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getOtherLoans();
			creditAmountAplied = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getAmountApplied();
			employmentType = BaseActivity.getSingletonUserDataModelArrayList()
					.get(0).getEmployment();
		}
		
		switch (activityStatus) {
		case BaseActivity.TYPE_NONE:
			
			/* Start the PIN code Activity */
			Intent intent = new Intent(EnterPINCodeActivity.this, AllAccountsActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			/**
			 * This is a workaround FLAG for progress bar animation on the
			 * homescreen to be shown only once in a session
			 **/
			BaseActivity.setFirstVisit(true);
			
			EnterPINCodeActivity.this.finish();
			break;

		case BaseActivity.TYPE_DISPUTE:
			/**
			 * Make Webservice call here
			 */
			showProgressDialog();
			initiateDisputeService = new InitiateDisputeService(ApplicationEx.getInstance().getUUID(), 
					ApplicationEx.getInstance().getSAMLTxt(), ApplicationEx.getInstance().getCookie(), 
					ApplicationEx.getInstance().getAccountsArrayList().get(getAccountPosition()).getAccountNumber(),
					transactionID, billingAmount, description, email, knownTransaction, mobile, reason, transactionDate);
			
			initiateDisputeService.setInitiateDisputeListener(EnterPINCodeActivity.this);
			ApplicationEx.operationsQueue.execute(initiateDisputeService);
			break;
			
		case BaseActivity.TYPE_TRANSFER_FUNDS:
			
			showProgressDialog();
			fundsTransferService = new FundsTransferService(ApplicationEx.getInstance().getUUID(), 
					ApplicationEx.getInstance().getCookie(), 
					ApplicationEx.getInstance().getSAMLTxt(), 
					ApplicationEx.getInstance().getAccountsArrayList().get(getAccountPosition()).getAccountNumber(), 
					fundsTransferAccountNoTxt, messageTxt, 
					amount, benificiaryNameTxt);
			
			fundsTransferService.setTransactionListener(EnterPINCodeActivity.this);
			ApplicationEx.operationsQueue.execute(fundsTransferService);
			break;

		case BaseActivity.TYPE_CLI:
			
			showProgressDialog();
			creditLineIncreaseService = new CreditLineIncreaseService(
					ApplicationEx.getInstance().getUUID(), ApplicationEx
							.getInstance().getCookie(), ApplicationEx
							.getInstance().getSAMLTxt(), ApplicationEx
							.getInstance().getAccountsArrayList()
							.get(getAccountPosition()).getAccountNumber(),
					mortgage, creditAmountAplied, yearlyIncome, otherLoans,
					employmentType);
			
			creditLineIncreaseService.setTransactionListener(EnterPINCodeActivity.this);
			ApplicationEx.operationsQueue.execute(creditLineIncreaseService);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Retry error dialog.
	 *
	 * @param msg the msg
	 */
	private void retryErrorDialog(String msg) {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setTitle(getResources().getString(R.string.alert_title))
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@SuppressLint("InlinedApi") 
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								closeKeyBoard();
								
								//Clear the shared preference of all Activation related FLAGS
								PreferenceHelper helper = new PreferenceHelper(EnterPINCodeActivity.this);
								helper.clear();
								
								//Set the Client only to False again to Activate the app
								controller.setClientOnly(false);
								
								dialog.dismiss();
								finish();
								
								if(CompatibilityUtils.getSdkVersion() < 11) {
									Intent intent = new Intent(getApplicationContext(), ActivateAppActivity.class);
									ComponentName cn = intent.getComponent();
									Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
									startActivity(mainIntent);
								} else {
									Intent intent = new Intent(EnterPINCodeActivity.this, ActivateAppActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							}
						}).show();
	}
	
	/**
	 * Gets the to customer screen dialog.
	 *
	 * @param title the title
	 * @param msg the msg
	 * @param context the context
	 * @return the to customer screen dialog
	 */
	public void getToCustomerScreenDialog(String title, String msg, Context context) {
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								
								Intent intent = new Intent(EnterPINCodeActivity.this, CustomerServiceScreen.class);
								//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
								
								EnterPINCodeActivity.this.finish();
								
							}
						}).show();

	}
	
	/**
	 * Gets the client date.
	 *
	 * @return the client date
	 */
	protected String getClientDate() {
		if (!controller.isOperationInProgress()) {
			clientDate = new Date().toString();
		}
		return clientDate == null ? clientDate : Base64.encode(clientDate.getBytes());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_call:
			Log.d("COOP", "-----action_call PRESSED-----");
			
			closeKeyBoard();
			
			Intent intent = new Intent(EnterPINCodeActivity.this, CustomerServiceScreen.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.abc_slide_in_top, 0);
			
			EnterPINCodeActivity.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(ACTIVITY_STATE == BaseActivity.TYPE_NONE) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.activity_enterpin_actions, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.FundsTransferService.FundsTransferListener#onFundsTransferSuccess(java.lang.String)
	 */
	@Override
	public void onFundsTransferSuccess(String resp) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----success-----"+RESULT_OK);
				setResult(RESULT_OK);
				EnterPINCodeActivity.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.FundsTransferService.FundsTransferListener#onFundsTransferFailed(java.lang.String)
	 */
	@Override
	public void onFundsTransferFailed(final String error) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----error-----"+error);
				setResult(RESULT_CANCELED);
				EnterPINCodeActivity.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.InitiateDisputeService.InitiateDisputeListener#onInitiateDisputeFinished(java.lang.String)
	 */
	@Override
	public void onInitiateDisputeFinished(String resp) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----success-----"+RESULT_OK);
				setResult(RESULT_OK);
				EnterPINCodeActivity.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.InitiateDisputeService.InitiateDisputeListener#onInitiateDisputeFailed(java.lang.String)
	 */
	@Override
	public void onInitiateDisputeFailed(final String error) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----error-----"+error);
				setResult(RESULT_CANCELED);
				EnterPINCodeActivity.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.CreditLineIncreaseService.CreditLineIncreaseListener#onCreditLineIncreaseSuccess(java.lang.String)
	 */
	@Override
	public void onCreditLineIncreaseSuccess(String resp) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----success-----"+RESULT_OK);
				setResult(RESULT_OK);
				EnterPINCodeActivity.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.CreditLineIncreaseService.CreditLineIncreaseListener#onCreditLineIncreaseFailed(java.lang.String)
	 */
	@Override
	public void onCreditLineIncreaseFailed(final String error) {
		hideProgressDialog();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("COOP", "-----error-----"+error);
				setResult(RESULT_CANCELED);
				EnterPINCodeActivity.this.finish();
			}
		});
	}
}