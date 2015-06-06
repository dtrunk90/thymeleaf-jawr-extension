package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.shared;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrUseRandomParamAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "useRandomParam";

	public JawrUseRandomParamAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.USE_RANDOM_PARAM;
	}
}