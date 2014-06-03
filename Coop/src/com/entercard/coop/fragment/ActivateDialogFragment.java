package com.entercard.coop.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.StartActivationResult;
import com.entercard.coop.ActivateAppActivity;
import com.entercard.coop.EnterPINCodeActivity;
import com.entercard.coop.R;
import com.entercard.coop.R.style;
import com.entercard.coop.helpers.AlertHelper;
import com.entercard.coop.helpers.NetworkHelper;

/**
 * 
 * @author mgatare
 *
 */
public class ActivateDialogFragment extends DialogFragment implements OnClickListener {

	private ActivateAppActivity parentActivity;
	private Handler handler;
	private EditText actCodeEditText;
	
    public static ActivateDialogFragment newInstance(int title) {
        ActivateDialogFragment frag = new ActivateDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }
    
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(0, style.Theme_Cooptheme);
				
		parentActivity = (ActivateAppActivity) getActivity();
		handler = new Handler();
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.view_single_textview, null);
		actCodeEditText = (EditText) innerView.findViewById(R.id.actCodeEditText);
		
		return new AlertDialog.Builder(getActivity())
				.setTitle("Enter your pin")
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/***/
								String code = actCodeEditText.getText().toString();
								if(!TextUtils.isEmpty(code)) {
								
									boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
									if(isNetworkAvailable) {
									
										parentActivity.showProgressDialog();
										
										parentActivity.controller.startActivation(code, new AsyncCallback<StartActivationResult>() {
												@Override
												public void onFailure(Throwable err) {
													Log.i("ENCAP","->>>>>startActivation onFailure--->>>>>("+ err+ ")");
													parentActivity.hideProgressDialog();
													AlertHelper.Alert(err.getLocalizedMessage(), parentActivity);
												}
												@Override
												public void onSuccess(StartActivationResult result) {
													parentActivity.hideProgressDialog();
													Log.i("ENCAP","->>>startActivation onSuccess--->("+ result + ")");
													
													/*Start the PIN code Activity*/
													Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
													intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
				.setNegativeButton("Cancel",
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
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("", "onResume() calledddddddddddddddddddddddddddddddddddd");
    	actCodeEditText.requestFocus();
		actCodeEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Get focus and show the Keyboard
				InputMethodManager inputMgr = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMgr.toggleSoftInput(0, 0);
			}
		}, 200);
    }
    
    @Override
	public void onClick(View v) {
		this.dismiss();
	}
}