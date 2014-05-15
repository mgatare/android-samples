package com.entercard.coop;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entercard.coop.fragment.CreatePinFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class EnterPinActivity_New.
 */
public class EnterPinActivity extends FragmentActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pin);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.lytContainer, new EnterPinFragment())
					.setCustomAnimations(R.anim.enter, R.anim.exit).commit();

		}
	}

	/**
	 * The Class EnterPinFragment.
	 * 
	 * @author mgatare
	 */
	public static class EnterPinFragment extends Fragment implements
			OnClickListener {

		private TextView bodytextTextView;
		private TextView headerTextView;
		private Button btnOk;

		public EnterPinFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View parentView = inflater.inflate(R.layout.fragment_enter_pin,
					container, false);
			// LinearLayout titleLayout = (LinearLayout)
			// parentView.findViewById(R.id.layoutTitle);
			RelativeLayout layoutActivation = (RelativeLayout) parentView
					.findViewById(R.id.layoutActivation);

			bodytextTextView = (TextView) layoutActivation
					.findViewById(R.id.bodytextTextView);
			headerTextView = (TextView) layoutActivation
					.findViewById(R.id.headerTextView);
			btnOk = (Button) layoutActivation.findViewById(R.id.btnOk);

			// titleTextView = (TextView)
			// titleLayout.findViewById(R.id.title_text);
			//titleTextView.setText(getResources().getString(R.string.app_name));

			bodytextTextView.setText(Html
					.fromHtml(getString(R.string.activation_code_text)));
			bodytextTextView
					.setMovementMethod(LinkMovementMethod.getInstance());
			bodytextTextView.setLinkTextColor(Color.WHITE);
			headerTextView.setText(R.string.activate_app);
			btnOk.setText(R.string.enter_activation_code);
			btnOk.setOnClickListener(this);
			return parentView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.btnOk:

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
				transaction.replace(R.id.lytContainer, new CreatePinFragment());
				transaction.addToBackStack(null);
				transaction.commit();

				break;

			default:
				break;
			}
		}
	}
}
