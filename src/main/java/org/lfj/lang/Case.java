package org.lfj.lang;

import org.objectweb.asm.Type;
import org.lfj.lang.op.LogicalFunction;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;

import java.util.Arrays;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * The case expression is a Lisp equivalent of the switch statement.
 */
public class Case extends Applicative {

	public void apply(GeneratorAdapter mv, Form[] args) {
	
		Form expr = args[0];
		Form[] params = Arrays.copyOfRange(args,1,args.length);
		
		var endLabel = new Label();
		
		for(var i = 0; (i+1) < params.length; i += 2) {
			var testForm = params[i];
			var thenForm = params[i+1];
			
			var elseLabel = new Label();
			new CompoundForm(new LogicalFunction("="),
					new Form[] {testForm, expr}).accept(mv);
			mv.visitJumpInsn(Opcodes.IFNE, elseLabel);
			thenForm.accept(mv);
			mv.pop();
			if((i+2) < params.length) {
				mv.visitJumpInsn(Opcodes.GOTO, endLabel);
			}
			mv.visitLabel(elseLabel);
		}
		
		mv.visitLabel(endLabel);
		
	}

	@Override
	public Type returnType(Form[] params) {
		if(params.length <= 2) {
			return VOID_TYPE;
		}
		return params[2].getType();
	}

}
