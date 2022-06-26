package org.lfj.lang;

import java.util.Arrays;
import java.util.Hashtable;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.lfj.lang.op.ArrayAccessor;
import org.lfj.lang.op.ArrayLength;
import org.lfj.lang.op.Constructor;
import org.lfj.lang.op.DefinedApplicative;
import org.lfj.lang.op.FieldAccessor;
import org.lfj.lang.op.LogicalFunction;
import org.lfj.lang.op.MathematicalFunction;
import org.lfj.lang.op.StandardProcedure;
import org.lfj.lang.util.LocalizedFieldName;
import org.lfj.lang.util.ReflectiveTypeDispatcher;
import org.lfj.lang.util.TypeDispatcher;
import org.objectweb.asm.Label;

/**
 * Perform semantic analysis and resolve 
 */
public class Resolver {
	public static Hashtable<String, Class<?>> primitiveTypes;
	
	static {
		primitiveTypes = new Hashtable<String, Class<?>>();
		primitiveTypes.put("I", int.class);
		primitiveTypes.put("J", long.class);
		primitiveTypes.put("F", float.class);
		primitiveTypes.put("D", double.class);
		primitiveTypes.put("Z", boolean.class);
		primitiveTypes.put("B", byte.class);
		primitiveTypes.put("C", char.class);
		primitiveTypes.put("S", short.class);
		primitiveTypes.put("V", void.class);
	}
	
	int nextLocal = 0;
	public Hashtable<String, Form> valueEnvironment = new Hashtable<String, Form>();
	public Hashtable<String, Combiner> functionEnvironment = new Hashtable<String, Combiner>();
	public Hashtable<String, Label> labels = new Hashtable<String, Label>();
	public Hashtable<String, Label> specialLabels = new Hashtable<String,Label>();
	public Defclass parent;
	
	public Resolver() {}

	public void declareLabel(String name) {
		if (!labels.containsKey(name)) {
			labels.put(name, new Label());
		}
	}

	public void declareLocal(String name, Type type) {
		this.valueEnvironment.put(name, this.newLocal(type));
	}

	public LocalVariable newLocal(Type type) {
		var rval = new LocalVariable(nextLocal, type);
		this.nextLocal += type.getSize();
		return rval;
	}

	private static boolean isMemberString(String[] coll, String val) {
		for (String s : coll) {
			if (s.equals(val)) {
				return true;
			}
		}
		return false;
	}

	// Reflections based upon static values
	public static ClassVariable resolveStaticValue(String name) throws Exception {
		int divider = name.lastIndexOf("/");
		String className = name.substring(0, divider);
		String fieldName = name.substring(divider + 1, name.length());
		Class<?> ownerClass = Class.forName(className);
		
		var fieldType = (new ReflectiveTypeDispatcher(ownerClass)).getStaticFieldType(fieldName);
		return new ClassVariable(new LocalizedFieldName(ownerClass, fieldName), fieldType);
	}
	
	public static Combiner resolveStaticFunction(String name) throws Exception {
		int divider = name.lastIndexOf("/");
		String className = name.substring(0, divider);
		String methodName = name.substring(divider + 1, name.length());
		Class<?> ownerClass = Class.forName(className);
		
		return new StaticCall(Type.getType(ownerClass), new ReflectiveTypeDispatcher(ownerClass), methodName);
	}

	// Instance fields
	public  FieldAccessor resolveFieldAccessor(String fieldName, Form[] params) throws Exception {
		boolean selfCall = params[0].getType().getDescriptor().equals(parent.getType().getDescriptor());
		TypeDispatcher dispatcher = selfCall ? parent.getDispatcher() : new ReflectiveTypeDispatcher(params[0].getFormClass());
		return new FieldAccessor(fieldName,dispatcher.getFieldType(fieldName));
	}

