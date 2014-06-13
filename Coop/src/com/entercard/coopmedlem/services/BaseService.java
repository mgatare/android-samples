package com.entercard.coopmedlem.services;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import org.apache.http.Header;
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
import org.json.JSONException;

import android.util.Log;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.utils.NetworkHelper;

public abstract class BaseService implements Runnable {

	private static final String TAG = "BaseService";
	protected static final int GET = 10;
	protected static final int POST = 11;
	protected static final int PUT = 12;

	private static String BASE_URL = ""; //
	private static final String DEV_URL = "https://mobappt.entercard.com/ecmobile/";
	private static final String STAGING_URL = "https://mobapps.entercard.com/ecmobile/";
	private final String HTTP_HEADER_ACCEPT = "application/vnd.no.entercard.coop-medlem+json; version=2.0";
	
	
	private static final boolean isStaging = false;
	private static final int CONNECTION_TIMEOUT = 150000;

	public static final int NETWORK_NOT_AVAILABLE = 2001;
	public static final int NO_CONTENT = 204;
	public static final int BAD_REQUEST = 400;
	public static final int USER_NOT_AUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int PERMISSION_DENIED = 550;
	
	protected int statusCode = -1;
	protected Header[] headerArray;
	private ArrayList<NameValuePair> headers;
	
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

	public String getHeaderAccept() {
		return HTTP_HEADER_ACCEPT;
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
	 * @throws IOException
	 */
	public String makeRequest(String methodname, String postData, int type)
			throws KeyStoreException, KeyManagementException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			ClientProtocolException, IOException {

		String authResponse = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = null;
		
		if(NetworkHelper.isOnline(ApplicationEx.applicationEx)) {
			try {
				url = BASE_URL + methodname;
				httpclient.getConnectionManager().getSchemeRegistry();//.register(sch);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
				HttpResponse httpResponse = null;
				
				if (type == POST) { // make post request
					HttpPost httppost = new HttpPost(url);
					httppost.setHeader("Content-Type", "application/json");
					
					for (NameValuePair nameValuePair : headers) {
						httppost.setHeader(nameValuePair.getName(), nameValuePair.getValue());
					}
					
					httppost.setParams(httpParams);
					StringEntity input = new StringEntity(postData);
					httppost.setEntity(input);
					httpResponse = httpclient.execute(httppost);
				} else if (type == GET) { // make get request
					HttpGet httpget = new HttpGet(url);
					httpget.setHeader("Content-Type", "application/json");
					
					for (NameValuePair nameValuePair : headers) {
						httpget.setHeader(nameValuePair.getName(), nameValuePair.getValue());
					}
					
					httpget.setParams(httpParams);
					httpResponse = httpclient.execute(httpget);
					
				} else if (type == PUT) { // make put request
					HttpPut httpPut = new HttpPut(url);
					httpPut.setHeader("Content-Type", "application/json");
					
					for (NameValuePair nameValuePair : headers) {
						httpPut.setHeader(nameValuePair.getName(), nameValuePair.getValue());
					}
					
					StringEntity input = new StringEntity(postData);
					httpPut.setEntity(input);
					httpPut.setParams(httpParams);
					httpResponse = httpclient.execute(httpPut);
				}
				statusCode = httpResponse.getStatusLine().getStatusCode();
				
				//Retrive the Headers from the Response
				getHeadersFromResponse(httpResponse);
				
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
	
	/**
	 * @param httpResponse
	 */
	private void getHeadersFromResponse(HttpResponse httpResponse) {

		Header[] headers = httpResponse.getAllHeaders();
		for (Header header : headers) {
			Log.i(TAG, "HEADER Key : " + header.getName() + " : Value : "+ header.getValue());

			if (header.getName().equalsIgnoreCase("UUID"))
				ApplicationEx.applicationEx.setUUID(header.getValue());
			
			else if (header.getName().equalsIgnoreCase("jsessionid"))
				ApplicationEx.applicationEx.setSessionID(header.getValue());
			
			else if (header.getName().equalsIgnoreCase("Set-Cookie"))
				ApplicationEx.applicationEx.setCookie(header.getValue());
		}
	}
	/**
	 * 
	 * @param e
	 * @return
	 */
	public String getExceptionType(Exception e) {
		if(e instanceof JSONException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_json_errorr);
		} else if(e instanceof KeyManagementException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_key_management);
		} else if(e instanceof UnrecoverableKeyException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_unrecoverable_key);
		} else if(e instanceof KeyStoreException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_keystore);
		} else if(e instanceof NoSuchAlgorithmException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_algorithm_not_found_exception);
		} else if(e instanceof CertificateException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_certificate_not_found);
		} else if(e instanceof IOException) {
			return ApplicationEx.applicationEx.getString(R.string.exception_io_exception);
		} else if(e instanceof Exception) {
			Log.i("", "EXCEPTION MESSAGE :::: "+e.getMessage());
			return e.getMessage();
		} else {
			return ApplicationEx.applicationEx.getString(R.string.exception_general);
		}
	}
}