package com.github.dtrunk90.thymeleaf.jawr.dialect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractXHTMLEnabledDialect;
import org.thymeleaf.processor.IProcessor;

import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrAlternateAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrAsyncAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrCSSElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrDeferAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrDisplayAlternateAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrHrefAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrJavascriptElementSubstitutionAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrMediaAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrSrcAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrTitleAttrProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.attr.impl.JawrUseRandomParamAttrProcessor;

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
				/* Common attributes */
				new JawrUseRandomParamAttrProcessor(),

				/* Javascript attributes */
				new JawrSrcAttrProcessor(),
				new JawrAsyncAttrProcessor(),
				new JawrDeferAttrProcessor(),
				new JawrJavascriptElementSubstitutionAttrProcessor(),

				/* CSS attributes */
				new JawrHrefAttrProcessor(),
				new JawrMediaAttrProcessor(),
				new JawrAlternateAttrProcessor(),
				new JawrTitleAttrProcessor(),
				new JawrDisplayAlternateAttrProcessor(),
				new JawrCSSElementSubstitutionAttrProcessor()));
	}
}