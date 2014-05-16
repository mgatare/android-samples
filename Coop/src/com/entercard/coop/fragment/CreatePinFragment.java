package com.entercard.coop.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entercard.coop.AccountsActivity;
import com.entercard.coop.R;

public class CreatePinFragment extends Fragment implements OnClickListener {

	//private TextView titleTextView;
	private TextView bodytextTextView;
	private TextView headerTextView;
	private Button btnAction;

	public CreatePinFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_create_pin,
				container, false);

		// LinearLayout titleLayout = (LinearLayout) parentView
		// .findViewById(R.id.layoutTitle);
		RelativeLayout layoutActivation = (RelativeLayout) parentView
				.findViewById(R.id.layoutActivation);

		bodytextTextView = (TextView) layoutActivation
				.findViewById(R.id.bodytextTextView);
		headerTextView = (TextView) layoutActivation
				.findViewById(R.id.headerTextView);
		btnAction = (Button) layoutActivation.findViewById(R.id.btnOk);

		// titleTextView = (TextView) titleLayout.findViewById(R.id.title_text);
		//titleTextView.setText(getResources().getString(R.string.app_name));

		headerTextView.setText(R.string.create_pin_code);
		bodytextTextView.setText(R.string.create_four_digit_code);
		bodytextTextView.setMovementMethod(LinkMovementMethod.getInstance());
		bodytextTextView.setLinkTextColor(Color.WHITE);

		btnAction.setText(R.string.btn_create_pin);
		btnAction.setOnClickListener(this);

		return parentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:

			Intent intent = new Intent(getActivity(), AccountsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;

		default:
			break;
		}
	}
}
