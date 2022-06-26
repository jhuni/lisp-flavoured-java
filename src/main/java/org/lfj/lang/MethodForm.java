package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.Opcodes;
import org.lfj.lang.op.SwitchProcedure;
import org.lfj.lang.util.AccessFlags;
import org.lfj.lang.util.MemberSpec;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.commons.GeneratorAdapter;
import clojure.lang.Symbol;
import java.lang.reflect.Modifier;

import java.util.Arrays;
import java.util.List;

/**
 * An abstraction of methods used by the compiler.
 */
public class MethodForm extends ClassMember {

	int modifiers;
	public String name;
	public Type[] argumentTypes;
	public String[] argumentNames;
	public Object[] code;
	Type returnType;

	Type[] exceptions = null;
	Defclass parent;
	
	public MethodForm(Object[] coll, Defclass parent) throws Exception {
		int lastAccessFlag = 0;
		while(AccessFlags.isAccessSymbol(coll[lastAccessFlag+1])) lastAccessFlag++;
		int lastSymbol = lastAccessFlag+2;
		
		String[] accessFlagNames = new String[lastAccessFlag+1];
		for(var i = 0; i <= lastAccessFlag; i++) accessFlagNames[i] = coll[i].toString();
		modifiers = AccessFlags.getAccessFlag(accessFlagNames);
		
		returnType = Type.getType((Class<?>) ((Constant) (new Runtime()).fm(coll[lastAccessFlag+1])).val);
		name = coll[lastSymbol].toString();
		
		Object[] args = ((List<?>) coll[lastSymbol+1]).toArray();
		Object[] argNameObjects = SwitchProcedure.getOddValues(args);
		Object[] argTypeObjects = SwitchProcedure.getEvenValues(args);
		argumentNames = new String[argNameObjects.length];
		for(var i = 0; i < argNameObjects.length; i++) argumentNames[i] = argNameObjects[i].toString();
		argumentTypes = new Type[argTypeObjects.length];
		var r = new Runtime();
		for(var i = 0; i < argTypeObjects.length; i++) argumentTypes[i] = getType(r, argTypeObjects[i]);
		
		Object[] remainingValues = Arrays.copyOfRange(coll,lastSymbol+2,coll.length);
		code = remainingValues;

		this.parent = parent;
	}
	
//	public void init() throws Exception {
//		fm = ld(code, argumentNames, argumentTypes, !Modifier.isStatic(modifiers), parent);	
//	}
	
	public void visit(ClassWriter cw) throws Exception {	
		var fm =  ld(code, argumentNames, argumentTypes, !Modifier.isStatic(modifiers), parent);	
		var currentMethod = new Method(name, fm.getType(), argumentTypes);
		GeneratorAdapter mv = new GeneratorAdapter(modifiers, currentMethod, null, exceptions, cw);
		
		mv.visitCode();
		fm.accept(mv);
		mv.returnValue();
		mv.visitMaxs(-1,-1);
		mv.visitEnd();
	}
	
	// this requires initialization
	public MemberSpec getSpec() {
		return new MemberSpec(modifiers,name,returnType,argumentTypes);
	}
	
	// Argument types is already given to us here
	private static Form ld(Object[] code, String[] argumentNames, Type[] argumentTypes, boolean usesThis, Defclass parent) throws Exception {
		Runtime r = new Runtime();
		
		for(ClassVariable i : parent.getStaticVariables()) r.res.valueEnvironment.put(i.id.name,i);
		for(StaticCall i : parent.getStaticMethods()) r.res.functionEnvironment.put(i.methodName, i);
		if(usesThis) {
			r.res.declareLocal("this", parent.getType());
			r.res.valueEnvironment.put("super", new LocalVariable(0, parent.getSuperType()));
		}
		r.res.parent = parent;
		
		for(var i = 0; i < argumentNames.length; i++) r.res.declareLocal(argumentNames[i], argumentTypes[i]);
		
		return (new Do()).combine(code, r);
	}

	private static Type getType(Runtime r, Object obj) throws Exception {
		return Type.getType((Class<?>) ((Constant) r.fm(obj)).val);
	}
	
}
