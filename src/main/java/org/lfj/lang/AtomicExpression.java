package org.lfj.lang;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * An atomic expression in the sense of Lisp, which is anything not contained
 * within the paranthesis.
 */
public abstract class AtomicExpression extends Form {

	public boolean isExpression() {
		return true;
	}

}
