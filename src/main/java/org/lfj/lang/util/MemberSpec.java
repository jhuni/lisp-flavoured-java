package org.lfj.lang.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Type;

/**
 * Member specs generalize the properties of both fields and methods.
 * Fields are distinguished from methods by a flag and a lack of parameters.
 * This defines the modifiers, name, and type data of the class member but not
 * other attributes like the implementation.
 */
public class MemberSpec {
	/**
	 * This is marked true if this class member is a field.
	 */
	public boolean isField = true;
	
	/**
	 * This is the access modifiers of the field or method.
	 */
	public int modifiers;
	
	/**
	 * The name of the field or method.
	 */
	public String name;
	
	/**
	 * The type of a field or the return type of a method.
	 */
	public Type type;
	
	/**
	 * This is empty for a field or it consists of all the parameter types passed to the method.
	 * This does not include the this parameter passed onto the stack for instance methods.
	 */
	public Type[] properParamTypes = new Type[]{};
	
	/**
	 * This a constructor for field specs.
	 */
	public MemberSpec(int modifiers, String name, Type type) {
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
	}
	
	/**
	 * This a constructor for method specs.
	 */
	public MemberSpec(int modifiers, String name, Type type,  Type[] properParamTypes) {
		this.isField = false;
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
		this.properParamTypes = properParamTypes;
	}
	
	/**
	 * Get a member spec from a java reflection field by setting up its appropriate properties.
	 */
	public MemberSpec(Field field) {
		this(field.getModifiers(),field.getName(),Type.getType(field.getType()));
	}
	
	/**
	 * Get a member spec from a java reflection method by setting up its appropriate properties.
	 */
	public MemberSpec(Method method) {
		this(method.getModifiers(),method.getName(),Type.getType(method.getReturnType()), toTypeArray(method.getParameterTypes()));
	}
	
	/**
	 * Get a member spec from a java reflection constructor by setting up its appropriate properties.
	 */
	public MemberSpec(Constructor constructor) {
		this(constructor.getModifiers(), "<init>", Type.getType(Void.class), toTypeArray(constructor.getParameterTypes()));
	}
	
	/**
	 * This return true if this member spec is a static field.
	 */
	public boolean isStaticField() {
		return isField && Modifier.isStatic(modifiers);
	}
	
	/**
	 * This return true if this member spec is a static field.
	 */
	public boolean isInstanceField() {
		return isField && !Modifier.isStatic(modifiers);
	}
	
	/**
	 * This returns true if this member spec is a static method.
	 */
	public boolean isStaticMethod() {
		return !isField && Modifier.isStatic(modifiers);
	}
	
	/**
	 * This returns ture if this member spec is an instance method.
	 */
	public boolean isInstanceMethod() {
		return !isField && !Modifier.isStatic(modifiers);
	}
	
	private static Type[] toTypeArray(Class<?>[] paramTypes) {
		Type[] types = new Type[paramTypes.length];
		for(var i = 0; i < paramTypes.length; i++) types[i] = Type.getType(paramTypes[i]);
		return types;
	}
	
}
