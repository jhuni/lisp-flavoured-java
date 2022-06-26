package org.lfj.lang;

import java.util.Arrays;
import org.lfj.lang.util.AccessFlags;
import org.lfj.lang.util.MemberSpec;
import org.objectweb.asm.*;
import clojure.lang.PersistentList;

/**
 * An astraction of fields used by the compiler.
 */
public class FieldForm extends ClassMember {
	public int modifiers;
	public String name;
	public Type type;
	
	public FieldForm(Object[] coll) throws Exception {
		var lastIndex = coll.length - 1;
		
		var accessFlags = Arrays.copyOfRange(coll,0,lastIndex-1);
		String[] accessFlagNames = new String[accessFlags.length];
		for(var i = 0; i < accessFlags.length; i++) accessFlagNames[i] = accessFlags[i].toString();
		modifiers = AccessFlags.getAccessFlag(accessFlagNames);
		
		type = Type.getType((Class<?>) ((Constant) (new Runtime()).fm(coll[lastIndex-1])).val);
		name = coll[lastIndex].toString();
	}
	
	public void visit(ClassWriter cw) throws Exception { 
		cw.visitField(modifiers,name,type.getDescriptor(),null,null);
	}
	
	public MemberSpec getSpec() {
		return new MemberSpec(modifiers,name,type);
	}
		
}
