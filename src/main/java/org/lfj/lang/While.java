package org.lfj.lang;

import org.objectweb.asm.Type;
import org.lfj.lang.op.LogicalFunction;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;

import java.util.Arrays;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * A java like while loop.
 */
public class While extends Combiner {

	@Override
	public Form combine(Object[] args, Runtime rt) throws Exception {
		var continueLabel = new Label();
		var breakLabel = new Label();
		
		rt.res.specialLabels.put("continue", continueLabel);
		rt.res.specialLabels.put("break", breakLabel);
		
		Form[] params = new Form[args.length];
		for(var i = 0; i < args.length; i++) params[i] = rt.fm(args[i]);
		
		return new Form() {
			public Type getType() { return VOID_TYPE; }
			public void accept(GeneratorAdapter mv) {
				
				var conditionForm = params[0];
				var restParams = Arrays.copyOfRange(params, 1, params.length);
				
				mv.visitLabel(continueLabel);
				conditionForm.accept(mv);
				mv.visitJumpInsn(Opcodes.IFEQ, breakLabel);
				(new Do()).combine(restParams).accept(mv);
				mv.goTo(continueLabel);
				mv.visitLabel(breakLabel);
			}
		};
		
	}
	
}
