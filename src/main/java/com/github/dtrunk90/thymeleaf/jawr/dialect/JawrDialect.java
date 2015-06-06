package com.github.dtrunk90.thymeleaf.jawr.dialect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractXHTMLEnabledDialect;
import org.thymeleaf.processor.IProcessor;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrAlternateAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrCssElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrDisplayAlternateAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrHrefAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrMediaAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.css.JawrTitleAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.img.JawrBase64AttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.img.JawrImgElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js.JawrAsyncAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js.JawrDeferAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.js.JawrJsElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.shared.JawrSrcAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.shared.JawrUseRandomParamAttrProcessor;

public class JawrDialect extends AbstractXHTMLEnabledDialect {
	public static final String REQUEST_ATTR_NAME = JawrDialect.class.getName();
	public static final String PREFIX = "jawr";

	@Override
	public String getPrefix() {
		return PREFIX;
	}

	@Override
	public Set<IProcessor> getProcessors() {
		return new HashSet<IProcessor>(Arrays.asList(
				/* Shared attributes */
				new JawrSrcAttrProcessor(),
				new JawrUseRandomParamAttrProcessor(),

				/* Javascript attributes */
				new JawrAsyncAttrProcessor(),
				new JawrDeferAttrProcessor(),
				new JawrJsElementSubstitutionAttrProcessor("script"),

				/* CSS attributes */
				new JawrAlternateAttrProcessor(),
				new JawrDisplayAlternateAttrProcessor(),
				new JawrHrefAttrProcessor(),
				new JawrMediaAttrProcessor(),
				new JawrTitleAttrProcessor(),
				new JawrCssElementSubstitutionAttrProcessor("link"),

				/* Image attributes */
				new JawrBase64AttrProcessor(),
				new JawrImgElementSubstitutionAttrProcessor("img"),
				new JawrImgElementSubstitutionAttrProcessor("input")));
	}
}