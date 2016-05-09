package com.github.dtrunk90.thymeleaf.jawr.dialect.test.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jawr.web.JawrConstant;
import net.jawr.web.servlet.JawrSpringController;
import ognl.Ognl;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring4.naming.SpringContextVariableNames;
import org.thymeleaf.testing.templateengine.context.IProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.context.ITestContext;
import org.thymeleaf.testing.templateengine.context.ITestContextExpression;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;
import org.thymeleaf.testing.templateengine.exception.TestEngineExecutionException;
import org.thymeleaf.testing.templateengine.testable.ITest;
import org.thymeleaf.util.Validate;

import com.github.dtrunk90.thymeleaf.jawr.dialect.test.resolver.JawrDialectTestableResolver.JawrTest;

// see https://github.com/thymeleaf/thymeleaf-testing/issues/12
public class JawrDialectProcessingContextBuilder implements IProcessingContextBuilder {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	private static final String REQUEST_PARAMS_PREFIX = "param";
	private static final String REQUEST_ATTRS_PREFIX = "request";
	private static final String SESSION_ATTRS_PREFIX = "session";
	private static final String SERVLETCONTEXT_ATTRS_PREFIX = "application";

	private static final String BUNDLE_RENDERER_CONTEXT_ATTR_PREFIX = "net.jawr.web.resource.renderer.BUNDLE_RENDERER_CONTEXT";

	private final WebApplicationContext applicationContext;
	private final ServletContext servletContext;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ServletWebRequest webRequest;

	public JawrDialectProcessingContextBuilder(WebApplicationContext applicationContext, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ServletWebRequest webRequest) {
		Validate.notNull(applicationContext, "WebApplicationContext cannot be null");
		Validate.notNull(servletContext, "ServletContext cannot be null");
		Validate.notNull(request, "HttpServletRequest cannot be null");
		Validate.notNull(response, "HttpServletResponse cannot be null");
		Validate.notNull(webRequest, "ServletWebRequest cannot be null");

		this.applicationContext = applicationContext;
		this.servletContext = servletContext;
		this.request = request;
		this.response = response;
		this.webRequest = webRequest;
	}

	@Override
	public IContext build(ITest test) {
		if (test == null) {
			return null;
		}

		ITestContext testContext = test.getContext();

		Locale locale = DEFAULT_LOCALE;
		ITestContextExpression localeExpression = testContext.getLocale();
		if (localeExpression != null) {
			Object exprResult = localeExpression.evaluate(Collections.<String, Object> emptyMap(), DEFAULT_LOCALE);
			if (exprResult != null) {
				locale = LocaleUtils.toLocale(exprResult.toString());
			}
		}

		Map<String, Object> variables = new HashMap<String, Object>();

		Map<String, Object[]> requestParameters = new LinkedHashMap<String, Object[]>();
		variables.put(REQUEST_PARAMS_PREFIX, requestParameters);

		Map<String, Object> requestAttributes = new LinkedHashMap<String, Object>();
		variables.put(REQUEST_ATTRS_PREFIX, requestAttributes);

		Map<String, Object> sessionAttributes = new LinkedHashMap<String, Object>();
		variables.put(SESSION_ATTRS_PREFIX, sessionAttributes);

		Map<String, Object> servletContextAttributes = new LinkedHashMap<String, Object>();
		variables.put(SERVLETCONTEXT_ATTRS_PREFIX, servletContextAttributes);

		for (Map.Entry<String, ITestContextExpression> entry : testContext.getVariables().entrySet()) {
			resolve(entry.getKey(), entry.getValue(), variables, locale);
		}

		for (Map.Entry<String, ITestContextExpression[]> entry : testContext.getRequestParameters().entrySet()) {
			int firstPoint = entry.getKey().indexOf('.');

			String paramName = firstPoint == -1 ? entry.getKey() : entry.getKey().substring(0, firstPoint);
			Object[] paramValues = new Object[entry.getValue().length];

			requestParameters.put(paramName, paramValues);

			String remainder = firstPoint == -1 ? "" : entry.getKey().substring(firstPoint);
			int expressionsLen = entry.getValue().length;

			for (int i = 0; i < expressionsLen; i++) {
				resolve(REQUEST_PARAMS_PREFIX + "." + paramName + "[" + i + "]" + remainder, entry.getValue()[i], variables, locale);
			}
		}

		for (Map.Entry<String, ITestContextExpression> entry : testContext.getRequestAttributes().entrySet()) {
			resolve(REQUEST_ATTRS_PREFIX + "." + entry.getKey(), entry.getValue(), variables, locale);
		}

		for (Map.Entry<String, ITestContextExpression> entry : testContext.getSessionAttributes().entrySet()) {
			resolve(SESSION_ATTRS_PREFIX + "." + entry.getKey(), entry.getValue(), variables, locale);
		}

		for (Map.Entry<String, ITestContextExpression> entry : testContext.getServletContextAttributes().entrySet()) {
			resolve(SERVLETCONTEXT_ATTRS_PREFIX + "." + entry.getKey(),entry.getValue(), variables, locale);
		}

		variables.remove(REQUEST_PARAMS_PREFIX);
		variables.remove(REQUEST_ATTRS_PREFIX);
		variables.remove(SESSION_ATTRS_PREFIX);
		variables.remove(SERVLETCONTEXT_ATTRS_PREFIX);

		doAdditionalVariableProcessing(test, locale, variables);
		return new WebContext(request, response, servletContext, locale, variables);
	}

