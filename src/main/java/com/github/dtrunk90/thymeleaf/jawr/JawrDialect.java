package com.github.dtrunk90.thymeleaf.jawr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractXHTMLEnabledDialect;
import org.thymeleaf.doctype.translation.IDocTypeTranslation;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;

/**
 * Jawr dialect class that defines it's prefix as {@code "jawr"} and uses tag
 * removing versions of attribute processors.
 *
 * @author Miloš Milivojević
 * @author Danny Trunk
 *
 */
public class JawrDialect extends AbstractXHTMLEnabledDialect {
	public static final String PREFIX = "jawr";

	@Override
	public Set<IDocTypeTranslation> getDocTypeTranslations() {
		return SpringStandardDialect.SPRING4_DOC_TYPE_TRANSLATIONS;
	}

	@Override
	public Set<IProcessor> getProcessors() {
		return new HashSet<IProcessor>(Arrays.asList(
				new JawrTagRemovingAttrProcessor(new JawrScriptAttrProcessor()),
				new JawrTagRemovingAttrProcessor(new JawrStyleAttrProcessor())));
	}

	public String getPrefix() {
		return PREFIX;
	}
}