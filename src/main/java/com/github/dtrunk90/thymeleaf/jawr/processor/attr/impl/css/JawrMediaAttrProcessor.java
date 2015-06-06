package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrMediaAttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "media";

	public JawrMediaAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.MEDIA;
	}
}