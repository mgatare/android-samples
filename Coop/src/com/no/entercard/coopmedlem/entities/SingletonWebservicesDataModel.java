package com.no.entercard.coopmedlem.entities;

import java.math.BigInteger;

public class SingletonWebservicesDataModel {

	private String increaseInCreditLimit;
	// private String adult;
	// private String child;
	// private String occupancy;
	private BigInteger yearlyIncome;
	private BigInteger mortgage;
	private BigInteger otherLoans;
	private BigInteger amountApplied;
	private String employment;

	private boolean isKnownTransaction;
	private String disputeReason;
	private String disputeEmail;
	private String disputePhone;
	private String disputebillingAmount;
	private String disputedescription;
	private String disputetransactionDate;
	private String disputetransactionId;

	private String benificiaryName;
	private String fundsAccNumer;
	private BigInteger fundsAmount;
	private String fundsMessage;

	// private String amount;

	public String getIncreaseInCreditLimit() {
		return increaseInCreditLimit;
	}

	public void setIncreaseInCreditLimit(String increaseInCreditLimit) {
		this.increaseInCreditLimit = increaseInCreditLimit;
	}

	public String getEmployment() {
		return employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public boolean isKnownTransaction() {
		return isKnownTransaction;
	}

	public void setKnownTransaction(boolean isKnownTransaction) {
		this.isKnownTransaction = isKnownTransaction;
	}

	public String getDisputeReason() {
		return disputeReason;
	}

	public void setDisputeReason(String disputeReason) {
		this.disputeReason = disputeReason;
	}

	public String getDisputeEmail() {
		return disputeEmail;
	}

	public void setDisputeEmail(String disputeEmail) {
		this.disputeEmail = disputeEmail;
	}

	public String getDisputePhone() {
		return disputePhone;
	}

	public void setDisputePhone(String disputePhone) {
		this.disputePhone = disputePhone;
	}

	public String getDisputebillingAmount() {
		return disputebillingAmount;
	}

	public void setDisputebillingAmount(String disputebillingAmount) {
		this.disputebillingAmount = disputebillingAmount;
	}

	public String getDisputedescription() {
		return disputedescription;
	}

	public void setDisputedescription(String disputedescription) {
		this.disputedescription = disputedescription;
	}

	public String getDisputetransactionDate() {
		return disputetransactionDate;
	}

	public void setDisputetransactionDate(String disputetransactionDate) {
		this.disputetransactionDate = disputetransactionDate;
	}

	public String getDisputetransactionId() {
		return disputetransactionId;
	}

	public void setDisputetransactionId(String disputetransactionId) {
		this.disputetransactionId = disputetransactionId;
	}

	public String getBenificiaryName() {
		return benificiaryName;
	}

	public void setBenificiaryName(String benificiaryName) {
		this.benificiaryName = benificiaryName;
	}

	public String getFundsAccNumer() {
		return fundsAccNumer;
	}

	public void setFundsAccNumer(String fundsAccNumer) {
		this.fundsAccNumer = fundsAccNumer;
	}

	public BigInteger getFundsAmount() {
		return fundsAmount;
	}

	public void setFundsAmount(BigInteger fundsAmount) {
		this.fundsAmount = fundsAmount;
	}

	public String getFundsMessage() {
		return fundsMessage;
	}

	public void setFundsMessage(String fundsMessage) {
		this.fundsMessage = fundsMessage;
	}

	public BigInteger getYearlyIncome() {
		return yearlyIncome;
	}

	public void setYearlyIncome(BigInteger yearlyIncome) {
		this.yearlyIncome = yearlyIncome;
	}

	public BigInteger getMortgage() {
		return mortgage;
	}

	public void setMortgage(BigInteger mortgageTxt) {
		this.mortgage = mortgageTxt;
	}

	public BigInteger getOtherLoans() {
		return otherLoans;
	}

	public void setOtherLoans(BigInteger otherLoans) {
		this.otherLoans = otherLoans;
	}

	public BigInteger getAmountApplied() {
		return amountApplied;
	}

	public void setAmountApplied(BigInteger amountApplied) {
		this.amountApplied = amountApplied;
	}

	// public String getAmount() {
	// return amount;
	// }
	//
	// public void setAmount(String amount) {
	// this.amount = amount;
	// }
}