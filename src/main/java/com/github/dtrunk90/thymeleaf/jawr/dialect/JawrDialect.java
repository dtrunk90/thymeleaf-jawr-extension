package com.github.dtrunk90.thymeleaf.jawr.dialect;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import com.github.dtrunk90.thymeleaf.jawr.expression.JawrExpressionObjectFactory;
import com.github.dtrunk90.thymeleaf.jawr.processor.element.impl.JawrCssAttributeTagProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.element.impl.JawrJsAttributeTagProcessor;

public class JawrDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {
	public static final String NAMESPACE = "http://jawr.java.net";
	public static final String PREFIX = "jawr";

	public static final int PROCESSOR_PRECEDENCE = 2000;

	public JawrDialect() {
		super(NAMESPACE, PREFIX, PROCESSOR_PRECEDENCE);
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new JawrExpressionObjectFactory();
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		return Collections.unmodifiableSet(new LinkedHashSet<IProcessor>(Arrays.asList(
				new IProcessor[] {
						new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix),
						new JawrCssAttributeTagProcessor(),
						new JawrJsAttributeTagProcessor()
				}
				)));
	}
}