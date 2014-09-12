package com.no.entercard.coopmedlem.utils;

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
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager;
import android.widget.EditText;

import com.no.entercard.coopmedlem.ApplicationEx;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
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
	 * TIMEOUT for the App
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
			if (minutes >= 3)//minutes < 0 || 
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
		int zoomLevel = 8;
		StringBuilder builder = new StringBuilder();

		if (!TextUtils.isEmpty(city)) {
			builder.append(StringUtils.removeBlankSpaces(city));
			builder.append(","); //%20
			zoomLevel = 17;
		}

		if (!TextUtils.isEmpty(country)) {
			builder.append(StringUtils.removeBlankSpaces(country));
			//zoomLevel = 6;
		}

		params = builder.toString();
		
		if(TextUtils.isEmpty(params)) {
			params = "Norway";
			//zoomLevel = 6;
		}
		
		WindowManager wm = (WindowManager) ApplicationEx.getInstance().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		Log.e("Coop", "width>>"+width);
		Log.e("Coop", "height>>"+height);
		
		if(!TextUtils.isEmpty(params)) {
			
			double density = ApplicationEx.getInstance().getResources().getDisplayMetrics().density;
			
			Log.e("Coop", "zoomLevel>>"+zoomLevel);
			Log.e("Coop", "density>>"+density);
			
			if (density >= 4.0) {
			   //"xxxhdpi";
				Log.e("COOP", ":::xxxhdpi::::");
				URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size="+width+"x380&maptype=roadmap";
			}
			else if (density >= 3.0 && density < 4.0) {
			   //xxhdpi
				Log.e("COOP", ":::xxhdpi::::");
				URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size="+width+"x355&maptype=roadmap";
			}
			else if (density >= 2.0) {
			   //xhdpi
				Log.e("COOP", ":::xhdpi::::");
				URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size="+width+"x380&maptype=roadmap";
			}
			else if (density >= 1.5 && density < 2.0) {
			   //hdpi
				Log.e("COOP", ":::hdpi::::");
				URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size="+width+"x300&maptype=roadmap";
			}
			else if (density >= 1.0 && density < 1.5) {
			   //mdpi
				Log.e("COOP", ":::mdpi::::");
				URL = "http://maps.google.com/maps/api/staticmap?center="+ params.trim() + "&zoom=" + zoomLevel 
						+ "&size="+width+"x300&maptype=roadmap";
			}
			
		} else {
			return null;
		}
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
