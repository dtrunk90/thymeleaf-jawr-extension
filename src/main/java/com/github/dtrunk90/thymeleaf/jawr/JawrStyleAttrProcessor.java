package com.github.dtrunk90.thymeleaf.jawr;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.CSSHTMLBundleLinkRenderer;

import org.thymeleaf.context.WebContext;

/**
 * Jawr attribute processor used for rendering the CSS bundle imports; applied
 * only to style attributes inside of link tags.
 *
 * @author Miloš Milivojević
 * @author Danny Trunk
 *
 */
public class JawrStyleAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "style";
	public static final String TAG_NAME = "link";
	public static final String MEDIA = "all";

	public JawrStyleAttrProcessor() {
		super(ATTR_NAME, TAG_NAME);
	}

	@Override
	protected final BundleRenderer createRenderer(WebContext webContext) {
		CSSHTMLBundleLinkRenderer renderer = new CSSHTMLBundleLinkRenderer();
		renderer.init(getResourceBundleHandlerFromContext(webContext, JawrConstant.CSS_CONTEXT_ATTRIBUTE), false, MEDIA, false, false, null);
		return renderer;
	}
}