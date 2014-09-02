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

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationEx.
 */
public class ApplicationEx extends Application {

	/** The Constant CORE_POOL_SIZE. */
	private static final int CORE_POOL_SIZE = 6;
	
	/** The Constant MAXIMUM_POOL_SIZE. */
	private static final int MAXIMUM_POOL_SIZE = 6;
	
	/** The operations queue. */
	public static ThreadPoolExecutor operationsQueue;
	
	/** The application ex. */
	private static ApplicationEx applicationEx;
	
	/** The controller. */
	private Controller controller;
	
	/** The log tag. */
	private String LOG_TAG = getClass().getName();
	
	/** The isdeveloper mode. */
	boolean isdeveloperMode = false;//set to false on Deployment/Production
	
	/*All data*/
	/** The accounts array list. */
	private ArrayList<AccountsModel> accountsArrayList;
	
	/** The SAML txt. */
	private String SAMLTxt;
	
	/** The session id. */
	private String serverSessionID;
	
	/** The uuid. */
	private String UUID;
	
	/** The cookie. */
	private String cookie;
	
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		init();
		
		operationsQueue = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE, 100000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		applicationEx = (ApplicationEx) getApplicationContext();
		//applicationEx = this;
	}

	public static ApplicationEx getInstance() {
		if(null!= applicationEx)
			return applicationEx;
		else
			return new ApplicationEx();
	}
	
	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Inits the.
	 */
	private void init() {
		Log.d(LOG_TAG, "init()");
		//TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
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
	
	/**
	 * Initialize controller from preferences.
	 */
	public void initializeControllerFromPreferences() {
		String serverUrl = getResources().getString(R.string.encap_server_url);
		controller.setServerUrl(serverUrl);

		String applicationId = getResources().getString(R.string.encap_app_id);
		controller.setApplicationId(applicationId);

		String apiKey = getResources().getString(R.string.encap_api_key);
		controller.setApiKey(apiKey);

		/* Suggested by Signicat Support(support@signicat.com) to use the Intermediate HASH key*/
		String publicKeyHashes = getResources().getString(R.string.encap_public_key_hash_intermediate);
		
		/*Intermediate (ThawteSSLCA): sha1/Lb4cyh2b83PU2HP6yrlPxhq7Gk0=
		leaf (encap.test.signicat.com): sha1/+PyESkyd4MxX8ZOBdHbEdx1JBbE=*/
		 
		try {
			setPublicKeyHashes(publicKeyHashes);
		} catch (Exception ex) {
			Log.e(LOG_TAG, "******Set public key hashes encountered exception: " + ex);
		}
		controller.setConnectionTimeout(60000);//Integer.parseInt(getResources().getString(R.string.encap_server_timeout))
		controller.setSigningEnabled(false);
		//controller.setSigningKeySize(Integer.parseInt(getResources().getString(R.string.encap_signing_key_size)));
		controller.setClientOnly(false);

	}

	/**
	 * Sets the public key hashes.
	 *
	 * @param publicKeyHashes the new public key hashes
	 * @throws Exception the exception
	 */
	public void setPublicKeyHashes(String publicKeyHashes) throws Exception {
		String[] hash = publicKeyHashes.split(",");
		controller.setPublicKeyHashes(hash);
	}

	/**
	 * Gets the accounts array list.
	 *
	 * @return the accounts array list
	 */
	public ArrayList<AccountsModel> getAccountsArrayList() {
		return accountsArrayList;
	}

	/**
	 * Sets the accounts array list.
	 *
	 * @param accountsArrayList the new accounts array list
	 */
	public void setAccountsArrayList(ArrayList<AccountsModel> accountsArrayList) {
		this.accountsArrayList = accountsArrayList;
	}

	/**
	 * Gets the SAML txt.
	 * 
	 * @return the SAML txt
	 */
	public String getSAMLTxt() {
		return SAMLTxt;
	}

	/**
	 * Sets the SAML txt.
	 * 
	 * @param sAMLTxt
	 *            the new SAML txt
	 */
	public void setSAMLTxt(String sAMLTxt) {
		SAMLTxt = sAMLTxt;
	}

	/**
	 * Gets the cookie.
	 * 
	 * @return the cookie
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * Sets the cookie.
	 * 
	 * @param cookie
	 *            the new cookie
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	/**
	 * Gets the session id.
	 *
	 * @return the session id
	 */
	public String getServerSessionID() {
		return serverSessionID;
	}

	/**
	 * Sets the session id.
	 *
	 * @param sessionID the new session id
	 */
	public void setServerSessionID(String sessionID) {
		this.serverSessionID = sessionID;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uUID the new uuid
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	/**
	 * 
	 */
	public void clearGlobalContents() {
		setAccountsArrayList(null);
		setCookie(null);
		setSAMLTxt(null);
		setServerSessionID(null);
		setUUID(null);
		//preferenceHelper.addString(getResources().getString(R.string.pref_device_session), null);
	}
}
