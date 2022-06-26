package org.lfj.lang;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.Opcodes;

/**
 * These are atomic expressions generalizing local variables available in the java virtual machine.
 */
public class LocalVariable extends Variable {
	public Type type;
	public int id;
	public boolean isArg = false;
	
	public LocalVariable(int num, Type currentType) {
		id = num;
		type = currentType;
	}
	
	public Type getType() { 
		return type; 
	}
	
	public void accept(GeneratorAdapter mv) {
		mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), id);
	}
	
	public void store(GeneratorAdapter mv, Form newValue) {
		newValue.accept(mv);
		
		mv.visitVarInsn(type.getOpcode(Opcodes.ISTORE), id);
	}
}
