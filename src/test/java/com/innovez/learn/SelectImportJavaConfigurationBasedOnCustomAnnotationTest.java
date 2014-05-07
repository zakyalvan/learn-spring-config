package com.innovez.learn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.innovez.learn.SelectImportJavaConfigurationBasedOnCustomAnnotationTest.MainConfiguration;

/**
 * <p>Show how to load spring configuration class base on custom annotation config attributes.
 * The main contract for this feature is spring's {@link ImportSelector} interface. 
 * Please read the docs to get more informations.
 * 
 * <p>Actually we simply could import any java configuration in custom annotation config by meta-annotate
 * our custom annotation using {@link Import}.
 * 
 * <p>Honestly, i don't know how to create equivalent functionality using xml schema configuration. 
 * Maybe next time I will exploring on it.
 * 
 * <p>Firstly inspired by <a href="http://blog.fawnanddoug.com/2012/08/how-those-spring-enable-annotations-work.html?m=1"><strong>this blog post</strong></a>
 * 
 * @author zakyalvan
 * @see RegisteringBeanBasedOnCustomAnnotationConfigTest
 */
@ContextConfiguration(classes=MainConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SelectImportJavaConfigurationBasedOnCustomAnnotationTest {
	private Logger logger = Logger.getLogger(SelectImportJavaConfigurationBasedOnCustomAnnotationTest.class);
	
	/**
	 * Main configuration, loaded by default. Just an empty configuration class.
	 * Please change the {@link EnableEnvironment#type()} te load different type configuration.
	 * 
	 * @author zakyalvan
	 */
	@Configuration
	@EnableEnvironment(type=EnvironmentType.PRODUCTION)
	public static class MainConfiguration {}
	
	@Configuration
	public static class DevelomentConfiguration {
		@Bean
		public EnvironmentDescriptor descriptor() {
			return new EnvironmentDescriptor() {
				@Override
				public EnvironmentType getType() {
					return EnvironmentType.DEVELOPMENT;
				}
				
				@Override
				public String getDescription() {
					return "Development environment!";
				}
				
				public String toString() {
					return "Type : " + getType() + ", Description : " + getDescription();
				}
			};
		}
	}
	
	@Configuration
	public static class ProductionConfiguration {
		@Bean
		public EnvironmentDescriptor descriptor() {
			return new EnvironmentDescriptor() {
				@Override
				public EnvironmentType getType() {
					return EnvironmentType.PRODUCTION;
				}
				
				@Override
				public String getDescription() {
					return "Production environment!";
				}
				
				public String toString() {
					return "Type : " + getType() + ", Description : " + getDescription();
				}
			};
		}
	}
	
	@Autowired
	private EnvironmentDescriptor descriptor;
	
	@Test
	public void testDumpLoadedConfiguration() {
		logger.debug("************************************* Loaded environment descriptor : " + descriptor);
	}
	
	/**
	 * Custom annotation config, to select environment configuration.
	 * 
	 * @author zakyalvan
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Import(ImportEnvironmentBeansFamilySelector.class)
	public static @interface EnableEnvironment {
		/**
		 * Select environment type to enable.
		 * 
		 * @return
		 */
		EnvironmentType type();
	}
	
	/**
	 * For simplicity sake, i only use two environment type.
	 * 
	 * @author zakyalvan
	 */
	public static enum EnvironmentType {
		DEVELOPMENT, PRODUCTION
	}
	
	/**
	 * Import selector. Selecting configuration class to import based on {@link EnableEnvironment#type()} attribute.
	 * 
	 * @author zakyalvan
	 */
	public static class ImportEnvironmentBeansFamilySelector implements ImportSelector {
		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableEnvironment.class.getName());
			EnvironmentType environmentType = (EnvironmentType) annotationAttributes.get("type");
			if(environmentType == EnvironmentType.DEVELOPMENT) {
				return new String[] {DevelomentConfiguration.class.getName()};
			}
			else {
				return new String[] {ProductionConfiguration.class.getName()};
			}
		}
	}
	
	public static interface EnvironmentDescriptor {
		EnvironmentType getType();
		String getDescription();
	}
}
