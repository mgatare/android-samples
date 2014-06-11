package com.entercard.coopmedlem.services.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ServiceUtils {

	public static DateTimeFormatter appDateTimeFormatter = DateTimeFormat
			.forPattern("dd/MM/yyyy");
	public static DateTimeFormatter yyyyMMddFormatter = DateTimeFormat
			.forPattern("yyyy/MM/dd'T'HH:mm:ss");
	public static DateTimeFormatter yyyyMMddWithTimeHyphenFormatter = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss");
	public static DateTimeFormatter ddMMyyyyFormatter = DateTimeFormat
			.forPattern("dd/MM/yyyy'T'HH:mm:ss");
	public static DateTimeFormatter yyyyMMddHyphenFormatter = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	/**
	 * 
	 * @param date
	 * @param dateTimeFormatter
	 * @return
	 */
	public static String convertDate(String date,
			DateTimeFormatter dateTimeFormatter) {
		DateTime dateTime = DateTime.parse(date, dateTimeFormatter);
		return appDateTimeFormatter.print(dateTime.getMillis());
	}

	/**
	 * 
	 * @param date
	 * @param fromDateTimeFormatter
	 * @param toDateTimeFormatter
	 * @return
	 */
	public static String convertDateToSpecificFormat(String date,
			DateTimeFormatter fromDateTimeFormatter,
			DateTimeFormatter toDateTimeFormatter) {
		DateTime dateTime = DateTime.parse(date, fromDateTimeFormatter);
		return toDateTimeFormatter.print(dateTime.getMillis());
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static String convertArrayListToString(ArrayList<?> list) {
		if (null != list) {
			return list.toString().replace("[", "").replace("]", "");
		} else {
			return "";
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
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		String md5CodedValue = "";
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			md5CodedValue = sb.toString();
			System.out.println("md5PAss:" + sb.toString());
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5CodedValue;
	}

	/**
	 * 
	 * @param responseJsonObj
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static String getParsedErrorFromSerivce(JSONObject responseJsonObj)
			throws JSONException, Exception {
		boolean status = responseJsonObj.getBoolean("success");
		if (!status) {
			if (responseJsonObj.has("errors")) {
				JSONObject errorJSON = responseJsonObj.getJSONObject("errors");
				Iterator iterator = errorJSON.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = errorJSON.optString(key);
					return value;
				}
			}
		}
		return null;
	}
}