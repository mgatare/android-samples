package com.entercard.coop.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkHelper {

//	public static boolean isNetworkAvailable(Context ctx) {
//
//		ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		boolean isWifiConn = networkInfo.isConnected();
//		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//		if (null != networkInfo) {
//			boolean isMobileConn = networkInfo.isConnected();
//			networkInfo = connMgr.getActiveNetworkInfo();
//			boolean isActiveNetwork = (networkInfo != null && networkInfo.isConnected());
//			return ((isWifiConn || isMobileConn) && isActiveNetwork);
//		} else {
//			networkInfo = connMgr.getActiveNetworkInfo();
//			boolean isActiveNetwork = (networkInfo != null && networkInfo.isConnected());
//			return ((isWifiConn) && isActiveNetwork);
//		}
//	}
	
	
	public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	

	public static boolean checkWIFI(Context cxt) {
		WifiManager wifi = (WifiManager) cxt.getSystemService(Context.WIFI_SERVICE);
		if ((wifi.isWifiEnabled() == true)) {

			WifiInfo wifiInf = wifi.getConnectionInfo();
			/* Get the MAC ADD of WIFI */
			// Commons.MAC_ID = wifiInf.getMacAddress();
			return true;
		} else {
			return false;

		}
	}

	public static void startActionCall(String phoneNumber, Context cxt) {
		Uri phoneNum = Uri.parse("tel:" + phoneNumber);
		cxt.startActivity(new Intent(Intent.ACTION_CALL, phoneNum));
	}
}
