package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * 
 * A statement applicative for combining statements together.
 *
 */
public class Do extends Applicative {	
	
	/**
	 * Apply the applicative to the method visitor with the parameters.
	 * 
	 * @param the method visitor to apply this to
	 * @param the parameters of the applicative
	 */
	public void apply(GeneratorAdapter mv, Form[] params) {
		
		for(var i = 0; i < params.length; i++) {
			params[i].accept(mv);
		}
		
	}
	
	/**
	 * Return the type of the last form parameter.
	 * 
	 * @param the parameters of the applicative
	 */
	public Type returnType(Form[] params) {
		if(params.length == 0) {
			return VOID_TYPE;
		}
		return params[params.length - 1].getType();
	}
	
}
