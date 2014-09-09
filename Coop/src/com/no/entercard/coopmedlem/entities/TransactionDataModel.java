package com.no.entercard.coopmedlem.entities;

public class TransactionDataModel {

	private String billingAmount;
	private String billingCurrency;
	private String description;
	private String id;
	private String amount;
	private String city;
	private String country;
	private String currency;
	private String date;
	private String type;
	private boolean isDisputable;
	
	private int page;
	private int perPage;
	private int total;
	

	/**
	 * { "billingAmount": 324, "billingCurrency": "NOK", "city": "Stockholm",
	 * "country": "Sweden", "description": "Harrods", "id": "220345165",
	 * "amount": 324, "currency": "NOK", "date": "2013-07-02", "type": "Debit",
	 * "isDisputable": true },
	 **/
	/**,"pagination": {
    "page": Number, //Page number
    "perPage": Number, //Transactions per page
    "total": Number //Total number of transactions
	}*/
	public String getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(String billingAmount) {
		this.billingAmount = billingAmount;
	}

	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getIsDisputable() {
		return isDisputable;
	}

	public void setIsDisputable(boolean isDisputable) {
		this.isDisputable = isDisputable;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
