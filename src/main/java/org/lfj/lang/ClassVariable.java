package org.lfj.lang;

import org.lfj.lang.util.LocalizedFieldName;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is an atomic expression defined by static variables of classes. They can be expressed within
 * programs with the form Class/name.
 */
public class ClassVariable extends Variable {
	Type type;
	LocalizedFieldName id;
	
	public ClassVariable(LocalizedFieldName currentId, Type currentType) {
		type = currentType;
		id = currentId;
	}
	
	public ClassVariable(Type currentOwnerType, String currentName, Type currentType) {
		type = currentType;
		id = new LocalizedFieldName(currentOwnerType, currentName);
	}
	
	public Type getType() {
		return type;
	}
	
	public void accept(GeneratorAdapter mv) {
		mv.getStatic(id.owner, id.name, type);
	}
	
	public void store(GeneratorAdapter mv, Form newValue) {
		newValue.accept(mv);
		mv.putStatic(id.owner,id.name,type);
	}
}