	private void resolve(String expression, ITestContextExpression contextExpression, Map<String, Object> variables, Locale locale) {
		try {
			Object result = contextExpression.evaluate(variables, locale);

			Object parsedExpression = Ognl.parseExpression(expression);
			Ognl.setValue(parsedExpression, variables, result);
		} catch (Throwable t) {
			throw new TestEngineExecutionException("Exception while trying to evaluate expression \"" + expression + "\" on context for test \"" + TestExecutor.getThreadTestName() + "\"", t);
		}
	}

	private void doAdditionalVariableProcessing(ITest test, Locale locale, Map<String, Object> variables) {
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

		request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

		RequestContextHolder.setRequestAttributes(webRequest);

		ConversionService conversionService = getConversionService();

		RequestContext requestContext = new RequestContext(request, response, servletContext, variables);
		variables.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, requestContext);

		additionalVariableProcessing(conversionService, variables);

		initializeBindingResults(test, conversionService, locale, variables);

		injectJawrProperties(test);
	}

	private ConversionService getConversionService() {
		if (applicationContext == null) {
			return null;
		}

		Map<String, ConversionService> conversionServices = applicationContext.getBeansOfType(ConversionService.class);
		if (conversionServices.size() == 0) {
			return null;
		}

		return (ConversionService) conversionServices.values().toArray()[0];
	}

	private void additionalVariableProcessing(ConversionService conversionService, Map<String, Object> variables) {
		RequestContext requestContext = (RequestContext) variables.get(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE);
		variables.put(SpringContextVariableNames.SPRING_REQUEST_CONTEXT, requestContext);

		ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, conversionService);

		variables.put(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);
	}

	private void initializeBindingResults(ITest test, ConversionService conversionService, Locale locale, Map<String, Object> variables) {
		List<String> variableNames = new ArrayList<String>(variables.keySet());
		for (String variableName : variableNames) {
			Object bindingObject = variables.get(variableName);
			if (isBindingCandidate(variableName, bindingObject)) {
				String bindingVariableName = BindingResult.MODEL_KEY_PREFIX + variableName;
				if (!variables.containsKey(bindingVariableName)) {
					WebDataBinder dataBinders = createBinding(test, variableName, bindingVariableName, bindingObject, conversionService, locale, variables);
					variables.put(bindingVariableName, dataBinders.getBindingResult());
				}
			}
		}
	}

	private boolean isBindingCandidate(String variableName, Object bindingObject) {
		if (variableName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
			return false;
		}

		return bindingObject != null && !bindingObject.getClass().isArray() && !(bindingObject instanceof Collection) && !(bindingObject instanceof Map) && !BeanUtils.isSimpleValueType(bindingObject.getClass());
	}

	private WebDataBinder createBinding(ITest test, String variableName, String bindingVariableName, Object bindingObject, ConversionService conversionService, Locale locale, Map<String, Object> variables) {
		WebDataBinder dataBinder = new WebDataBinder(bindingObject, bindingVariableName);
		dataBinder.setConversionService(conversionService);
		return dataBinder;
	}

	private void injectJawrProperties(ITest test) {
		if (!(test instanceof JawrTest)) {
			throw new TestEngineExecutionException("Test is an instance of \"" + test.getClass().getName() + "\" but should be an instance of \"" + JawrTest.class.getName() + "\"");
		}

		request.removeAttribute(BUNDLE_RENDERER_CONTEXT_ATTR_PREFIX + JawrConstant.CSS_TYPE);
		request.removeAttribute(BUNDLE_RENDERER_CONTEXT_ATTR_PREFIX + JawrConstant.JS_TYPE);

		JawrTest jawrTest = (JawrTest) test;
		Properties configuration = jawrTest.getJawrProperties().getProperties();

		JawrSpringController currentJawrSpringController = null;

		try {
			for (JawrSpringController jawrSpringController : applicationContext.getBeansOfType(JawrSpringController.class).values()) {
				currentJawrSpringController = jawrSpringController;

				jawrSpringController.setConfiguration(configuration);
				jawrSpringController.afterPropertiesSet();
			}
		} catch (Throwable t) {
			throw new TestEngineExecutionException("Exception while trying to inject Jawr properties into JawrSpringController of type \"" + currentJawrSpringController.getType() + "\"", t);
		}
	}
}