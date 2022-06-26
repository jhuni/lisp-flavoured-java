package org.lfj.lang.op;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.Form;
import org.lfj.lang.LocalVariable;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is the iinc operation in the instruction set. 
 */
public class IncrementationProcedure extends Procedure {
	/**
	 * The local variable to be modified.
	 */
	public LocalVariable var;
	
	/**
	 * The amount to increment the local variable by.
	 */
	public int factor;
	
	public IncrementationProcedure(LocalVariable v, int f) {
		var = v;
		factor = f;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		mv.visitIincInsn(var.id, factor);
	}
	
}
