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
import com.github.dtrunk90.thymeleaf.jawr.util.ContextUtils;

public class JawrJsElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	public static final String ELEMENT_NAME = "script";

	public JawrJsElementSubstitutionAttrProcessor() {
		super(JawrSrcAttrProcessor.ATTR_NAME, ELEMENT_NAME);
	}

	@Override
	protected String render(Arguments arguments, Element element, Map<Attr, Object> attrMap) throws IOException {
		String type = attrMap.containsKey(Attr.TYPE) ? (String) attrMap.get(Attr.TYPE) : null;
		Boolean useRandomParam = attrMap.containsKey(Attr.USE_RANDOM_PARAM) ? (Boolean) attrMap.get(Attr.USE_RANDOM_PARAM) : true;
		Boolean async = attrMap.containsKey(Attr.ASYNC) ? (Boolean) attrMap.get(Attr.ASYNC) : false;
		Boolean defer = attrMap.containsKey(Attr.DEFER) ? (Boolean) attrMap.get(Attr.DEFER) : false;
		String crossorigin = attrMap.containsKey(Attr.CROSSORIGIN) ? (String) attrMap.get(Attr.CROSSORIGIN) : null;

		WebContext context = (WebContext) arguments.getContext();
		HttpServletRequest request = context.getHttpServletRequest();

		ResourceBundlesHandler bundler = (ResourceBundlesHandler) ContextUtils.getHandlerFromContext(context.getServletContext(), JawrConstant.JS_CONTEXT_ATTRIBUTE);
		BundleRenderer renderer = RendererFactory.getJsBundleRenderer(bundler, type, useRandomParam, async, defer, crossorigin);
		BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);

		StringWriter out = new StringWriter();
		renderer.renderBundleLinks((String) attrMap.get(Attr.SRC), rendererContext, out);
		out.flush();

		return out.toString();
	}
}