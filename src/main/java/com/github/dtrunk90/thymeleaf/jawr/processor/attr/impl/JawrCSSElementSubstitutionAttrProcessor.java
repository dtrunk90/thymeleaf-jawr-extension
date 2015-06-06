package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl;

import java.util.Map;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.RendererFactory;

import org.thymeleaf.context.WebContext;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrCSSElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	public static final String ELEMENT_NAME_FILTER = "link";

	public JawrCSSElementSubstitutionAttrProcessor() {
		super(JawrHrefAttrProcessor.ATTR_NAME, ELEMENT_NAME_FILTER);
	}

	@Override
	protected BundleRenderer createRenderer(WebContext webContext, Map<Attr, Object> attrMap) {
		String media = (String) attrMap.get(Attr.MEDIA);
		String title = (String) attrMap.get(Attr.TITLE);

		Boolean useRandomParam = attrMap.containsKey(Attr.USE_RANDOM_PARAM) ? (Boolean) attrMap.get(Attr.USE_RANDOM_PARAM) : true;
		boolean alternate = attrMap.containsKey(Attr.ALTERNATE) ? (Boolean) attrMap.get(Attr.ALTERNATE) : false;
		boolean displayAlternateStyles = attrMap.containsKey(Attr.DISPLAY_ALTERNATE) ? (Boolean) attrMap.get(Attr.DISPLAY_ALTERNATE) : false;

		ResourceBundlesHandler bundler = getResourceBundleHandlerFromContext(webContext, JawrConstant.CSS_CONTEXT_ATTRIBUTE);
		return RendererFactory.getCssBundleRenderer(bundler, useRandomParam, media, alternate, displayAlternateStyles, title);
	}

	@Override
	protected Object getAttributeValue(Map<Attr, Object> attrMap) {
		return attrMap.get(Attr.HREF);
	}
}