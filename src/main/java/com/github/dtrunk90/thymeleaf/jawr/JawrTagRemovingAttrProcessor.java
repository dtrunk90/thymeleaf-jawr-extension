package com.github.dtrunk90.thymeleaf.jawr;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

/**
 * A wrapper class for Jawr processors that removes the attribute's enclosing
 * tag after using the provided Jawr processor to render the tag's content
 *
 * @author Miloš Milivojević
 * @author Danny Trunk
 *
 */
public class JawrTagRemovingAttrProcessor extends AbstractAttrProcessor {
	private final AbstractJawrAttrProcessor processor;

	public JawrTagRemovingAttrProcessor(AbstractJawrAttrProcessor processor) {
		super((IAttributeNameProcessorMatcher) processor.getMatcher());
		this.processor = processor;
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
		processor.processAttribute(arguments, element, attributeName);

		NestableNode parent = element.getParent();
		for (Node child : element.getChildren()) {
			element.removeChild(child);
			parent.insertBefore(element, child);
		}

		parent.removeChild(element);
		return ProcessorResult.OK;
	}
}