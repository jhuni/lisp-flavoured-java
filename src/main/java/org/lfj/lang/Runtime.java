package org.lfj.lang;

import clojure.lang.RT;
import clojure.lang.Symbol;

import java.util.Arrays;

import clojure.lang.PersistentList;

/**
 * The runtime is a thin wrapper over the resolver. It performs semantic analysis on its arguments
 * thereby produced a form.
 */
public class Runtime {
	public Resolver res = new Resolver();

	/**
	 * Get the form of an object. Symbols and lists produce various forms which are not
	 * equivalent to constants. A constant value is returned otherwise.
	 * 
	 * @param obj an object to be analyzed
	 */
	public Form fm(Object obj) throws Exception {
		if(obj instanceof Symbol) {
			return res.symbolValue(obj.toString());
		} else if(obj instanceof PersistentList) {
			var list = (PersistentList) obj;
			Object[] values = list.toArray();
			String combinerName = values[0].toString();
			Object[] params = Arrays.copyOfRange(values, 1, values.length);
			Form[] castedParams = new Form[params.length];
			for(var i = 0; i < castedParams.length; i++) castedParams[i] = this.fm(params[i]);
			
			var combiner = res.complexFunctionResolution(combinerName, castedParams);
			return combiner.combine(params, this);
		} else {
			return Constant.loadValue(obj);
		}
	}
	
}
