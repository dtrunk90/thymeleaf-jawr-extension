package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrSrcAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "src";

	public JawrSrcAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.SRC;
	}

	@Override
	protected boolean getRemoveAttribute(Arguments arguments, Element element, String attributeName) {
		return false;
	}
}