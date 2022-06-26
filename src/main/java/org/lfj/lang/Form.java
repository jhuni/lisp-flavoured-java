package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Any value of the programming language is expressed as a form.
 */
public abstract class Form {
	public abstract Type getType();
	public abstract void accept(GeneratorAdapter mv);
	
	public boolean isExpression() {
		return this.getType().getSort() == VOID;
	}
	
	public boolean isStatement() {
		return this.getType().getSort() == VOID;
	}
	
	public Class<?> getFormClass() throws Exception {
		var currentType = this.getType();
		switch(currentType.getSort()) {
		case VOID: return void.class;
		case BOOLEAN: return boolean.class;
		case BYTE: return byte.class;
		case SHORT: return short.class;
		case CHAR: return char.class;
		case INT: return int.class;
		case FLOAT: return float.class;
		case LONG: return long.class;
		case DOUBLE: return double.class;
		// case ARRAY
		default: return Class.forName(this.getType().getClassName());
		}
	}
}
