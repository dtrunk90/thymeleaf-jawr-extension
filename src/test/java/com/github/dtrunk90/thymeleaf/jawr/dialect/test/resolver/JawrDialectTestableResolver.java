package com.github.dtrunk90.thymeleaf.jawr.dialect.test.resolver;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.thymeleaf.testing.templateengine.exception.TestEngineExecutionException;
import org.thymeleaf.testing.templateengine.resource.ITestResource;
import org.thymeleaf.testing.templateengine.resource.ITestResourceResolver;
import org.thymeleaf.testing.templateengine.standard.resolver.StandardTestableResolver;
import org.thymeleaf.testing.templateengine.standard.test.StandardTest;
import org.thymeleaf.testing.templateengine.standard.test.StandardTestValueType;
import org.thymeleaf.testing.templateengine.standard.test.builder.IStandardTestBuilder;
import org.thymeleaf.testing.templateengine.standard.test.builder.StandardTestBuilder;
import org.thymeleaf.testing.templateengine.standard.test.data.StandardTestEvaluatedData;
import org.thymeleaf.testing.templateengine.standard.test.data.StandardTestEvaluatedField;
import org.thymeleaf.testing.templateengine.standard.test.data.StandardTestFieldNaming;
import org.thymeleaf.testing.templateengine.standard.test.evaluator.IStandardTestEvaluator;
import org.thymeleaf.testing.templateengine.standard.test.evaluator.StandardTestEvaluator;
import org.thymeleaf.testing.templateengine.standard.test.evaluator.field.IStandardTestFieldEvaluator;
import org.thymeleaf.testing.templateengine.standard.test.evaluator.field.StandardTestFieldEvaluationSpec;
import org.thymeleaf.testing.templateengine.standard.test.evaluator.field.defaultevaluators.AbstractStandardTestFieldEvaluator;
import org.thymeleaf.testing.templateengine.testable.ITest;
import org.thymeleaf.util.Validate;

public class JawrDialectTestableResolver extends StandardTestableResolver {
	private static final String FIELD_NAME = "JAWR";

	public static class JawrProperties {
		private final Properties properties = new Properties();

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			Validate.notNull(properties, "Properties cannot be null");
			this.properties.clear();
			this.properties.putAll(properties);
		}

		public JawrProperties aggregate(JawrProperties properties) {
			JawrProperties newProperties = new JawrProperties();
			newProperties.setProperties(this.properties);

			if (properties == null) {
				return newProperties;
			}

			newProperties.properties.putAll(properties.properties);

			return newProperties;
		}
	}

	public static class JawrTest extends StandardTest {
		private JawrProperties jawrProperties = new JawrProperties();
		private StandardTestValueType jawrPropertiesValueType = StandardTestValueType.NO_VALUE;

		public JawrProperties getJawrProperties() {
			return jawrProperties;
		}

		public void setJawrProperties(JawrProperties jawrProperties, StandardTestValueType valueType) {
			Validate.notNull(jawrProperties, "JawrProperties cannot be null");
			Validate.notNull(valueType, "Value type cannot be null");
			this.jawrProperties = jawrProperties;
			jawrPropertiesValueType = valueType;
		}

		public StandardTestValueType getJawrPropertiesValueType() {
			return jawrPropertiesValueType;
		}
	}

	@Override
	public IStandardTestEvaluator getTestEvaluator() {
		return new StandardTestEvaluator() {
			@Override
			protected Set<StandardTestFieldEvaluationSpec> getFieldSpecSet() {
				IStandardTestFieldEvaluator jawrStandardTestFieldEvaluator = new AbstractStandardTestFieldEvaluator(JawrProperties.class) {
					@Override
					protected StandardTestEvaluatedField getValue(String executionId, ITestResource resource, ITestResourceResolver testResourceResolver, String fieldName, String fieldQualifier, String fieldValue) {
						if (fieldValue == null || fieldValue.trim().equals("")) {
							return StandardTestEvaluatedField.forDefaultValue(new JawrProperties());
						}

						Properties properties = new Properties();

						try {
							byte[] valueAsBytes = fieldValue.getBytes("ISO-8859-1");
							ByteArrayInputStream inputStream = new ByteArrayInputStream(valueAsBytes);
							properties.load(inputStream);
						} catch (Throwable t) {
							throw new TestEngineExecutionException("Error while reading context specification", t);
						}

						JawrProperties jawrTestContext = new JawrProperties();
						jawrTestContext.setProperties(properties);

						return StandardTestEvaluatedField.forSpecifiedValue(jawrTestContext);
					}
				};

				Set<StandardTestFieldEvaluationSpec> fieldSpecSet = new HashSet<StandardTestFieldEvaluationSpec>(super.getFieldSpecSet());
				fieldSpecSet.add(new StandardTestFieldEvaluationSpec(FIELD_NAME, jawrStandardTestFieldEvaluator));
				return fieldSpecSet;
			}
		};
	}

	@Override
	public IStandardTestBuilder getTestBuilder() {
		return new StandardTestBuilder() {
			@Override
			protected void additionalInitialization(StandardTest test, ITest parentTest, StandardTestEvaluatedData data) {
				JawrTest jawrTest = (JawrTest) test;

				StandardTestEvaluatedField jawr = null;
				Map<String,StandardTestEvaluatedField> valuesByQualifierForField = data.getValuesByQualifierForField(FIELD_NAME);
				if (valuesByQualifierForField != null) {
					jawr = valuesByQualifierForField.get(StandardTestFieldNaming.FIELD_QUALIFIER_MAIN);
				}

				if (jawr != null) {
					jawrTest.setJawrProperties((JawrProperties) jawr.getValue(), jawr.getValueType());
				}

				if (parentTest != null) {
					JawrTest jawrParentTest = parentTest instanceof JawrTest ? (JawrTest) parentTest : null;
					JawrProperties parentJawrProperties = jawrParentTest != null ? jawrParentTest.getJawrProperties() : null;
					JawrProperties newJawrProperties = parentJawrProperties != null ? parentJawrProperties.aggregate(jawrTest.getJawrProperties()) : jawrTest.getJawrProperties();
					jawrTest.setJawrProperties(newJawrProperties, StandardTestValueType.SPECIFIED);
				}
			}

			@Override
			protected StandardTest createTestInstance() {
				return new JawrTest();
			}
		};
	}
}