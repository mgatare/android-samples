package com.entercard.coopmedlem.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.entercard.coopmedlem.ApplicationEx;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	/** The tag. */
	public static String TAG = "Utils";

	/** The date format. */
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss", Locale.getDefault());

	/**
	 *  The locale.
	 *
	 * @param text the text
	 * @return the string
	 */
	// public static Locale locale = new Locale("US");

	/**
	 * Format to locale.
	 * 
	 * @param text
	 *            the text
	 * @return the string
	 */
	public static String formatToLocaleUS(String text) {

		double currency = Double.parseDouble(text);
		// Format to US locale
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
		return format.format(currency);
	}

	/**
	 * Gets the current time stamp.
	 *
	 * @return the current time stamp
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTimeStamp() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStamp = dateFormat.format(new Date());

			return currentTimeStamp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressLint("SimpleDateFormat")
	public static String getTransactionTimeStamp(String date) {
		// 2014-07-03T00:00:00Z
		String formatedDate = "";
		String oldFormat = "yyyy-MM-dd";
		String newFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

		try {
			formatedDate = sdf2.format(sdf1.parse(date));
			Log.i("", "currentTimeStamp>>>>"+formatedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatedDate;
	}
	
	
	/**
	 * Gets the locale specif name.
	 *
	 * @return the locale specific name
	 */
	public static String getLocaleSpecifName() {
		return Calendar.getInstance(Locale.getDefault()).getTimeZone()
				.getDisplayName();
	}

	/**
	 * Gets the formated date.
	 * 
	 * @param date
	 *            the date
	 * @return the formated date
	 */
