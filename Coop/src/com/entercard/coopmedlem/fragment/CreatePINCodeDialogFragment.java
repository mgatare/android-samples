package com.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.FinishActivationResult;
import com.encapsecurity.encap.android.client.api.exception.UnexpectedException;
import com.entercard.coopmedlem.ActivateAppActivity;
import com.entercard.coopmedlem.ActivateAppActivity.ActivateAppFragment;
import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.BaseActivity;
import com.entercard.coopmedlem.EnterPINCodeActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.R.style;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.NetworkHelper;
import com.entercard.coopmedlem.utils.PreferenceHelper;

/**
 * @author mgatare
 */
public class CreatePINCodeDialogFragment extends DialogFragment {

	private ActivateAppActivity parentActivity;
	private EditText txtCreatePIN;
	
    public static CreatePINCodeDialogFragment newInstance() {
        CreatePINCodeDialogFragment frag = new CreatePINCodeDialogFragment();
        return frag;
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
		
		LayoutInflater layoutInflater = parentActivity.getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.fragment_dialog_create_pin, null);
		txtCreatePIN = (EditText) innerView.findViewById(R.id.txtCreatePIN);
		
		// Show soft keyboard automatically
		txtCreatePIN.requestFocus();
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.create_your_pin_code))
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
									
										parentActivity.showProgressDialog();
										
										parentActivity.controller.finishActivation(code, new AsyncCallback<FinishActivationResult>() {
											@Override
											public void onFailure(Throwable arg0) {
												Log.i("COOP", ">>Finish Activation onFailure>>" + arg0);
												parentActivity.hideProgressDialog();
												//AlertHelper.Alert(getResources().getString(R.string.encap_error), parentActivity);
												AlertHelper.Alert(ApplicationEx.getInstance().getResources().getString(R.string.encap_unknown_registration_error), getActivity());
												
												if(arg0 instanceof UnexpectedException) {
													Log.i("COOP", ">>IllegalStateException RASIEDDD>>");
													
													//Clear the shared preference of all Activation related FLAGS
													PreferenceHelper helper = new PreferenceHelper(getActivity());
													helper.clear();
													
													parentActivity.getSupportFragmentManager().beginTransaction()
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
												
												Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												intent.putExtra(parentActivity.getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
												parentActivity.startActivity(intent);
												parentActivity.finish();
											}
										});
									} else {
										//Network not available
										Toast.makeText(parentActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(parentActivity, getResources().getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton(getResources().getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
								//parentActivity.closeKeyBoard();
							}
						})
						.setView(innerView)
						.create();
	}
}