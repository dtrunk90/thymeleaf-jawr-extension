package com.github.dtrunk90.thymeleaf.jawr.processor.attr;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.resource.bundle.renderer.BundleRendererContext;
import net.jawr.web.servlet.RendererRequestUtils;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

public abstract class AbstractJawrElementSubstitutionAttrProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {
	public static final int ATTR_PRECEDENCE = 1800;

	public AbstractJawrElementSubstitutionAttrProcessor(String attributeName, String elementNameFilter) {
		super(new AttributeNameProcessorMatcher(attributeName, elementNameFilter));
	}

	@Override
	public int getPrecedence() {
		return ATTR_PRECEDENCE;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected String getText(Arguments arguments, Element element, String attributeName) {
		WebContext context = (WebContext) arguments.getContext();

		HttpServletRequest request = context.getHttpServletRequest();
		Map<Attr, Object> attrMap = ((Map<Element, Map<Attr, Object>>) request.getAttribute(JawrDialect.REQUEST_ATTR_NAME)).get(element);

		StringWriter out = new StringWriter();

		try {
			BundleRenderer renderer = createRenderer(context, attrMap);
			BundleRendererContext rendererContext = RendererRequestUtils.getBundleRendererContext(request, renderer);
			renderer.renderBundleLinks((String) getAttributeValue(attrMap), rendererContext, out);
			out.flush();
		} catch (IOException e) {
			throw new TemplateProcessingException("Error during template processing", e);
		}

		return out.toString();
	}

	@Override
	protected boolean getReplaceHostElement(Arguments arguments, Element element, String attributeName) {
		return true;
	}

	protected ResourceBundlesHandler getResourceBundleHandlerFromContext(WebContext webContext, String attributeName) {
		Object attributeValue = webContext.getServletContext().getAttribute(attributeName);
		if (attributeValue == null) {
			throw new IllegalStateException("ResourceBundlesHandler not present in servlet context. Initialization of Jawr either failed or never occurred.");
		}

		return (ResourceBundlesHandler) attributeValue;
	}

	protected abstract BundleRenderer createRenderer(WebContext webContext, Map<Attr, Object> attrMap);
	protected abstract Object getAttributeValue(Map<Attr, Object> attrMap);
}