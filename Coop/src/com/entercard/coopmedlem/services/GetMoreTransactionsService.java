package com.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.Utils;

public class GetMoreTransactionsService extends BaseService {

	//http://127.0.0.1:9393/accounts/5299369000100666/transactions?page=3&perPage=50
	
	private final String TAG_TRANSACTIONS= "/transactions";
	private final String TAG_ACCOUNTS= "accounts";
	private String TAG_PER_PAGE = "&perPage=50";
	private String TAG_PAGE = "?page=";
	private GetMoreTransaction getMoreTransactions;
	
	private String strAccountID;
	private String strUUID;
	private String sessionID;
	private int perNumber;
	private int pageNumber;

	public interface GetMoreTransaction {
		void onGetMoreTransactionFinished(ArrayList<TransactionDataModel> accountArrayList);
		void onGetMoreTransactionFailed(String error);
	}

	public GetMoreTransactionsService(String uuid, String sessionID, String accountID, int pageNumber) {
		this.strUUID = uuid;
		this.sessionID = sessionID;
		this.strAccountID = accountID;
		this.pageNumber = pageNumber;
	}
	
	@Override
	void sentFailure(String codeTxt) {
		if (getMoreTransactions != null) {
			getMoreTransactions.onGetMoreTransactionFailed(codeTxt);
		}
	}
	
	public void setTransactionListener(GetMoreTransaction serviceistener) {
		this.getMoreTransactions = serviceistener;
	}

	@Override
	void executeRequest() {
		
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_uuid), strUUID);
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_jsessionid), sessionID);
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_set_cookie), sessionID);//Cookie == JSESSIONID
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		
		try {
			
			String url = TAG_ACCOUNTS + "/" + strAccountID + "/" + TAG_TRANSACTIONS + TAG_PAGE + pageNumber + TAG_PER_PAGE;
			//Log.i("COOP", ">>>>>>>>>>>>>>>>>>"+url);
			String response = makeRequest(url, null, GET);
			Utils.writeToTextFile(response, ApplicationEx.applicationEx, "dumpData.tmp");
			//Log.i("", "RESPONSE::::"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.applicationEx.getString(R.string.no_internet_connection));
			}else if(!TextUtils.isEmpty(response)) {
				
				ArrayList<TransactionDataModel> transactionArrayList = new ArrayList<TransactionDataModel>();
				transactionArrayList = parseJSONResponse(response);
				
				if (getMoreTransactions != null) {
					getMoreTransactions.onGetMoreTransactionFinished(transactionArrayList);
				}
			} else {
				sentFailure(ApplicationEx.applicationEx.getString(R.string.exception_general));
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
	private ArrayList<TransactionDataModel> parseJSONResponse(String response)throws Exception, JSONException {

		JSONObject responseJSON = new JSONObject(response);
		
		if (responseJSON.has("error")) {
			JSONObject errorJson = responseJSON.getJSONObject("error");
			String reason = null;
			if (errorJson.has("reason")) {
				reason = errorJson.getString("reason");
				throw new Exception(reason);
			}
		}

		/**,"pagination": {
        "page": Number, //Page number
        "perPage": Number, //Transactions per page
        "total": Number //Total number of transactions
    	}*/
		
		int page = 0;
		int perPage = 0;
		int total = 0;
		
		if (responseJSON.has("pagination")) {
			
			JSONObject paginationJSON = responseJSON.getJSONObject("pagination");
			
			if (paginationJSON.has("page")) {
				page = paginationJSON.getInt("page");
			}
			if (paginationJSON.has("perPage")) {
				perPage = paginationJSON.getInt("perPage");
			}
			if (paginationJSON.has("total")) {
				total = paginationJSON.getInt("total");
			}
			Log.i("COOP", "PAGE::"+paginationJSON.getInt("page")+"::PERPAGE::"+ paginationJSON.getInt("perPage")+"::TOTAL::"+paginationJSON.getInt("total"));
		}
		
		ArrayList<TransactionDataModel> arrayListTransaction = new ArrayList<TransactionDataModel>();
		// Parse and add the Transactions array
		if (responseJSON.has("transactions")) {
			JSONArray transactionJSONArray = responseJSON.getJSONArray("transactions");

			for (int k = 0; k < transactionJSONArray.length(); k++) {

				TransactionDataModel transactionModel = new TransactionDataModel();
				JSONObject transactionJSONObj = transactionJSONArray.getJSONObject(k);

				if (transactionJSONObj.has("billingAmount")) {
					transactionModel.setBillingAmount(transactionJSONObj.getString("billingAmount"));
				}
				if (transactionJSONObj.has("billingCurrency")) {
					transactionModel.setBillingCurrency(transactionJSONObj.getString("billingCurrency"));
				}
				if (transactionJSONObj.has("description")) {
					transactionModel.setDescription(transactionJSONObj.getString("description"));
				}
				if (transactionJSONObj.has("city")) {
					transactionModel.setCity(transactionJSONObj.getString("city"));
				}
				if (transactionJSONObj.has("country")) {
					transactionModel.setCountry(transactionJSONObj.getString("country"));
				}
				if (transactionJSONObj.has("id")) {
					transactionModel.setId(transactionJSONObj.getString("id"));
				}
				if (transactionJSONObj.has("amount")) {
					transactionModel.setAmount(transactionJSONObj.getString("amount"));
				}
				if (transactionJSONObj.has("currency")) {
					transactionModel.setCurrency(transactionJSONObj.getString("currency"));
				}
				if (transactionJSONObj.has("date")) {
					transactionModel.setDate(transactionJSONObj
							.getString("date"));
				}
				if (transactionJSONObj.has("type")) {
					transactionModel.setType(transactionJSONObj.getString("type"));
				}
				if (transactionJSONObj.has("isDisputable")) {
					transactionModel.setIsDisputable(transactionJSONObj.getString("isDisputable"));
				}
				
				transactionModel.setPage(page);
				transactionModel.setPerPage(perPage);
				transactionModel.setTotal(total);
				
				arrayListTransaction.add(transactionModel);
			}
		}
		//Log.i("", ":::::::::::ADDED TO ACCOUNTS ARRAYLIST:::::::"+ arrayListTransaction.size());
		return arrayListTransaction;
	}
}
