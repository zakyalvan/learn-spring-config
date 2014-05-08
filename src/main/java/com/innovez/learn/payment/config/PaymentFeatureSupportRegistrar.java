package com.innovez.learn.payment.config;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.innovez.learn.payment.annotation.EnablePaymentFeature;
import com.innovez.learn.payment.annotation.StrategyType;

public class PaymentFeatureSupportRegistrar implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnablePaymentFeature.class.getName());
		StrategyType strategyType = (StrategyType) annotationAttributes.get("strategy");
		if(strategyType == StrategyType.CASH) {
			
		}
		else {
			
		}
	}
}
