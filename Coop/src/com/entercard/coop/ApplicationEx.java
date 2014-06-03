package com.entercard.coop;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.util.Log;

import com.encapsecurity.encap.android.client.api.AndroidControllerFactory;
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
			
			controller = AndroidControllerFactory.getInstance(getBaseContext());
			initializeControllerFromPreferences();
			Log.d(LOG_TAG, "Successfully initialized.");
			
		} catch (Exception ex) {
			Log.d(LOG_TAG, "Error during initialization: ", ex);
		}
	}

	public void initializeControllerFromPreferences() {
		// Set URL for Encap server.
		String serverUrl = "https://encap.test.signicat.com/encap_entercard";
		controller.setServerUrl(serverUrl);

		String applicationId = "encap";
		controller.setApplicationId(applicationId);

		String apiKey = "9f333c24-efa4-47b2-ac57-77540a8b2881";
		controller.setApiKey(apiKey);

		String publicKeyHashes = "sha1/Lb4cyh2b83PU2HP6yrlPxhq7Gk0=";
		
		/**Intermediate (ThawteSSLCA): sha1/Lb4cyh2b83PU2HP6yrlPxhq7Gk0=
		leaf (encap.test.signicat.com): sha1/+PyESkyd4MxX8ZOBdHbEdx1JBbE=
		 **/
		
		try {
			setPublicKeyHashes(publicKeyHashes);
		} catch (Exception ex) {
			Log.e(LOG_TAG, "*********Unable to set public key hashes: exception: " + ex);
		}
		controller.setConnectionTimeout(120000);
		controller.setSigningEnabled(false);
		//controller.setSigningKeySize(2048);
		controller.setClientOnly(false);

	}

	/**
	 * @param publicKeyHashes
	 * @throws Exception
	 */
	public void setPublicKeyHashes(String publicKeyHashes) throws Exception {
		String [] hash = publicKeyHashes.split(",");
		controller.setPublicKeyHashes(hash);
	}
}
