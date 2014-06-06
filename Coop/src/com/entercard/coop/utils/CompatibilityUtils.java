package com.entercard.coop.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

public class CompatibilityUtils {

	/**
	 * Get the current Android API level.
	 */
	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getModelName() {
		return android.os.Build.MODEL;
	}

	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	

	/**
	 * Determine if the device is running API level 8 or higher.
	 */
	public static boolean isFroyo() {
		return getSdkVersion() >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Determine if the device is running API level 11 or higher.
	 */
	public static boolean isHoneycomb() {
		return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Determine if the device is a tablet (i.e. it has a large screen).
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Determine if the device is a HoneyComb tablet.
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isHoneycombTablet(Context context) {
		return isHoneycomb() && isTablet(context);
	}

	/**
	 * This class can't be instantiated.
	 */
	private CompatibilityUtils() {
	}

	private static boolean isSDCardPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	/**
	 * 
	 * @param context
	 * @return
	 */
	/*Currently attaching both the version name and the version code*/
	public static String getAppVersionsFromManifest(Context context) {
		String AppVersion = null;
		try {
			String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			int versionCode= context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			AppVersion = versionCode+"-"+version;
			return AppVersion;
		} catch (NameNotFoundException e1) {
			Log.d("Version", e1.toString());
		}
		return"";
	}
	
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			Log.d("Version", e1.toString());
		}
		return"";
	}
}