package org.lfj.lang.op;

import static org.objectweb.asm.Opcodes.*;

import org.lfj.lang.Form;
import org.objectweb.asm.Label;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is equivalent to a goto instruction in the instruction set.
 */
public class BranchProcedure extends Procedure {
	public Label target;
	
	public BranchProcedure(Label currentTarget) {
		target = currentTarget;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		mv.goTo(target);
//		mv.visitJumpInsn(GOTO, target);
	}

}
