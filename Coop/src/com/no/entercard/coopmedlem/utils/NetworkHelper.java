package com.no.entercard.coopmedlem.utils;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

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

			//WifiInfo wifiInf = wifi.getConnectionInfo();
			/* Get the MAC ADD of WIFI */
			// Commons.MAC_ID = wifiInf.getMacAddress();
			return true;
		} else {
			return false;

		}
	}
	/**
	 * 
	 * @param place
	 * @param contex
	 * @return
	 */
	public boolean getLatLongFromPlace(String venue, Context contex) {
		try {
			double latitude = 0.0;
			double longitude = 0.0;

			Locale locale = contex.getResources().getConfiguration().locale;
			Geocoder selected_place_geocoder = new Geocoder(contex, locale);
			List<Address> address;

			address = selected_place_geocoder.getFromLocationName(venue, 1);

			if (address == null) {
				return false;
			} else {
				Address location = address.get(0);
				latitude = location.getLatitude();
				longitude = location.getLongitude();

				Log.e("COOP", venue+" ::: latitide is:::"+latitude+"::::longitudeis:::"+longitude);
				
				if (latitude == 0.0 && longitude == 0.0) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
