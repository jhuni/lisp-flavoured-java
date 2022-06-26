package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Any form that isn't an atomic expression. Compound forms consist of a combiner
 * as well as an array of forms passed into the combiner. In Lisp this is expressed as
 * (combiner form1 form2 ...) and so on.
 */
public class CompoundForm extends Form {
	public Form[] params;
	public Applicative combiner;
	
	public CompoundForm(Applicative c, Form[] p) {
		params = p;
		combiner = c;
	}
	
	public Type getType() { 
		return combiner.returnType(params); 
	}
	
	public void accept(GeneratorAdapter mv) {
		combiner.apply(mv,params);
	}
}
