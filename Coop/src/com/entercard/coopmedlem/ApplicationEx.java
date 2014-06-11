package com.entercard.coopmedlem;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.util.Log;

import com.encapsecurity.encap.android.client.api.AndroidControllerFactory;
import com.encapsecurity.encap.android.client.api.Controller;
import com.entercard.coopmedlem.entities.AccountsModel;

public class ApplicationEx extends Application {

	private static final int CORE_POOL_SIZE = 6;
	private static final int MAXIMUM_POOL_SIZE = 6;
	public static ThreadPoolExecutor operationsQueue;
	public static ApplicationEx applicationEx;
	private Controller controller;
	private String LOG_TAG = getClass().getName();
	boolean isdeveloperMode = true;//set to false on Deployment/Production
	
	/*All data*/
	private ArrayList<AccountsModel> accountsArrayList;
	private String SAMLTxt;
	private String sessionID;
	private String UUID;
	private String cookie;
	
	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	@Override
	public void onCreate() {
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
			Log.d(LOG_TAG, "Encap Controller Successfully initialized.");
		} catch (Exception ex) {
			Log.d(LOG_TAG, "Error during initialization: ", ex);
		}
	}
	
	public void initializeControllerFromPreferences() {
		String serverUrl = getResources().getString(R.string.encap_server_url);
		controller.setServerUrl(serverUrl);

		String applicationId = getResources().getString(R.string.encap_app_id);
		controller.setApplicationId(applicationId);

		String apiKey = getResources().getString(R.string.encap_api_key);
		controller.setApiKey(apiKey);

		/*Suggested by Signicat Support(support@signicat.com) to use the Intermediate HASH key*/
		String publicKeyHashes = getResources().getString(R.string.encap_public_key_hash_intermediate);
		
		/*Intermediate (ThawteSSLCA): sha1/Lb4cyh2b83PU2HP6yrlPxhq7Gk0=
		leaf (encap.test.signicat.com): sha1/+PyESkyd4MxX8ZOBdHbEdx1JBbE=*/
		 
		try {
			setPublicKeyHashes(publicKeyHashes);
		} catch (Exception ex) {
			Log.e(LOG_TAG, "******Set public key hashes encountered exception: " + ex);
		}
		controller.setConnectionTimeout(Integer.parseInt(getResources().getString(R.string.encap_server_timeout)));
		controller.setSigningEnabled(false);
		//controller.setSigningKeySize(Integer.parseInt(getResources().getString(R.string.encap_signing_key_size)));
		controller.setClientOnly(false);

	}

	/**
	 * @param publicKeyHashes
	 * @throws Exception
	 */
	public void setPublicKeyHashes(String publicKeyHashes) throws Exception {
		String[] hash = publicKeyHashes.split(",");
		controller.setPublicKeyHashes(hash);
	}

	public ArrayList<AccountsModel> getAccountsArrayList() {
		return accountsArrayList;
	}

	public void setAccountsArrayList(ArrayList<AccountsModel> accountsArrayList) {
		this.accountsArrayList = accountsArrayList;
	}

	public String getSAMLTxt() {
		return SAMLTxt;
	}

	public void setSAMLTxt(String sAMLTxt) {
		SAMLTxt = sAMLTxt;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

}
