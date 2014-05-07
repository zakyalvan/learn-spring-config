package com.innovez.learn;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Test how to change any registered bean on application context.
 * 
 * @author zakyalvan
 */
public class ProgramaticallyChangeRegisteredBeanTest {
	private static final String DEFAULT_GREETING = "Hello World!";
	private static final String ANOTHER_GREETING = "Hallo Dunia!";
	
	@Configuration
	public static class TestConfiguration {
		@Bean
		public SampleBean sampleBean() {
			return new SampleBean(DEFAULT_GREETING);
		}
	}
	
	private AnnotationConfigApplicationContext applicationContext;
	
	@Before
	public void setUp() {
		applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
	}
	
	@Test
	public void testVerifyDefaultRegisteredSampleBeanGreeting() {
		SampleBean sampleBean = applicationContext.getBean(SampleBean.class);
		Assert.isTrue(sampleBean.getGreeting().equals(DEFAULT_GREETING));
	}
	
	@Test(expected=NoSuchBeanDefinitionException.class)
	public void testRemoveDefaultRegisteredSampleBean() {
		SampleBean sampleBean = applicationContext.getBean(SampleBean.class);
		applicationContext.removeBeanDefinition(sampleBean.getBeanName());
		applicationContext.getBean(SampleBean.class);
	}
	
	@Test
	public void testChangeRegisteredSampleBean() {
		SampleBean sampleBean = applicationContext.getBean(SampleBean.class);
		applicationContext.removeBeanDefinition(sampleBean.getBeanName());
		BeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(SampleBean.class)
					.addPropertyValue("greeting", ANOTHER_GREETING)
					.getBeanDefinition();
		applicationContext.registerBeanDefinition("anotherBeanName", beanDefinition);
		SampleBean newRegistered = applicationContext.getBean(SampleBean.class);
		Assert.isTrue(newRegistered.getGreeting().equals(ANOTHER_GREETING));
	}
	
	public static class SampleBean implements BeanNameAware {
		private String greeting;

		private String beanName;
		
		public SampleBean() {}
		public SampleBean(String greeting) {
			this.greeting = greeting;
		}

		public String getGreeting() {
			return greeting;
		}
		public void setGreeting(String greeting) {
			this.greeting = greeting;
		}
		
		@Override
		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}
		public String getBeanName() {
			return beanName;
		}
	}
}
