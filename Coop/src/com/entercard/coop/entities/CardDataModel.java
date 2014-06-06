package com.entercard.coop.entities;

public class CardDataModel {

	private String cardholderName;
	private String isPrimaryCard;
	private String cardNumberEndingWith;

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

}
