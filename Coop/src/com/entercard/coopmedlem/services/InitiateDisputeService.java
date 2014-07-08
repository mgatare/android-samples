package com.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONObject;

import android.text.TextUtils;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;

public class InitiateDisputeService extends BaseService {
	
	/***http://127.0.0.1:9393/accounts/5299369000100666/transactions/165/dispute
request.headers={
    Accept = "application/vnd.no.entercard.coop-medlem+json; version=2.0";
    "Accept-Language" = "en;q=1, fr;q=0.9, de;q=0.8, zh-Hans;q=0.7, zh-Hant;q=0.6, ja;q=0.5";
    "Content-Type" = "application/json; charset=utf-8";
    SAML = "PHNhbWQXNzZXJ0aW9uPg==";
    UUID = "051cf03a-1184-4401-bdec-8afde58b2008";
    jsessionid = "2014-05-30 14:20:51 +0530";
}*/

	private final String METHOD_DISPUTE = "/dispute";
	private final String TAG_TRANSACTION = "/transactions";
	private final String TAG_ACCOUNTS= "accounts";
	private String struuid;
	private String strSAML;
	private String strAccountNumer;
	private String strTransNumber;
	private String strJSessionID;
	
	private DisputeRaiseListener disputeRaiseListener;

	public interface DisputeRaiseListener {
		void onRaiseDisputeFinished(ArrayList<String> arrayList);
		void onRaiseDisputeFailed(String error);
	}

	public InitiateDisputeService(String uuid, String saml, String acountNumber, String transNumber, String jsessionID) {
		this.struuid = uuid;
		this.strSAML = saml;
		this.strAccountNumer = acountNumber;
		this.strTransNumber= transNumber;
		this.strJSessionID = jsessionID;
		
	}
	
	@Override
	void sentFailure(String codeTxt) {
		if (disputeRaiseListener != null) {
			disputeRaiseListener.onRaiseDisputeFailed(codeTxt);
		}
	}
	
	public void setAccountsListener(DisputeRaiseListener serviceListener) {
		this.disputeRaiseListener = serviceListener;
	}

	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), strSAML);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), struuid);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), strJSessionID);
		
		try {
			String strURL = TAG_ACCOUNTS + "/" + strAccountNumer + "/" + TAG_TRANSACTION + "/" + strTransNumber + "/" + METHOD_DISPUTE;
			String response = makeRequest(strURL, null, GET);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			}else if(!TextUtils.isEmpty(response)) {
				
				ArrayList<String> accountsArrayList = new ArrayList<String>();
				
				if (disputeRaiseListener != null) {
					disputeRaiseListener.onRaiseDisputeFinished(accountsArrayList);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(R.string.exception_general));
			}
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
	}
	
	private String getRequestJSONString() {
		/**{
	    "dispute": {
	        "billingAmount": Number,
	        "description": String,
	        "email": String,
	        "knownTransaction": boolean,
	        "mobile": String,
	        "reason": String,
	        "transactionDate": String
	    }
	}**/
		JSONObject requestJSON = new JSONObject();

		try {

			JSONObject innerJSON = new JSONObject();
			innerJSON.put("billingAmount", "");
			innerJSON.put("description", "");
			innerJSON.put("email", "");
			innerJSON.put("knownTransaction", "");
			innerJSON.put("mobile", "");
			innerJSON.put("reason", "");
			innerJSON.put("transactionDate", "");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
