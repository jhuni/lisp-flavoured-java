package org.lfj.lang;

import java.util.ArrayList;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;

import org.lfj.lang.util.AccessFlags;
import org.lfj.lang.util.LocalizedFieldName;
import org.lfj.lang.util.ManualTypeDispatcher;
import org.lfj.lang.util.MemberSpec;
import org.lfj.lang.util.ReflectiveTypeDispatcher;

import clojure.lang.Symbol;
import clojure.lang.PersistentList;
import clojure.lang.RT;

/**
 * The means by which classes are defined and compiled.
 */
public class Defclass {
	int accessFlag;
	String name;
	
	ArrayList<ClassMember> members = new ArrayList<ClassMember>();
	ArrayList<String> interfaces = new ArrayList<String>();
	ArrayList<String> parentClasses = new ArrayList<String>();
	
	private String parseClassName(String name) throws Exception {
		return Type.getType(((Class<?>) ((Constant) new Runtime().fm(RT.readString(name))).val)).getInternalName();
	}
	
	private void handleMember(Object obj) throws Exception {
		Object[] coll = ((PersistentList) obj).toArray();
		Object[] params = Arrays.copyOfRange(coll, 1, coll.length);
		String id = coll[0].toString();	
		
		switch(id) {
		case "extends":
			parentClasses.add(parseClassName(params[0].toString()));
			break;
		case "implements":
			for(Object i : params) interfaces.add(parseClassName(i.toString()));
			break;
		case "field":
			members.add(new FieldForm(params));
			break;
		case "method":
			members.add(new MethodForm(params, this));
			break;
		}
		
	}
	
	public Defclass(Object obj) throws Exception {
		PersistentList ps = (PersistentList) obj;
		Object[] coll = ps.toArray();
		Object[] params = Arrays.copyOfRange(coll, 1, coll.length);
		
		// get the symbols region:
		int lastSymbol = 0;
		while(params[lastSymbol+1] instanceof Symbol) lastSymbol++;
		
		// Deal with the name
		name = params[lastSymbol].toString();
		
		// Deal with the access flags
		String[] accessFlagNames = new String[lastSymbol];
		for(var i = 0; i < lastSymbol; i++) accessFlagNames[i] = params[i].toString();
		accessFlag = AccessFlags.getAccessFlag(accessFlagNames);
		
		// Del with the class members 
		Object[] remainingValues = Arrays.copyOfRange(params,Math.min(params.length-1,lastSymbol+1), params.length);
		for(int i = 0; i < remainingValues.length; i++) handleMember(remainingValues[i]);
	}
	
	public MemberSpec[] getSpecs() {
		MemberSpec[] rval = new MemberSpec[members.size()];
		for(int i = 0; i < members.size(); i++) rval[i] = members.get(i).getSpec();
		return rval;
	}
	
	public ClassWriter visit() throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		String[] interfaceNames = new String[interfaces.size()];
		for(var i = 0; i < interfaceNames.length; i++) interfaceNames[i] = (String) interfaces.get(i);
		cw.visit(V10, // version
				accessFlag, // access flag
				name, // name
				null, // signature
				(parentClasses.size() == 0) ? "java/lang/Object" : parentClasses.get(0), // super name
				(interfaces.size()) == 0 ? null : interfaceNames); // interface names array
		for(ClassMember i : members) i.visit(cw);
		return cw;
	}
	
	public Type getType() {
		return Type.getType("L" + this.name + ";");
	}
	
	public ManualTypeDispatcher getDispatcher() throws Exception {
		return new ManualTypeDispatcher(this.getSpecs(), new ReflectiveTypeDispatcher(this.getSuperClass()));
	}
	
	public ArrayList<ClassVariable> getStaticVariables() {
		ArrayList<ClassVariable> rval = new ArrayList<ClassVariable>();
		Type thisType = this.getType();
		
		for(MemberSpec i : this.getSpecs()) {
			if(i.isStaticField()) {
				rval.add(new ClassVariable(new LocalizedFieldName(thisType, i.name), i.type));
			}
		}
		
		return rval;
	}
	
	public ArrayList<StaticCall> getStaticMethods() throws Exception {
		Type thisType = this.getType();
		ManualTypeDispatcher thisDispatcher = this.getDispatcher();
		var methodNames = new ArrayList<String>();
		for(MemberSpec i : this.getSpecs()) if(i.isStaticMethod()) methodNames.add(i.name);
		
		var rval = new ArrayList<StaticCall>();
		for(String methodName : methodNames) rval.add(new StaticCall(thisType,thisDispatcher, methodName));
		return rval;
	}
	
	public Class<?> getSuperClass() throws Exception {
		if(parentClasses.size() == 0) return Object.class;
		return Class.forName(parentClasses.get(0));
	}
	
	public Type getSuperType() throws Exception {
		return Type.getType(this.getSuperClass());
	}
	
}
