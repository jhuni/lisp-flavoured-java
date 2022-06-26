package org.lfj.lang;

import org.lfj.lang.op.DefinedApplicative;
import org.lfj.lang.util.TypeDispatcher;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/**
 * Define a call to the static method and perform reflection when applied to determine the appropriate
 * return type of the static call used in the bytecode.
 */
public class StaticCall extends Quasiapplicative {
	Type ownerType;
	TypeDispatcher dispatcher;
	String methodName;
	
	public StaticCall(Type ownerType, TypeDispatcher dispatcher, String methodName) {
		this.ownerType = ownerType;
		this.dispatcher = dispatcher;
		this.methodName = methodName;
	}
	
	public Form combine(Form[] params) throws Exception {
		// Map the params array by type
		Type[] paramTypes = new Type[params.length];
		Class<?>[] paramClasses = new Class<?>[params.length];
		for(var i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getType();
			paramClasses[i] = params[i].getFormClass();
		}
		Type returnType = dispatcher.getStaticMethodReturnType(methodName,paramClasses);
		var combiner = new DefinedApplicative("invokestatic", ownerType,
				new Method(methodName, returnType, paramTypes));
		return new CompoundForm(combiner,params);
	}

}
