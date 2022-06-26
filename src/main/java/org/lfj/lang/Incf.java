package org.lfj.lang;

import org.lfj.lang.op.IncrementationProcedure;
import org.lfj.lang.op.MathematicalFunction;
import org.lfj.lang.op.ModificationProcedure;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.Arrays;

/**
 * This is equivalent to the incf function in common lisp. This incf function is defined
 * in such a way that iinc is compiled when necessary. 
 */
public class Incf extends Quasiapplicative {

	public Form combine(Form[] params) {
		var placeForm = params[0];
		var incrementForm = params[1];
		
		if(placeForm instanceof LocalVariable &&
				((LocalVariable) placeForm).type == INT_TYPE &&
				incrementForm instanceof Constant) {
			return new IncrementationProcedure(
					((LocalVariable) placeForm),
					((Number) ((Constant) incrementForm).val).intValue()).combine(new Form[] {});
		}
		
		return (new ModificationProcedure(placeForm)).combine(
				new Form[] {new MathematicalFunction("+").combine(
						new Form[] {placeForm, incrementForm})});
		
	}	

}
