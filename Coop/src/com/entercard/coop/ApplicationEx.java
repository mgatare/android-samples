package com.entercard.coop;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.util.Log;

import com.encapsecurity.encap.android.client.api.AndroidLoggingControllerFactory;
import com.encapsecurity.encap.android.client.api.Controller;

public class ApplicationEx extends Application {

	private static final int CORE_POOL_SIZE = 6;
	private static final int MAXIMUM_POOL_SIZE = 6;
	public static ThreadPoolExecutor operationsQueue;
	public static ApplicationEx applicationEx;
	private Controller controller;
	private String LOG_TAG = getClass().getName();

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "onCreate()");
		super.onCreate();
		init();
		operationsQueue = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE, 100000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		applicationEx = this;
	}

	public Controller getController() {
		return controller;
	}

	private void init() {
		Log.d(LOG_TAG, "init()");
		try {
			if (getController() != null) {
				Log.d(LOG_TAG, "Already initialized!");
				return;
			}
			controller = AndroidLoggingControllerFactory
					.getInstance(getBaseContext());
			initializeControllerFromPreferences();
			// initializePreferencesFromApplication();

			Log.d(LOG_TAG, "Successfully initialized.");
		} catch (Exception ex) {
			Log.d(LOG_TAG, "Error during initialization: ", ex);
		}
	}

	public void initializeControllerFromPreferences() {
		// This test app reads from preferences for test purposes,
		// production apps uses hard coded values.

		// Set URL for Encap server.
		// final String serverUrl= loadPreference(R.string.pref_serverUrl_key,
		// "http://test.encap.no/pt");
		final String serverUrl = "https://demo.encapsecurity.com/pt";

		controller.setServerUrl(serverUrl);

		// Set application ID.
		final String applicationId = "encap";
		controller.setApplicationId(applicationId);

		// Set the API key.
		final String apiKey = ("9f333c24-efa4-47b2-ac57-77540a8b2881");
		controller.setApiKey(apiKey);

		final String publicKeyHashes = ("sha1/OHmlAWx9n7hv8NECa3PEhC6t+rc="
				+ ", " + "sha1/bMq9fbR+lKV1mQG2p9/UXRwJHMw=" + ", "
				+ "sha1/sYEIGhmkwJQf+uiVKMEkyZs0rMc=");
		try {
			setPublicKeyHashes(publicKeyHashes);
		} catch (Exception ex) {
			Log.d(LOG_TAG, "initializeControllerFromPreferences():"
					+ " Unable to set public key hashes: " + publicKeyHashes
					+ ", exception: " + ex);
		}
		controller.setConnectionTimeout(10000);

	}

	/**
	 * Set public key hashes and indicate the result of the operation.
	 * 
	 * @param publicKeyHashes
	 *            The comma separated list of public key hashes on Chrome format
	 *            (sha1/....).
	 */
	public void setPublicKeyHashes(String publicKeyHashes) throws Exception {
		Log.d(LOG_TAG, "setPublicKeyHashes(" + publicKeyHashes + ")");
		controller.setPublicKeyHashes(publicKeyHashes.split(","));
	}
}
