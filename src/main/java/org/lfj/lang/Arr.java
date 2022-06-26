package org.lfj.lang;

import java.lang.reflect.Array;

/**
 * Wrap a class in an array class.
 */
public class Arr extends Combiner {

	public Form combine(Object[] params, Runtime rt) throws Exception {
		Class<?> currentClass = (Class<?>) (((Constant) rt.fm(params[0])).val);
		Class<?> arrayClass = Array.newInstance(currentClass,0).getClass();
		return new Constant(arrayClass);	
	}
	
}
