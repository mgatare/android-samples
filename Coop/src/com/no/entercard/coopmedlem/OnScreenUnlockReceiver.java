package com.no.entercard.coopmedlem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.no.entercard.coopmedlem.utils.PreferenceHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class OnScreenUnlockReceiver.
 */
public class OnScreenUnlockReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(context.getResources().getString(R.string.tag_act_finish));
		context.sendBroadcast(broadcastIntent);

		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			 Log.v("COOP", "onReceive ACTION_USER_PRESENT::"+context);
			
			PreferenceHelper preferenceHelper = new PreferenceHelper(context);
			if (preferenceHelper.getInt(context.getResources().getString(R.string.pref_is_activated)) == 1) {
				
				if(null!=ApplicationEx.getInstance()) {
					ApplicationEx.getInstance().clearGlobalContents();
					Log.e("RECEIVER", "ALL CLEAREDDD!!!!!!!!!!");
				}
				/* Start the PIN code Activity */
				Intent enterPINIntent = new Intent(context, EnterPINCodeActivity.class);
				enterPINIntent.putExtra(context.getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
				enterPINIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(enterPINIntent);
				
			} else {
				/* Start the Activate app Activity */
				Intent mainIntent = new Intent(context, ActivateAppActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(mainIntent);
			}
		}
	}
}