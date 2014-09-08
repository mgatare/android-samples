package com.entercard.coopmedlem.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entercard.coopmedlem.ActivateAppActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.NetworkHelper;

public class CreatePINCodeFragment extends Fragment implements OnClickListener {

	private TextView bodytextTextView;
	private TextView headerTextView;
	private ImageView imgIcon;
	private Button btnAction;
	private ActivateAppActivity parentActivity;

	public CreatePINCodeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_create_activation_code,
				container, false);
		parentActivity = (ActivateAppActivity) getActivity();
		RelativeLayout layoutActivation = (RelativeLayout) parentView.findViewById(R.id.layoutActivation);

		bodytextTextView = (TextView) layoutActivation
				.findViewById(R.id.lblBodytext);
		headerTextView = (TextView) layoutActivation
				.findViewById(R.id.lblHeader);
		imgIcon = (ImageView) layoutActivation
				.findViewById(R.id.imgIcon);
		btnAction = (Button) layoutActivation.findViewById(R.id.btnOk);

		imgIcon.setImageResource(R.drawable.create_pin);
		headerTextView.setText(R.string.create_your_pin_code);
		bodytextTextView.setText(R.string.please_create_pin_code_text);
		bodytextTextView.setMovementMethod(LinkMovementMethod.getInstance());
		bodytextTextView.setLinkTextColor(getResources().getColor(R.color.text_body));

		btnAction.setText(R.string.btn_create_pin);
		btnAction.setOnClickListener(this);
		
		return parentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:

			if (NetworkHelper.isOnline(parentActivity)) {
				
				DialogFragment dialogFragment = CreatePINCodeDialogFragment.newInstance();
				FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				dialogFragment.show(fragmentTransaction, "dialog_create_pin");
				
			} else {
				AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong),
						getResources().getString(R.string.no_internet_connection), parentActivity);
			}
			
//			Intent intent = new Intent(getActivity(), EnterPINCodeActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			parentActivity.finish();
			
			break;

		default:
			break;
		}
	}
}
