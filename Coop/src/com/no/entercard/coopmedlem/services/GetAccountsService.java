package com.no.entercard.coopmedlem.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.no.entercard.coopmedlem.ApplicationEx;
import com.no.entercard.coopmedlem.R;
import com.no.entercard.coopmedlem.entities.AccountsModel;
import com.no.entercard.coopmedlem.entities.CardDataModel;
import com.no.entercard.coopmedlem.entities.TransactionDataModel;
import com.no.entercard.coopmedlem.utils.CompatibilityUtils;

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
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_accept), getHeaderAccept());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_saml), ApplicationEx.getInstance().getSAMLTxt());
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_device_platform), "Android");
		AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_device_version), CompatibilityUtils.getAndroidVersion());
		if(!TextUtils.isEmpty(ApplicationEx.getInstance().getCookie())) {
			AddHeader(ApplicationEx.getInstance().getResources().getString(R.string.http_header_set_cookie), ApplicationEx.getInstance().getCookie());
			Log.i("", ":::Cookie() SENTTTTTTT::::>>>>>>>>>>>"+ApplicationEx.getInstance().getCookie());
		}
		
		try {

			String response = makeRequest(METHOD_ACCOUNTS, null, GET);
			//String response = Utils.readResponseFromAssetsFile(ApplicationEx.getInstance(), "getAccountsResponse.txt");
			//Log.i("", "RESPONSE::::"+response);

			if (response == null) {
				sentFailure(ApplicationEx.getInstance().getString(R.string.no_internet_connection));
			} else if (!TextUtils.isEmpty(response)) {

				ArrayList<AccountsModel> accountsArrayList = new ArrayList<AccountsModel>();
				accountsArrayList = parseJSONResponse(response);
				ApplicationEx.getInstance().setAccountsArrayList(accountsArrayList);

				if (getAccountsListener != null) {
					getAccountsListener.onGetAccountsFinished(accountsArrayList);
				}
			} else {
				sentFailure(ApplicationEx.getInstance().getString(
						R.string.exception_general));
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
		JSONObject responseJSON = new JSONObject(response);
		if (responseJSON.has("errorResponse")) {
			/**07-28 16:13:08.914: I/(3874): RESPONSE::::{"error":{"code":"2003","reason":"An error occurred. Please try again shortly."}}**/
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
		} else if(responseJSON.has("error")) {
			JSONObject errorJson = responseJSON.getJSONObject("error");
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
					Log.i("COOP", "accountsJSONObj.getString(\"accountNumber\")--->>"+accountsJSONObj.getString("accountNumber"));
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
						transactionModel.setIsDisputable(transactionJSONObj.getBoolean("isDisputable"));
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
