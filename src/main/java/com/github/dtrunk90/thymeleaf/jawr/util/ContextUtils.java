package com.github.dtrunk90.thymeleaf.jawr.util;

import org.thymeleaf.context.IWebContext;
import org.thymeleaf.exceptions.TemplateProcessingException;

public abstract class ContextUtils {
	public static Object getHandlerFromContext(IWebContext context, String attributeName) {
		Object attributeValue = context.getServletContext().getAttribute(attributeName);
		if (attributeValue == null) {
			throw new TemplateProcessingException("Handler \"" + attributeName + "\" not present in servlet context. Initialization of Jawr either failed or never occurred.");
		}

		return attributeValue;
	}
}