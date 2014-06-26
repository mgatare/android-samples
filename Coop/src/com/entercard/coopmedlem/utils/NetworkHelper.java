package com.entercard.coopmedlem.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

// TODO: Auto-generated Javadoc
/**
 * The Class NetworkHelper.
 */
public class NetworkHelper {

	/**
	 * Checks if is online.
	 *
	 * @param cxt the cxt
	 * @return true, if is online
	 */
	public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Check wifi.
	 *
	 * @param cxt the cxt
	 * @return true, if successful
	 */
	public static boolean checkWIFI(Context cxt) {
		WifiManager wifi = (WifiManager) cxt
				.getSystemService(Context.WIFI_SERVICE);
		if ((wifi.isWifiEnabled() == true)) {

			WifiInfo wifiInf = wifi.getConnectionInfo();
			/* Get the MAC ADD of WIFI */
			// Commons.MAC_ID = wifiInf.getMacAddress();
			return true;
		} else {
			return false;

		}
	}

	/**
	 * Start action call.
	 *
	 * @param phoneNumber the phone number
	 * @param cxt the cxt
	 */
	public static void startActionCall(String phoneNumber, Context cxt) {
		Uri phoneNum = Uri.parse("tel:" + phoneNumber);
		cxt.startActivity(new Intent(Intent.ACTION_CALL, phoneNum));
	}
}
