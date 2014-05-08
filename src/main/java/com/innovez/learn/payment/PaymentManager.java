package com.innovez.learn.payment;

public interface PaymentManager {
	public void pay(String orderId, Money money);
}
