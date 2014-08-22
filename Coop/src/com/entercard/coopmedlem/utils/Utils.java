/*
 * 
 */
package com.entercard.coopmedlem.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;

import com.entercard.coopmedlem.ApplicationEx;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {

	/**
	 * Write to text file.
	 * 
	 * @param data
	 *            the data
	 * @param ctx
	 *            the ctx
	 * @param filename
	 *            the filename
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
	 * @param savedTimestamp
	 * @param currentTimestamp
	 * @return
	 */
	public static boolean isDeviceSessionExpired(String savedTimestamp,String currentTimestamp) {
		int minutes;
		Log.d("", "savedTimestamp>>>>>>>>>>>>>>>>>>>" + savedTimestamp);
		Log.d("", "currentTimestamp>>>>>>>>>>>>>>>>>" + currentTimestamp);
		try {
			minutes = DateUtils.compareTimeStamps(savedTimestamp,currentTimestamp);
			if (minutes >= 15)
				return true;
			else
				return false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

	/**
	 * Read response from assets file.
	 * 
	 * @param context
	 *            the context
	 * @param filename
	 *            the filename
	 * @return the string
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
	 * Compare two dates.
	 * 
	 * @param strdate1
	 *            the strdate1
	 * @param strdate2
	 *            the strdate2
	 * @return the int
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
	 * Gets the difference between dates.
	 * 
	 * @param remainderdate
	 *            the remainderdate
	 * @param todaysDate
	 *            the todays date
	 * @return the difference between dates
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

	/**
	 * Gets the map thumbnail from city name.
	 * 
	 * @param city
	 *            the city
	 * @param country
	 *            the country
	 * @return the map thumbnail from city name
	 */
	public static String getMapThumbnailFromCityOrCountry(String city, String country) {

		String params = null;
		String URL = null;
		int zoomLevel = 9;
		StringBuilder builder = new StringBuilder();

		if (!TextUtils.isEmpty(city)) {
			builder.append(StringUtils.removeBlankSpaces(city));
			builder.append("+");
			//zoomLevel = 10;
		}

		if (!TextUtils.isEmpty(country)) {
			builder.append(StringUtils.removeBlankSpaces(country));
		}

		params = builder.toString();
		
		if(TextUtils.isEmpty(params)) {
			params = "Norway";
			zoomLevel = 6;
		}
			
		if(!TextUtils.isEmpty(params)) {
			Log.e("Coop", "zoomLevel>>"+zoomLevel);
			URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size=570x350&maptype=roadmap&sensor=true";
		} else {
			return null;
		}
		
//		Bitmap bitmap = null;
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpGet request = new HttpGet(URL);
//
//		InputStream in = null;
//		try {
//			in = httpclient.execute(request).getEntity().getContent();
//			bitmap = BitmapFactory.decodeStream(in);
//			in.close();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return URL;
	}

	/**
	 * Geo code address.
	 * 
	 * @param address
	 *            the address
	 * @return the location
	 */
	public static Location geoCodeAddress(String address) {
		Geocoder geoCoder = new Geocoder(ApplicationEx.getInstance(), Locale.UK);
		;
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {

				int lat = (int) (addresses.get(0).getLatitude() * 1E6);
				int lon = (int) (addresses.get(0).getLongitude() * 1E6);

				double latitude = lat / 1E6;
				double longtitude = lon / 1E6;

				Log.i("COOP", "getLatitude>>>" + latitude);
				Log.i("COOP", "getLongitude" + longtitude);

				Location location = new Location("");
				location.setLatitude(lat / 1E6);
				location.setLongitude(lon / 1E6);
				return location;
			}
		} catch (Exception e) {
			Log.e("ERR", "geoCodeAddress:::" + e.toString());
		}
		return null;
	}

	/**
	 * Copy stream.
	 * 
	 * @param is
	 *            the is
	 * @param os
	 *            the os
	 */
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	/**
	 * Disable view context menu options.
	 *
	 * @param editText the edit text
	 */
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
//	public static void disableViewContextMenuOptions(EditText editText) {
//
//		if (android.os.Build.VERSION.SDK_INT < 11) {
//			editText.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//				@Override
//				public void onCreateContextMenu(ContextMenu menu, View v,
//						ContextMenuInfo menuInfo) {
//					menu.clear();
//				}
//			});
//		} else {
//			Log.e("COOP", "SDKKKKKKKKKKKKKKKK 111");
//			editText.setCustomSelectionActionModeCallback(new Callback() {
//				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//					return false;
//				}
//
//				public void onDestroyActionMode(ActionMode mode) {
//				}
//
//				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//					return false;
//				}
//
//				public boolean onActionItemClicked(ActionMode mode,
//						MenuItem item) {
//					return false;
//				}
//			});
//		}
//		editText.setLongClickable(false);
//		editText.setTextIsSelectable(false);
//	}
	
	@SuppressLint("NewApi")
	public static void disableViewContextMenuOptions(EditText editText) {
		if (android.os.Build.VERSION.SDK_INT < 11) {
			editText.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					menu.clear();
				}
			});
		} else {
			editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public void onDestroyActionMode(ActionMode mode) {
				}

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					return false;
				}
			});
		}
		editText.setLongClickable(false);
		editText.setTextIsSelectable(false);
	}
}
