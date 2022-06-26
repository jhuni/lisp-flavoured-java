package org.lfj.lang.op;

import java.util.ArrayList;
import java.util.Arrays;
import static org.objectweb.asm.Opcodes.*;

import org.lfj.lang.Form;
import org.objectweb.asm.Label;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * This procedure deals with branching to different locations based upon some table.
 */
public class SwitchProcedure extends Procedure {
	/**
	 * The default target of this switch procedure
	 */
	public Label defaultTarget;
	
	/**
	 * The integer keys to look up.
	 */
	public int[] keys;
	
	/**
	 * The targets to be branched to corresponding to the keys.
	 */
	public Label[] targets;
	
	/**
	 * Determine a switch procedure from some object array.
	 * 
	 * @param coll an object array 
	 */
	public SwitchProcedure(Object[] coll) {
		this(getEvenValues(coll), getOddValues(coll));
	}
	
	/**
	 * Determine a switch procedure from a set of keys and targets.
	 * 
	 * @param evenValues the keys
	 * @param oddValues the targets
	 */
	public SwitchProcedure(Object[] evenValues, Object[] oddValues) {
		int lastIndex = Math.max(0, evenValues.length-1);
		int[] currentKeys = new int[lastIndex];
		Label[] currentTargets = new Label[lastIndex];
		
		for(int i = 0; i < lastIndex; i++) {
			currentKeys[i] = ((Number) evenValues[i]).intValue();
			currentTargets[i] = (Label) oddValues[i];
		}
		
		keys = currentKeys;
		targets = currentTargets;
		defaultTarget = (Label) oddValues[lastIndex];
	}
	
	/**
	 * Construct a switch procedure from its default target, keys, and lookup targets.
	 * 
	 * @param dt the default target
	 * @param k the keys
	 * @param t the other targets
	 */
	public SwitchProcedure(Label dt, int[] k, Label[] t) {
		defaultTarget = dt;
		keys = k;
		targets = t;
	}
	
	public void apply(GeneratorAdapter mv, Form[] params) {
		if(isConsecutive(keys)) {
			int[] sortedKeys = keys.clone();
			Arrays.sort(sortedKeys);
			mv.visitTableSwitchInsn(sortedKeys[0],sortedKeys[keys.length-1],defaultTarget,targets);
		} else {
			mv.visitLookupSwitchInsn(defaultTarget, keys, targets);
		}
	}
	
	public static Object[] getEvenValues(Object[] coll) {
		ArrayList<Object> rval = new ArrayList<Object>();
		
		for(int i = 0; i < coll.length; i += 2) {
			rval.add(coll[i]);
		}
		
		return rval.toArray();
	}
	
	public static Object[] getOddValues(Object[] coll) {
		ArrayList<Object> rval = new ArrayList<Object>();
		
		for(int i = 1; i < coll.length; i += 2) {
			rval.add(coll[i]);
		}
		
		return rval.toArray();
	}
	
	public int[] inclusiveRange(int start, int end) {
		int[] rval = new int[(end-start)+1];
		
		for(int i = 0; i < rval.length; i++) {
			rval[i] = start+i;
		}
		
		return rval;
	}
	
	public boolean isConsecutive(int[] coll) {
		int[] sortedColl = coll.clone();
		Arrays.sort(sortedColl);
		return Arrays.equals(coll,inclusiveRange(sortedColl[0],sortedColl[coll.length-1]));
	}

}
