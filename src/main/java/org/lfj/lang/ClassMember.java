package org.lfj.lang;

import org.lfj.lang.util.MemberSpec;
import org.objectweb.asm.ClassWriter;

/**
 * This is either a field or a method of a class.
 */
public abstract class ClassMember {
	public abstract void visit(ClassWriter cw) throws Exception;
	public abstract MemberSpec getSpec();
}
