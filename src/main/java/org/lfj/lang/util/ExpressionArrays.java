package org.lfj.lang.util;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.Form;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * A static helper class for dealing with arrays of expressions.
 */
public class ExpressionArrays {
	/**
	 * Take an array of expression forms and generate the instructions to load them onto the stack
	 * 
	 * @param mv the generator adapter to be effected
	 * @param coll the array of expression forms
	 */
	public static void unload(GeneratorAdapter mv, Form[] coll) {
		for(var i = 0; i < coll.length; i++) {
			var currentForm = coll[i];
			if (currentForm.isStatement()) {
				mv.visitInsn(org.objectweb.asm.Opcodes.ACONST_NULL);
			} else {
				currentForm.accept(mv);
			}
		}
	}
}
