package com.github.dtrunk90.thymeleaf.jawr.expression;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.taglib.ImageTagUtils;

import org.thymeleaf.context.Contexts;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.util.Validate;
import org.thymeleaf.web.servlet.IServletWebExchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Jawr {
	private final IServletWebExchange webExchange;

	public Jawr(IWebContext context) {
		Validate.notNull(context, "Context cannot be null");
		this.webExchange = Contexts.getServletWebExchange(context);
	}

	public String imagePath(String src) {
		return imagePath(src, false);
	}

	public String imagePath(String src, boolean base64) {
		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) webExchange.getApplication().getAttributeValue(JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		if (binaryRsHandler == null) {
			throw new TemplateProcessingException("Handler \"" + JawrConstant.BINARY_CONTEXT_ATTRIBUTE + "\" not present in servlet context. Initialization of Jawr either failed or never occurred.");
		}

		return ImageTagUtils.getImageUrl(src, base64, binaryRsHandler, (HttpServletRequest) webExchange.getNativeRequestObject(), (HttpServletResponse) webExchange.getNativeResponseObject());
	}
}