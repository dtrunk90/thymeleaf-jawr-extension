package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js;

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
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.shared.JawrSrcAttrProcessor;

public class JawrJsElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	public JawrJsElementSubstitutionAttrProcessor(String elementNameFilter) {
		super(JawrSrcAttrProcessor.ATTR_NAME, elementNameFilter);
	}

	@Override
	protected String render(Arguments arguments, Element element, Map<Attr, Object> attrMap) throws IOException {
		Boolean useRandomParam = attrMap.containsKey(Attr.USE_RANDOM_PARAM) ? (Boolean) attrMap.get(Attr.USE_RANDOM_PARAM) : true;
		Boolean async = attrMap.containsKey(Attr.ASYNC) ? (Boolean) attrMap.get(Attr.ASYNC) : false;
		Boolean defer = attrMap.containsKey(Attr.DEFER) ? (Boolean) attrMap.get(Attr.DEFER) : false;
		String type = attrMap.containsKey(Attr.TYPE) ? (String) attrMap.get(Attr.TYPE) : null;

		WebContext context = (WebContext) arguments.getContext();
		HttpServletRequest request = context.getHttpServletRequest();

		ResourceBundlesHandler bundler = (ResourceBundlesHandler) getHandlerFromContext(context, JawrConstant.JS_CONTEXT_ATTRIBUTE);
		BundleRenderer renderer = RendererFactory.getJsBundleRenderer(bundler, type, useRandomParam, async, defer);
		BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);

		StringWriter out = new StringWriter();
		renderer.renderBundleLinks((String) attrMap.get(Attr.SRC), rendererContext, out);
		out.flush();

		return out.toString();
	}
}