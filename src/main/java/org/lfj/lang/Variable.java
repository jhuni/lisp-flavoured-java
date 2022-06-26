package org.lfj.lang;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This defines atomic variables which come in at least two types. The local variables
 * and the class variables defined by getstatic.
 */
public abstract class Variable extends AtomicExpression {
	public abstract void store(GeneratorAdapter mv, Form val);
}
