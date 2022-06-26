package org.lfj.lang.op;

import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;

/**
 * This the array length instruction of the java virtual machine instruction set. It
 * retuns a primitive integer value.
 */
public class ArrayLength extends PrimitiveFunction {

	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv, params);
		mv.arrayLength();
	}
	
	public Type returnType(Form[] params) {
		return INT_TYPE;
	}
	
}
