package org.lfj.lang;

import org.objectweb.asm.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is an implementation of the Lisp cond combiner.
 */
public class CompoundConditional extends Applicative {

	public void apply(GeneratorAdapter mv, Form[] params) {
	
		var endLabel = new Label();
		
		for(var i = 0; (i+1) < params.length; i += 2) {
			var conditionForm = params[i];
			var thenForm = params[i+1];
			
			var elseLabel = new Label();
			conditionForm.accept(mv);
			mv.visitJumpInsn(Opcodes.IFNE, elseLabel);
			thenForm.accept(mv);
			if((i+2) < params.length) {
				mv.visitJumpInsn(Opcodes.GOTO, endLabel);
			}
			mv.visitLabel(elseLabel);
		}
		
		mv.visitLabel(endLabel);
		
	}

	@Override
	public Type returnType(Form[] params) {
		if(params.length <= 1) {
			return VOID_TYPE;
		}
		return params[1].getType();
	}

}
