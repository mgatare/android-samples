package com.entercard.coop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceHelper {

	private SharedPreferences preferences;

	public PreferenceHelper(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void clear() {
		// here you get your prefrences by either of two methods
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return preferences.getInt(key, 0);
	}
}
