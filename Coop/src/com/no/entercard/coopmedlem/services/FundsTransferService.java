package com.no.entercard.coopmedlem.services;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.no.entercard.coopmedlem.ApplicationEx;
import com.no.entercard.coopmedlem.R;

public class FundsTransferService extends BaseService {
	
	private final String TAG_FUNDS_TRANSFER= "fundsTransfer";
	private final String TAG_ACCOUNTS= "accounts";
	private FundsTransferListener fundsTransferListener;
	
	private String saml;
	private String uuid;
	private String cookie;
	private String accountNum;
	private String beneficiaryAccountNumber;
	private String message;
	private BigInteger amount;
	private String beneficiaryName;
	
	public interface FundsTransferListener {
		void onFundsTransferSuccess(String resp);
		void onFundsTransferFailed(String error);
	}

	public FundsTransferService(String uuid, String cookie, String saml,
			String accountNum, String beneficiaryAccountNumber, String message,
			BigInteger amount, String beneficiaryName) {
		
		this.uuid = uuid;
		this.cookie = cookie;
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
		
		Log.i("", "FundsTransferService ID"+cookie);
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), saml);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), uuid);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), cookie);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_set_cookie), cookie);
		
		try {
			
			String url = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_FUNDS_TRANSFER;
			Log.i("COOP", ">>>>>>>>>>>>>>>>>>"+url);
			String response = makeRequest(url, getRequestJSONString(), POST);
			Log.i("COOP", ">>>>RESPONSE>>>>>"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
				
			} else if (statusCode == 204) {// SINCE THE RESPONSE IN EMPTY SENT FROM WS
				if (null != fundsTransferListener) {
					fundsTransferListener.onFundsTransferSuccess("");
				}
			} else if (!TextUtils.isEmpty(response)) { //WILL BE NEEDED FOR ERROR CHECKING FORM RESPONS
				String resp = parseResponseJSON(response);
				if (null != fundsTransferListener) {
					fundsTransferListener.onFundsTransferFailed(resp);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(R.string.exception_general));
			}
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
	}
	/**
	 * 
	 * @return
	 */
	private String getRequestJSONString() {
		JSONObject fundsTransferJsonObject = new JSONObject();
		try {
			
			JSONObject innerJsonObject = new JSONObject();
			innerJsonObject.put("amount", amount);
			innerJsonObject.put("message", message);
			innerJsonObject.put("beneficiaryName", beneficiaryName);
			innerJsonObject.put("beneficiaryAccountNumber", beneficiaryAccountNumber);
			
			fundsTransferJsonObject.put("fundsTransfer", innerJsonObject);
			
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
		//Log.i("COOP", ">>>>>>>>fundsTransferJsonObject.toString()>>>>>>>>>"+fundsTransferJsonObject);
		return fundsTransferJsonObject.toString();
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
