package com.entercard.coop.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.entercard.coop.ApplicationEx;
import com.entercard.coop.R;
import com.entercard.coop.entities.AccountsModel;
import com.entercard.coop.entities.CardDataModel;
import com.entercard.coop.entities.TransactionDataModel;
import com.entercard.coop.utils.CompatibilityUtils;

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
			//String response = Utils.readResponseFromAssetsFile(ApplicationEx.applicationEx, "getAccountsResponse.txt");
			//Log.i("", "RESPONSE::::"+response);
			
			//sentFailure(ApplicationEx.applicationEx.getString(R.string.exception_network_not_found));
			ArrayList<AccountsModel> accountsArrayList = new ArrayList<AccountsModel>();
			accountsArrayList = parseJSONResponse(response);

			if (getAccountsListener != null) {
				getAccountsListener.onGetAccountsFinished(accountsArrayList);
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

		// Parse and get the Accounts related all data
		if (responseJSON.has("accounts")) {
			JSONArray accountsJsonArray = responseJSON.getJSONArray("accounts");
			
			for (int i = 0; i < accountsJsonArray.length(); i++) {
				
				AccountsModel accountsModel = new AccountsModel();
				JSONObject accountsJSONObj = accountsJsonArray.getJSONObject(i);
				
				if (accountsJSONObj.has("accountNumber")) {
					accountsModel.setAccountNumber(accountsJSONObj.getString("accountNumber"));
				}
				if (accountsJSONObj.has("creditLimit")) {
					accountsModel.setCreditLimit(accountsJSONObj.getString("creditLimit"));
				}
				if (accountsJSONObj.has("openToBuy")) {
					accountsModel.setOpenToBuy(accountsJSONObj.getString("openToBuy"));
				}
				if (accountsJSONObj.has("product")) {
					accountsModel.setProduct(accountsJSONObj.getString("product"));
				}
				if (accountsJSONObj.has("productName")) {
					accountsModel.setProductName(accountsJSONObj.getString("productName"));
				}
				if (accountsJSONObj.has("spent")) {
					accountsModel.setSpent(accountsJSONObj.getString("spent"));
				}
				if (accountsJSONObj.has("transactionsCount")) {
					accountsModel.setTransactionsCount(accountsJSONObj.getString("transactionsCount"));
				}
				
				//Parse and Add all the cards related array
				JSONArray cardsJSONArray = accountsJSONObj.getJSONArray("cards");
				ArrayList<CardDataModel> cardList = new ArrayList<CardDataModel>();
				
				for (int j = 0; j < cardsJSONArray.length(); j++) {
					
					CardDataModel cardModel = new CardDataModel();
					JSONObject carrdJSONObj = cardsJSONArray.getJSONObject(j);
					
					if (carrdJSONObj.has("cardholderName")) {
						cardModel.setCardholderName(carrdJSONObj.getString("cardholderName"));
					}
					if (carrdJSONObj.has("isPrimaryCard")) {
						cardModel.setIsPrimaryCard(carrdJSONObj.getString("isPrimaryCard"));
					}
					if (carrdJSONObj.has("cardNumberEndingWith")) {
						cardModel.setCardNumberEndingWith(carrdJSONObj.getString("cardNumberEndingWith"));
					}
					cardList.add(cardModel);
					accountsModel.setCardDataArrayList(cardList);
				}
				
				//Parse and add the Transactions array
				JSONArray transactionJSONArray = accountsJSONObj.getJSONArray("transactions");
				ArrayList<TransactionDataModel> transactionList = new ArrayList<TransactionDataModel>();
				
				for (int k = 0; k < transactionJSONArray.length(); k++) {
					
					TransactionDataModel transactionModel = new TransactionDataModel();
					JSONObject transactionJSONObj = transactionJSONArray.getJSONObject(k);
					
					if (transactionJSONObj.has("billingAmount")) {
						transactionModel.setBillingAmount(transactionJSONObj.getString("billingAmount"));
					}
					if (transactionJSONObj.has("billingCurrency")) {
						transactionModel .setBillingCurrency(transactionJSONObj.getString("billingCurrency"));
					}
					if (transactionJSONObj.has("description")) {
						transactionModel.setDescription(transactionJSONObj.getString("description"));
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
						transactionModel.setDate(transactionJSONObj.getString("date"));
					}
					if (transactionJSONObj.has("type")) {
						transactionModel.setType(transactionJSONObj.getString("type"));
					}
					if (transactionJSONObj.has("isDisputable")) {
						transactionModel.setIsDisputable(transactionJSONObj.getString("isDisputable"));
					}
					transactionList.add(transactionModel);
					accountsModel.setTransactionDataArraylist(transactionList);
				}
				
				arrayList.add(accountsModel);
				//Log.i("", ":::::::::::ADDED TO ACCOUNTS ARRAYLIST:::::::"+arrayList.size());
			}
		}
		//Log.i("", ":::::::::::SIZE OF ACCOUNTS ARRAY::::::::"+arrayList.size());
		return arrayList;
		
	}
}
