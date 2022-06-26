package org.lfj.lang;

import org.lfj.lang.op.StandardProcedure;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * An applicative directly evaluates all of its arguments.
 */
public abstract class Applicative extends Quasiapplicative {
	public abstract void apply(GeneratorAdapter mv, Form[] params);
	public abstract Type returnType(Form[] params);
	
	public Form combine(Form[] params) {
		return new CompoundForm(this, params);
	}
}
