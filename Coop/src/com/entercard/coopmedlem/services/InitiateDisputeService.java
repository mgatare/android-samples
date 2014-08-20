package com.entercard.coopmedlem.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;

// TODO: Auto-generated Javadoc
/**
 * The Class InitiateDisputeService.
 */
public class InitiateDisputeService extends BaseService {
	
	/** The method dispute. */
	private final String METHOD_DISPUTE = "dispute";
	
	/** The tag transaction. */
	private final String TAG_TRANSACTION = "transactions";
	
	/** The tag accounts. */
	private final String TAG_ACCOUNTS= "accounts";
	
	/** The uuid txt. */
	private String uuidTxt;
	
	/** The saml txt. */
	private String samlTxt;
	
	/** The cookie txt. */
	private String cookieTxt;
	
	/** The account num. */
	private String accountNum;
	
	/** The transaction id. */
	private String transactionID;
	
	/** The billing amount txt. */
	private float billingAmountTxt;
	
	/** The description txt. */
	private String descriptionTxt;
	
	/** The email. */
	private String email;
	
	/** The known transaction. */
	private boolean knownTransaction; 
	
	/** The mobile. */
	private String mobile;
	
	/** The reason. */
	private String reason;
	
	/** The transaction date. */
	private String transactionDate;
	
	/** The dispute raise listener. */
	private InitiateDisputeListener disputeRaiseListener;

	/**
	 * The listener interface for receiving initiateDispute events.
	 * The class that is interested in processing a initiateDispute
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addInitiateDisputeListener<code> method. When
	 * the initiateDispute event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see InitiateDisputeEvent
	 */
	public interface InitiateDisputeListener {
		
		/**
		 * On initiate dispute finished.
		 *
		 * @param response the response
		 */
		void onInitiateDisputeFinished(String response);
		
		/**
		 * On initiate dispute failed.
		 *
		 * @param error the error
		 */
		void onInitiateDisputeFailed(String error);
	}

	/**
	 * Instantiates a new initiate dispute service.
	 *
	 * @param uuid the uuid
	 * @param saml the saml
	 * @param cookie the cookie
	 * @param accountNum the account num
	 * @param transactionID the transaction id
	 * @param billingAmount the billing amount
	 * @param description the description
	 * @param email the email
	 * @param knownTransaction the known transaction
	 * @param mobile the mobile
	 * @param reason the reason
	 * @param transactionDate the transaction date
	 */
	public InitiateDisputeService(String uuid, String saml, String cookie, 
			String accountNum, String transactionID, String billingAmount, String description, String email, boolean knownTransaction, 
			String mobile, String reason, String transactionDate) {
		this.uuidTxt = uuid;
		this.samlTxt = saml;
		this.cookieTxt = cookie;
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
	
	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.BaseService#sentFailure(java.lang.String)
	 */
	@Override
	void sentFailure(String codeTxt) {
		if (disputeRaiseListener != null) {
			disputeRaiseListener.onInitiateDisputeFailed(codeTxt);
		}
	}
	
	/**
	 * Sets the initiate dispute listener.
	 *
	 * @param listener the new initiate dispute listener
	 */
	public void setInitiateDisputeListener(InitiateDisputeListener listener) {
		this.disputeRaiseListener = listener;
	}

	/* (non-Javadoc)
	 * @see com.entercard.coopmedlem.services.BaseService#executeRequest()
	 */
	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), samlTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_uuid), uuidTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_jsessionid), cookieTxt);
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_set_cookie), cookieTxt);
		
		try {
			String strURL = TAG_ACCOUNTS + "/" + accountNum + "/" + TAG_TRANSACTION + "/" + transactionID + "/" + METHOD_DISPUTE;
			String response = makeRequest(strURL, getRequestJSONString(), POST);
			//Log.d("", ">>RESPONSE>>"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} else if(statusCode == 204) {
				if (disputeRaiseListener != null) {
					disputeRaiseListener.onInitiateDisputeFinished("");
				}
			} else if(!TextUtils.isEmpty(response)) {
				
				String parsedResponse = parseResponseJSON(response);
				
				if (disputeRaiseListener != null) {
					disputeRaiseListener.onInitiateDisputeFailed(parsedResponse);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(R.string.exception_general));
			}
		} catch (Exception e) {
			sentFailure(getExceptionType(e));
		}
	}
	
	/**
	 * Gets the request json string.
	 *
	 * @return the request json string
	 */
	private String getRequestJSONString() {
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
		return requestJSON.toString();
	}
	
	/**
	 * Parses the response json.
	 *
	 * @param response the response
	 * @return the string
	 * @throws Exception the exception
	 * @throws JSONException the JSON exception
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
