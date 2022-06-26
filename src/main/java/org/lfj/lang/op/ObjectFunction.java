package org.lfj.lang.op;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * An object function requires a type argument to its instruction definition, then it
 * puts its arguments onto the stack in order to operate on them. 
 */
public class ObjectFunction extends ExpressionApplicative {
	/**
	 * The valid symbolic names of this function.
	 */
	public static String[] sym = new String[] {"new", "newarray", "multianewarray", "instanceof", "checkcast"};
	
	/**
	 * The symbolic name of this function.
	 */
	String name;
	
	/**
	 * The type of this object function. The distinguishing aspect of object functions is that they
	 * have some type passed to them that determines what effect they will have.
	 */
	Type type;
	
	/**
	 * Construct a named object function by determining its object type.
	 * 
	 * @param currentName the current name
	 * @param currentType the current type of the object function
	 */
	public ObjectFunction(String currentName, Type currentType) {
		name = currentName;
		type = currentType;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv,params);
		
		switch(name) {
		case "new":
			mv.newInstance(type);
			break;
		case "newarray":
			mv.newArray(type);
			break;
		case "multianewarray":
			mv.visitMultiANewArrayInsn(type.getDescriptor(),params.length);
			break;
		case "instanceof":
			mv.instanceOf(type);
			break;
		case "checkcast":
			mv.checkCast(type);
			break;
		}
	}
	
	public Type returnType(Form[] params) {
		Type currentType = VOID_TYPE;
		
		switch(name) {
		case "new":
			return type;
		case "newarray":
			return Type.getType("[" + type.getDescriptor());
		case "multianewarray":
			return type;
		case "checkcast":
			return params[0].getType();
		case "instanceof":
			return INT_TYPE;
		}
		
		return currentType;
	}
	
}