//	public static String getTransactionDate(String date) {
//		// 2013-09-18 -- COOP
//		String formatedDate = "";
//		String oldFormat = "yyyy-MM-dd";
//		String newFormat = null;
//		
//		String currentLocale = ApplicationEx.getInstance().getResources().getConfiguration().locale.toString();
//		if(currentLocale.equalsIgnoreCase("nb_NO")) {
//			newFormat = "d.M.yyyy";	
//		} 
////		else if(currentLocale.equalsIgnoreCase("nb_NO")) { 
////			newFormat = "d.M.yyyy";
////		} 
//		else {
//			newFormat = "dd.MM.yyyy";
//		}
//
//		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
//		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
//
//		try {
//			formatedDate = sdf2.format(sdf1.parse(date));
//
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return formatedDate;
//	}
	/**
	 * THIS IS ONLY USED FOR NORWAY LOCALE WHEN THE MAP IS EXPANDED
	 * @param date
	 * @return
	 */
	public static String formatDateNorwayWithLeadingZeros(String date) {
		String formatedDate = null;
		String oldFormat = "yyyy-MM-dd";
		String newFormat = "dd.MM.yyyy";
		
		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

		try {
			formatedDate = sdf2.format(sdf1.parse(date));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatedDate;
	}
	/**
	 * THIS IS ONLY USED FOR SWEDISH LOCALE WHEN THE MAP IS EXPANDED
	 * @param date
	 * @return
	 */
	public static String formatSwedendateWithLeadingZeros(String date) {
		String formatedDate = null;
		String oldFormat = "yyyy-MM-dd";
		String newFormat = "yyy-MM-dd";
		
		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

		try {
			formatedDate = sdf2.format(sdf1.parse(date));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatedDate;
	}
	
	public static String getFormatedTransactionDate(String date, boolean isYearRequired) {
		// 2013-09-18 -- COOP
		String formatedDate = "";
		String oldFormat = "yyyy-MM-dd";
		String newFormat = null;
		
		String currentLocale = ApplicationEx.getInstance().getResources().getConfiguration().locale.toString();
		if(currentLocale.equalsIgnoreCase("nb_NO")) {
			newFormat = "d.M.yyyy";	
		} else {
			newFormat = "MM/dd/yyyy";
		}

		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

		try {
			formatedDate = sdf2.format(sdf1.parse(date));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentLocale.equalsIgnoreCase("nb_NO")) {
			String arry[] = formatedDate.split("\\.");
			String formatdate = arry[0] +"."+arry[1]+".";
			
			if(isYearRequired)
				return formatdate + arry[2];
			else
				return formatdate;
			
		} else {
			String arry[] = formatedDate.split("\\/");
			String formatdate = arry[0] +"/"+arry[1];

			if(isYearRequired)
				return formatdate + "/" + arry[2];
			else
				return formatdate;
		}
	}
	
	
	/**
	 * Gets the formated time.
	 *
	 * @param time the time
	 * @return the formated time
	 */
	public static String getFormatedTime(String time) {
		// 2013-04-17 10:44:59
		String formatedDate = "";
		String oldFormat = "yyyy-MM-dd HH:mm:ss";
		String newFormat = "HH:mm";

		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

		try {
			formatedDate = sdf2.format(sdf1.parse(time));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatedDate;
	}

	/**
	 * Convert 24 hour time format to 12 hour.
	 *
	 * @param _24Hourtime the _24 hourtime
	 * @return the string
	 */
	public static String convertTime(String _24Hourtime) {
		String formatedDate = "";

		SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
		Date _24HourDt = null;
		try {
			_24HourDt = _24HourSDF.parse(_24Hourtime);
			formatedDate = _12HourSDF.format(_24HourDt);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(_24HourDt);
		// System.out.println(_12HourSDF.format(_24HourDt));
		return formatedDate;

	}

	/**
	 * Gets the timein milisec.
	 * 
	 * @param strTime
	 *            the str time
	 * @return the timein milisec
	 * @throws ParseException
	 *             the parse exception
	 */
	public static long getTimeinMilisec(String strTime) throws ParseException {
		Date date = dateFormat.parse(strTime);
		return date.getTime();
	}



	/**
	 * Sets the color.
	 * 
	 * @param intColor
	 *            the int color
	 * @param views
	 *            the views
	 */
	public static void setColor(int intColor, View... views) {
		for (View view : views) {
			if (view instanceof Button) {
				((Button) view).setTextColor(intColor);
			}
		}
	}

	/**
	 * Sets the tab button.
	 * 
	 * @param drawable
	 *            the drawable
	 * @param btn
	 *            the btn
	 */
	public static void setTabButton(int drawable, Button... btn) {
		for (Button button : btn) {
			button.setBackgroundResource(drawable);
		}
	}

	/**
	 * Dist from formulae.
	 *
	 * @param latFrom the lat from
	 * @param longFrom the long from
	 * @param latTo the lat to
	 * @param longTo the long to
	 * @return the double
	 */
	public static double distFromFormulae(double latFrom, double longFrom,
			double latTo, double longTo) {

		// 3690 in miles 6371 in km
		double earthRadius = 3690000;
		// double earthRadius = 6371000;
		double dLat = Math.toRadians(latTo - latFrom);
		double dLng = Math.toRadians(longTo - longFrom);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(latFrom))
				* Math.cos(Math.toRadians(latTo)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(dist));

	}

	/**
	 * Gps to meters.
	 *
	 * @param lat_a the lat_a
	 * @param lng_a the lng_a
	 * @param lat_b the lat_b
	 * @param lng_b the lng_b
	 * @return the double
	 */
	public static double gpsToMeters(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		double pk = (180 / 3.14169);
		double a1 = lat_a / pk;
		double a2 = lng_a / pk;
		double b1 = lat_b / pk;
		double b2 = lng_b / pk;
		double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
		double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
		double t3 = Math.sin(a1) * Math.sin(b1);
		double tt = Math.acos(t1 + t2 + t3);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(6366000 * tt));
		// return 6366000 * tt;
	}

	/**
	 * Gets the UTC time.
	 *
	 * @return the UTC time
	 */
	public static String getUTCTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public static String getTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow;
	}

	/**
	 * Gets the optimized date.
	 *
	 * @return the optimized date
	 */
	public static String getOptimizedDate() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow.replace("-", "").trim();
	}

	/**
	 * 
	 * @param savedTimestamp
	 * @param surrentTimestamp
	 * @return
	 * @throws ParseException
	 */
	public static int compareTimeStamps(String savedTimestamp,String surrentTimestamp) throws ParseException {

		long difference;
		int days;
		int hours;
		int min;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date savedDateTime = simpleDateFormat.parse(savedTimestamp);// "2014-08-20 15:49:38"
		Date currentDateTime = simpleDateFormat.parse(surrentTimestamp);// "2014-08-20 15:59:38"

		difference = currentDateTime.getTime() - savedDateTime.getTime();
		days = (int) (difference / (1000 * 60 * 60 * 24));
		hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
		min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))/ (1000 * 60);
		Log.i("COOP", "::::min:::" + min);
		return min;
	}
}
