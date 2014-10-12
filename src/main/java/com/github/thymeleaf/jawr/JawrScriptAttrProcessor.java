package com.github.thymeleaf.jawr;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.JavascriptHTMLBundleLinkRenderer;

import org.thymeleaf.context.WebContext;

/**
 * Jawr attribute processor used for rendering the javascript bundle imports;
 * applied only to script attributes inside of script tags.
 *
 * @author Miloš Milivojević
 * @author Danny Trunk
 *
 */
public class JawrScriptAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "script";
	public static final String TAG_NAME = "script";

	public JawrScriptAttrProcessor() {
		super(ATTR_NAME, TAG_NAME);
	}

	@Override
	protected final BundleRenderer createRenderer(WebContext webContext) {
		JavascriptHTMLBundleLinkRenderer renderer = new JavascriptHTMLBundleLinkRenderer();
		renderer.init(getResourceBundleHandlerFromContext(webContext, JawrConstant.JS_CONTEXT_ATTRIBUTE), false);
		return renderer;
	}
}