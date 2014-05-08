package com.innovez.learn.payment;

/**
 * Contract for payment strategy.
 * 
 * @author zakyalvan
 */
public interface PaymentStrategy {
	void pay(Money money);
}
