package com.no.entercard.coopmedlem;
//package com.entercard.coopmedlem;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//import com.entercard.coopmedlem.utils.PreferenceHelper;
//
//public class IncomingCallInterceptor extends BroadcastReceiver {
//
//	private final String LOG_TAG = "CallStateReceiver";
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		Bundle bundle = intent.getExtras();
//		if (bundle == null) {
//			return;
//		}
//		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
//		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
//			Log.i(this.LOG_TAG,
//					"###############Call EXTRA_STATE_IDLE#################");
//		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//			Log.i(this.LOG_TAG,
//					"###############Call EXTRA_STATE_OFFHOOK#################");
//		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
//			Log.i(this.LOG_TAG, "###############Call EXTRA_STATE_RINGING#################");
//			
//			PreferenceHelper preferenceHelper = new PreferenceHelper(context);
//			if (preferenceHelper.getInt(context.getResources().getString(R.string.pref_is_activated)) == 1) {
//				
//				 //Start the PIN code Activity 
//				Intent enterPINIntent = new Intent(context, EnterPINCodeActivity.class);
//				enterPINIntent.putExtra(context.getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
//				enterPINIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(enterPINIntent);
//			}
//		}
//	}
//}