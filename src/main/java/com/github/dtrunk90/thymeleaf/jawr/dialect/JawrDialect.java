package com.github.dtrunk90.thymeleaf.jawr.dialect;

import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import com.github.dtrunk90.thymeleaf.jawr.processor.element.impl.JawrBinaryAttributeTagProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.element.impl.JawrCssAttributeTagProcessor;
import com.github.dtrunk90.thymeleaf.jawr.processor.element.impl.JawrJsAttributeTagProcessor;

public class JawrDialect extends AbstractProcessorDialect {
	public static final String NAME = "Jawr";
	public static final String PREFIX = "jawr";
	public static final int PROCESSOR_PRECEDENCE = 2000;

	public JawrDialect() {
		super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
		processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
		processors.add(new JawrBinaryAttributeTagProcessor("img"));
		processors.add(new JawrBinaryAttributeTagProcessor("input"));
		processors.add(new JawrCssAttributeTagProcessor());
		processors.add(new JawrJsAttributeTagProcessor());
		return processors;
	}
}