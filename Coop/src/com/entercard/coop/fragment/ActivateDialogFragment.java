package com.entercard.coop.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.encapsecurity.encap.android.client.api.Controller;
import com.encapsecurity.encap.android.client.api.StartActivationResult;
import com.entercard.coop.ActivateAppActivity;
import com.entercard.coop.ApplicationEx;
import com.entercard.coop.EnterPINCodeActivity;
import com.entercard.coop.R;
import com.entercard.coop.helpers.AlertHelper;

/**
 * 
 * @author mgatare
 *
 */
public class ActivateDialogFragment extends DialogFragment implements OnClickListener {

	private ActivateAppActivity parentActivity;
	private Controller controller;
	private Handler handler;
	
    public static ActivateDialogFragment newInstance(int title) {
        ActivateDialogFragment frag = new ActivateDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(0, android.R.style.Theme_DeviceDefault_Light);
		
		parentActivity = (ActivateAppActivity) getActivity();
		controller = ApplicationEx.applicationEx.getController();
		handler = new Handler();
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.view_single_textview, null);
		final EditText actCodeEditText = (EditText) innerView.findViewById(R.id.actCodeEditText);
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Get focus and show the Keyboard
				InputMethodManager inputMgr = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMgr.toggleSoftInput(0, 0);
			}
		}, 100);
		
		return new AlertDialog.Builder(getActivity())
				.setTitle("Enter your pin")
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/***/
								String code = actCodeEditText.getText().toString();
								if(!TextUtils.isEmpty(code)) {
								
									parentActivity.showProgressDialog();
									
									controller.startActivation(code, new AsyncCallback<StartActivationResult>() {
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
												Log.e("", ""+result.getPinCodeLength());
												Log.e("", ""+result.getPinCodeType());
												
												/*Start the PIN code Activity*/
												Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												parentActivity.startActivity(intent);
												
												parentActivity.finish();
											}
										});
								} else {
									Toast.makeText(parentActivity, "Please enter code", Toast.LENGTH_SHORT).show();
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
	public void onClick(View v) {
		this.dismiss();
	}
}