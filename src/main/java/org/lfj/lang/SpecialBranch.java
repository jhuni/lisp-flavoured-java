package org.lfj.lang;

import org.lfj.lang.op.BranchProcedure;

/**
 * This is an abstraction of the Java break and continue statements.
 */
public class SpecialBranch extends Combiner {
	public String name;
	
	public SpecialBranch(String name) {
		this.name = name;
	}

	@Override
	public Form combine(Object[] params, Runtime rt) throws Exception {
		if(rt.res.specialLabels.containsKey(name)) {
			var currentLabel = rt.res.specialLabels.get(name);
			return new CompoundForm(new BranchProcedure(currentLabel), new Form[] {});
		}
		return VoidForm.instance;
	}

}
