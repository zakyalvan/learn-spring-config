package com.innovez.learn;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * How to configure bean definition on context load time.
 * The key type in configuring bean definition are {@link BeanFactoryPostProcessor} and {@link BeanDefinition}.
 * 
 * @author zakyalvan
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgramaticallyConfiguringBeanDefinitionTest {
	private static final Logger LOGGER = Logger.getLogger(ProgramaticallyConfiguringBeanDefinitionTest.class);
	
	private static final String SAMPLE_BEAN_NAME = "sampleBean";
	
	private static final String DEFAULT_GREETING = "Hello World!";
	private static final String ANOTHER_GREETING = "Hallo Dunia!";
	
	/**
	 * Configuration of our test. Placed here for simplicity reason.
	 * 
	 * @author zakyalvan
	 */
	@Configuration
	public static class Config {
		/**
		 * Just a sample bean.
		 * 
		 * @return
		 */
		@Bean(name=SAMPLE_BEAN_NAME)
		public SampleBean beanSample() {
			return new SampleBean(DEFAULT_GREETING);
		}
		
		/**
		 * <p>{@link BeanFactoryPostProcessor} which responsible for changing sample bean greeting value.
		 * This is simplified example. In real application you could change whatever properties, regardless of type.
		 * 
		 * <p><strong>NOTE</strong> Read {@link Bean} java-docs to know, why this method marked with static modifier.
		 * 
		 * @return
		 */
		@Bean
		public static BeanFactoryPostProcessor sampleBeanConfigurer() {
			return new BeanFactoryPostProcessor() {
				@Override
				public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
					/**
					 * Actually, we could add additional logic before configuring bean definition.
					 * Here we simply change the greeting property of sampleBean.
					 */
					BeanDefinition beanDefinition = beanFactory.getBeanDefinition(SAMPLE_BEAN_NAME);
					beanDefinition.getPropertyValues().add("greeting", ANOTHER_GREETING);
				}
			};
		}
	}
	
	@Autowired
	private SampleBean sampleBean;
	
	/**
	 * Simple check.
	 */
	@Test
	public void testVerifyChangedSampleBeanGreeting() {
		Assert.isTrue(sampleBean.getGreeting().equals(ANOTHER_GREETING) && !sampleBean.getGreeting().equals(DEFAULT_GREETING));
		LOGGER.debug("******************************** Sample bean greeting : " + sampleBean.getGreeting());
	}
	
	/**
	 * Just a simple bean class, used for demonstrating how to change bean property 
	 * before bean initialization.
	 * 
	 * @author zakyalvan
	 */
	public static class SampleBean {
		private String greeting;

		public SampleBean(String greeting) {
			this.greeting = greeting;
		}
		
		public String getGreeting() {
			return greeting;
		}
		public void setGreeting(String greeting) {
			this.greeting = greeting;
		}
	}
}
