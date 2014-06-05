package com.entercard.coop;

import java.util.ArrayList;
import java.util.Date;

import org.kobjects.base64.Base64;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.Controller;
import com.encapsecurity.encap.android.client.api.FinishActivationResult;
import com.encapsecurity.encap.android.client.api.FinishAuthenticationResult;
import com.encapsecurity.encap.android.client.api.StartAuthenticationResult;
import com.encapsecurity.encap.android.client.api.exception.AuthenticationFailedException;
import com.encapsecurity.encap.android.client.api.exception.InputFormatException;
import com.entercard.coop.helpers.AlertHelper;
import com.entercard.coop.helpers.PreferenceHelper;
import com.entercard.coop.model.AccountsModel;
import com.entercard.coop.services.GetAccountsService;
import com.entercard.coop.services.GetAccountsService.GetAccountsListener;
import com.entercard.coop.utils.Utils;

public class EnterPINCodeActivity extends BaseActivity{

	private EditText pin1EditText;
	private EditText pin2EditText;
	private EditText pin3EditText;
	private EditText pin4EditText;
	private String newPIN = null;
	private int isActivated;
	private Controller controller;
	private PreferenceHelper preferenceHelper;
	private EditText dummyEditText;
	private String clientDate = null;
	private StringBuilder stringBuilder;
	private GetAccountsService accountsService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		controller =  ((ApplicationEx) getApplication()).getController();
		preferenceHelper = new PreferenceHelper(this);
		stringBuilder = new StringBuilder();
		
		pin1EditText = (EditText) findViewById(R.id.pin1EditText);
		pin2EditText = (EditText) findViewById(R.id.pin2EditText);
		pin3EditText = (EditText) findViewById(R.id.pin3EditText);
		pin4EditText = (EditText) findViewById(R.id.pin4EditText);
		dummyEditText = (EditText) findViewById(R.id.dummyEditText);
		
		getFocusToDummyEditText();
		
		isActivated = preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated));
		if(isActivated == 1) {
			startAuthentication();
		}
		
