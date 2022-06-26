package org.lfj.lang.op;

import org.objectweb.asm.commons.Method;

import clojure.asm.Opcodes;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import static org.objectweb.asm.commons.GeneratorAdapter.*;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;

/**
 * These are the applicatives which are defined by programs in the java virtual machine
 * rather then ones that are defined by the java virtual machine instruction set itself.
 * This includes all static and instance methods. 
 */
public class DefinedApplicative extends ExpressionApplicative {
	/**
	 * The name of the type of operation being used here.
	 */
	public String accessType;
	
	/**
	 * The owner of this particular applicative.
	 */
	public Type ownerType;
	
	/**
	 * The method class used to determine this applicative.
	 */
	public Method currentMethod;
	
	/**
	 * Construct a defined applicative which uses the appropriate invocation method.
	 * 
	 * @param currentAccessType
	 * @param currentOwnerType
	 * @param thisMethod
	 */
	public DefinedApplicative(String currentAccessType, Type currentOwnerType, Method thisMethod) {
		accessType = currentAccessType;
		ownerType = currentOwnerType;
		currentMethod = thisMethod;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv, params);
		
		switch(accessType) {
		case "invokestatic":
			mv.invokeStatic(ownerType,currentMethod);
			break;
		case "invokevirtual":
			mv.invokeVirtual(ownerType,currentMethod);
			break;
		case "invokeinterface":
			mv.invokeInterface(ownerType,currentMethod);
			break;
		case "invokespecial":
			String owner = ownerType.getSort() == Type.ARRAY ? ownerType.getDescriptor() : ownerType.getInternalName();
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, owner, currentMethod.getName(), currentMethod.getDescriptor(), false);
		}
		
	}
	
	public Type returnType(Form[] params) {
		return currentMethod.getReturnType();
	}
	
}
