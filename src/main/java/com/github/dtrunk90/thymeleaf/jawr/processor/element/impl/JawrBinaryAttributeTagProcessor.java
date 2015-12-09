package com.github.dtrunk90.thymeleaf.jawr.processor.element.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jawr.web.JawrConstant;
import net.jawr.web.resource.BinaryResourcesHandler;
import net.jawr.web.resource.bundle.renderer.RendererFactory;
import net.jawr.web.resource.bundle.renderer.image.ImgRenderer;
import net.jawr.web.taglib.ImageTagUtils;

import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.AbstractJawrAttributeTagProcessor;

public class JawrBinaryAttributeTagProcessor extends AbstractJawrAttributeTagProcessor {
	public static final Attr ATTRIBUTE = Attr.SRC;
	public static final int PRECEDENCE = 100;

	@SuppressWarnings("serial")
	public static final Map<Attr, Object> OPTIONAL_ATTRIBUTES = new HashMap<Attr, Object>() {{
		put(Attr.BASE64, false);
	}};

	public JawrBinaryAttributeTagProcessor(IProcessorDialect dialect, String elementName) {
		super(dialect, elementName, ATTRIBUTE, PRECEDENCE, OPTIONAL_ATTRIBUTES);
	}

	@Override
	protected String render(IWebContext context, IProcessableElementTag tag, Map<Attr, Object> attributes) throws IOException {
		String elementName = getMatchingElementName().getMatchingElementName().getElementName();
		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) getHandlerFromContext(context, JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		ImgRenderer renderer = RendererFactory.getImgRenderer(binaryRsHandler.getConfig(), "img".equalsIgnoreCase(elementName));

		Map<String, Object> additionalAttributes = new HashMap<String, Object>();
		for (AttributeName attributeName : tag.getAttributes().getAllAttributeNames()) {
			String attrName = attributeName.getAttributeName();
			if (!ATTRIBUTE.toString().equalsIgnoreCase(attrName) && !"input".equalsIgnoreCase(elementName) && !"type".equalsIgnoreCase(attrName)) {
				additionalAttributes.put(attrName, tag.getAttributes().getValue(attributeName));
			}
		}

		Boolean base64 = (Boolean) attributes.get(Attr.BASE64);

		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();

		StringWriter out = new StringWriter();
		String imgSource = ImageTagUtils.getImageUrl((String) attributes.get(ATTRIBUTE), base64, binaryRsHandler, request, response);
		renderer.renderImage(imgSource, additionalAttributes, out);
		out.flush();

		return out.toString();
	}
}