package com.no.entercard.coopmedlem;

import java.util.Locale;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.no.entercard.coopmedlem.utils.PreferenceHelper;

public class SplashScreenActivity extends FragmentActivity {
	private final int SPLAST_TIMEOUT = 1200;
	private PreferenceHelper preferenceHelper;
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_splash);
		preferenceHelper = new PreferenceHelper(this);
		
		//Log.i("", ">>>>>>>>>"+Double.parseDouble(dummy));
		//Log.i("", ">>>>>>>>>"+Float.parseFloat(dummy));
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//Initialize GEOCODER
				Locale locale = getResources().getConfiguration().locale;
				Geocoder geocoder = new Geocoder(SplashScreenActivity.this, locale);
				
				if (preferenceHelper.getInt(getResources().getString(R.string.pref_is_activated)) == 1) {
					
					/* Start the PIN code Activity */
					Intent intent = new Intent(SplashScreenActivity.this, EnterPINCodeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
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