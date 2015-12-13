package com.github.dtrunk90.thymeleaf.jawr.dialect.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.jawr.web.JawrConstant;
import net.jawr.web.servlet.JawrSpringController;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.testing.templateengine.context.IProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;
import org.thymeleaf.testing.templateengine.resolver.ITestableResolver;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;
import com.github.dtrunk90.thymeleaf.jawr.dialect.test.context.JawrDialectProcessingContextBuilder;
import com.github.dtrunk90.thymeleaf.jawr.dialect.test.resolver.JawrDialectTestableResolver;

@WebAppConfiguration
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
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
			controller.setConfiguration(new Properties());
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
	}

	@Autowired
	private WebApplicationContext applicationContext;

	@Autowired
	private MockServletContext servletContext;

	@Autowired
	private MockHttpServletRequest request;

	@Autowired
	private MockHttpServletResponse response;

	@Autowired
	private ServletWebRequest webRequest;

	private TestExecutor executor;

	@Before
	public void setUp() {
		IProcessingContextBuilder processingContextBuilder = new JawrDialectProcessingContextBuilder(applicationContext, servletContext, request, response, webRequest);

		ITestableResolver testableResolver = new JawrDialectTestableResolver();

		List<IDialect> dialects = new ArrayList<IDialect>();
		dialects.add(new SpringStandardDialect());
		dialects.add(new JawrDialect());

		executor = new TestExecutor();
		executor.setProcessingContextBuilder(processingContextBuilder);
		executor.setTestableResolver(testableResolver);
		executor.setDialects(dialects);
	}

	@Test
	public void runTests() {
		executor.execute("classpath:thtest");
		Assert.assertTrue(executor.isAllOK());
	}
}