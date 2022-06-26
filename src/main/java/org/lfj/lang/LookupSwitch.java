package org.lfj.lang;
import java.util.List;

import org.lfj.lang.op.SwitchProcedure;
import org.objectweb.asm.Label;

/**
 * An abstraction of the lookupswitch used by the java virtual machine.
 */
public class LookupSwitch extends Combiner {

	@Override
	public Form combine(Object[] params, Runtime rt) throws Exception {
		Object[] table = ((List) params[0]).toArray();
		Form operand = rt.fm(params[1]);
		Object[] evenValues = SwitchProcedure.getEvenValues(table);
		Object[] oddValues = SwitchProcedure.getOddValues(table);
		Label[] targets = new Label[oddValues.length];
		
		for(int i = 0; i < targets.length; i++) {
			String currentName = oddValues[i].toString();
			rt.res.declareLabel(currentName);
			targets[i] = rt.res.labels.get(currentName);
		}
		
		return new SwitchProcedure(evenValues, targets).combine(new Form[] {operand});
	}

}
