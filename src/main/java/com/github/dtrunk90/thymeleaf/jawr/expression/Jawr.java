package com.github.dtrunk90.thymeleaf.jawr.expression;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.taglib.ImageTagUtils;

import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import com.github.dtrunk90.thymeleaf.jawr.util.ContextUtils;

public class Jawr {
	private final WebContext context;

	public Jawr(IContext context) {
		this.context = (WebContext) context;
	}

	public String imagePath(String src) {
		return imagePath(src, false);
	}

	public String imagePath(String src, boolean base64) {
		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) ContextUtils.getHandlerFromContext(context.getServletContext(), JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		return ImageTagUtils.getImageUrl(src, base64, binaryRsHandler, context.getHttpServletRequest(), context.getHttpServletResponse());
	}
}