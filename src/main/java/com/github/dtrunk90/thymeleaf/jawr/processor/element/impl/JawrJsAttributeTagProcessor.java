package com.github.dtrunk90.thymeleaf.jawr.processor.element.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.BundleRendererContext;
import net.jawr.web.resource.bundle.renderer.RendererFactory;
import net.jawr.web.servlet.RendererRequestUtils;

import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.AbstractJawrAttributeTagProcessor;
import com.github.dtrunk90.thymeleaf.jawr.util.ContextUtils;

public class JawrJsAttributeTagProcessor extends AbstractJawrAttributeTagProcessor {
	public static final String ELEMENT = "script";
	public static final Attr ATTRIBUTE = Attr.SRC;
	public static final int PRECEDENCE = 300;

	@SuppressWarnings("serial")
	public static final Map<Attr, Object> OPTIONAL_ATTRIBUTES = new HashMap<Attr, Object>() {{
		put(Attr.ASYNC, false);
		put(Attr.DEFER, false);
		put(Attr.TYPE, null);
		put(Attr.USE_RANDOM_PARAM, true);
	}};

	public JawrJsAttributeTagProcessor() {
		super(ELEMENT, ATTRIBUTE, PRECEDENCE, OPTIONAL_ATTRIBUTES);
	}

	@Override
	protected String render(IWebContext context, IProcessableElementTag tag, Map<Attr, Object> attributes) throws IOException {
		Boolean async = (Boolean) attributes.get(Attr.ASYNC);
		Boolean defer = (Boolean) attributes.get(Attr.DEFER);
		String type = (String) attributes.get(Attr.TYPE);
		Boolean useRandomParam = (Boolean) attributes.get(Attr.USE_RANDOM_PARAM);

		HttpServletRequest request = context.getRequest();

		ResourceBundlesHandler bundler = (ResourceBundlesHandler) ContextUtils.getHandlerFromContext(context, JawrConstant.JS_CONTEXT_ATTRIBUTE);
		BundleRenderer renderer = RendererFactory.getJsBundleRenderer(bundler, type, useRandomParam, async, defer);
		BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);

		StringWriter out = new StringWriter();
		renderer.renderBundleLinks((String) attributes.get(ATTRIBUTE), rendererContext, out);
		out.flush();

		return out.toString();
	}
}