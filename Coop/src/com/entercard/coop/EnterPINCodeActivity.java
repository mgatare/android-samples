package com.entercard.coop;

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
import com.entercard.coop.helpers.AlertHelper;
import com.entercard.coop.helpers.PreferenceHelper;
import com.entercard.coop.utils.StringUtils;

public class EnterPINCodeActivity extends BaseActivity {

	private EditText pin1EditText;
	private EditText pin2EditText;
	private EditText pin3EditText;
	private EditText pin4EditText;
	private Handler handler;
	private Controller controller;
	private PreferenceHelper preferenceHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		handler = new Handler();
		controller = ApplicationEx.applicationEx.getController();
		preferenceHelper = new PreferenceHelper(this);
		
		pin1EditText = (EditText) findViewById(R.id.pin1EditText);
		pin2EditText = (EditText) findViewById(R.id.pin2EditText);
		pin3EditText = (EditText) findViewById(R.id.pin3EditText);
		pin4EditText = (EditText) findViewById(R.id.pin4EditText);
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				pin1EditText.setCursorVisible(false);
				pin2EditText.setCursorVisible(false);
				pin3EditText.setCursorVisible(false);
				pin4EditText.setCursorVisible(false);

				pin1EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
				pin2EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
				pin3EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
				pin4EditText.setRawInputType(Configuration.KEYBOARD_12KEY);
			}
		});

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pin1EditText.setFocusable(false);
				pin2EditText.setFocusable(false);
				pin3EditText.setFocusable(false);
				pin4EditText.setFocusable(false);

				pin1EditText.setFocusableInTouchMode(false);
				pin2EditText.setFocusableInTouchMode(false);
				pin3EditText.setFocusableInTouchMode(false);
				pin4EditText.setFocusableInTouchMode(false);
			}
		}, 200);
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
			
			//Get all the codes to send to Encap
			checkPinWithEncap();
		} 
	}

	public void deleteTextForEditText() {
		
		if(pin4EditText.getText().length()>0) {
			pin4EditText.setText("");
			pin4EditText.setBackgroundColor(Color.WHITE);
		}else if(pin3EditText.getText().length()>0) {
			pin3EditText.setText("");
			pin3EditText.setBackgroundColor(Color.WHITE);
		}else if(pin2EditText.getText().length()>0) {
			pin2EditText.setText("");
			pin2EditText.setBackgroundColor(Color.WHITE);
		}else if(pin1EditText.getText().length()>0) {
			pin1EditText.setText("");
			pin1EditText.setBackgroundColor(Color.WHITE);
		} else {
			//TODO
		}
	}
	
	private void checkPinWithEncap() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(pin1EditText.getText().toString());
		buffer.append(pin2EditText.getText().toString());
		buffer.append(pin3EditText.getText().toString());
		buffer.append(pin4EditText.getText().toString());
		
		Log.i("", "--------PIN CODE IS----" + buffer.toString().trim());
		String code = buffer.toString().trim();

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
						
						/*Start the PIN code Activity*/
						Intent intent = new Intent(EnterPINCodeActivity.this, AccountsActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
						/*App is Activated sucessfully*/
						preferenceHelper.addInt(getResources().getString(R.string.pref_is_activated), 1);
						
						//finish();
					}
				});
	}
}
