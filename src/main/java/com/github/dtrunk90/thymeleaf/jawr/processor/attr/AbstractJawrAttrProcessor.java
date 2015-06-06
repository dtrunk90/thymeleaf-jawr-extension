package com.github.dtrunk90.thymeleaf.jawr.processor.attr;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

public abstract class AbstractJawrAttrProcessor extends AbstractAttrProcessor {
	public static final int ATTR_PRECEDENCE = 1700;

	protected AbstractJawrAttrProcessor(String attributeName) {
		super(attributeName);
	}

	@Override
	public int getPrecedence() {
		return ATTR_PRECEDENCE;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
		Object attributeValue = element.getAttributeValue(attributeName);

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		Map<Element, Map<Attr, Object>> elementAttrMap = (Map<Element, Map<Attr, Object>>) request.getAttribute(JawrDialect.REQUEST_ATTR_NAME);

		if (elementAttrMap == null) {
			elementAttrMap = new HashMap<Element, Map<Attr, Object>>();
			request.setAttribute(JawrDialect.REQUEST_ATTR_NAME, elementAttrMap);
		}

		Map<Attr, Object> attrMap = elementAttrMap.get(element);

		if (attrMap == null) {
			attrMap = new HashMap<Attr, Object>();
			elementAttrMap.put(element, attrMap);
		}

		try {
			Configuration configuration = arguments.getConfiguration();
			IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
			IStandardExpression expression = parser.parseExpression(configuration, arguments, (String) attributeValue);

			attributeValue = expression.execute(configuration, arguments);
		} catch (TemplateProcessingException e) {}

		attrMap.put(getAttr(), attributeValue);
		return ProcessorResult.OK;
	}

	protected abstract Attr getAttr();
}