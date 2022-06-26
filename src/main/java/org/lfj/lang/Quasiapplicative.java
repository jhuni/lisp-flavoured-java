package org.lfj.lang;

/**
 * A quasi applicative performs semantic analysis on all of its arguments but
 * it doesn't necessary evaluate them.
 */
public abstract class Quasiapplicative extends Combiner {
	public abstract Form combine(Form[] params) throws Exception;
	
	public Form combine(Object[] params, Runtime rt) throws Exception {
		Form[] castedParams = new Form[params.length];
		for(var i = 0; i < castedParams.length; i++) {
			castedParams[i] = rt.fm(params[i]);
		}
		return this.combine(castedParams);
	}
}