	// Instance methods
	public  DefinedApplicative resolveMethod(String name, Form[] args) throws Exception {
		String methodName = name.substring(1, name.length());
		
		Form[] params = Arrays.copyOfRange(args, 1, args.length);
		Class<?>[] argumentClasses = new Class<?>[params.length];
		Type[] argumentTypes = new Type[params.length];
		for (var i = 0; i < params.length; i++) {
			argumentClasses[i] = params[i].getFormClass();
			argumentTypes[i] = params[i].getType();
		}
		
		if(args[0].getType().getDescriptor().equals(parent.getType().getDescriptor())) {
			TypeDispatcher dispatcher = parent.getDispatcher();
			var returnSpec = dispatcher.getMethodReturnSpec(methodName, argumentClasses);
			var returnType = returnSpec.type;
			
			String opname = (Modifier.isInterface(parent.accessFlag)) ? "invokeinterface" : "invokevirtual";
			if(methodName.equals("<init>") || methodName.equals("<clinit>")) opname = "invokespecial";
			if(Modifier.isPrivate(returnSpec.modifiers)) opname = "invokespecial";
			
			return new DefinedApplicative(opname, parent.getType(), new Method(methodName, returnType, argumentTypes));
		}
		
		Class<?> ownerClass = args[0].getFormClass();
		var returnType = (new ReflectiveTypeDispatcher(ownerClass)).getMethodReturnType(methodName, argumentClasses);
		String opname = (ownerClass.isInterface()) ? "invokeinterface" : "invokevirtual";
		if(methodName.equals("<init>") 
				|| methodName.equals("<clinit>")
				|| args[0].getType().getDescriptor().equals(parent.getSuperType().getDescriptor())) opname = "invokespecial";

		return new DefinedApplicative(opname, Type.getType(ownerClass),
				new Method(methodName, returnType, argumentTypes));
	}

	// Resolve the actual value of symbols
	public Form symbolValue(String name) throws Exception {
		if (name.indexOf("/") != -1) {
			return resolveStaticValue(name);
		} else if (name.indexOf(".") != -1) {
			return new Constant(Class.forName(name));
		}

		if (primitiveTypes.containsKey(name)) {
			return new Constant(primitiveTypes.get(name));
		}

		return valueEnvironment.get(name);
	}

	public Combiner symbolFunction(String name) throws Exception {
		if (name.equals("type")) {
			return new TypeDeclaration();
		} else if (name.equals("arr")) {
			return new Arr();
		} else if (name.equals("if")) {
			return new Conditional();
		} else if (name.equals("tag")) {
			return new Tag();
		} else if (name.equals("go")) {
			return new Go();
		} else if (name.equals("var")) {
			return new VariableDeclaration();
		} else if (name.equals("while")) {
			return new While();
		} else if (name.equals("cond")) {
			return new CompoundConditional();
		} else if (name.equals("incf")) {
			return new Incf();
		} else if (name.equals("lookupswitch")) {
			return new LookupSwitch();
		} else if (name.equals("cast")) {
			return new Cast();
		} else if (name.equals("break")) {
			return new SpecialBranch("break");
		} else if (name.equals("continue")) {
			return new SpecialBranch("continue");
		} else if (name.equals("dowhile")) {
			return new DoWhile();
		} else if (name.equals("case")) {
			return new Case();
		}
		
		// Functions
		if (isMemberString(MathematicalFunction.sym, name)) {
			return new MathematicalFunction(name);
		} else if (isMemberString(LogicalFunction.sym, name)) {
			return new LogicalFunction(name);
		} else if (name.equals("arraylength")) {
			return new ArrayLength();
		} else if (name.equals("aload")) {
			return new ArrayAccessor();
		}

		// Object combiner
		if (isMemberString(ObjectCombiner.sym, name)) {
			return new ObjectCombiner(name);
		}

		// Procedures
		if (isMemberString(StandardProcedure.sym, name)) {
			return new StandardProcedure(name);
		} else if (name.equals("setf")) {
			return new Setf();
		}

		// Statement applicative
		if (name.equals("do")) {
			return new Do();
		}
		
		// Constructor
		if (name.endsWith(".")) {
			return new Constructor(Class.forName(name.substring(0, name.length() - 1)));
		}
		
		// Static calls
		if(name.indexOf("/") != -1 && !name.equals("/")) {
			return resolveStaticFunction(name);
		}
		
		if(functionEnvironment.containsKey(name)) {
			return functionEnvironment.get(name);
		} else {
			for(String i : functionEnvironment.keySet()) System.out.println(i);
			System.out.println(functionEnvironment.size());
			System.out.println(name);
		}

		return new StandardProcedure("nop");
	}

	// Instance based resolution
	public Combiner complexFunctionResolution(String name, Form[] params) throws Exception {
		if (name.startsWith(".-")) {
			return resolveFieldAccessor(name.substring(2, name.length()), params);
		} else if (name.startsWith(".")) {
			return resolveMethod(name, params);
		} 

		return symbolFunction(name);
	}

}
