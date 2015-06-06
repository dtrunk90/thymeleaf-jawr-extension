package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrAsyncAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "async";

	public JawrAsyncAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.ASYNC;
	}
}