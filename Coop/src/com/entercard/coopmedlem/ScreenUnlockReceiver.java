package com.entercard.coopmedlem;

import com.entercard.coopmedlem.utils.PreferenceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenUnlockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(context.getResources().getString(
				R.string.tag_act_finish));
		context.sendBroadcast(broadcastIntent);

		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
//			// Log.v("COOP", "In Method:  ACTION_USER_PRESENT::"+context);
//			Intent newIntent = new Intent(context, EnterPINCodeActivity.class);
//			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			context.startActivity(newIntent);
			
			PreferenceHelper preferenceHelper = new PreferenceHelper(context);
			if (preferenceHelper.getInt(context.getResources().getString(R.string.pref_is_activated)) == 1) {
				/* Start the PIN code Activity */
				Intent enterPINIntent = new Intent(context, EnterPINCodeActivity.class);
				enterPINIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(enterPINIntent);
				
			} else {
				/* Start the Activate app Activity */
				Intent mainIntent = new Intent(context,ActivateAppActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(mainIntent);
			}
		}
	}
}