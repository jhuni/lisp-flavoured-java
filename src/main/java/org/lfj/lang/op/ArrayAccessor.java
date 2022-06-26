package org.lfj.lang.op;

import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;
import org.lfj.lang.Place;
import org.lfj.lang.util.ExpressionArrays;

/**
 * This is like an aload instruction except it is also a place form. 
 */
public class ArrayAccessor extends ExpressionApplicative implements Place {

	private static Type getComponentType(Form[] params) {
		String desc = params[0].getType().getDescriptor();
		return Type.getType(desc.substring(1,desc.length()));
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv, params);
		mv.arrayLoad(getComponentType(params));
	}
	
	public Type returnType(Form[] params) {
		return getComponentType(params);
	}
	
	public void setf(GeneratorAdapter mv, Form[] params, Form newValue) {
		ExpressionArrays.unload(mv, params);
		newValue.accept(mv);
		mv.arrayStore(getComponentType(params));
	}
	
}
