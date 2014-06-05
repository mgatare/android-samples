package com.entercard.coop.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.entercard.coop.ApplicationEx;
import com.entercard.coop.R;
import com.entercard.coop.helpers.CompatibilityUtils;
import com.entercard.coop.model.AccountsModel;

public class GetAccountsService extends BaseService {

	private static final String METHOD_ACCOUNTS = "accounts";
	private final String HTTP_HEADER_ACCEPT = "application/vnd.no.entercard.coop-medlem+json; version=2.0";
	
	private GetAccountsListener getAccountsListener;

	public interface GetAccountsListener {
		void onGetAccountsFinished(ArrayList<AccountsModel> accountArrayList);
		void onGetAccountsFailed(String error);
	}

	public GetAccountsService() {
	}
	
	@Override
	void sentFailure(String code) {
		Log.i("", "sentFailure::::"+getAccountsListener);
		if (getAccountsListener != null) {
			getAccountsListener.onGetAccountsFailed(code);
		}
	}
	
	public void setAccountsListener(GetAccountsListener faqServiceListener) {
		this.getAccountsListener = faqServiceListener;
	}

	@Override
	void executeRequest() {
		
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_accept), HTTP_HEADER_ACCEPT);
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_saml), ApplicationEx.applicationEx.getSAMLTxt());
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_device_platform), "Android");
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_device_version), CompatibilityUtils.getAndroidVersion());
		
		try {
			
			String response = makeRequest(METHOD_ACCOUNTS, null, GET);
			Log.i("", "RESPONSE::::"+response);
			
			//sentFailure(ApplicationEx.applicationEx.getString(R.string.exception_network_not_found));
			ArrayList<AccountsModel> faqArrayList = new ArrayList<AccountsModel>();
			faqArrayList = parseJSONResponse(response);

			if (getAccountsListener != null) {
				getAccountsListener.onGetAccountsFinished(faqArrayList);
			}
			
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		} 
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws JSONException
	 */
	private ArrayList<AccountsModel> parseJSONResponse(String response)
			throws Exception, JSONException {
		ArrayList<AccountsModel> arrayList = new ArrayList<AccountsModel>();
		AccountsModel accountsModel = new AccountsModel();

		/**
		 * { "error": { "code": String, "reason": String } }
		 **/
		JSONObject responseJSON = new JSONObject(response);
		if (responseJSON.has("error")) {

			JSONObject errorJson = responseJSON.getJSONObject("error");
			String code = null;
			String reason = null;

			if (errorJson.has("code")) {
				code = errorJson.getString("code");
			}
			if (errorJson.has("reason")) {
				reason = errorJson.getString("reason");
				throw new Exception(reason);
			}
		}

		if (responseJSON.has("accounts")) {
			JSONArray accountsJsonArray = responseJSON.getJSONArray("accounts");
			
			for (int i = 0; i < accountsJsonArray.length(); i++) {
				//TODO
				JSONObject accountsJSON = accountsJsonArray.getJSONObject(i);
				
			}
		}
		return arrayList;
	}
}
