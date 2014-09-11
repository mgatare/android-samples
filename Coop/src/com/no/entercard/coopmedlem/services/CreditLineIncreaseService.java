package com.no.entercard.coopmedlem.services;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.no.entercard.coopmedlem.ApplicationEx;
import com.no.entercard.coopmedlem.R;

public class CreditLineIncreaseService extends BaseService {
	
	private final String TAG_CREDIT_LINE_INCREASE= "creditLineIncrease";
	private final String TAG_ACCOUNTS= "accounts";
	private CreditLineIncreaseListener creditLineIncreaseListener;
	
	private String saml;
	private String uuid;
	private String cookie;
	private String accountNum;
	private BigInteger mortgage;
	private BigInteger amountApplied;
	private String employmentType;
	private BigInteger yearlyIncome;
	private BigInteger otherLoans;
	
	public interface CreditLineIncreaseListener {
		void onCreditLineIncreaseSuccess(String resp);
		void onCreditLineIncreaseFailed(String error);
	}

	public CreditLineIncreaseService(String uuid, String cookie,
			String saml, String accountNum, BigInteger mortgage,
			BigInteger amountApplied, BigInteger yearlyIncome,
			BigInteger otherLoans,String employmentType) {
		this.uuid = uuid;
		this.cookie = cookie;
		this.saml = saml;
		this.accountNum = accountNum;
		this.mortgage = mortgage;
		this.amountApplied = amountApplied;
		this.employmentType = employmentType;
		this.yearlyIncome = yearlyIncome;
		this.otherLoans = otherLoans;
	}

	@Override
	void sentFailure(String codeTxt) {
		if (creditLineIncreaseListener != null) {
			creditLineIncreaseListener.onCreditLineIncreaseFailed(codeTxt);
		}
	}
	
	public void setTransactionListener(CreditLineIncreaseListener listener) {
		this.creditLineIncreaseListener = listener;
	}

	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), saml);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), uuid);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), cookie);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_set_cookie), cookie);
		
		try {
			
			String url = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_CREDIT_LINE_INCREASE;
			Log.i("COOP", ">>URL CLI>>"+url);
			String response = makeRequest(url, getRequestJSONString(), POST);
			Log.d("", "RESPONSE::::"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} 
			else if (statusCode == 204) {
				if (creditLineIncreaseListener != null) {
					creditLineIncreaseListener.onCreditLineIncreaseSuccess("");
				}
			} else if (!TextUtils.isEmpty(response)) {

				String parsedResponse = parseResponseJSON(response);

				if (creditLineIncreaseListener != null) {
					creditLineIncreaseListener.onCreditLineIncreaseFailed(parsedResponse);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(R.string.exception_general));
			}
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
	}

	private String getRequestJSONString() {
		JSONObject creditIncreaseJsonObject = new JSONObject();
		try {

			JSONObject innerJsonObject = new JSONObject();

			innerJsonObject.put("mortgage", mortgage);
			innerJsonObject.put("amountApplied", amountApplied);
			innerJsonObject.put("yearlyIncome", yearlyIncome);
			innerJsonObject.put("otherLoans", otherLoans);
			innerJsonObject.put("employmentType", employmentType);
			
			creditIncreaseJsonObject.put("creditLineIncrease", innerJsonObject);
			
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
		return creditIncreaseJsonObject.toString();
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
