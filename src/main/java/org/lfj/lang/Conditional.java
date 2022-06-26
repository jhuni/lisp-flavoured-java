package org.lfj.lang;

import org.objectweb.asm.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.op.BranchProcedure;
import org.lfj.lang.op.LogicalFunction;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is an implementation of the if expression.
 */
public class Conditional extends Applicative {

	public void apply(GeneratorAdapter mv, Form[] params) {
		var elseIf = new Label();
		var endIf = new Label();
		
		var condition = params[0];
		var thenForm = params[1];
		// Only use iftrue to do a if then go procedure
		if(thenForm instanceof CompoundForm 
				&& ((CompoundForm) thenForm).combiner instanceof BranchProcedure
				&& params.length == 2) {
			var label = ((BranchProcedure) ((CompoundForm) thenForm).combiner).target;
			LogicalFunction.iftrue(mv,condition,label);
			return;
		}
		
		condition.accept(mv);
		mv.visitJumpInsn(Opcodes.IFEQ, elseIf);
		thenForm.accept(mv);
		if(params.length >= 3) {
			mv.goTo(endIf);
		}
		mv.visitLabel(elseIf);
		if(params.length >= 3) {
			params[2].accept(mv);
			mv.visitLabel(endIf);
		};
	}

	@Override
	public Type returnType(Form[] params) {
		if(params.length <= 1) {
			return VOID_TYPE;
		}
		return params[1].getType();
	}

}
