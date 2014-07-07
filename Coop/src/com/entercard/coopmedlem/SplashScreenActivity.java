package com.entercard.coopmedlem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.entercard.coopmedlem.utils.PreferenceHelper;

public class SplashScreenActivity extends FragmentActivity {
	private final int SPLAST_TIMEOUT = 1200;
	private PreferenceHelper preferenceHelper;
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_splash);
		preferenceHelper = new PreferenceHelper(this);
		//Log.i("", ">>>>>>>>>"+DateUtils.getCurrentTimeStamp());
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated)) == 1) {
					
					/* Start the PIN code Activity */
					Intent intent = new Intent(SplashScreenActivity.this, EnterPINCodeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
					SplashScreenActivity.this.startActivity(intent);
					SplashScreenActivity.this.finish();
					
				} else {
					Intent mainIntent = new Intent(SplashScreenActivity.this,ActivateAppActivity.class);
					mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					SplashScreenActivity.this.startActivity(mainIntent);
					SplashScreenActivity.this.finish();
				}
			}
		}, SPLAST_TIMEOUT);
	}
}
