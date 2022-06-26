package org.lfj.lang.op;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import static org.objectweb.asm.commons.GeneratorAdapter.*;

import org.lfj.lang.CompoundForm;
import org.lfj.lang.Constant;
import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;

/**
 * A class of functions that deal with all the mathematical functions defined by
 * the java virtual machine including all arithmetic, logic, and cast operations.
 * This operations are generalized so that they produce the appropriate
 * instruction based upon their arguments making them generic.
 */
public class MathematicalFunction extends PrimitiveFunction {
	/**
	 * The names of all the mathematical operations.
	 */
	public static String[] sym = new String[] {
		"+", "*", "-", "/", "rem", "neg",
		"and", "or", "xor", "not", 
		"shl", "shr", "ushr",
		"boolean", "char", "byte", "short", "int", "long", "float", "double"
	};
	
	/**
	 * The names of the numeric cast operations.
	 */
	public static String[] numericCasts = new String[] {
		"byte", "short", "int", "long", "float", "double"	
	};
	
	/**
	 * The name of the current operation.
	 */
	public String name;
	
	/**
	 * Construct a mathematical function from its name
	 * @param currentName the name of this mathematical function
	 */
	public MathematicalFunction(String currentName) {
		name = currentName;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		ExpressionArrays.unload(mv,params);
		
		switch (name) {
		case "+":
			mv.math(ADD, this.returnType(params));
			break;
		case "*":
			mv.math(MUL, this.returnType(params));
			break;
		case "-":
			mv.math(SUB, this.returnType(params));
			break;
		case "/":
			mv.math(DIV, this.returnType(params));
			break;
		case "rem":
			mv.math(REM, this.returnType(params));
			break;
		case "neg":
			mv.math(NEG, this.returnType(params));
			break;
		case "and":
			mv.math(AND, this.returnType(params));
			break;
		case "or":
			mv.math(OR, this.returnType(params));
			break;
		case "xor":
			mv.math(XOR, this.returnType(params));
			break;
		case "not":
			Type argumentType = this.returnType(params);
			if(argumentType == INT_TYPE) {
				mv.push(-1);
			} else {
				mv.push(-1L);
			}
			mv.math(XOR, argumentType);
			break;
		case "shl":
			mv.math(SHL, this.returnType(params));
			break;
		case "shr":
			mv.math(SHR, this.returnType(params));
			break;
		case "ushr":
			mv.math(USHR, this.returnType(params));
			break;
		case "boolean":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "char":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "byte":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "short":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "int":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "long":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "float":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "double":
			mv.cast(params[0].getType(), this.returnType(params));
			break;
		case "cmp":
			mv.visitInsn(LCMP);
			break;
		case "cmpg":
			if(params[0].getType() == FLOAT_TYPE) {
				mv.visitInsn(FCMPG);
			} else {
				mv.visitInsn(DCMPG);
			}
			break;
		case "cmpl":
			if(params[0].getType() == DOUBLE_TYPE) {
				mv.visitInsn(FCMPL);
			} else {
				mv.visitInsn(DCMPL);
			}
			break;			
		}
		
	}
	
	public Type returnType(Form[] params) {
		boolean isShift = (name == "shr") || (name == "shl") || (name == "ushr");
		boolean isComparison = (name == "cmp") || (name == "cmpl") || (name == "cmg");
		
		// Deal with shifts
		if(isShift) { 
			return params[1].getType();
		}
		
		if(isComparison) {
			return INT_TYPE;
		}
		
		// Deal with casts
		switch(name) {
		case "boolean":
			return BOOLEAN_TYPE;
		case "char":
			return CHAR_TYPE;
		case "byte":
			return BYTE_TYPE;
		case "short":
			return SHORT_TYPE;
		case "int":
			return INT_TYPE;
		case "long":
			return LONG_TYPE;
		case "float":
			return FLOAT_TYPE;
		case "double":
			return DOUBLE_TYPE;
		}
		
		return params[0].getType();
	}
	
	private static boolean isMemberString(String[] coll, String val) {
		for(String s : coll) {
			if(s.equals(val)) {
				return true;
			}
		}
		return false;
	}
	
	// Override to use constant folding 
	public Form combine(Form[] params) {
		if(isMemberString(numericCasts, name) &&
				params.length != 0 &&
				params[0] instanceof Constant) {
			
			var numberConstant = (Number) ((Constant) params[0]).val;
			switch(name) {
			case "int":
				return new Constant(numberConstant.intValue());
			case "long":
				return new Constant(numberConstant.longValue());
			case "float":
				return new Constant(numberConstant.floatValue());
			case "double":
				return new Constant(numberConstant.doubleValue());
			case "short":
				return new Constant(numberConstant.shortValue());
			case "byte":
				return new Constant(numberConstant.byteValue());
			}
			
		}
		return new CompoundForm(this, params);
	}
	
}
