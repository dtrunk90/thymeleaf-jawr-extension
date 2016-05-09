package com.github.dtrunk90.thymeleaf.jawr.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

public class JawrExpressionObjectFactory implements IExpressionObjectFactory {
	@Override
	public Set<String> getAllExpressionObjectNames() {
		return Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(
				new String[] {
						JawrDialect.PREFIX
				}
		)));
	}

	@Override
	public Object buildObject(IExpressionContext context, String expressionObjectName) {
		if (JawrDialect.PREFIX.equals(expressionObjectName)) {
			if (context instanceof IWebContext) {
				return new Jawr((IWebContext) context);
			}
		}

		return null;
	}

	@Override
	public boolean isCacheable(String expressionObjectName) {
		return true;
	}
}