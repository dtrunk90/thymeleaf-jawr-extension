package com.github.dtrunk90.thymeleaf.jawr.dialect.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.testing.templateengine.context.IProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;
import com.github.dtrunk90.thymeleaf.jawr.dialect.test.context.JawrDialectProcessingContextBuilder;
import com.github.dtrunk90.thymeleaf.jawr.dialect.test.resolver.JawrDialectTestableResolver;

import net.jawr.web.JawrConstant;
import net.jawr.web.servlet.JawrSpringController;

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

	private static final TestExecutor executor = new TestExecutor();

	@BeforeClass
	public static void configureSequence() {
		executor.setTestableResolver(new JawrDialectTestableResolver());
		executor.setDialects(Arrays.asList(new SpringStandardDialect(), new JawrDialect()));
	}

	@Before
	public void configureTest() {
		IProcessingContextBuilder processingContextBuilder = new JawrDialectProcessingContextBuilder(applicationContext, servletContext, request, response, webRequest);
		executor.setProcessingContextBuilder(processingContextBuilder);

		executor.reset();
	}

	@Test
	public void testBinary() {
		executor.execute("classpath:thtest/binary");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testCss() {
		executor.execute("classpath:thtest/css");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testJs() {
		executor.execute("classpath:thtest/js");
		Assert.assertTrue(executor.isAllOK());
	}
}