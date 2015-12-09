package com.github.dtrunk90.thymeleaf.jawr.dialect.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jawr.web.JawrConstant;
import net.jawr.web.servlet.JawrSpringController;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.testing.templateengine.context.web.SpringWebProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class)
public class JawrDialectTest {
	@Configuration
	public static class ContextConfiguration {
		@Bean
		public HandlerMapping handlerMapping() {
			SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
			Map<String, Object> urlMap = new HashMap<String, Object>();
			urlMap.put("/res/**/*.js", jawrJsController());
			urlMap.put("/res/**/*.css", jawrCssController());
			urlMap.put("/res/bin/**", jawrBinaryController());
			mapping.setUrlMap(urlMap);
			return mapping;
		}

		private JawrSpringController jawrController(String type) {
			JawrSpringController controller = new JawrSpringController();
			controller.setConfigLocation("jawr.properties");
			controller.setControllerMapping(JawrConstant.BINARY_TYPE.equals(type) ? "/res/bin" : "/res");
			controller.setType(type);
			return controller;
		}

		@Bean
		@DependsOn("jawrBinaryController")
		public JawrSpringController jawrCssController() {
			return jawrController(JawrConstant.CSS_TYPE);
		}

		@Bean
		public JawrSpringController jawrBinaryController() {
			return jawrController(JawrConstant.BINARY_TYPE);
		}

		@Bean
		public JawrSpringController jawrJsController() {
			return jawrController(JawrConstant.JS_TYPE);
		}

		@Bean
		public TestExecutor testExecutor() {
			List<IDialect> dialects = new ArrayList<IDialect>();
			dialects.add(new SpringStandardDialect());
			dialects.add(new JawrDialect());

			SpringWebProcessingContextBuilder springPCBuilder = new SpringWebProcessingContextBuilder();
			springPCBuilder.setApplicationContextConfigLocation(null);

			TestExecutor executor = new TestExecutor();
			executor.setProcessingContextBuilder(springPCBuilder);
			executor.setDialects(dialects);

			return executor;
		}
	}

	@Autowired
	private TestExecutor executor;

	//@Test
	public void testBinary() {
		executor.execute("classpath:img.thtest");
		Assert.assertTrue(executor.isAllOK());
		executor.reset();

		executor.execute("classpath:input.thtest");
		Assert.assertTrue(executor.isAllOK());
		executor.reset();
	}

	@Test
	public void testCss() {
		executor.execute("classpath:css.thtest");
		Assert.assertTrue(executor.isAllOK());
		executor.reset();
	}

	//@Test
	public void testJs() {
		executor.execute("classpath:js.thtest");
		Assert.assertTrue(executor.isAllOK());
		executor.reset();
	}
}