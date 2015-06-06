package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl;

import java.util.Map;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.RendererFactory;

import org.thymeleaf.context.WebContext;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrJavascriptElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	public static final String ELEMENT_NAME_FILTER = "script";

	public JawrJavascriptElementSubstitutionAttrProcessor() {
		super(JawrSrcAttrProcessor.ATTR_NAME, ELEMENT_NAME_FILTER);
	}

	@Override
	protected BundleRenderer createRenderer(WebContext webContext, Map<Attr, Object> attrMap) {
		Boolean useRandomParam = attrMap.containsKey(Attr.USE_RANDOM_PARAM) ? (Boolean) attrMap.get(Attr.USE_RANDOM_PARAM) : true;
		Boolean async = attrMap.containsKey(Attr.ASYNC) ? (Boolean) attrMap.get(Attr.ASYNC) : false;
		Boolean defer = attrMap.containsKey(Attr.DEFER) ? (Boolean) attrMap.get(Attr.DEFER) : false;

		ResourceBundlesHandler bundler = getResourceBundleHandlerFromContext(webContext, JawrConstant.JS_CONTEXT_ATTRIBUTE);
		return RendererFactory.getJsBundleRenderer(bundler, useRandomParam, async, defer);
	}

	@Override
	protected Object getAttributeValue(Map<Attr, Object> attrMap) {
		return attrMap.get(Attr.SRC);
	}
}