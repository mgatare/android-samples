package com.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.StartActivationResult;
import com.entercard.coopmedlem.ActivateAppActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.R.style;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.NetworkHelper;
import com.entercard.coopmedlem.utils.PreferenceHelper;

/**
 * 
 * @author mgatare
 *
 */
public class ActivationDialogFragment extends DialogFragment {

	private ActivateAppActivity parentActivity;
	private EditText actCodeEditText;
	
    public static ActivationDialogFragment newInstance(int title) {
        ActivationDialogFragment frag = new ActivationDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
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
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.view_single_edittextview, null);
		actCodeEditText = (EditText) innerView.findViewById(R.id.txtActCode);
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.enter_activation_code))
				.setPositiveButton(getResources().getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/***/
								
								parentActivity.closeKeyBoard();
								
								String code = actCodeEditText.getText().toString();
								if(!TextUtils.isEmpty(code)) {
								
									boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
									if(isNetworkAvailable) {
									
										parentActivity.showProgressDialog();
										parentActivity.closeKeyBoard();
										
										parentActivity.controller.startActivation(code, new AsyncCallback<StartActivationResult>() {
												@Override
												public void onFailure(Throwable err) {
													Log.i("ENCAP","->>>>>startActivation onFailure--->>>>>("+ err.getLocalizedMessage()+ ")");
													parentActivity.hideProgressDialog();
													AlertHelper.Alert(err.getLocalizedMessage(), parentActivity);
												}
												@Override
												public void onSuccess(StartActivationResult result) {
													parentActivity.hideProgressDialog();
													Log.i("ENCAP","->>>startActivation onSuccess--->("+ result + ")");
													
													PreferenceHelper helper = new PreferenceHelper(parentActivity);
													
													//Flag stating the Activation code is VERIFIED by EnCap, and not needed to be done again
													helper.addInt(parentActivity.getResources().getString(R.string.pref_is_activation_code_verified), 1);
													
													FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
													FragmentTransaction transaction = fragmentManager.beginTransaction();
													transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
													transaction.addToBackStack(null);
													transaction.replace(R.id.lytContainer,new CreateActivationCodeFragment());
													transaction.commit();
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
								parentActivity.closeKeyBoard();
							}
						})
						.setView(innerView)
						.create();
	}
}