package com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.img;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.AbstractJawrAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.Attr;

public class JawrBase64AttrProcessor extends AbstractJawrAttrProcessor {
	public static final String ATTR_NAME = "base64";

	public JawrBase64AttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Attr getAttr() {
		return Attr.BASE64;
	}
}