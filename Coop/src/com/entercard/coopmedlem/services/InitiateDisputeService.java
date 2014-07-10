package com.entercard.coopmedlem.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

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
    
    /accounts/{accountNumber}/transactions/{transactionId}/dispute
}*/

	private final String METHOD_DISPUTE = "dispute";
	private final String TAG_TRANSACTION = "transactions";
	private final String TAG_ACCOUNTS= "accounts";
	private String uuidTxt;
	private String samlTxt;
	private String jSessionIDTxt;
	private String accountNum;
	private String transactionID;
	private float billingAmountTxt;
	private String descriptionTxt;
	private String email;
	private boolean knownTransaction; 
	private String mobile;
	private String reason;
	private String transactionDate;
	
	private InitiateDisputeListener disputeRaiseListener;

	public interface InitiateDisputeListener {
		void onInitiateDisputeFinished(String response);
		void onInitiateDisputeFailed(String error);
	}

	public InitiateDisputeService(String uuid, String saml, String jsessionID, 
			String accountNum, String transactionID, String billingAmount, String description, String email, boolean knownTransaction, 
			String mobile, String reason, String transactionDate) {
		this.uuidTxt = uuid;
		this.samlTxt = saml;
		this.jSessionIDTxt = jsessionID;
		this.accountNum = accountNum;
		this.transactionID = transactionID;
		this.billingAmountTxt = Float.parseFloat(billingAmount);
		this.descriptionTxt= description;
		this.email = email;
		this.knownTransaction= knownTransaction;
		this.mobile = mobile;
		this.reason= reason;
		this.transactionDate = transactionDate;
	}
	
	@Override
	void sentFailure(String codeTxt) {
		if (disputeRaiseListener != null) {
			disputeRaiseListener.onInitiateDisputeFailed(codeTxt);
		}
	}
	
	public void setInitiateDisputeListener(InitiateDisputeListener listener) {
		this.disputeRaiseListener = listener;
	}

	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), samlTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), uuidTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), jSessionIDTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_set_cookie), jSessionIDTxt);
		
		try {
			String strURL = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_TRANSACTION + "/" + transactionID + "/" + METHOD_DISPUTE;
			Log.d("", ">>URLLLLLLLLLL>>"+strURL);
			String response = makeRequest(strURL, getRequestJSONString(), POST);
			Log.d("", ">>RESPONSE>>"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} else if(statusCode == 204) {
				if (disputeRaiseListener != null) {
					disputeRaiseListener.onInitiateDisputeFinished("");
				}
			} else if(!TextUtils.isEmpty(response)) {
				
				String parsedResponse = parseResponseJSON(response);
				
				if (disputeRaiseListener != null) {
					disputeRaiseListener.onInitiateDisputeFinished(parsedResponse);
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
			innerJSON.put("billingAmount", billingAmountTxt);
			innerJSON.put("description", descriptionTxt);
			innerJSON.put("email", email);
			innerJSON.put("knownTransaction", knownTransaction);
			innerJSON.put("mobile", mobile);
			innerJSON.put("reason", reason);
			innerJSON.put("transactionDate", transactionDate);

			requestJSON.put("dispute", innerJSON);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("COOP", ">>>>>>>>fundsTransferJsonObject.toString()>>>>>>>>>"+requestJSON.toString());
		return requestJSON.toString();
	}
	/**
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws JSONException
	 */
	private String parseResponseJSON(String response) throws Exception, JSONException {

		JSONObject responseJSON = new JSONObject(response);
		
		if (responseJSON.has("error")) {
			JSONObject errorJson = responseJSON.getJSONObject("error");
			String reason = null;
			if (errorJson.has("reason")) {
				reason = errorJson.getString("reason");
				throw new Exception(reason);
			}
		}
		return "";
	}
}
