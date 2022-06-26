package org.lfj.lang;

/**
 * The base class of all combiners used in the programming language. Combiners are any
 * value that can appear at the start of a compound form in a lisp expression.
 */
public abstract class Combiner {
	public abstract Form combine(Object[] params, Runtime rt) throws Exception;
}
