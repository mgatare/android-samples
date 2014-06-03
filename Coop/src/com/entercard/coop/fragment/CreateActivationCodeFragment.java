package com.entercard.coop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.LoadConfigResult;
import com.entercard.coop.ActivateAppActivity;
import com.entercard.coop.R;

public class CreateActivationCodeFragment extends Fragment implements OnClickListener {

	private TextView bodytextTextView;
	private TextView headerTextView;
	private Button btnAction;
	private ActivateAppActivity parentActivity;

	public CreateActivationCodeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_create_activation_code,
				container, false);
		parentActivity = (ActivateAppActivity) getActivity();
		RelativeLayout layoutActivation = (RelativeLayout) parentView.findViewById(R.id.layoutActivation);

		bodytextTextView = (TextView) layoutActivation
				.findViewById(R.id.bodytextTextView);
		headerTextView = (TextView) layoutActivation
				.findViewById(R.id.headerTextView);
		btnAction = (Button) layoutActivation.findViewById(R.id.btnOk);

		headerTextView.setText(R.string.create_pin_code);
		bodytextTextView.setText(R.string.create_four_digit_code);
		bodytextTextView.setMovementMethod(LinkMovementMethod.getInstance());
		bodytextTextView.setLinkTextColor(Color.WHITE);

		btnAction.setText(R.string.btn_create_pin);
		btnAction.setOnClickListener(this);
		
		parentActivity.showProgressDialog();
		parentActivity.controller.loadConfig(new AsyncCallback<LoadConfigResult>() {
			
			@Override
			public void onFailure(Throwable arg0) {
				parentActivity.hideProgressDialog();
				parentActivity.longToast(arg0.getLocalizedMessage());
			}

			@Override
			public void onSuccess(LoadConfigResult arg0) {
				Log.i("COOP", "CONFIGURATION onSuccess::"+arg0.getActivationCodeInputType());
				parentActivity.hideProgressDialog();
			}
		});
		
		//Show the Get Pin code dialogue
		return parentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:

			DialogFragment newFragment = ActivateDialogFragment.newInstance(0);
			FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
			newFragment.show(fragmentTransaction, "dialog_activate");
			
//			Intent intent = new Intent(getActivity(), EnterPINCodeActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			
			break;

		default:
			break;
		}
	}
}
