package com.entercard.coopmedlem.entities;

import java.util.ArrayList;

public class AccountsModel {

	private String accountNumber;
	private String creditLimit;
	private String openToBuy;
	private String product;
	private String productName;
	private String spent;
	private String transactionsCount;

	private String cardholderName;
	private String isPrimaryCard;
	private String cardNumberEndingWith;

	private String billingAmount;
	private String billingCurrency;
	private String description;
	private String id;
	private String amount;
	private String currency;
	private String date;
	private String type;
	private String isDisputable;

	private ArrayList<TransactionDataModel> transactionDataArraylist;
	private ArrayList<CardDataModel> cardDataArrayList;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getOpenToBuy() {
		return openToBuy;
	}

	public void setOpenToBuy(String openToBuy) {
		this.openToBuy = openToBuy;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSpent() {
		return spent;
	}

	public void setSpent(String spent) {
		this.spent = spent;
	}

	public String getTransactionsCount() {
		return transactionsCount;
	}

	public void setTransactionsCount(String transactionsCount) {
		this.transactionsCount = transactionsCount;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getIsPrimaryCard() {
		return isPrimaryCard;
	}

	public void setIsPrimaryCard(String isPrimaryCard) {
		this.isPrimaryCard = isPrimaryCard;
	}

	public String getCardNumberEndingWith() {
		return cardNumberEndingWith;
	}

	public void setCardNumberEndingWith(String cardNumberEndingWith) {
		this.cardNumberEndingWith = cardNumberEndingWith;
	}

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

	public String getIsDisputable() {
		return isDisputable;
	}

	public void setIsDisputable(String isDisputable) {
		this.isDisputable = isDisputable;
	}

	public ArrayList<TransactionDataModel> getTransactionDataArraylist() {
		return transactionDataArraylist;
	}

	public void setTransactionDataArraylist(
			ArrayList<TransactionDataModel> transactionDataArraylist) {
		this.transactionDataArraylist = transactionDataArraylist;
	}

	public ArrayList<CardDataModel> getCardDataArrayList() {
		return cardDataArrayList;
	}

	public void setCardDataArrayList(ArrayList<CardDataModel> cardDataArrayList) {
		this.cardDataArrayList = cardDataArrayList;
	}

}
