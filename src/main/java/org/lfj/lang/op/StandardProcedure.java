package org.lfj.lang.op;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is a container class for miscellaneous procedures of the java virtual machine which are
 * not defined elsewhere.
 */
public class StandardProcedure extends Procedure {
	/**
	 * The valid names of this procedure.
	 */
	public static String[] sym = new String[] {"nop", "pop", "astore", "return", "athrow", "monitorenter", "monitorexit"};
	
	/**
	 * The current name of this procedure instance.
	 */
	public String name;
	
	/**
	 * Construct a standard procedure with some name.
	 * 
	 * @param currentName the current name of this standard procedure.
	 */
	public StandardProcedure(String currentName) {
		name = currentName;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv,params);
		
		switch(name) {
		case "nop":
			mv.visitInsn(org.objectweb.asm.Opcodes.NOP);
			break;
		case "pop":
			var firstParamForm = params[0];
			if(firstParamForm.getType().getSize() == 1) {
				mv.pop();
			} else {
				mv.pop2();
			}
			
			break;
		case "astore":
			mv.arrayStore(getComponentType(params));
			break;
		case "return":
			mv.returnValue();
			break;
		case "athrow":
			mv.throwException();
			break;
		case "monitorenter":
			mv.monitorEnter();
			break;
		case "monitorexit":
			mv.monitorExit();
			break;
		}		
	}
	
	private static Type getComponentType(Form[] params) {
		String desc = params[0].getType().getDescriptor();
		return Type.getType(desc.substring(1,desc.length()));
	}
}
