package org.lfj.lang;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Perform a cast of an expression to some other type, well checking if that cast is possible.
 * This is distinguished from the cast instructions.
 */
public class Cast extends Applicative {

	public void apply(GeneratorAdapter mv, Form[] params) {
		params[1].accept(mv);
		mv.checkCast(this.returnType(params));
	}

	public Type returnType(Form[] params) {
		Class <?> valueClass = (Class<?>) ((Constant) params[0]).val;
		return Type.getType(valueClass);
	}

}
