package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.VOID_TYPE;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is a form that does nothing and returns no value. Its purpose is to be returned by declarations
 * that effect the runtime but which do not have any purpose as forms. It shouldn't be instantiated more
 * then one and its value should instead be accessed by the static field instance.
 */
public class VoidForm extends Form {

	public static VoidForm instance;
	
	static {
		instance = new VoidForm();
	}
	
	public Type getType() { 
		return VOID_TYPE; 
	}
	
	public void accept(GeneratorAdapter mv) {}
}
