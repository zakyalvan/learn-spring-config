package com.innovez.learn.payment;

import java.io.Serializable;
import java.util.Currency;

@SuppressWarnings("serial")
public class Money implements Serializable {
	private final Currency currency;
	private final Double value;
	
	public Money(Currency currency, Double value) {
		this.currency = currency;
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}
	public Double getValue() {
		return value;
	}
}
