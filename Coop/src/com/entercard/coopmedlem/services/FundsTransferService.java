package com.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONObject;

import android.text.TextUtils;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;

public class FundsTransferService extends BaseService {

	/**http://127.0.0.1:9393/accounts/5299369000100666/fundsTransfer

request.headers={
    Accept = "application/vnd.no.entercard.coop-medlem+json; version=2.0";
    "Accept-Language" = "en;q=1, fr;q=0.9, de;q=0.8, zh-Hans;q=0.7, zh-Hant;q=0.6, ja;q=0.5";
    "Content-Type" = "application/json; charset=utf-8";
    SAML = "PHNhbWwyOkFzc2QXNzZXJ0aW9uPg==";
    UUID = "051cf03a-1184-4401-bdec-8afde58b2008";
    "User-Agent" = "Remember/558.15 (iPhone Simulator; iOS 7.1; Scale/2.00)";
    jsessionid = "2014-05-30 14:20:51 +0530";
}
**/
	
	private final String TAG_FUNDS_TRANSFER= "/fundsTransfer";
	private final String TAG_ACCOUNTS= "accounts";
	private FundsTransferListener fundsTransferListener;
	
	private String saml;
	private String uuid;
	private String jsessionID;
	private String accountNum;
	private String beneficiaryAccountNumber;
	private String message;
	private String amount;
	private String beneficiaryName;
	
	public interface FundsTransferListener {
		void onFundsTransferFinished(ArrayList<String> arrayList);
		void onFundsTransferFailed(String error);
	}

	public FundsTransferService(String uuid, String sessionID, String saml,
			String accountNum, String beneficiaryAccountNumber, String message,
			String amount, String beneficiaryName) {
		
		this.uuid = uuid;
		this.jsessionID = sessionID;
		this.saml = saml;
		this.accountNum = accountNum;
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
		this.message = message;
		this.amount = amount;
		this.beneficiaryName = beneficiaryName;
	}
	
	@Override
	void sentFailure(String codeTxt) {
		if (fundsTransferListener != null) {
			fundsTransferListener.onFundsTransferFailed(codeTxt);
		}
	}
	
	public void setTransactionListener(FundsTransferListener listener) {
		this.fundsTransferListener = listener;
	}

	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), saml);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), uuid);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), jsessionID);
		
		try {
			
			String url = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_FUNDS_TRANSFER;
			//Log.i("COOP", ">>>>>>>>>>>>>>>>>>"+url);
			String response = makeRequest(url, getRequestJSONString(), POST);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} else if(!TextUtils.isEmpty(response)) {
				
				ArrayList<String> transactionArrayList = new ArrayList<String>();
				
				if (fundsTransferListener != null) {
					fundsTransferListener.onFundsTransferFinished(transactionArrayList);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(R.string.exception_general));
			}
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
	}

	private String getRequestJSONString() {
		/**
		 * request.body={"fundsTransfer":{"beneficiaryAccountNumber":
		 * "11111111111"
		 * ,"message":"asdas","amount":1111,"beneficiaryName":"Amits"}}
		 **/
		JSONObject fundsTransferJsonObject = new JSONObject();
		try {

			JSONObject innerJsonObject = new JSONObject();

			innerJsonObject.put("beneficiaryAccountNumber", beneficiaryAccountNumber);
			innerJsonObject.put("message", message);
			innerJsonObject.put("amount", amount);
			innerJsonObject.put("beneficiaryName", beneficiaryName);
			
			fundsTransferJsonObject.put("fundsTransfer", innerJsonObject.toString());
			
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
		return fundsTransferJsonObject.toString();
	}
}
