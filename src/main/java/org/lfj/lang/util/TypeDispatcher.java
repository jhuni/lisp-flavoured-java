package org.lfj.lang.util;

import org.objectweb.asm.Type;

/**
 * A type dispatcher dispatches class member specs for its parameters. This can be used
 * to handle reflection based upon both classes that are loaded and classes that are
 * currently being defined. In certain cases it is necessary to get the member spec
 * and not just the type of some class member like when determining if a declared
 * method is private or not. If it is private then invokespecial can be used
 * instead of invokevirtual for example.
 */
public abstract class TypeDispatcher {
	/**
	 * Get the member spec of a field.
	 */
	public abstract MemberSpec getFieldSpec(String name);
	
	/**
	 * Get the member spec of a static field. 
	 */
	public abstract MemberSpec getStaticFieldSpec(String name);
	
	/**
	 * Get the member spec of an instance method.
	 */
	public abstract MemberSpec getMethodReturnSpec(String name, Class<?>[] properParamTypes);
	
	/**
	 * Get the member spec of a static method.
	 */
	public abstract MemberSpec getStaticMethodReturnSpec(String name, Class<?>[] properParamTypes);
	
	/**
	 * Get the type of the field spec associated with this name.
	 */
	public Type getFieldType(String name) {
		return getFieldSpec(name).type;
	}
	
	/**
	 * Get the type of the static field spec associated with this name.
	 */
	public Type getStaticFieldType(String name) {
		return getStaticFieldSpec(name).type;
	}
	
	/**
	 * Get the return type of an instance method associated with this name and these parameter types.
	 */
	public Type getMethodReturnType(String name, Class<?>[] properParamTypes) {
		return getMethodReturnSpec(name, properParamTypes).type;
	}
	
	/**
	 * Get the return type of a static method associated with this name and these parameter types.
	 */
	public Type getStaticMethodReturnType(String name, Class<?>[] properParamTypes) {
		return getStaticMethodReturnSpec(name, properParamTypes).type;
	}	
}
