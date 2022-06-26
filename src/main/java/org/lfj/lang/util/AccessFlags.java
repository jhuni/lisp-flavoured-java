package org.lfj.lang.util;

import java.util.Hashtable;
import static org.objectweb.asm.Opcodes.*;
import clojure.lang.Symbol;

/**
 * A static helper class for dealing with named access flags.
 */
public class AccessFlags {
	/**
	 * A static field containing the name of the access flags of a class.
	 */
	public static Hashtable<String, Integer> accessFlagNames = new Hashtable<String,Integer>();
	
	static {
		accessFlagNames.put("public", ACC_PUBLIC);
		accessFlagNames.put("private", ACC_PRIVATE);
		accessFlagNames.put("protected", ACC_PROTECTED);
		accessFlagNames.put("static", ACC_STATIC);
		
		accessFlagNames.put("final", ACC_FINAL);
		accessFlagNames.put("super", ACC_SUPER);
		accessFlagNames.put("synchronized", ACC_SYNCHRONIZED);
		accessFlagNames.put("volatile", ACC_VOLATILE);
		accessFlagNames.put("bridge", ACC_BRIDGE);
		accessFlagNames.put("varargs", ACC_VARARGS);
		accessFlagNames.put("transient", ACC_TRANSIENT);
		
		accessFlagNames.put("native", ACC_NATIVE);
		accessFlagNames.put("interface", ACC_INTERFACE);
		accessFlagNames.put("abstract", ACC_ABSTRACT);
		accessFlagNames.put("strict", ACC_STRICT);
		
		accessFlagNames.put("synthetic", ACC_SYNTHETIC);
		accessFlagNames.put("annotation", ACC_ANNOTATION);
		accessFlagNames.put("enum", ACC_ENUM);
		accessFlagNames.put("mandated", ACC_MANDATED);
	}
	
	/**
	 * Get the modifier value for a collection of access flag names.
	 * 
	 * @param coll the collection of objects whose string representations are accessflags 
	 */
	public static int getAccessFlag(Object[] coll) {
		int rval = 0;
		
		for(Object i : coll) rval |= accessFlagNames.get(i.toString());
		
		return rval;
	}
	
	/**
	 * This returns true if the object is both a symbol and its an access flag name.
	 * 
	 * @param obj an object to be tested 
	 */
	public static boolean isAccessSymbol(Object obj) {
		return obj instanceof Symbol && accessFlagNames.containsKey(obj.toString());
	}
	
	private static boolean isMemberString(String[] coll, String val) {
		for(String s : coll) {
			if(s.equals(val)) {
				return true;
			}
		}
		return false;
	}

}
