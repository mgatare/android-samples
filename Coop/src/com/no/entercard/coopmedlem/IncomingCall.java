package com.no.entercard.coopmedlem;
//package com.entercard.coopmedlem;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.Toast;
//
//public class IncomingCall extends BroadcastReceiver {
//	
//	private Context context;
//
//	public void onReceive(Context context, Intent intent) {//ACTION.PHONE.CALL
//		this.context = context;
//		Log.e("Phone Receive Error", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + intent);
//		if(null==intent) {
//			return;
//		}
//		try {
//		    MyPhoneStateListener phoneListener=new MyPhoneStateListener();
//		    TelephonyManager telephony = (TelephonyManager) 
//		    context.getSystemService(Context.TELEPHONY_SERVICE);
//		    telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
//		} catch (Exception e) {
//			Log.e("Phone Receive Error", " " + e);
//		}
//	}
//
//	private class MyPhoneStateListener extends PhoneStateListener {
//		public void onCallStateChanged(int state, String incomingNumber) {
//			Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
//			if (state == 1) {
//				String msg = "New Phone Call Event. Incomming Number : "+ incomingNumber;
//				int duration = Toast.LENGTH_LONG;
//				Toast toast = Toast.makeText(context, msg, duration);
//				toast.show();
//				
////				//Start the PIN code Activity 
////				Intent enterPINIntent = new Intent(ApplicationEx.getInstance(), EnterPINCodeActivity.class);
////				enterPINIntent.putExtra(ApplicationEx.getInstance().getResources().getString(R.string.pref_verify_pin), BaseActivity.NO_STATE);
////				enterPINIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				enterPINIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////				ApplicationEx.getInstance().startActivity(enterPINIntent);
//			}
//		}
//	}
//}