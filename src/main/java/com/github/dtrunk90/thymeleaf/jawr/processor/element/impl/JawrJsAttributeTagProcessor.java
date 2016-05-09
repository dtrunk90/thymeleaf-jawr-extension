package com.github.dtrunk90.thymeleaf.jawr.processor.element.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.RendererFactory;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.AbstractJawrAttributeTagProcessor;

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
				//(String) attributes.get(Attr.TYPE),
				(Boolean) attributes.get(Attr.USE_RANDOM_PARAM),
				(Boolean) attributes.get(Attr.ASYNC),
				(Boolean) attributes.get(Attr.DEFER));
	}

	@Override
	protected String getContextAttributeName() {
		return JawrConstant.JS_CONTEXT_ATTRIBUTE;
	}

	@Override
	@SuppressWarnings("serial")
	protected Map<Attr, Object> getOptionalAttributes() {
		return Collections.unmodifiableMap(new HashMap<Attr, Object>() {{
			put(Attr.ASYNC, false);
			put(Attr.DEFER, false);
			put(Attr.TYPE, null);
			put(Attr.USE_RANDOM_PARAM, true);
		}});
	}
}