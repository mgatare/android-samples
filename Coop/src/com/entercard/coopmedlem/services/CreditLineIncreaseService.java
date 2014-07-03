package com.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONObject;

import android.text.TextUtils;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;

public class CreditLineIncreaseService extends BaseService {

	/**http://127.0.0.1:9393/accounts/5299369000100666/creditLineIncrease':
request.headers={
    Accept = "application/vnd.no.entercard.coop-medlem+json; version=2.0";
    "Accept-Language" = "en;q=1, fr;q=0.9, de;q=0.8, zh-Hans;q=0.7, zh-Hant;q=0.6, ja;q=0.5";
    "Content-Type" = "application/json; charset=utf-8";
    SAML = "PHNhbWwyOkFzc2VydJ0aW9uPg==";
    UUID = "051cf03a-1184-4401-bdec-8afde58b2008";
    "User-Agent" = "Remember/558.15 (iPhone Simulator; iOS 7.1; Scale/2.00)";
    jsessionid = "2014-05-30 14:20:51 +0530";
}
*/
	
	private final String TAG_CREDIT_LINE_INCREASE= "/creditLineIncrease";
	private final String TAG_ACCOUNTS= "accounts";
	private CreditLineIncreaseListener creditLineIncreaseListener;
	
	private String saml;
	private String uuid;
	private String sessionID;
	private String accountNum;
	private String mortgage;
	private String amountApplied;
	private String employmentType;
	private String yearlyIncome;
	private String otherLoans;
	
	public interface CreditLineIncreaseListener {
		void onCreditLineIncreaseFinished(ArrayList<String> arrayList);
		void onCreditLineIncreaseFailed(String error);
	}

	public CreditLineIncreaseService(String uuid, String sessionID,
			String saml, String accountNum, String mortgage,
			String amountApplied, String employmentType, String yearlyIncome,
			String otherLoans) {
		this.uuid = uuid;
		this.sessionID = sessionID;
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
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), sessionID);
		
		try {
			
			String url = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_CREDIT_LINE_INCREASE;
			//Log.i("COOP", ">>>>>>>>>>>>>>>>>>"+url);
			String response = makeRequest(url, getRequestJSONString(), POST);
			//Utils.writeToTextFile(response, ApplicationEx.applicationEx, "dumpData.tmp");
			//Log.d("", "RESPONSE::::"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} else if(!TextUtils.isEmpty(response)) {
				
				ArrayList<String> arrayList = new ArrayList<String>();
				
				if (creditLineIncreaseListener != null) {
					creditLineIncreaseListener.onCreditLineIncreaseFinished(arrayList);
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
		 * request.body={"creditLineIncrease":{"mortgage":3,"amountApplied":
		 * 20000
		 * ,"employmentType":"Self-employed","yearlyIncome":44444,"otherLoans"
		 * :2}}
		 */
		JSONObject creditIncreaseJsonObject = new JSONObject();
		try {

			JSONObject innerJsonObject = new JSONObject();

			innerJsonObject.put("mortgage", mortgage);
			innerJsonObject.put("amountApplied", amountApplied);
			innerJsonObject.put("employmentType", employmentType);
			innerJsonObject.put("yearlyIncome", yearlyIncome);
			innerJsonObject.put("otherLoans", otherLoans);
			
			creditIncreaseJsonObject.put("creditLineIncrease", innerJsonObject.toString());
			
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
		return creditIncreaseJsonObject.toString();
	}
}
