package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrHrefAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "href";

	public JawrHrefAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.HREF;
	}

	@Override
	protected boolean getRemoveAttribute(Arguments arguments, Element element, String attributeName) {
		return false;
	}
}