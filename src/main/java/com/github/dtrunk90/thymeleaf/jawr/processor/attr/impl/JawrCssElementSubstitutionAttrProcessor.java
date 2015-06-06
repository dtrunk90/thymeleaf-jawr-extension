package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.BundleRendererContext;
import net.jawr.web.resource.bundle.renderer.RendererFactory;
import net.jawr.web.servlet.RendererRequestUtils;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrHrefAttrProcessor;

public class JawrCssElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	public JawrCssElementSubstitutionAttrProcessor(String elementNameFilter) {
		super(JawrHrefAttrProcessor.ATTR_NAME, elementNameFilter);
	}

	@Override
	protected String render(Arguments arguments, Element element, Map<Attr, Object> attrMap) throws IOException {
		String media = (String) attrMap.get(Attr.MEDIA);
		String title = (String) attrMap.get(Attr.TITLE);

		Boolean useRandomParam = attrMap.containsKey(Attr.USE_RANDOM_PARAM) ? (Boolean) attrMap.get(Attr.USE_RANDOM_PARAM) : true;
		boolean alternate = attrMap.containsKey(Attr.ALTERNATE) ? (Boolean) attrMap.get(Attr.ALTERNATE) : false;
		boolean displayAlternateStyles = attrMap.containsKey(Attr.DISPLAY_ALTERNATE) ? (Boolean) attrMap.get(Attr.DISPLAY_ALTERNATE) : false;

		WebContext context = (WebContext) arguments.getContext();
		HttpServletRequest request = context.getHttpServletRequest();

		ResourceBundlesHandler bundler = (ResourceBundlesHandler) getHandlerFromContext(context, JawrConstant.CSS_CONTEXT_ATTRIBUTE);
		BundleRenderer renderer = RendererFactory.getCssBundleRenderer(bundler, useRandomParam, media, alternate, displayAlternateStyles, title);
		BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);

		StringWriter out = new StringWriter();
		renderer.renderBundleLinks((String) attrMap.get(Attr.HREF), rendererContext, out);
		out.flush();

		return out.toString();
	}
}