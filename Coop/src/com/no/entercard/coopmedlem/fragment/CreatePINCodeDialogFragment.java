package com.no.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.FinishActivationResult;
import com.encapsecurity.encap.android.client.api.exception.SessionException;
import com.encapsecurity.encap.android.client.api.exception.UnexpectedException;
import com.no.entercard.coopmedlem.ActivateAppActivity;
import com.no.entercard.coopmedlem.ActivateAppActivity.ActivateAppFragment;
import com.no.entercard.coopmedlem.BaseActivity;
import com.no.entercard.coopmedlem.EnterPINCodeActivity;
import com.no.entercard.coopmedlem.R;
import com.no.entercard.coopmedlem.R.style;
import com.no.entercard.coopmedlem.utils.AlertHelper;
import com.no.entercard.coopmedlem.utils.DateUtils;
import com.no.entercard.coopmedlem.utils.NetworkHelper;
import com.no.entercard.coopmedlem.utils.PreferenceHelper;

/**
 * @author mgatare
 */
public class CreatePINCodeDialogFragment extends DialogFragment {

	private ActivateAppActivity parentActivity;
	private EditText txtCreatePIN;
	private Button positiveButton;
	
    public static CreatePINCodeDialogFragment newInstance() {
        CreatePINCodeDialogFragment frag = new CreatePINCodeDialogFragment();
        return frag;
    }
    
	@Override
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
			positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setEnabled(false);
		}
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(0, style.Theme_Coopmedlem);
		
		parentActivity = (ActivateAppActivity) getActivity();
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.fragment_dialog_create_pin, null);
		txtCreatePIN = (EditText) innerView.findViewById(R.id.txtCreatePIN);
		
		// Show soft keyboard automatically
		//txtCreatePIN.requestFocus();
		
		AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
		
		builder.setTitle(getResources().getString(R.string.create_your_pin_code))
				.setView(innerView)
				.setPositiveButton(getResources().getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
						/***/
						parentActivity.closeKeyBoard();
						
						String code = txtCreatePIN.getText().toString();
						if(!TextUtils.isEmpty(code)) {
						
							boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
							if(isNetworkAvailable) {
							
								hideDelayedKeyboard();
								parentActivity.showProgressDialog();
								
								parentActivity.controller.finishActivation(code, new AsyncCallback<FinishActivationResult>() {
									@Override
									public void onFailure(Throwable arg0) {
										Log.i("COOP", ">>Finish Activation onFailure>>" + arg0);
										parentActivity.hideProgressDialog();

										if (isAdded() == true) {
											//AlertHelper.Alert(getResources().getString(R.string.encap_unknown_registration_error), parentActivity);
											AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong), 
													getResources().getString(R.string.encap_error), parentActivity);
										} else {
											
											//AlertHelper.Alert(parentActivity.getResources().getString(R.string.encap_unknown_registration_error), parentActivity);
											AlertHelper.Alert(parentActivity.getResources().getString(R.string.encap_something_went_wrong), 
													getResources().getString(R.string.encap_error), parentActivity);
										}
										/***/
										if(arg0 instanceof SessionException) {
											Log.i("COOP", ">>SessionException RASIEDDD>>");
											
											//Clear the shared preference of all Activation related FLAGS
											PreferenceHelper helper = new PreferenceHelper(getActivity());
											helper.clear();
											
											parentActivity.getSupportFragmentManager()
															.beginTransaction()
															.add(R.id.lytContainer, new ActivateAppFragment())
															.setCustomAnimations(R.anim.exit, R.anim.enter)
															.commit();													
										}
										/***/
										if(arg0 instanceof UnexpectedException) {
											Log.i("COOP", ">>UnexpectedException RASIEDDD>>");
											
											//Clear the shared preference of all Activation related FLAGS
											PreferenceHelper helper = new PreferenceHelper(getActivity());
											helper.clear();
											
											parentActivity.getSupportFragmentManager()
															.beginTransaction()
															.add(R.id.lytContainer, new ActivateAppFragment())
															.setCustomAnimations(R.anim.exit, R.anim.enter)
															.commit();													
										}
									}

									@Override
									public void onSuccess(FinishActivationResult result) {
										Log.i("COOP", ">>finish Activation onSuccess>>" + result);
										parentActivity.hideProgressDialog();
										/**
										 * The App is Activated successfully now. Set the Flag to 1 = ACTIVATED
										 **/
										PreferenceHelper preferenceHelper = new PreferenceHelper(parentActivity);
										preferenceHelper.addInt(parentActivity.getResources().getString(R.string.pref_is_activated), 1);
										
										if(TextUtils.isEmpty(preferenceHelper.getString(parentActivity.getResources()
												.getString(R.string.pref_device_session)))) {
											//Add current time stamp to Maintain the Session management in the application
															preferenceHelper.addString(parentActivity.getResources().getString(R.string.pref_device_session)
																	,DateUtils.getCurrentTimeStamp());
															Log.i("", ">>>>>>>>>>>>>>>>>>>>DATE TIME SESSION ADDED>>>>>>>>>>>>>>>>>>>>>");
										} 
										
										Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										intent.putExtra(parentActivity.getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
										parentActivity.startActivity(intent);
										parentActivity.finish();
									}
								});
							} else {
								//Network not available
								Toast.makeText(parentActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
							}
						} else {
							return;
							//Toast.makeText(parentActivity, getResources().getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
						}
					}
				})
		.setNegativeButton(
						getResources().getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
								// parentActivity.closeKeyBoard();
							}
						});
		
		txtCreatePIN.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString();
				AlertDialog alert = (AlertDialog) getDialog();
				if (!TextUtils.isEmpty(text)) {
					if (alert != null) {
						Button buttonNo = alert.getButton(AlertDialog.BUTTON_POSITIVE);
						buttonNo.setEnabled(true);
					}
				} else {
					if (alert != null) {
						Button buttonNo = alert.getButton(AlertDialog.BUTTON_POSITIVE);
						buttonNo.setEnabled(false);
					}
				}
			}
		});
		
		return builder.create();
    	}
    
	private void hideDelayedKeyboard() {
		View currentFocus = getDialog().getCurrentFocus();
		if (currentFocus != null) {
			InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(currentFocus.getApplicationWindowToken(), 0);
		}
	}
}
		
