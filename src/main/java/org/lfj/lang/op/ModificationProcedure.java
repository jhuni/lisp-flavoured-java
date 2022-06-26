package org.lfj.lang.op;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.CompoundForm;
import org.lfj.lang.Form;
import org.lfj.lang.Place;
import org.lfj.lang.Variable;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This sets some place form to some value which is passed as an argument.
 */
public class ModificationProcedure extends Procedure {
	/**
	 * The symbolic names of this class of applicative.
	 */
	public static String[] sym = new String[] {"setf"};
	
	/**
	 * The place form to be modified.
	 */
	public Form var;
	
	/**
	 * Construct a modification procedure for some place form. The value it is to be set
	 * to is an operand which will be passed on the stack.
	 */
	public ModificationProcedure(Form v) {
		var = v;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		Form target = params[0];
		
		if(var instanceof Variable) {
			((Variable) var).store(mv, target);
		} else if(var instanceof CompoundForm) {
			CompoundForm compoundVar = ((CompoundForm) var);
			if(compoundVar.combiner instanceof Place) {
				((Place) compoundVar.combiner).setf(mv, compoundVar.params, target);
			}
		}
	}
	
}
