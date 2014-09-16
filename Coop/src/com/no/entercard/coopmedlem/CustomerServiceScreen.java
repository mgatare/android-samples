package com.no.entercard.coopmedlem;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.no.entercard.coopmedlem.utils.StringUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
public class CustomerServiceScreen extends BaseActivity {

	private Button btnLogin;
	private LinearLayout linearBlockCard;
	private LinearLayout linearCardService;
	private TextView lblCardServiceNumber;
	private TextView lblBlockCardNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_service);

		init();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(null);
		actionBar.setIcon(R.drawable.icon_coop);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		CustomerServiceScreen.this.finish();
	}
	
	private void init() {

		btnLogin = (Button) findViewById(R.id.btnLogin);

		linearBlockCard = (LinearLayout) findViewById(R.id.linearBlockCard);
		linearCardService = (LinearLayout) findViewById(R.id.linearCardService);
		lblCardServiceNumber = (TextView) findViewById(R.id.lblCardServiceNumber);
		lblBlockCardNumber = (TextView) findViewById(R.id.lblBlockCardNumber);
		
		linearBlockCard.setOnClickListener(onClickListener);
		linearCardService.setOnClickListener(onClickListener);
		btnLogin.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnLogin:
				
				Intent intent = new Intent(CustomerServiceScreen.this, EnterPINCodeActivity.class);
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
				startActivity(intent);
				overridePendingTransition(R.anim.abc_slide_in_bottom, 0);
				
				CustomerServiceScreen.this.finish();
				
				break;
			case R.id.linearCardService:
				makeCall(lblCardServiceNumber.getText().toString());
				break;
				
			case R.id.linearBlockCard:
				makeCall(lblBlockCardNumber.getText().toString());
				break;
			default:
				break;
			}
		}
	};
	
	private void makeCall(String string) {
		/*Check if Phone calling is supported or not then initiate the phone call*/
		PackageManager packageManager = getPackageManager();
		boolean canCall = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
		if (canCall) {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + StringUtils.trimStringOnly(string)));
			startActivity(intent);
		} else {
			longToast("This device does not support telephonic facility.");
		}
	}
}
