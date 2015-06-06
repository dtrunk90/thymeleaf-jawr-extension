package com.github.dtrunk90.thymeleaf.jawr.processor.attr;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

public abstract class AbstractJawrElementSubstitutionAttrProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {
	public static final int ATTR_PRECEDENCE = 1800;

	public AbstractJawrElementSubstitutionAttrProcessor(String attributeName, String elementNameFilter) {
		super(new AttributeNameProcessorMatcher(attributeName, elementNameFilter));
	}

	@Override
	public int getPrecedence() {
		return ATTR_PRECEDENCE;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected String getText(Arguments arguments, Element element, String attributeName) {
		WebContext context = (WebContext) arguments.getContext();

		HttpServletRequest request = context.getHttpServletRequest();
		Map<Attr, Object> attrMap = ((Map<Element, Map<Attr, Object>>) request.getAttribute(JawrDialect.REQUEST_ATTR_NAME)).get(element);

		try {
			return render(arguments, element, attrMap);
		} catch (IOException e) {
			throw new TemplateProcessingException("Error during template processing", e);
		}
	}

	@Override
	protected boolean getReplaceHostElement(Arguments arguments, Element element, String attributeName) {
		return true;
	}

	protected Object getHandlerFromContext(WebContext context, String attributeName) {
		Object attributeValue = context.getServletContext().getAttribute(attributeName);
		if (attributeValue == null) {
			throw new TemplateProcessingException("Handler \"" + attributeName + "\" not present in servlet context. Initialization of Jawr either failed or never occurred.");
		}

		return attributeValue;
	}

	protected abstract String render(Arguments arguments, Element element, Map<Attr, Object> attrMap) throws IOException;
}