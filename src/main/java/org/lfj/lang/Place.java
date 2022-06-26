package org.lfj.lang;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * An abstraction of Common Lisp like place forms.
 */
public interface Place {
	public void setf(GeneratorAdapter mv, Form[] params, Form newValue); 
}
