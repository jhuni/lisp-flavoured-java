package org.lfj.lang.op;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;

/**
 * A class of applicatives that do not return anything onto the stack.
 */
public abstract class Procedure extends ExpressionApplicative {
	public Type returnType(Form[] params) {
		return VOID_TYPE;
	}
}
