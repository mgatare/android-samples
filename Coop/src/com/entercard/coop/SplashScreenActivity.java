package com.entercard.coop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

public class SplashScreenActivity extends FragmentActivity {
	private final int SPLAST_TIMEOUT = 1500;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(SplashScreenActivity.this,
						ActivateAppActivity.class);
				SplashScreenActivity.this.startActivity(mainIntent);
				SplashScreenActivity.this.finish();
			}
		}, SPLAST_TIMEOUT);
	}
}
