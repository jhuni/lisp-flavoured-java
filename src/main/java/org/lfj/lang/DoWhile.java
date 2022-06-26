package org.lfj.lang;

import org.objectweb.asm.Type;
import org.lfj.lang.op.LogicalFunction;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;

import java.util.Arrays;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * A java like do while loop.
 */
public class DoWhile extends Combiner {

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
				
				var conditionForm = params[params.length-1];
				var butlastParams = Arrays.copyOfRange(params,0,params.length-1);
				
				mv.visitLabel(continueLabel);
				(new Do()).combine(butlastParams).accept(mv);
				conditionForm.accept(mv);
				mv.visitJumpInsn(Opcodes.IFEQ, breakLabel);
				mv.goTo(continueLabel);
				mv.visitLabel(breakLabel);
				
			}
		};
		
	}
	
}
