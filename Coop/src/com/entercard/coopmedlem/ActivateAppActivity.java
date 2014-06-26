package com.entercard.coopmedlem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.entercard.coopmedlem.fragment.ActivationDialogFragment;
import com.entercard.coopmedlem.fragment.CreateActivationCodeFragment;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.NetworkHelper;
import com.entercard.coopmedlem.utils.PreferenceHelper;
import com.entercard.coopmedlem.utils.StringUtils;

public class ActivateAppActivity extends BaseActivity {

	public Controller controller;
	private ActionBar actionBar;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activate_app);
		PreferenceHelper preferenceHelper = new PreferenceHelper(this);
		
		if (savedInstanceState == null) {

			if (preferenceHelper.getInt(getResources().getString(R.string.pref_is_activation_code_verified)) == 1) {
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.lytContainer, new CreateActivationCodeFragment())
						.addToBackStack(null)
						.commit();
			} else {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.lytContainer, new ActivateAppFragment())
						.setCustomAnimations(R.anim.enter, R.anim.exit)
						.commit();
			}
		}

		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
		// Gets only one instance
		controller = ((ApplicationEx) getApplication()).getController();
		
		if(NetworkHelper.isOnline(this)) {
			showProgressDialog();
			controller.loadConfig(new AsyncCallback<LoadConfigResult>() {
				@Override
				public void onFailure(Throwable arg0) {
					hideProgressDialog();
					//longToast(arg0.getLocalizedMessage());
					showDeveloperLog("onFailure"+arg0.getLocalizedMessage());
					AlertHelper.Alert(arg0.getLocalizedMessage(), ActivateAppActivity.this);
				}
				@Override
				public void onSuccess(LoadConfigResult arg0) {
					Log.i("COOP", "CONFIGURATION onSuccess::"+arg0.getActivationCodeInputType());
					hideProgressDialog();
				}
			});
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_internet_connection), this);
		}
	}

	/**
	 * The Class EnterPinFragment.
	 */
	public static class ActivateAppFragment extends Fragment implements
			OnClickListener {

		private TextView bodytextTextView;
		private TextView headerTextView;
		private ImageView imgIcon;
		private Button btnOk;
		private ActivateAppActivity parentActivity;

		public ActivateAppFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			parentActivity = (ActivateAppActivity) getActivity();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View parentView = inflater.inflate(R.layout.fragment_activate_app, container, false);
			RelativeLayout layoutActivation = (RelativeLayout) parentView.findViewById(R.id.layoutActivation);

			bodytextTextView = (TextView) layoutActivation.findViewById(R.id.lblBodytext);
			headerTextView = (TextView) layoutActivation.findViewById(R.id.lblHeader);
			imgIcon = (ImageView) layoutActivation.findViewById(R.id.imgIcon);
			btnOk = (Button) layoutActivation.findViewById(R.id.btnOk);
			
			imgIcon.setImageResource(R.drawable.activate_pin);
			
			bodytextTextView.setText(StringUtils.getStyledTextFromHtml(getResources().getString(R.string.activation_code_text)));
			bodytextTextView.setMovementMethod(LinkMovementMethod.getInstance());
			bodytextTextView.setLinkTextColor(getResources().getColor(R.color.text_heading));
			
			headerTextView.setText(R.string.activate_app);
			btnOk.setText(R.string.enter_activation_code);
			btnOk.setOnClickListener(this);
			
			return parentView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnOk:

				// FragmentManager fragmentManager = getFragmentManager();
				// FragmentTransaction transaction =
				// fragmentManager.beginTransaction();
				// transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
				// transaction.replace(R.id.lytContainer,new
				// CreateActivationCodeFragment());
				// transaction.addToBackStack(null);
				// transaction.commit();
				
				if (NetworkHelper.isOnline(parentActivity)) {
					DialogFragment newFragment = ActivationDialogFragment.newInstance(0);
					FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					newFragment.show(fragmentTransaction, "dialog_activate");
				} else {
					AlertHelper.Alert(getResources().getString(R.string.no_internet_connection), parentActivity);
				}
				break;

			default:
				break;
			}
		}
	}
}
