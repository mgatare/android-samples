package com.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.AccountsModel;
import com.entercard.coopmedlem.entities.CardDataModel;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.CompatibilityUtils;

public class GetAccountsService extends BaseService {

	private static final String METHOD_ACCOUNTS = "accounts";
	
	private GetAccountsListener getAccountsListener;

	public interface GetAccountsListener {
		void onGetAccountsFinished(ArrayList<AccountsModel> accountArrayList);
		void onGetAccountsFailed(String error);
	}

	public GetAccountsService() {
	}
	
	@Override
	void sentFailure(String codeTxt) {
		if (getAccountsListener != null) {
			getAccountsListener.onGetAccountsFailed(codeTxt);
		}
	}
	
	public void setAccountsListener(GetAccountsListener serviceListener) {
		this.getAccountsListener = serviceListener;
	}

	@Override
	void executeRequest() {
		//Add headers to HTTP Request
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_saml), ApplicationEx.applicationEx.getSAMLTxt());
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_device_platform), "Android");
		AddHeader(ApplicationEx.applicationEx.getResources().getString(R.string.http_header_device_version), CompatibilityUtils.getAndroidVersion());
		
		try {
			
			String response = makeRequest(METHOD_ACCOUNTS, null, GET);
			//String response = "{\"errorResponse\":{\"status\":\"NOK\",\"code\":\"4004\",\"reason\":\"Not Found\"}}";
			//String response = Utils.readResponseFromAssetsFile(ApplicationEx.applicationEx, "getAccountsResponse.txt");
			Log.i("", "RESPONSE::::"+response);
			
			if(response == null) {
				sentFailure(ApplicationEx.applicationEx.getString(R.string.no_internet_connection));
			}else if(!TextUtils.isEmpty(response)) {
				
				ArrayList<AccountsModel> accountsArrayList = new ArrayList<AccountsModel>();
				accountsArrayList = parseJSONResponse(response);
				
				if (getAccountsListener != null) {
					getAccountsListener.onGetAccountsFinished(accountsArrayList);
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
	private ArrayList<AccountsModel> parseJSONResponse(String response)
			throws Exception, JSONException {
		
		ArrayList<AccountsModel> arrayList = new ArrayList<AccountsModel>();
		/**
		 * { "error": { "code": String, "reason": String } }
		 * ::{"errorResponse":{"status":"NOK","code":"4004","reason":"Not Found"}}
		 **/
		JSONObject responseJSON = new JSONObject(response);
		if (responseJSON.has("errorResponse")) {
			
			JSONObject errorJson = responseJSON.getJSONObject("errorResponse");
			//String code = null;
			String reason = null;
			
			/*if (errorJson.has("code")) {
				code = errorJson.getString("code");
				throw new Exception(code);
			}*/
			
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
				}
				accountsModel.setCardDataArrayList(cardList);
				
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
						transactionModel.setDate(transactionJSONObj.getString("date"));
					}
					if (transactionJSONObj.has("type")) {
						transactionModel.setType(transactionJSONObj.getString("type"));
					}
					if (transactionJSONObj.has("isDisputable")) {
						transactionModel.setIsDisputable(transactionJSONObj.getString("isDisputable"));
					}
					transactionList.add(transactionModel);
				}
				accountsModel.setTransactionDataArraylist(transactionList);
				arrayList.add(accountsModel);
				//Log.i("", ":::::::::::transactionList ARRAYLIST SIZE:::::::"+transactionList.size());
			}
		}
		//Log.i("", ":::::::::::SIZE OF ACCOUNTS ARRAY::::::::"+arrayList.size());
		return arrayList;
	}
}
