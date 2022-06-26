package org.lfj.lang.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.objectweb.asm.Type;

/**
 * This is a manual type dispatcher which allows you to dispatch types based upon a set of declared
 * specs. This is used to do reflection upon currently being defined classes that haven't yet 
 * been loaded into memory by the compiler. 
 */
public class ManualTypeDispatcher extends TypeDispatcher {
	/**
	 * The declared specs of this type dispatcher.
	 */
	MemberSpec[] declaredSpecs;
	
	/**
	 * The fallback dispatcher to go to when nothing matches. Usually this is the reflective dispatcher
	 * produced by the super class so that inherited methods can be reflected upon.
	 */
	TypeDispatcher fallbackDispatcher;
	
	private HashSet<String> declaredNames = new HashSet<String>();
	
	/**
	 * Construct a manual type dispatcher from an array of declared specs and its fallback dispatcher
	 * to be used when there is no name matching specs among the declared specs.
	 */
	public ManualTypeDispatcher(MemberSpec[] specs, TypeDispatcher fallbackDispatcher) {
		this.declaredSpecs = specs;
		this.fallbackDispatcher = fallbackDispatcher;
		for(MemberSpec i : specs) declaredNames.add(i.name);
	}
	
	public MemberSpec getFieldSpec(String name) {
		if(!declaredNames.contains(name)) return fallbackDispatcher.getFieldSpec(name);
		
		var matchingSpecs = new ArrayList<MemberSpec>();
		for(MemberSpec i : declaredSpecs) if(i.isInstanceField() && i.name.equals(name)) matchingSpecs.add(i);
		return matchingSpecs.get(0);
	}

	public MemberSpec getStaticFieldSpec(String name) {
		if(!declaredNames.contains(name)) return fallbackDispatcher.getStaticFieldSpec(name);
		
		var matchingSpecs = new ArrayList<MemberSpec>();
		for(MemberSpec i : declaredSpecs) if(i.isStaticField() && i.name.equals(name)) matchingSpecs.add(i);
		return matchingSpecs.get(0);
	}

	public MemberSpec getMethodReturnSpec(String name, Class<?>[] properParamTypes) {
		if(!declaredNames.contains(name)) return fallbackDispatcher.getMethodReturnSpec(name,properParamTypes);
		
		var matchingSpecs = new ArrayList<MemberSpec>();
		for(MemberSpec i : declaredSpecs) {
			if(i.isInstanceMethod() 
					&& i.name.equals(name)
					&& Arrays.deepEquals(i.properParamTypes, toTypeArray(properParamTypes))) matchingSpecs.add(i);
		}
		return matchingSpecs.get(0);
	}

	public MemberSpec getStaticMethodReturnSpec(String name, Class<?>[] properParamTypes) {
		if(!declaredNames.contains(name)) return fallbackDispatcher.getStaticMethodReturnSpec(name,properParamTypes);
		
		var matchingSpecs = new ArrayList<MemberSpec>();
		for(MemberSpec i : declaredSpecs) {
			if(i.isStaticMethod() 
					&& i.name.equals(name)
					&& Arrays.deepEquals(i.properParamTypes, toTypeArray(properParamTypes))) matchingSpecs.add(i);
		}
		return matchingSpecs.get(0);
	}
	
	private static Type[] toTypeArray(Class<?>[] paramTypes) {
		Type[] types = new Type[paramTypes.length];
		for(var i = 0; i < paramTypes.length; i++) types[i] = Type.getType(paramTypes[i]);
		return types;
	}
	
}
