package com.github.dtrunk90.thymeleaf.jawr.processor.element;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.EscapedAttributeUtils;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

public abstract class AbstractJawrAttributeTagProcessor extends AbstractAttributeTagProcessor {
	public static enum Attr {
		ALTERNATE, ASYNC, BASE64, DEFER, DISPLAY_ALTERNATE, HREF, MEDIA, SRC, TITLE, TYPE, USE_RANDOM_PARAM
	}

	private final Map<Attr, Object> optionalAttributes;
	private final Attr attribute;

	protected AbstractJawrAttributeTagProcessor(String elementName, Attr attribute, int precedence, Map<Attr, Object> optionalAttributes) {
		super(TemplateMode.HTML, JawrDialect.PREFIX, elementName, false, attribute.toString(), true, precedence, false);

		this.optionalAttributes = Collections.unmodifiableMap(optionalAttributes);
		this.attribute = attribute;
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
		if (!(context instanceof IWebContext)) {
			throw new ConfigurationException("Thymeleaf execution context is not a web context. Jawr integration can only be used in web environments.");
		}

		Map<Attr, Object> attributes = new HashMap<Attr, Object>();

		IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
		Object expressionResult = parseExpression(expressionParser, context, attributeValue);
		tag.getAttributes().removeAttribute(attributeName);

		int line = 0;
		int col = 0;

		try {
			for (Map.Entry<Attr, Object> optionalAttribute : optionalAttributes.entrySet()) {
				Object optionalExpressionResult = optionalAttribute.getValue();

				AttributeDefinition optionalAttributeDefinition = tag.getAttributes().getAttributeDefinition(JawrDialect.PREFIX, optionalAttribute.getKey().toString());
				if (optionalAttributeDefinition != null) {
					attributeName = optionalAttributeDefinition.getAttributeName();
					attributeValue = EscapedAttributeUtils.unescapeAttribute(context.getTemplateMode(), tag.getAttributes().getValue(attributeName));
					line = tag.getAttributes().getLine(attributeName);
					col = tag.getAttributes().getCol(attributeName);

					optionalExpressionResult = parseExpression(expressionParser, context, attributeValue);
					tag.getAttributes().removeAttribute(attributeName);
				}

				attributes.put(optionalAttribute.getKey(), optionalExpressionResult);
			}
		} catch (final TemplateProcessingException e) {
			if (tag.hasLocation()) {
				if (!e.hasLineAndCol()) {
					if (attributeName != null) {
						e.setLineAndCol(line, col);
					}
				}
			}

			throw e;
		}

		attributes.put(attribute, expressionResult);

		try {
			structureHandler.replaceWith(render((IWebContext) context, tag, attributes), false);
		} catch (IOException e) {
			throw new TemplateProcessingException("Error during execution of processor '" + getClass().getName() + "'", tag.getTemplateName(), line, col, e);
		}
	}

	private Object parseExpression(IStandardExpressionParser expressionParser, ITemplateContext context, String attributeValue) {
		if (attributeValue == null) {
			return null;
		}

		Object result = null;

		try {
			IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
			result = expression.execute(context);
		} catch (TemplateProcessingException e) {
			result = attributeValue;
		}

		return result;
	}

	protected abstract String render(IWebContext context, IProcessableElementTag tag, Map<Attr, Object> attributes) throws IOException;
}