package com.github.dtrunk90.thymeleaf.jawr.processor.element.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.AbstractJawrAttributeTagProcessor;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.RendererFactory;

public class JawrCssAttributeTagProcessor extends AbstractJawrAttributeTagProcessor {
	public static final String ELEMENT = "link";
	public static final Attr ATTRIBUTE = Attr.HREF;
	public static final int PRECEDENCE = 100;

	public JawrCssAttributeTagProcessor() {
		super(ELEMENT, ATTRIBUTE, PRECEDENCE);
	}

	@Override
	protected BundleRenderer createRenderer(ResourceBundlesHandler rsHandler, Map<Attr, Object> attributes) {
		return RendererFactory.getCssBundleRenderer(rsHandler,
				(Boolean) attributes.get(Attr.USE_RANDOM_PARAM),
				(String) attributes.get(Attr.MEDIA),
				(Boolean) attributes.get(Attr.ALTERNATE),
				(Boolean) attributes.get(Attr.DISPLAY_ALTERNATE),
				(String) attributes.get(Attr.TITLE));
	}

	@Override
	protected String getContextAttributeName() {
		return JawrConstant.CSS_CONTEXT_ATTRIBUTE;
	}

	@Override
	@SuppressWarnings("serial")
	protected Map<Attr, Object> getOptionalAttributes() {
		return Collections.unmodifiableMap(new HashMap<Attr, Object>() {{
			put(Attr.ALTERNATE, false);
			put(Attr.DISPLAY_ALTERNATE, false);
			put(Attr.MEDIA, null);
			put(Attr.TITLE, null);
			put(Attr.USE_RANDOM_PARAM, null);
		}});
	}
}