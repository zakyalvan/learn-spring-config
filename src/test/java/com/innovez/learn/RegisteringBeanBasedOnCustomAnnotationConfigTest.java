package com.innovez.learn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * <p>Showing mechanism how to selectively registering bean to application context.
 * Selection based on (custom) annotation on loaded context {@link Configuration} class.
 * The main contract for this purpose is {@link ImportBeanDefinitionRegistrar} interface, please look at
 * our simple implementation {@link StrategyBeanDefinitionRegistrar} and appropriate annotation
 * which explicitly {@link Import} our registrar implementation, {@link EnableStrategy}.
 * 
 * <p>Honestly, i don't know how to create equivalent functionality using xml schema configuration. 
 * Maybe next time I will exploring it.
 * 
 * <p>Firstly inspired by <a href="http://blog.fawnanddoug.com/2012/08/how-those-spring-enable-annotations-work.html?m=1"><strong>this blog post</strong></a>
 * 
 * @author zakyalvan
 */
public class RegisteringBeanBasedOnCustomAnnotationConfigTest {
	/**
	 * Just an empty configuration.
	 * 
	 * @author zakyalvan
	 */
	@Configuration
	@EnableStrategy(strategyType=FirstStrategyBean.class)
	public static class FirstConfiguration {}
	
	/**
	 * Same as before, just an empty configuration.
	 * 
	 * @author zakyalvan
	 */
	@Configuration
	@EnableStrategy(strategyType=SecondStrategyBean.class)
	public static class SecondConfiguration {}
	
	/**
	 * Please look at {@link EnableStrategy} annotation attribute on {@link FirstConfiguration}
	 */
	@Test
	public void testAppContextLoadUsingFirstConfigurationType() {
		AnnotationConfigApplicationContext applicationContext = 
				new AnnotationConfigApplicationContext(FirstConfiguration.class);
		StrategyBean strategyBean = applicationContext.getBean(StrategyBean.class);
		Assert.isInstanceOf(FirstStrategyBean.class, strategyBean);
		applicationContext.close();
	}
	
	/**
	 * Please look at {@link EnableStrategy} annotation attribute on {@link SecondConfiguration}
	 */
	@Test
	public void testAppContextLoadUsingLoadSecondConfigurationType() {
		AnnotationConfigApplicationContext applicationContext = 
				new AnnotationConfigApplicationContext(SecondConfiguration.class);
		StrategyBean strategyBean = applicationContext.getBean(StrategyBean.class);
		Assert.isInstanceOf(SecondStrategyBean.class, strategyBean);
		applicationContext.close();
	}
	
	/**
	 * Sample of annotation configuration.
	 * Just to show how to import bean based on annotation attribute.
	 * This annotation must be attached to {@link Configuration} class.
	 * 
	 * @author zakyalvan
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@Import(StrategyBeanDefinitionRegistrar.class)
	public static @interface EnableStrategy {
		Class<? extends StrategyBean> strategyType();
	}
	
	/**
	 * Registrar of strategy bean. This object select which bean will be registered based on {@link EnableStrategy#strategyType()}.
	 * 
	 * @author zakyalvan
	 */
	public static class StrategyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
		/**
		 * You could customize registration process. Please refer to {@link AnnotationMetadata} documentations to get more information from annotations.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableStrategy.class.getName());
			Class<? extends StrategyBean> strategyType = (Class<? extends StrategyBean>) annotationAttributes.get("strategyType");
			
			BeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(strategyType).getBeanDefinition();
			registry.registerBeanDefinition("strategyBean", beanDefinition);
		}
	}
	
	/**
	 * Contract for strategy object.
	 * 
	 * @author zakyalvan
	 */
	public static interface StrategyBean {}
	
	/**
	 * First strategy type.
	 * 
	 * @author zakyalvan
	 */
	public static class FirstStrategyBean implements StrategyBean {}
	
	/**
	 * Second strategy type.
	 * 
	 * @author zakyalvan
	 */
	public static class SecondStrategyBean implements StrategyBean {}
}
