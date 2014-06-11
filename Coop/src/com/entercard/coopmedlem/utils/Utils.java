package com.entercard.coopmedlem.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Utils {

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	public static boolean servicesConnected(FragmentActivity context) {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					context, 0);
			if (dialog != null) {
				dialog.show();
			}
		}
		return false;
	}

	/**
	 * 
	 * @param data
	 * @param ctx
	 * @param filename
	 */
	public static void writeToTextFile(String data, Context ctx, String filename) {
		try {
			File myFile = new File(Environment.getExternalStorageDirectory(),
					filename);
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			FileOutputStream fOut = new FileOutputStream(myFile);
			fOut.write(data.getBytes());
			fOut.close();
		} catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	/**
	 * 
	 * @param context
	 * @param filename
	 * @return
	 */
	public static String readResponseFromAssetsFile(Context context,
			String filename) {
		String response = null;
		try {
			InputStream inputStream = context.getAssets().open(filename);
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			response = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 
	 * @param strdate1
	 * @param strdate2
	 * @return
	 */
	public static int compareTwoDates(String strdate1, String strdate2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateTo = sdf.parse(strdate1);
			Date dateFrom = sdf.parse(strdate2);

			Calendar calendarTo = Calendar.getInstance();
			Calendar calendarFrom = Calendar.getInstance();

			calendarTo.setTime(dateTo);
			calendarFrom.setTime(dateFrom);

			if (calendarTo.after(calendarFrom)) {
				return 1;
			}
			if (calendarTo.before(calendarFrom)) {
				return -1;
			}
			if (calendarTo.equals(calendarFrom)) {
				return 0;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @param remainderdate
	 * @param todaysDate
	 */
	public static int getDifferenceBetweenDates(String remainderdate,
			String todaysDate) {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		// remainderdate = "09/04/2014";
		// todaysDate = "10/04/2014";

		try {
			Date dateFrom = myFormat.parse(remainderdate);
			Date dateTo = myFormat.parse(todaysDate);
			long diff = dateFrom.getTime() - dateTo.getTime();
			// System.out.println("Days::::::: "+ TimeUnit.DAYS.convert(diff,
			// TimeUnit.MILLISECONDS));
			return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
