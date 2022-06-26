package org.lfj.lang.op;

import org.lfj.lang.ExpressionApplicative;
import org.lfj.lang.Form;
import org.lfj.lang.Place;
import org.lfj.lang.util.ExpressionArrays;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This is equivalent to a getfield instruction except it is also a place form.
 */
public class FieldAccessor extends ExpressionApplicative implements Place {
	/**
	 * The name of the field being accessed. 
	 */
	public String id;
	
	/**
	 * The return type of this field access operation.
	 */
	public Type type;
	
	/**
	 * Construct a field accessor from an owner type and a field.
	 * 
	 * @param currentId the field name.
	 * @param currentType the return type of this field access operation.
	 */
	public FieldAccessor(String currentId, Type currentType) {
		id = currentId;
		type = currentType;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv, params);
		Type ownerType = params[0].getType();
		mv.getField(ownerType,id,type);
	}
	
	public Type returnType(Form[] params) {
		return type;
	}
	
	public void setf(GeneratorAdapter mv,  Form[] params, Form newValue) {
		ExpressionArrays.unload(mv, params);
		newValue.accept(mv);
		Type ownerType = params[0].getType();
		mv.putField(ownerType, id, type);
	}
}
