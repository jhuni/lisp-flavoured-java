package org.lfj.lang.util;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Type;

/**
 * A localized field name is simply a field name with an owner type.
 */
public class LocalizedFieldName {
	/**
	 * The owner of the localized field name.
	 */
	public Type owner;
	
	/**
	 * The name of the localized field name.
	 */
	public String name;
	
	/**
	 * Construct a localized field name from a type and a name.
	 */
	public LocalizedFieldName(Type currentOwner, String currentName) {
		owner = currentOwner;
		name = currentName;
	}
	
	/**
	 * Construct a localized field name from a class and a name. 
	 */
	public LocalizedFieldName(Class<?> currentOwner, String currentName) {
		owner = Type.getType(currentOwner);
		name = currentName;
	}
}
