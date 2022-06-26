package org.lfj.lang;

import org.lfj.lang.op.ModificationProcedure;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.Arrays;

/**
 * This is based upon the Common Lisp setf function.
 */
public class Setf extends Quasiapplicative {

	public Form combine(Form[] params) {
		var restParams = Arrays.copyOfRange(params,1,params.length);
		var firstParam = params[0];
		return new CompoundForm(new ModificationProcedure(firstParam), restParams);
	}	

}