//		 String str = "hello\r\n\tjava\r\nbook";
//		 System.out.println(str);
//		 str = str.replaceAll("(\\r|\\n|\\t)", "");
//		 System.out.println(str);
		
	}

	private void getFocusToDummyEditText() {
		dummyEditText.requestFocus();
		dummyEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		/**
		 * For JelyBean and above devices we are not able to get the KeyEvents,
		 * so for this we need to have a TextWatcher that will act in place of
		 * keyevents.
		 * http://developer.android.com/reference/android/view/KeyEvent.html
		 */
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
			dummyEditText.addTextChangedListener(new PINTextWatcher(dummyEditText));
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
	        	Log.i("", "------------------BACK PRESSED-------------------");
	    		finish();
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

		private View view;

		private PINTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			//Log.i("", "===text===" + text);
			
			if(stringBuilder.length() == 0) {
				
				pin1EditText.setText(text);
				pin1EditText.setBackgroundColor(Color.RED);
				stringBuilder.append(text);
				
			} else if(stringBuilder.length() == 1) {
				
				pin2EditText.setText(text.substring(1));
				pin2EditText.setBackgroundColor(Color.RED);
				stringBuilder.append(text.substring(1));
				
			} else if(stringBuilder.length() == 2) {
				
				pin3EditText.setText(text.substring(2));
				pin3EditText.setBackgroundColor(Color.RED);
				stringBuilder.append(text.substring(2));
				
			} else if(stringBuilder.length() == 3) {
				
				pin4EditText.setText(text.substring(3));
				pin4EditText.setBackgroundColor(Color.RED);
				stringBuilder.append(text.substring(3));
				
				Log.i("", "--------PIN CODE IS----" + stringBuilder.toString().trim());
				newPIN = stringBuilder.toString().trim();
				
				/*Is App is Already activated then Just Authenticate the PIN*/
				if(isActivated != 1) 
					checkPinWithEncap(newPIN);
				else
					finishAuthentication(newPIN);
				
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
			pin1EditText.setBackgroundColor(Color.RED);
			stringBuilder.append(text);
			
		}else if(pin2EditText.getText().length()==0) {
			pin2EditText.setText(text);
			pin2EditText.setBackgroundColor(Color.RED);
			stringBuilder.append(text);
			
		}else if(pin3EditText.getText().length()==0) {
			pin3EditText.setText(text);
			pin3EditText.setBackgroundColor(Color.RED);
			stringBuilder.append(text);
			
		}else if(pin4EditText.getText().length()==0) {
			pin4EditText.setText(text);
			pin4EditText.setBackgroundColor(Color.RED);
			stringBuilder.append(text);
			
			Log.i("", "--------PIN CODE IS----" + stringBuilder.toString().trim());
			newPIN = stringBuilder.toString().trim();
			
			/*Is App is Already activated then Just Authenticate the PIN*/
			if(isActivated != 1) 
				checkPinWithEncap(newPIN);
			else
				finishAuthentication(newPIN);
		} 
	}

	public void deleteTextForEditText() {
		
		if(pin4EditText.getText().length()>0) {
			pin4EditText.setText("");
			pin4EditText.setBackgroundColor(Color.WHITE);
			stringBuilder.deleteCharAt(3).trimToSize();
			//Log.i("", "---deleteCharAt(3)---"+stringBuilder.toString());
			
		} else if(pin3EditText.getText().length()>0) {
			pin3EditText.setText("");
			pin3EditText.setBackgroundColor(Color.WHITE);
			stringBuilder.deleteCharAt(2).trimToSize();
			//Log.i("", "---deleteCharAt(2)---"+stringBuilder.toString());
			
		} else if(pin2EditText.getText().length()>0) {
			pin2EditText.setText("");
			pin2EditText.setBackgroundColor(Color.WHITE);
			stringBuilder.deleteCharAt(1).trimToSize();
			//Log.i("", "---deleteCharAt(1)---"+stringBuilder.toString());
			
		} else if(pin1EditText.getText().length()>0) {
			stringBuilder.deleteCharAt(0).trimToSize();
			//Log.i("", "---deleteCharAt(0)---"+stringBuilder.toString());
			
			stringBuilder = new StringBuilder();
			
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
		
		pin1EditText.setBackgroundColor(Color.WHITE);
		pin2EditText.setBackgroundColor(Color.WHITE);
		pin3EditText.setBackgroundColor(Color.WHITE);
		pin4EditText.setBackgroundColor(Color.WHITE);
		
	}
	
	private void checkPinWithEncap(final String code) {
		
		showProgressDialog();
		
		controller.finishActivation(code,
				new AsyncCallback<FinishActivationResult>() {
					@Override
					public void onFailure(Throwable arg0) {
						Log.i("", ">>>>Finish Activation onFailure" + arg0);
						hideProgressDialog();
						AlertHelper.Alert(arg0.getLocalizedMessage(), EnterPINCodeActivity.this);
					}

					@Override
					public void onSuccess(FinishActivationResult result) {
						Log.i("", ">>>>finish Activation onSuccess" + result);
						hideProgressDialog();
						/*App is Activated sucessfully. Set the Flag to 1=ACTIVATED*/
						preferenceHelper.addInt(getResources().getString(R.string.pref_is_activated), 1);
						startAuthentication();
					}
				});
	}

	protected void startAuthentication() {
		showProgressDialog();
		
		/**
		 * Need to set the Client only true as the Entercard Authentication is based on Client side setting true for all app authentication
		 * as suggested by the support@encapsecurity.com
		 */
		controller.setClientOnly(true);
		controller.startAuthentication(getClientDate(), new AsyncCallback<StartAuthenticationResult>() {
			
					public void onFailure(final Throwable throwable) {
						hideProgressDialog();
						Log.i("COOP", ">>>>startAuthentication onFailure>>"+ throwable);
						AlertHelper.Alert(throwable.getLocalizedMessage(),EnterPINCodeActivity.this);
					}
					
					public void onSuccess(final StartAuthenticationResult result) {
						Log.i("COOP", ">>>startAuthentication onSuccess>>"+ result);
						hideProgressDialog();
						
						if(isActivated !=1) {
							finishAuthentication(newPIN);
						}
					}
				});
	}
	
	protected void finishAuthentication(String code) {
		showProgressDialog();
		Log.i("COOP", ">>>finishAuthentication called");
		controller.finishAuthentication(code, new AsyncCallback<FinishAuthenticationResult>() {
             public void onFailure(final Throwable throwable) {
            	 
            	 hideProgressDialog();
            	 //Log.i("COOP", ">>>finishAuthentication onFailure"+ throwable);
                 
                 if (throwable instanceof AuthenticationFailedException) {
                     // Authentication failed (most likely wrong PIN), retry.
                     final int remainingAttempts = ((AuthenticationFailedException) throwable).getRemainingAttempts();
                     //Log.i("COOP", "AuthenticationFailed. " + remainingAttempts);
                     AlertHelper.Alert(throwable.getLocalizedMessage() +". Remaining attempts "+remainingAttempts,EnterPINCodeActivity.this);
                     resetPINFields();
                     
                 } else if (throwable instanceof InputFormatException) {
                	 //Log.i("COOP", ">>onFailure: throwable" + throwable);
                	 AlertHelper.Alert(throwable.getLocalizedMessage(),EnterPINCodeActivity.this);
                	 
                 } else {
                     // Authentication error, cannot retry.
                	 //Log.i("COOP", ">>Authentication error, cannot retry" + throwable);
                	 AlertHelper.Alert(throwable.getLocalizedMessage(),EnterPINCodeActivity.this);
                 }
             }
             public void onSuccess(final FinishAuthenticationResult result) {
            	Log.d("COOP", ">>>finishAuthentication onSuccess"+ result);
            	hideProgressDialog();

		        if (result.hasResponseContent()) {
		        	
		        	String samlData = Base64.encode(result.getResponseContentAsBytes()).replaceAll("(\\r|\\n|\\t)", "");
		        	Log.i("", "-----SAML Length-----"+samlData.length());
		        	
                    /* Add to TXT file for testing purposes. Remove on Staging/Deployment
                     * Create the SAML token here by encoding the response to Base64 */
		        	
		        	if(ApplicationEx.applicationEx.isdeveloperMode) {
                    	Utils.writeToTextFile(samlData, EnterPINCodeActivity.this, "dump.tmp");
		        	}
                    startAccountsScreen(samlData);
                    
		        } else {
		        	AlertHelper.Alert("SAML data not found.",EnterPINCodeActivity.this);
		        }
             }
         });
	}
    /**
     * 
     * @param samlData 
     * @param samlClientDate 
     * @param samlTxt
     */
	private void startAccountsScreen(String samlData) {
		
		ApplicationEx.applicationEx.setSAMLTxt(samlData);
		
		/* Start the PIN code Activity */
		Intent intent = new Intent(EnterPINCodeActivity.this,AllAccountsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

		finish();
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getClientDate() {
		if (!controller.isOperationInProgress()) {
			clientDate = new Date().toString();
		}
		return clientDate == null ? clientDate : Base64.encode(clientDate.getBytes());
	}
}
