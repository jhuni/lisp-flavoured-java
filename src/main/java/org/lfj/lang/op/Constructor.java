package org.lfj.lang.op;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

/**
 * This is a generalization of constructor functions in the java virtual machine.
 */
public class Constructor extends ExpressionApplicative {
	/**
	 * The class of the object being created.
	 */
	public Class<?> instanceClass;
	
	/**
	 * Create a constructor applicative from the class of the object being created.
	 * 
	 * @param currentClass the class of the object being created.
	 */
	public Constructor(Class<?> currentClass) {
		instanceClass = currentClass;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		mv.newInstance(Type.getType(instanceClass));
		mv.dup();
		ExpressionArrays.unload(mv,params);
		
		Type[] paramTypes = new Type[params.length];
		for(var i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getType();
		}
		
		mv.invokeConstructor(
				Type.getType(instanceClass),
				new Method("<init>", Type.VOID_TYPE, paramTypes));
	}
	
	public Type returnType(Form[] params) {
		return Type.getType(instanceClass);
	}
}
