package org.lfj.lang;

import org.objectweb.asm.Type;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * These are atomic expressions which are constant.
 */
public class Constant extends AtomicExpression {

	public Object val;
	public boolean isPrimitive;
	
	public Constant(boolean v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(short v) {
		val = v;
		isPrimitive = true;
	}
	public Constant(byte v) {
		val = v;
		isPrimitive = true;
	}
	public Constant(char v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(int v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(long v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(float v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(double v) {
		val = v;
		isPrimitive = true;
	}
	
	public Constant(Object v) {
		val = v;
		isPrimitive = false;
	}
	
	public int numberOfParameters() {
		return 0;
	}

	public Type getType() {
		Class<?> currentClass = (isPrimitive) ? getUnboxedClass(val.getClass()) : val.getClass();
		return Type.getType(currentClass);
	}
	
	public void accept(GeneratorAdapter mv) {
		
		if(val == null) {
			mv.visitInsn(org.objectweb.asm.Opcodes.ACONST_NULL);
			return;
		}
		
		var valueClass = val.getClass();
		
		if (isWrapperClass(valueClass)) {
			if (valueClass == Byte.class) {
				mv.push((byte) val);
			} else if (valueClass == Boolean.class) {
				mv.push((boolean) val);
			} else if (valueClass == Short.class) {
				mv.push((short) val);
			} else if (valueClass == Character.class) {
				mv.push((char) val);
			} else if (valueClass == Integer.class) {
				mv.push((int) val);	
			} else if (valueClass == Long.class) {
				mv.push((long) val);
			} else if (valueClass == Float.class) {
				mv.push((float) val);	
			} else if (valueClass == Double.class) {
				mv.push((double) val);
			}
			
			// Perform autoboxing when necessary
			if(!isPrimitive) {
				mv.valueOf(Type.getType(getUnboxedClass(valueClass)));
			}
		} else if (valueClass == String.class) {
			mv.push((String) val);
		}

		if(valueClass.isArray()) {
			var componentType = Type.getType(valueClass.getComponentType());
			var constantArray = constantWrapArray(val);

			mv.push(constantArray.length);
			mv.newArray(componentType);
			
			for(var i = 0; i < constantArray.length; i++) {
				mv.dup();
				mv.push(i);
				constantArray[i].accept(mv);
				mv.arrayStore(componentType);
			}
		}
		
	}
	
	public static boolean isWrapperClass(Class<?> valueClass) {
		return (valueClass == Boolean.class)
				|| (valueClass == Character.class)
				|| (valueClass == Byte.class) 
				|| (valueClass == Short.class)
				|| (valueClass == Integer.class)
				|| (valueClass == Long.class)
				|| (valueClass == Float.class)
				|| (valueClass == Double.class);
	}
	
	public static Class<?> getUnboxedClass(Class<?> valueClass) {
		if (valueClass == Integer.class) {
			return int.class;
		} else if (valueClass == Long.class) {
			return long.class;
		} else if (valueClass == Float.class) {
			return float.class;
		} else if (valueClass == Double.class) {
			return double.class;
		} else if (valueClass == Boolean.class) {
			return boolean.class;
		} else if (valueClass == Byte.class) {
			return byte.class; 
		} else if (valueClass == Short.class) {
			return short.class;
		} else if (valueClass == Character.class) {
			return char.class;
		}
		return valueClass;
	}

	public static Constant[] constantWrapArray(Object coll) {
		var valueClass = coll.getClass();
		var elementClass = valueClass.getComponentType();

		if(elementClass.isPrimitive()) {
			if(elementClass == boolean.class) {
				boolean[] castedColl = (boolean[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if (elementClass == char.class) {
				char[] castedColl = (char[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if(elementClass == byte.class) {
				byte[] castedColl = (byte[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if (elementClass == short.class) {
				short[] castedColl = (short[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if(elementClass == int.class) {
				int[] castedColl = (int[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if (elementClass == long.class) {
				long[] castedColl = (long[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if (elementClass == float.class) {
				float[] castedColl = (float[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			} else if (elementClass == double.class) {
				double[] castedColl = (double[]) coll;

				Constant[] rval = new Constant[castedColl.length];
				for (var i = 0; i < castedColl.length; i++) {
					rval[i] = new Constant(castedColl[i]);
				}
				return rval;
			}
		}
		
		Object[] objectArray = (Object[]) coll;
		Constant[] rval = new Constant[objectArray.length];
		for (var i = 0; i < objectArray.length; i++) {
			rval[i] = new Constant(objectArray[i]);
		}
		
		return rval;
	}
	
	public static Constant loadValue(Object obj) {
		var valueClass = obj.getClass();
		var currentConstant = new Constant(obj);
		if(isWrapperClass(valueClass)) {
			currentConstant.isPrimitive = true;
			if(obj instanceof Long) {
				long longValue = ((Long) obj).longValue();
				if((Integer.MIN_VALUE <= longValue) &&  (longValue <= Integer.MAX_VALUE)) {
					return new Constant((int) longValue);
				}
			}
		}
		return currentConstant;
	}
	
}
