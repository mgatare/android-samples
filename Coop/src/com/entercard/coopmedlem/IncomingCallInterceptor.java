package com.entercard.coopmedlem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class IncomingCallInterceptor extends BroadcastReceiver {

	private final String LOG_TAG = "CallStateReceiver";
	public static String prevState = TelephonyManager.EXTRA_STATE_IDLE;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			return;
		}
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)
				&& !prevState
						.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {

			Log.i(this.LOG_TAG, "###############Call ended#################");
		}
		prevState = state;
	}
}