package com.github.dtrunk90.thymeleaf.jawr;

import java.io.IOException;
import java.io.StringWriter;

import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.BundleRendererContext;
import net.jawr.web.servlet.RendererRequestUtils;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * An abstract parent class that uses a Jawr renderer to get the unescaped text
 * that will be used to render a tag's content; because this processor writes
 * the renderer-provided text inside of it's enclosing tag, the result will
 * often be malformed HTML so it is therefore suggested to use an instance of
 * {@link JawrTagRemovingAttrProcessor} to wrap your attribute processors.
 *
 * @author Miloš Milivojević
 * @author Danny Trunk
 *
 */
public abstract class AbstractJawrAttrProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {
	private static final int PRECEDENCE = 900;

	public AbstractJawrAttrProcessor(String attributeName, String elementNameFilter) {
		super(new AttributeNameProcessorMatcher(attributeName, elementNameFilter));
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	@Override
	protected final String getText(Arguments arguments, Element element, String attributeName) {
		try {
			Configuration configuration = arguments.getConfiguration();
			IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
			IStandardExpression expression = parser.parseExpression(configuration, arguments, element.getAttributeValue(attributeName));
			return getRenderedString((String) expression.execute(configuration, arguments), (WebContext) arguments.getContext());
		} catch (IOException e) {}

		return "";
	}

	protected ResourceBundlesHandler getResourceBundleHandlerFromContext(WebContext webContext, String attributeName) {
		Object attributeValue = webContext.getServletContext().getAttribute(attributeName);
		if (attributeValue == null) {
			throw new IllegalStateException("ResourceBundlesHandler not present in servlet context. Initialization of Jawr either failed or never occurred.");
		}

		return (ResourceBundlesHandler) attributeValue;
	}

	protected String getRenderedString(String attributeValue, WebContext webContext) throws IOException {
		StringWriter out = new StringWriter();

		BundleRenderer renderer = createRenderer(webContext);
		renderer.renderBundleLinks(attributeValue, getRendererContext(webContext, renderer), out);
		out.flush();

		return out.toString();
	}

	private BundleRendererContext getRendererContext(WebContext webContext, BundleRenderer renderer) {
		return RendererRequestUtils.getBundleRendererContext(webContext.getHttpServletRequest(), renderer);
	}

	protected abstract BundleRenderer createRenderer(WebContext webContext);
}
