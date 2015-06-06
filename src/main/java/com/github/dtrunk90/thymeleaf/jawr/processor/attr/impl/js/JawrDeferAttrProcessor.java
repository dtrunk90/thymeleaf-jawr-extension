package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrDeferAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "defer";

	public JawrDeferAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.DEFER;
	}
}