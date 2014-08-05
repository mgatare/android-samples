package com.entercard.coopmedlem.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class CompatibilityUtils.
 */
public class CompatibilityUtils {

	/**
	 * Get the current Android API level.
	 *
	 * @return the sdk version
	 */
	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * Gets the model name.
	 *
	 * @return the model name
	 */
	public static String getModelName() {
		return android.os.Build.MODEL;
	}

	/**
	 * Gets the manufacturer.
	 *
	 * @return the manufacturer
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * Gets the android version.
	 *
	 * @return the android version
	 */
	public static String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * Determine if the device is running API level 8 or higher.
	 *
	 * @return true, if is froyo
	 */
	public static boolean isFroyo() {
		return getSdkVersion() >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Determine if the device is running API level 11 or higher.
	 *
	 * @return true, if is honeycomb
	 */
	public static boolean isHoneycomb() {
		return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Determine if the device is a tablet (i.e. it has a large screen).
	 *
	 * @param context            The calling context.
	 * @return true, if is tablet
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Determine if the device is a HoneyComb tablet.
	 *
	 * @param context            The calling context.
	 * @return true, if is honeycomb tablet
	 */
	public static boolean isHoneycombTablet(Context context) {
		return isHoneycomb() && isTablet(context);
	}

	/**
	 * This class can't be instantiated.
	 */
	private CompatibilityUtils() {
	}

	/**
	 * Checks if is SD card present.
	 *
	 * @return true, if is SD card present
	 */
	public static boolean isSDCardPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Gets the app versions from manifest.
	 *
	 * @param context the context
	 * @return the app versions from manifest
	 */
	/* Currently attaching both the version name and the version code */
	public static String getAppVersionsFromManifest(Context context) {
		String AppVersion = null;
		try {
			String version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			AppVersion = versionCode + "-" + version;
			return AppVersion;
		} catch (NameNotFoundException e1) {
			Log.d("Version", e1.toString());
		}
		return "";
	}

	/**
	 * Gets the app version name.
	 * 
	 * @param context
	 *            the context
	 * @return the app version name
	 */
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			Log.d("Version", e1.toString());
		}
		return "";
	}

	/**
	 * Checks if is application sent to background.
	 *
	 * @param context the context
	 * @return true, if is application sent to background
	 */
	public boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}