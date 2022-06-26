package org.lfj.lang;
import org.objectweb.asm.Type;

import clojure.lang.Symbol;

/**
 * This declares a variable. It is an abstraction of the Java var statement, and it performs
 * local variable type inference, so that this language can do type inference in the same manner
 * as the Java programming language.
 */
public class VariableDeclaration extends Combiner {
	public Form combine(Object[] params, Runtime rt) throws Exception {
		Object variableName = params[0];
		Object variableValue = params[1];
		
		rt.res.declareLocal(
				variableName.toString(),
				rt.fm(variableValue).getType());
		
		return (new Setf()).combine(new Form[] {rt.res.valueEnvironment.get(variableName.toString()), rt.fm(variableValue)});
	}
}
