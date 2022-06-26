package org.lfj.lang;

import org.lfj.lang.op.BranchProcedure;

/**
 * This is equivalent to the goto statement used in the java virtual machine.
 */
public class Go extends Combiner {

	@Override
	public Form combine(Object[] params, Runtime rt) throws Exception {
		String name = params[0].toString();
		rt.res.declareLabel(name);
		return new BranchProcedure(rt.res.labels.get(name)).combine(new Form[] {});
	}

}
