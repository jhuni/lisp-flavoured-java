package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * A combiner that produces a label.
 */
public class Tag extends Combiner {

	@Override
	public Form combine(Object[] params, Runtime rt) throws Exception {
		rt.res.declareLabel(params[0].toString());
		return new Form() {
			public Type getType() { return VOID_TYPE; }
			public void accept(GeneratorAdapter mv) {
				mv.visitLabel(rt.res.labels.get(params[0].toString()));
			}
		};
	}

}
