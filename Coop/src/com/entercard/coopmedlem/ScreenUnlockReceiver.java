package com.entercard.coopmedlem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenUnlockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			Log.v("COOP", "In Method:  ACTION_USER_PRESENT::"+context);
			// Handle resuming events

			//AlertHelper.Alert("MILAAAAAAAAAA", context);
			
			Intent newIntent = new Intent(context, EnterPINCodeActivity.class);
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(newIntent);
		}
	}
}