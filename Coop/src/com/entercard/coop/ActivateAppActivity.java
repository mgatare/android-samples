package com.entercard.coop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.encapsecurity.encap.android.client.api.Controller;
import com.entercard.coop.fragment.CreateActivationCodeFragment;
import com.entercard.coop.helpers.PreferenceHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class EnterPinActivity_New.
 */
public class ActivateAppActivity extends BaseActivity {

	public Controller controller;
	public PreferenceHelper preferenceHelper;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activate_app);
		preferenceHelper = new PreferenceHelper(this);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.lytContainer, new ActivateAppFragment())
					.setCustomAnimations(R.anim.enter, R.anim.exit).commit();

		}

		// Application ensures there is one instance,
		controller = ((ApplicationEx) getApplication()).getController();
	}

	/**
	 * The Class EnterPinFragment.
	 * 
	 * @author mgatare
	 */
	public static class ActivateAppFragment extends Fragment implements
			OnClickListener {

		private TextView bodytextTextView;
		private TextView headerTextView;
		private Button btnOk;

		public ActivateAppFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View parentView = inflater.inflate(R.layout.fragment_activate_app,
					container, false);
			
			RelativeLayout layoutActivation = (RelativeLayout) parentView
					.findViewById(R.id.layoutActivation);

			bodytextTextView = (TextView) layoutActivation
					.findViewById(R.id.bodytextTextView);
			headerTextView = (TextView) layoutActivation
					.findViewById(R.id.headerTextView);
			btnOk = (Button) layoutActivation.findViewById(R.id.btnOk);

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
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
				transaction.replace(R.id.lytContainer,new CreateActivationCodeFragment());
				transaction.addToBackStack(null);
				transaction.commit();

				break;

			default:
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated)) == 0) {
			
			/* Start the PIN code Activity */
			Intent intent = new Intent(this, AccountsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			finish();
		}
	}
}
