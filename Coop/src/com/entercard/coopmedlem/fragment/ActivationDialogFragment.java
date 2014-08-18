package com.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
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

public class ActivationDialogFragment extends DialogFragment {

	private ActivateAppActivity parentActivity;
	private EditText actCodeEditText;
	private Button positiveButton;

	public static ActivationDialogFragment newInstance(int title) {
		ActivationDialogFragment frag = new ActivationDialogFragment();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return super.onCreateView(inflater, container, savedInstanceState);
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		setStyle(0, style.Theme_Coopmedlem);
		parentActivity = (ActivateAppActivity) getActivity();

		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(
				R.layout.view_single_edittextview, null);
		actCodeEditText = (EditText) innerView.findViewById(R.id.txtActCode);

		AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

		builder.setTitle(getResources().getString(R.string.activation_code))
				.setView(innerView)
				.setPositiveButton(getResources().getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								/***/
								parentActivity.closeKeyBoard();

								String code = actCodeEditText.getText().toString();
								if (!TextUtils.isEmpty(code)) {

									boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
									if (isNetworkAvailable) {

										parentActivity.showProgressDialog();
										parentActivity.controller.startActivation(code,new AsyncCallback<StartActivationResult>() {
															@Override
															public void onFailure(Throwable err) {
																Log.i("ENCAP","->>>>>startActivation onFailure--->>>>>("+ err.getLocalizedMessage()+ ")");
																parentActivity.hideProgressDialog();

																AlertHelper.Alert(parentActivity.getResources()
																				.getString(R.string.encap_something_went_wrong),
																				parentActivity.getResources().getString(R.string.encap_error),
																				parentActivity);
															}

															@Override
															public void onSuccess(StartActivationResult result) {
																parentActivity.hideProgressDialog();
																Log.i("ENCAP","->>>startActivation onSuccess--->("+ result+ ")");

																PreferenceHelper helper = new PreferenceHelper(parentActivity);

																// Flag stating the Activation code is VERIFIED by EnCap, and not needed to be done again
																helper.addInt(parentActivity.getResources().getString(R.string.pref_is_activation_code_verified),1);

																FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
																FragmentTransaction transaction = fragmentManager.beginTransaction();
																transaction.setCustomAnimations(R.anim.enter,R.anim.exit);
																transaction.replace(R.id.lytContainer,new CreatePINCodeFragment());
																transaction.commit();
															}
														});
									} else {
										// Network not available
										Toast.makeText(parentActivity,getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
									}
								} else {
									return;
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

		actCodeEditText.addTextChangedListener(new TextWatcher() {
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

		// return new AlertDialog.Builder(getActivity())
		// .setTitle(getResources().getString(R.string.activation_code))
		// .setPositiveButton(getResources().getString(android.R.string.ok),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// /***/
		// parentActivity.closeKeyBoard();
		//
		// String code = actCodeEditText.getText().toString();
		// if(!TextUtils.isEmpty(code)) {
		//
		// boolean isNetworkAvailable = NetworkHelper.isOnline(getActivity());
		// if(isNetworkAvailable) {
		//
		// parentActivity.showProgressDialog();
		// parentActivity.controller.startActivation(code, new
		// AsyncCallback<StartActivationResult>() {
		// @Override
		// public void onFailure(Throwable err) {
		// Log.i("ENCAP","->>>>>startActivation onFailure--->>>>>("+
		// err.getLocalizedMessage()+ ")");
		// parentActivity.hideProgressDialog();
		//
		// AlertHelper.Alert(parentActivity.getResources().getString(R.string.encap_something_went_wrong),
		// parentActivity.getResources().getString(R.string.encap_error),
		// parentActivity);
		// }
		// @Override
		// public void onSuccess(StartActivationResult result) {
		// parentActivity.hideProgressDialog();
		// Log.i("ENCAP","->>>startActivation onSuccess--->("+ result + ")");
		//
		// PreferenceHelper helper = new PreferenceHelper(parentActivity);
		//
		// // Flag stating the Activation code is VERIFIED by EnCap, and not
		// needed to be done again
		// helper.addInt(parentActivity.getResources().getString(R.string.pref_is_activation_code_verified),
		// 1);
		//
		// FragmentManager fragmentManager =
		// parentActivity.getSupportFragmentManager();
		// FragmentTransaction transaction = fragmentManager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
		// //transaction.addToBackStack(null);
		// transaction.replace(R.id.lytContainer,new CreatePINCodeFragment());
		// transaction.commit();
		// }
		// });
		// } else {
		// //Network not available
		// Toast.makeText(parentActivity,
		// getResources().getString(R.string.no_internet_connection),
		// Toast.LENGTH_SHORT).show();
		// }
		// } else {
		// return;
		// //Toast.makeText(parentActivity,
		// getResources().getString(R.string.please_enter_code),
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// })
		// .setNegativeButton(getResources().getString(android.R.string.cancel),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// dialog.dismiss();
		// //parentActivity.closeKeyBoard();
		// }
		// })
		// .setView(innerView)
		// .create();
	}
}