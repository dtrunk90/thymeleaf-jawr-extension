package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl;

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

import org.thymeleaf.Arguments;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.shared.JawrSrcAttrProcessor;

public class JawrImgElementSubstitutionAttrProcessor extends AbstractJawrElementSubstitutionAttrProcessor {
	private final String elementNameFilter;

	public JawrImgElementSubstitutionAttrProcessor(String elementNameFilter) {
		super(JawrSrcAttrProcessor.ATTR_NAME, elementNameFilter);
		this.elementNameFilter = elementNameFilter;
	}

	@Override
	protected String render(Arguments arguments, Element element, Map<Attr, Object> attrMap) throws IOException {
		Boolean base64 = attrMap.containsKey(Attr.BASE64) ? (Boolean) attrMap.get(Attr.BASE64) : false;

		WebContext context = (WebContext) arguments.getContext();
		HttpServletRequest request = context.getHttpServletRequest();
		HttpServletResponse response = context.getHttpServletResponse();

		BinaryResourcesHandler binaryRsHandler = (BinaryResourcesHandler) getHandlerFromContext(context, JawrConstant.BINARY_CONTEXT_ATTRIBUTE);
		ImgRenderer renderer = RendererFactory.getImgRenderer(binaryRsHandler.getConfig(), !"input".equalsIgnoreCase(elementNameFilter));

		Map<String, Object> attributes = new HashMap<String, Object>();
		for (Map.Entry<String, Attribute> attr : element.getAttributeMap().entrySet()) {
			String attrName = attr.getKey();
			if (!attrName.equalsIgnoreCase(JawrDialect.PREFIX + ":" + JawrSrcAttrProcessor.ATTR_NAME) && !attrName.equalsIgnoreCase(JawrSrcAttrProcessor.ATTR_NAME) && !"input".equalsIgnoreCase(elementNameFilter) && !"type".equalsIgnoreCase(attrName)) {
				attributes.put(attrName, attr.getValue().getEscapedValue());
			}
		}

		StringWriter out = new StringWriter();
		String imgSource = ImageTagUtils.getImageUrl((String) attrMap.get(Attr.SRC), base64, binaryRsHandler, request, response);
		renderer.renderImage(imgSource, attributes, out);
		out.flush();

		return out.toString();
	}
}