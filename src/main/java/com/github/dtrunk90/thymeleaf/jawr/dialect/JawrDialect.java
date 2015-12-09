package com.github.dtrunk90.thymeleaf.jawr.dialect;

import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
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
		//processors.add(new StandardXmlNsTagProcessor(this, TemplateMode.HTML, dialectPrefix)); // TODO: check if necessary
		processors.add(new JawrBinaryAttributeTagProcessor(this, "img"));
		processors.add(new JawrBinaryAttributeTagProcessor(this, "input"));
		processors.add(new JawrCssAttributeTagProcessor(this));
		processors.add(new JawrJsAttributeTagProcessor(this));
		return processors;
	}
}