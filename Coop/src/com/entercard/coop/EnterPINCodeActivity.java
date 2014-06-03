package com.entercard.coop;

import java.util.Date;

import org.kobjects.base64.Base64;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
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
import com.entercard.coop.utils.StringUtils;
import com.entercard.coop.utils.Utils;

public class EnterPINCodeActivity extends BaseActivity {

	private EditText pin1EditText;
	private EditText pin2EditText;
	private EditText pin3EditText;
	private EditText pin4EditText;
	private String newPIN = null;
	private int isActivated;
	private Handler handler;
	private Controller controller;
	private PreferenceHelper preferenceHelper;
	private EditText dummyEditText;
	private String clientDate = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		handler = new Handler();
		controller =  ((ApplicationEx) getApplication()).getController();
		preferenceHelper = new PreferenceHelper(this);
		
		pin1EditText = (EditText) findViewById(R.id.pin1EditText);
		pin2EditText = (EditText) findViewById(R.id.pin2EditText);
		pin3EditText = (EditText) findViewById(R.id.pin3EditText);
		pin4EditText = (EditText) findViewById(R.id.pin4EditText);
		dummyEditText = (EditText) findViewById(R.id.dummyEditText);
		
		getFocusToDummyEditText();
		
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				pin1EditText.setCursorVisible(false);
//				pin2EditText.setCursorVisible(false);
//				pin3EditText.setCursorVisible(false);
//				pin4EditText.setCursorVisible(false);
//
//				pin1EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
//				pin2EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
//				pin3EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
//				pin4EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
//			}
//		});
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				pin1EditText.setFocusable(false);
//				pin2EditText.setFocusable(false);
//				pin3EditText.setFocusable(false);
//				pin4EditText.setFocusable(false);
//
//				pin1EditText.setFocusableInTouchMode(false);
//				pin2EditText.setFocusableInTouchMode(false);
//				pin3EditText.setFocusableInTouchMode(false);
//				pin4EditText.setFocusableInTouchMode(false);
//			}
//		}, 200);
		
		isActivated = preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated));
		if(isActivated == 1) {
			startAuthentication();
		}
	}

	private void getFocusToDummyEditText() {
		dummyEditText.requestFocus();
		dummyEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		/*Log.i("", "---keyCode--"+keyCode);
		Log.i("", "---getKeyCode--"+event.getKeyCode());
		Log.i("", "---getDisplayLabel--"+event.getDisplayLabel());*/
		
	    switch (keyCode) {
	    
	        case KeyEvent.KEYCODE_0:
	        	setTextForEditField("0");
	            return true;
	            
	        case KeyEvent.KEYCODE_1:
	        	setTextForEditField("1");
	            return true;
	            
	        case KeyEvent.KEYCODE_2:
	        	setTextForEditField("2");
	            return true;
	            
	        case KeyEvent.KEYCODE_3:
	        	setTextForEditField("3");
	            return true;
	            
	        case KeyEvent.KEYCODE_4:
	        	setTextForEditField("4");
	            return true;
	            
	        case KeyEvent.KEYCODE_5:
	        	setTextForEditField("5");
	            return true;
	            
	        case KeyEvent.KEYCODE_6:
	        	setTextForEditField("6");
	            return true;
	            
	        case KeyEvent.KEYCODE_7:
	        	setTextForEditField("7");
	            return true;
	            
	        case KeyEvent.KEYCODE_8:
	        	setTextForEditField("8");
	            return true;
	            
	        case KeyEvent.KEYCODE_9:
	        	setTextForEditField("9");
	            return true;
	            
	        case KeyEvent.KEYCODE_DEL:
	        	deleteTextForEditText();
	            return true;
	            
	        case KeyEvent.KEYCODE_BACK:
	        	Log.i("", "------------------BACK PRESSED-------------------");
	    		handler.removeCallbacks(null);
	    		finish();
	            return true;
	            
	        default:
	            return super.onKeyUp(keyCode, event);
	    }
	}
	
	public void setTextForEditField(String text) {
		
		pin1EditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1),StringUtils.getNumbersInputFilter() });
		pin2EditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1),StringUtils.getNumbersInputFilter() });
		pin3EditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1),StringUtils.getNumbersInputFilter() });
		pin4EditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1),StringUtils.getNumbersInputFilter() });
		
		if(pin1EditText.getText().length()==0) {
			pin1EditText.setText(text);
			pin1EditText.setBackgroundColor(Color.RED);
		}else if(pin2EditText.getText().length()==0) {
			pin2EditText.setText(text);
			pin2EditText.setBackgroundColor(Color.RED);
		}else if(pin3EditText.getText().length()==0) {
			pin3EditText.setText(text);
			pin3EditText.setBackgroundColor(Color.RED);
		}else if(pin4EditText.getText().length()==0) {
			pin4EditText.setText(text);
			pin4EditText.setBackgroundColor(Color.RED);
			
			/*Append all the code here*/
			StringBuffer buffer = new StringBuffer();
			buffer.append(pin1EditText.getText().toString());
			buffer.append(pin2EditText.getText().toString());
			buffer.append(pin3EditText.getText().toString());
			buffer.append(pin4EditText.getText().toString());
			
			Log.i("", "--------PIN CODE IS----" + buffer.toString().trim());
			newPIN = buffer.toString().trim();
			
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
		} else if(pin3EditText.getText().length()>0) {
			pin3EditText.setText("");
			pin3EditText.setBackgroundColor(Color.WHITE);
		} else if(pin2EditText.getText().length()>0) {
			pin2EditText.setText("");
			pin2EditText.setBackgroundColor(Color.WHITE);
		} else if(pin1EditText.getText().length()>0) {
			pin1EditText.setText("");
			pin1EditText.setBackgroundColor(Color.WHITE);
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
						// if (result.getContent().hasData()) {
						// //
						// }
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
                    Log.i("", ""+result.getResponseContent());
                    
                    /* Create the SAML token here by encoding the response to Base64 */
                    String samlClientDate = new String(Base64.encode(result.getResponseContentAsBytes()));
                    Utils.writeToTextFile(samlClientDate, EnterPINCodeActivity.this, "SAML.txt");
                    startAccountsScreen();
                    
		        } else {
		        	//
		        	AlertHelper.Alert("SAML data not found.",EnterPINCodeActivity.this);
		        }
             }
         });
	}
    /**
     * 
     * @param samlTxt
     */
	private void startAccountsScreen() {
			/*Start the PIN code Activity*/
			Intent intent = new Intent(EnterPINCodeActivity.this, AllAccountsActivity.class);
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
	/**
	 * 
	 * @param result
	 * @param message
	 */
//	protected String parseSAMLToken(Result result, StringBuffer message) {
//		
//		String samlTxt = result.getResponseContent().toString();
//		SamlAssertion samlAssertion = new SamlAssertion(samlTxt);
//		String samlClientBase64Data = null;
//		
//		if (clientDate != null) {
//			// Verify against SAML response attribute
//			samlClientBase64Data = samlAssertion.getAttributeValue("SmartDevice.ClientData");
//			Log.i("", "samlClientBase64Data>>>>" + samlClientBase64Data);
//
//			/* Decode the date just to chk */
//			String samlClientDate = new String(Base64.decode(samlClientBase64Data));
//			//Log.i("", "samlClientDate>>>>" + samlClientDate);
//
//			if (!clientDate.equals(samlClientDate)) {
//				throw new RuntimeException("Expected: " + clientDate + ", got: " + samlClientDate);
//			}
//		}
//		return samlClientBase64Data;
//	}
}
