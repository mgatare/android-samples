package com.entercard.coop.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.entercard.coop.ApplicationEx;
import com.entercard.coop.helpers.NetworkHelper;

public abstract class BaseService implements Runnable {

	private static final String TAG = "BaseService";
	private static final int GET = 10;
	private static final int POST = 11;
	private static final int PUT = 12;

	private static String BASE_URL = ""; //
	private static final String DEV_URL = "https://mobappt.entercard.com/ecmobile/";
	private static final String STAGING_URL = "https://mobapps.entercard.com/ecmobile/";
	
	private String HTTP_HEADER_ACCEPT = "application/vnd.no.entercard.remember+json; version=2.0";
	
	private ArrayList<NameValuePair> headers;
	
	private static final boolean isStaging = true;
	private static final int CONNECTION_TIMEOUT = 30000;

	public static final int NETWORK_NOT_AVAILABLE = 2001;
	public static final int EMPTY_RESPONSE = 204;
	public static final int BAD_REQUEST = 400;
	public static final int USER_NOT_AUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int PERMISSION_DENIED = 550;
	
	protected int statusCode = -1;
	
	public static final String INCORRECT_JSON_RESPONSE = "Incorrect response received from server";
	public static final String SERVER_NOT_RESPONDING = "Server not responding";
	public static final String INVALID_SEARCH = "Invalid search criteria";
	
	public BaseService() {
		headers = new ArrayList<NameValuePair>();
		if (isStaging) {
			BASE_URL = STAGING_URL;
		} else {
			BASE_URL = DEV_URL;
		}
	}
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	/**
	 * Execute the request based on the type of service
	 */
	abstract void executeRequest();

	void sentFailure(String code) {
	}

	@Override
	public void run() {
		executeRequest();
	}
	/**
	 * 
	 * @param url
	 * @param postData
	 * @param type
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public String makeRequest(String methodname, String postData, int type) throws NoSuchAlgorithmException, KeyStoreException,
			CertificateException, UnrecoverableKeyException, KeyManagementException, ClientProtocolException,
			UnsupportedEncodingException, IOException {

		String authResponse = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = null;
		
		if(NetworkHelper.isOnline(ApplicationEx.applicationEx)) {
			try {
				url = BASE_URL + methodname;
//				Resources resources = ApplicationEx.applicationEx.getResources();
//				AssetManager assetManager = resources.getAssets();
//				InputStream is = assetManager.open(CERT_NAME);
//				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//				try {
//					trustStore.load(is, "7layer".toCharArray());
//				} finally {
//					is.close();
//				}
//				SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
//				Scheme sch = new Scheme("https", socketFactory, 8443);
				httpclient.getConnectionManager().getSchemeRegistry();//.register(sch)
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
				HttpResponse httpResponse = null;
				
				if (type == POST) { // make post request
					HttpPost httppost = new HttpPost(url);
//					httppost.setHeader("Content-Type", "application/json");
//					httppost.setHeader("Accept", HTTP_HEADER_ACCEPT);
					
					for (NameValuePair h : headers) {
						httppost.setHeader(h.getName(), h.getValue());
					}
					
					httppost.setParams(httpParams);
					StringEntity input = new StringEntity(postData);
					httppost.setEntity(input);
					httpResponse = httpclient.execute(httppost);
				} else if (type == GET) { // make get request
					HttpGet httpget = new HttpGet(url);
//					httpget.setHeader("Content-Type", "application/json");
//					httpget.setHeader("X-API-Key", API_KEY);
					
					for (NameValuePair h : headers) {
						httpget.setHeader(h.getName(), h.getValue());
					}
					
					httpget.setParams(httpParams);
					httpResponse = httpclient.execute(httpget);
				} else if (type == PUT) { // make put request
					HttpPut httpPut = new HttpPut(url);
//					httpPut.setHeader("Content-Type", "application/json");
//					httpPut.setHeader("X-API-Key", API_KEY);
					
					for (NameValuePair h : headers) {
						httpPut.setHeader(h.getName(), h.getValue());
					}
					
					StringEntity input = new StringEntity(postData);
					httpPut.setEntity(input);
					httpPut.setParams(httpParams);
					httpResponse = httpclient.execute(httpPut);
				}
				statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.i(TAG, "STATUS CODE:: "+statusCode);
				if (statusCode == 204) {// 204 is empty response
					return "";
				}
	
				HttpEntity responseEntity = httpResponse.getEntity();
				InputStream entityRes = responseEntity.getContent();
				int i = 0;
				StringBuilder sb = new StringBuilder();
				while ((i = entityRes.read()) != -1) {
					sb.append((char) i);
				}
				entityRes.close();
				authResponse = sb.toString();
			} finally {
				httpclient.getConnectionManager().shutdown();
			}
			return authResponse;
		} else {
			return null;
		}
	}
	
	public int getStatusCode() {
		return statusCode;
	}
}