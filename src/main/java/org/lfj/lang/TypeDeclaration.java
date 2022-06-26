package org.lfj.lang;
import org.objectweb.asm.Type;

import clojure.lang.Symbol;

/**
 * Declare the static type of a symbol defining a variable and inform the resolver of this.
 */
public class TypeDeclaration extends Combiner {
	public Form combine(Object[] params, Runtime rt) throws Exception {
		Object valueType = params[0];
		Object valueName = params[1];
		
		rt.res.declareLocal(
				valueName.toString(),
				Type.getType((Class<?>) ((Constant) rt.fm(valueType)).val));
		
		return VoidForm.instance;
	}
}
