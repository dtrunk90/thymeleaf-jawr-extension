package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrCrossoriginAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "crossorigin";

	public JawrCrossoriginAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.CROSSORIGIN;
	}
}