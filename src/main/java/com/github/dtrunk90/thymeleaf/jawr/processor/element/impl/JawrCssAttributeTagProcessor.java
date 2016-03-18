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

public class JawrCssAttributeTagProcessor extends AbstractJawrAttributeTagProcessor {
	public static final String ELEMENT = "link";
	public static final Attr ATTRIBUTE = Attr.HREF;
	public static final int PRECEDENCE = 200;

	@SuppressWarnings("serial")
	public static final Map<Attr, Object> OPTIONAL_ATTRIBUTES = new HashMap<Attr, Object>() {{
		put(Attr.ALTERNATE, false);
		put(Attr.DISPLAY_ALTERNATE, false);
		put(Attr.MEDIA, null);
		put(Attr.TITLE, null);
		put(Attr.USE_RANDOM_PARAM, true);
	}};

	public JawrCssAttributeTagProcessor() {
		super(ELEMENT, ATTRIBUTE, PRECEDENCE, OPTIONAL_ATTRIBUTES);
	}

	@Override
	protected String render(IWebContext context, IProcessableElementTag tag, Map<Attr, Object> attributes) throws IOException {
		String media = (String) attributes.get(Attr.MEDIA);
		String title = (String) attributes.get(Attr.TITLE);

		Boolean useRandomParam = (Boolean) attributes.get(Attr.USE_RANDOM_PARAM);
		Boolean alternate = (Boolean) attributes.get(Attr.ALTERNATE);
		Boolean displayAlternateStyles = (Boolean) attributes.get(Attr.DISPLAY_ALTERNATE);

		HttpServletRequest request = context.getRequest();

		ResourceBundlesHandler bundler = (ResourceBundlesHandler) ContextUtils.getHandlerFromContext(context, JawrConstant.CSS_CONTEXT_ATTRIBUTE);
		BundleRenderer renderer = RendererFactory.getCssBundleRenderer(bundler, useRandomParam, media, alternate, displayAlternateStyles, title);
		BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);

		StringWriter out = new StringWriter();
		renderer.renderBundleLinks((String) attributes.get(ATTRIBUTE), rendererContext, out);
		out.flush();

		return out.toString();
	}
}