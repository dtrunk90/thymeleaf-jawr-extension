package com.github.dtrunk90.thymeleaf.jawr.expression;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.taglib.ImageTagUtils;

import org.thymeleaf.context.IWebContext;
import org.thymeleaf.util.Validate;

import com.github.dtrunk90.thymeleaf.jawr.util.ContextUtils;

public class Jawr {
	private final IWebContext context;

	public Jawr(IWebContext context) {
		Validate.notNull(context, "Context cannot be null");
		this.context = context;
	}

	public String imagePath(String src) {
		return imagePath(src, false);
	}

	public String imagePath(String src, boolean base64) {
		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) ContextUtils.getHandlerFromContext(context, JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		return ImageTagUtils.getImageUrl(src, base64, binaryRsHandler, context.getRequest(), context.getResponse());
	}
}