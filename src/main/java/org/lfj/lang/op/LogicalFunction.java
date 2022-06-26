package org.lfj.lang.op;

import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;

import org.lfj.lang.CompoundForm;
import org.lfj.lang.Constant;
import org.lfj.lang.Form;
import org.lfj.lang.util.ExpressionArrays;
import org.objectweb.asm.Label;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * A generalization of the logical functions of the java virtual machine.
 */
public class LogicalFunction extends PrimitiveFunction {
	/**
	 * The valid names of the logical function.
	 */
	public static String[] sym = new String[] {
			"iszero", "isnotzero",
			"ispositive", "isnonpositive", "isnegative", "isnonnegative",
			"=", "!=", "<", "<=", ">", ">="
	};

	/**
	 * The current name.
	 */
	public String name;

	/**
	 * Construct a logical function with some name.
	 */
	public LogicalFunction(String currentName) {
		name = currentName;
	}

	/**
	 * Branch to a target if the condition is true.
	 */
	public static void iftrue(GeneratorAdapter mv, Form condition, Label l1) {
		if(condition instanceof CompoundForm 
				&& ((CompoundForm) condition).combiner instanceof LogicalFunction) {
			
			var compoundCondition = (CompoundForm) condition;
			var combinerName = ((LogicalFunction) compoundCondition.combiner).name;
			Form[] params = compoundCondition.params;
			var argType = params[0].getType();

			/// Unload the arguments to the condition
			ExpressionArrays.unload(mv,params);

			switch(combinerName) {
			case "iszero":
				mv.visitJumpInsn(IFEQ, l1);
				break;
			case "isnotzero":
				mv.visitJumpInsn(IFNE, l1);
				break;
			case "ispositive":
				mv.visitJumpInsn(IFGT, l1);
			case "isnonpositive":
				mv.visitJumpInsn(IFLE, l1);
			case "isnegative":
				mv.visitJumpInsn(IFLT, l1);
			case "isnonnegative":
				mv.visitJumpInsn(IFGE, l1);
			case "isnull":
				mv.visitJumpInsn(IFNULL, l1);
			case "isnonnull":
				mv.visitJumpInsn(IFNONNULL, l1);
			case "=":
				if(argType.getSort() == OBJECT || argType.getSort() == ARRAY) {
					mv.visitJumpInsn(IF_ACMPEQ, l1);
				} else if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFEQ,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPL);
					mv.visitJumpInsn(IFEQ,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPL);
					mv.visitJumpInsn(IFEQ,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPEQ, l1);
				}
				break;
			case "!=":
				if(argType.getSort() == OBJECT || argType.getSort() == ARRAY) {
					mv.visitJumpInsn(IF_ACMPNE, l1);
				} else if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFNE,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPL);
					mv.visitJumpInsn(IFNE,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPL);
					mv.visitJumpInsn(IFNE,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPNE, l1);
				}
				break;
			case "<":			
				if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFLT,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPL);
					mv.visitJumpInsn(IFLT,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPL);
					mv.visitJumpInsn(IFLT,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPLT, l1);
				}
				break;
			case "<=":
				if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFLE,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPL);
					mv.visitJumpInsn(IFLE,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPL);
					mv.visitJumpInsn(IFLE,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPLE, l1);
				}
				break;
			case ">":			
				if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFGT,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPG);
					mv.visitJumpInsn(IFGT,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPG);
					mv.visitJumpInsn(IFGT,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPGT, l1);
				}
				break;
			case ">=":
				if (argType == LONG_TYPE) {
					mv.visitInsn(LCMP);
					mv.visitJumpInsn(IFGE,l1);
				} else if (argType == FLOAT_TYPE) {
					mv.visitInsn(FCMPG);
					mv.visitJumpInsn(IFGE,l1);
				} else if (argType == DOUBLE_TYPE) {
					mv.visitInsn(DCMPG);
					mv.visitJumpInsn(IFGE,l1);
				} else {
					mv.visitJumpInsn(IF_ICMPGE, l1);
				}
				break;

			}
		} else if (condition instanceof Constant) {
			var constantCondition = ((Constant) condition);
			if((boolean) constantCondition.val) {
				mv.goTo(l1);
			}
 		} else {
			condition.accept(mv);
			mv.visitJumpInsn(IFEQ, l1);
		}
	}

	public void apply(GeneratorAdapter mv, Form[] params) {
		var condition = params[0];
		var elseIf = new Label();
		var endIf = new Label();
		
		iftrue(mv, new CompoundForm(this,params), elseIf);
		mv.push(0);
		mv.goTo(endIf);
		mv.visitLabel(elseIf);
		mv.push(1);
		mv.visitLabel(endIf);
		
//		var l1 = new Label();
//		iftrue(mv, params[0], l1);
//		mv.push(0);
//		mv.visitLabel(l1);
//		mv.push(1);
	}

	public Type returnType(Form[] params) {
		return BOOLEAN_TYPE;
	}

}