//		/******/
//		return new AlertDialog.Builder(getActivity())
//				.setTitle(getResources().getString(R.string.create_your_pin_code))
//				.setPositiveButton(getResources().getString(android.R.string.ok),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								/***/
//								parentActivity.closeKeyBoard();
//								
//								String code = txtCreatePIN.getText().toString();
//								if(!TextUtils.isEmpty(code)) {
//								
//									boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
//									if(isNetworkAvailable) {
//									
//										parentActivity.showProgressDialog();
//										
//										parentActivity.controller.finishActivation(code, new AsyncCallback<FinishActivationResult>() {
//											@Override
//											public void onFailure(Throwable arg0) {
//												Log.i("COOP", ">>Finish Activation onFailure>>" + arg0);
//												parentActivity.hideProgressDialog();
//
//												if (isAdded() == true) {
//													//AlertHelper.Alert(getResources().getString(R.string.encap_unknown_registration_error), parentActivity);
//													AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong), 
//															getResources().getString(R.string.encap_error), parentActivity);
//												} else {
//													
//													//AlertHelper.Alert(parentActivity.getResources().getString(R.string.encap_unknown_registration_error), parentActivity);
//													AlertHelper.Alert(parentActivity.getResources().getString(R.string.encap_something_went_wrong), 
//															getResources().getString(R.string.encap_error), parentActivity);
//												}
//												/***/
//												if(arg0 instanceof SessionException) {
//													Log.i("COOP", ">>SessionException RASIEDDD>>");
//													
//													//Clear the shared preference of all Activation related FLAGS
//													PreferenceHelper helper = new PreferenceHelper(getActivity());
//													helper.clear();
//													
//													parentActivity.getSupportFragmentManager()
//																	.beginTransaction()
//																	.add(R.id.lytContainer, new ActivateAppFragment())
//																	.setCustomAnimations(R.anim.exit, R.anim.enter)
//																	.commit();													
//												}
//												/***/
//												if(arg0 instanceof UnexpectedException) {
//													Log.i("COOP", ">>UnexpectedException RASIEDDD>>");
//													
//													//Clear the shared preference of all Activation related FLAGS
//													PreferenceHelper helper = new PreferenceHelper(getActivity());
//													helper.clear();
//													
//													parentActivity.getSupportFragmentManager()
//																	.beginTransaction()
//																	.add(R.id.lytContainer, new ActivateAppFragment())
//																	.setCustomAnimations(R.anim.exit, R.anim.enter)
//																	.commit();													
//												}
//											}
//
//											@Override
//											public void onSuccess(FinishActivationResult result) {
//												Log.i("COOP", ">>finish Activation onSuccess>>" + result);
//												parentActivity.hideProgressDialog();
//												/**
//												 * The App is Activated successfully now. Set the Flag to 1 = ACTIVATED
//												 **/
//												PreferenceHelper preferenceHelper = new PreferenceHelper(parentActivity);
//												preferenceHelper.addInt(parentActivity.getResources().getString(R.string.pref_is_activated), 1);
//												
//												if(TextUtils.isEmpty(preferenceHelper.getString(parentActivity.getResources()
//														.getString(R.string.pref_device_session)))) {
//													//Add current time stamp to Maintain the Session management in the application
//																	preferenceHelper.addString(parentActivity
//																							.getResources()
//																							.getString(R.string.pref_device_session),
//																					DateUtils.getCurrentTimeStamp());
//																	Log.i("", ">>>>>>>>>>>>>>>>>>>>DATE TIME SESSION ADDED>>>>>>>>>>>>>>>>>>>>>");
//												} 
//												
//												
//												Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
//												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//												intent.putExtra(parentActivity.getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
//												parentActivity.startActivity(intent);
//												parentActivity.finish();
//											}
//										});
//									} else {
//										//Network not available
//										Toast.makeText(parentActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
//									}
//								} else {
//									Toast.makeText(parentActivity, getResources().getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
//								}
//							}
//						})
//				.setNegativeButton(getResources().getString(android.R.string.cancel),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								dismiss();
//								//parentActivity.closeKeyBoard();
//							}
//						})
//						.setView(innerView)
//						.create();
//	}
//}