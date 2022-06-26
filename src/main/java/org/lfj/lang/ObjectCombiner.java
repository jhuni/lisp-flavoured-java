package org.lfj.lang;

import org.lfj.lang.op.ObjectFunction;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.Arrays;

/**
 * These combiners are defined by the fact that they take in some class type as an argument to them
 * but that class type is not passed onto the stack so this is equivalent to an expression applicative
 * which is defined by that class type argument which takes in the remaining arguments.
 */
public class ObjectCombiner extends Quasiapplicative {	
	public static String[] sym = new String[] {"new", "newarray", "multianewarray", "instanceof", "checkcast"};
	
	public String name;
	
	public ObjectCombiner(String currentName) {
		name = currentName;
	}
	
	public Form combine(Form[] params) {
		var restParams = Arrays.copyOfRange(params,1,params.length);
//		Type type = (Type) ((Constant) params[0]).val;
//		return new CompoundForm(new ObjectFunction(name,type), restParams);
		Class <?> valueClass = (Class<?>) ((Constant) params[0]).val;
		return new CompoundForm(new ObjectFunction(name,Type.getType(valueClass)), restParams);
	}
}
