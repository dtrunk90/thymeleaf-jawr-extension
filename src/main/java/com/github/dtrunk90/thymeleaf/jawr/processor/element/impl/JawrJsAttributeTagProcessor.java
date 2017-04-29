package com.github.dtrunk90.thymeleaf.jawr.processor.element.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.AbstractJawrAttributeTagProcessor;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.RendererFactory;

public class JawrJsAttributeTagProcessor extends AbstractJawrAttributeTagProcessor {
	public static final String ELEMENT = "script";
	public static final Attr ATTRIBUTE = Attr.SRC;
	public static final int PRECEDENCE = 200;

	public JawrJsAttributeTagProcessor() {
		super(ELEMENT, ATTRIBUTE, PRECEDENCE);
	}

	@Override
	protected BundleRenderer createRenderer(ResourceBundlesHandler rsHandler, Map<Attr, Object> attributes) {
		return RendererFactory.getJsBundleRenderer(rsHandler,
				(String) attributes.get(Attr.TYPE),
				(Boolean) attributes.get(Attr.USE_RANDOM_PARAM),
				(Boolean) attributes.get(Attr.ASYNC),
				(Boolean) attributes.get(Attr.DEFER),
				(String) attributes.get(Attr.CROSSORIGIN));
	}

	@Override
	protected String getContextAttributeName() {
		return JawrConstant.JS_CONTEXT_ATTRIBUTE;
	}

	@Override
	@SuppressWarnings("serial")
	protected Map<Attr, Object> getOptionalAttributes() {
		return Collections.unmodifiableMap(new HashMap<Attr, Object>() {{
			put(Attr.ASYNC, null);
			put(Attr.CROSSORIGIN, null);
			put(Attr.DEFER, null);
			put(Attr.TYPE, null);
			put(Attr.USE_RANDOM_PARAM, null);
		}});
	}
}