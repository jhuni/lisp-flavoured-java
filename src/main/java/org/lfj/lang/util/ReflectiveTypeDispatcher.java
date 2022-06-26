package org.lfj.lang.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import org.objectweb.asm.Type;

/**
 * This dispatches member specs based upon reflection upon a given class which has already been loaded.
 * This can be achieved by using the java reflection library and going through the fields and methods
 * defined by the class.
 */
public class ReflectiveTypeDispatcher extends TypeDispatcher {
	/**
	 * The owner class that reflection is done upon to dispatch member specs.
	 */
	Class<?> owner;

	/**
	 * Construct the reflective type dispatcher based upon the owner.
	 */
	public ReflectiveTypeDispatcher(Class<?> owner) {
		this.owner = owner;
	}
	
	public MemberSpec getFieldSpec(String name) {
		return new MemberSpec(getMatchingFields(owner,name).get(0));
	}
	
	public MemberSpec getStaticFieldSpec(String name) {
		return new MemberSpec(getMatchingStaticFields(owner,name).get(0));
	}
	
	public MemberSpec getMethodReturnSpec(String name, Class<?>[] properParamTypes) {
		if(name.equals("<init>")) {
			return new MemberSpec(getMatchingConstructors(owner,properParamTypes).get(0));
		}
		return new MemberSpec(getMatchingMethods(owner,name,properParamTypes).get(0));
	}
	
	public MemberSpec getStaticMethodReturnSpec(String name, Class<?>[] properParamTypes) {
		return new MemberSpec(getMatchingStaticMethods(owner,name,properParamTypes).get(0));
	}
	
	public Type getStaticFieldType(String name) {
		return Type.getType((getMatchingStaticFields(owner,name)).get(0).getType());
	}
	
	public Type getFieldType(String name) {
		return Type.getType(getMatchingFields(owner,name).get(0).getType());
	}
	
	public Type getMethodReturnType(String name, Class<?>[] properParamTypes) {
		if(name.equals("<init>")) {
			return Type.getType(void.class);
		}
		return Type.getType(getMatchingMethods(owner,name,properParamTypes).get(0).getReturnType());
	}
	
	public Type getStaticMethodReturnType(String name, Class<?>[] properParamTypes) {
		return Type.getType(getMatchingStaticMethods(owner,name,properParamTypes).get(0).getReturnType());		
	}
	
	private static ArrayList<Field> getMatchingStaticFields(Class<?> owner, String name) {
		Field[] fields = owner.getFields();
		ArrayList<Field> matchingFields = new ArrayList<Field>();

		for(Field currentField : fields) {
			if(currentField.getName().equals(name) && Modifier.isStatic(currentField.getModifiers())) {
				matchingFields.add(currentField);
			}
		}
		
		return matchingFields;
	}

	private static ArrayList<Field> getMatchingFields(Class<?> owner, String name) {
		Field[] fields = owner.getFields();
		ArrayList<Field> matchingFields = new ArrayList<Field>();
		
		for(Field currentField : fields) {
			if(currentField.getName().equals(name) && !(Modifier.isStatic(currentField.getModifiers()))) {
				matchingFields.add(currentField);
			}
		}
		
		return matchingFields;
	}
	
	private static ArrayList<Method> getMatchingStaticMethods(Class<?> owner, String name, Class<?>[] paramTypes) {
		Method[] methods = owner.getMethods();
		ArrayList<Method> matchingMethods = new ArrayList<Method>();
		
		for(Method currentMethod : methods) {
			if(currentMethod.getName().equals(name) 
					&& Modifier.isStatic(currentMethod.getModifiers())
					&& Arrays.deepEquals(currentMethod.getParameterTypes(), paramTypes)) {
				matchingMethods.add(currentMethod);
			}
		}
		
		return matchingMethods;
	}
	
	private static ArrayList<Method> getMatchingMethods(Class<?> owner, String name, Class<?>[] paramTypes) {
		Method[] methods = owner.getMethods();
		ArrayList<Method> matchingMethods = new ArrayList<Method>();
		
		for(Method currentMethod : methods) {
			if(currentMethod.getName().equals(name) 
					&& !(Modifier.isStatic(currentMethod.getModifiers()))
					&& Arrays.deepEquals(currentMethod.getParameterTypes(), paramTypes)) {
				matchingMethods.add(currentMethod);
			}
		}
		
		return matchingMethods;
		
	}
	
	private static ArrayList<Constructor<?>> getMatchingConstructors(Class<?> owner, Class<?>[] paramTypes) {
		Constructor<?>[] constructors = owner.getConstructors();
		ArrayList<Constructor<?>> matchingConstructors = new ArrayList<Constructor<?>>();
		
		for(Constructor<?> currentConstructor : constructors) {
			if(Arrays.deepEquals(currentConstructor.getParameterTypes(), paramTypes)) {
				matchingConstructors.add(currentConstructor);
			}
		}
		
		return matchingConstructors;
	}
	
}
