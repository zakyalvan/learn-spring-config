package com.innovez.learn.payment.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.innovez.learn.payment.config.PaymentFeatureSupportRegistrar;

/**
 * Enable payment feature.
 * 
 * @author zakyalvan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(PaymentFeatureSupportRegistrar.class)
public @interface EnablePaymentFeature {
	StrategyType strategy();
}
