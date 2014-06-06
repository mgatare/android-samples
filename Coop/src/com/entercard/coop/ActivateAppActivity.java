package com.entercard.coop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.encapsecurity.encap.android.client.api.AsyncCallback;
import com.encapsecurity.encap.android.client.api.Controller;
import com.encapsecurity.encap.android.client.api.LoadConfigResult;
import com.entercard.coop.fragment.ActivateDialogFragment;
import com.entercard.coop.utils.AlertHelper;
import com.entercard.coop.utils.NetworkHelper;
import com.entercard.coop.utils.PreferenceHelper;
import com.entercard.coop.utils.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class EnterPinActivity_New.
 */
public class ActivateAppActivity extends BaseActivity {

	public Controller controller;
	private PreferenceHelper preferenceHelper;
	private ActionBar actionBar;

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

		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		//actionBar.setDisplayShowCustomEnabled(true);
		
		// Gets only one instance
		controller = ((ApplicationEx) getApplication()).getController();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		if (preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated)) == 1) {

			/* Start the PIN code Activity */
			Intent intent = new Intent(this, EnterPINCodeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
			finish();

		} 
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
		private ImageView imgIcon;
		private Button btnOk;
		private ActivateAppActivity parentActivity;

		public ActivateAppFragment() {
			// empty constructor
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			parentActivity = (ActivateAppActivity) getActivity();
			
			if(NetworkHelper.isOnline(parentActivity)) {
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
			} else {
				AlertHelper.Alert(getResources().getString(R.string.no_internet_connection), parentActivity);
			}
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
			imgIcon = (ImageView) layoutActivation
					.findViewById(R.id.imgIcon);
			btnOk = (Button) layoutActivation.findViewById(R.id.btnOk);
			
			imgIcon.setImageResource(R.drawable.activate_pin);
			
			bodytextTextView.setText(StringUtils.getStyledTextFromHtml(getResources().getString(R.string.activation_code_text)));
			bodytextTextView
					.setMovementMethod(LinkMovementMethod.getInstance());
			bodytextTextView.setLinkTextColor(getResources().getColor(R.color.text_title));
			
			headerTextView.setText(R.string.activate_app);
			btnOk.setText(R.string.enter_activation_code);
			btnOk.setOnClickListener(this);
			
			return parentView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnOk:

//				FragmentManager fragmentManager = getFragmentManager();
//				FragmentTransaction transaction = fragmentManager.beginTransaction();
//				transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
//				transaction.replace(R.id.lytContainer,new CreateActivationCodeFragment());
//				transaction.addToBackStack(null);
//				transaction.commit();
				
				DialogFragment newFragment = ActivateDialogFragment.newInstance(0);
				FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
				newFragment.show(fragmentTransaction, "dialog_activate");
				
				break;

			default:
				break;
			}
		}
	}
}
