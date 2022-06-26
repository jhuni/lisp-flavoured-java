package org.lfj.lang;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;
import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.*;

import org.lfj.lang.util.AccessFlags;
import org.lfj.lang.util.ManualTypeDispatcher;
import org.objectweb.asm.*;
import static org.objectweb.asm.Type.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import clojure.lang.PersistentList;
import clojure.lang.RT;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
//		System.out.println(args[0]);
//		System.out.println(System.getProperty("user.dir"));
		
		String homeDirectory = System.getProperty("user.dir");
		String fileName = homeDirectory + "/" + args[0];
		
		String text = new String(
				Files.readAllBytes(
						Paths.get(fileName)), 
				"UTF-8");
		var c = new Defclass(RT.readString(text));
		
		var fos = new FileOutputStream(homeDirectory + "/" + c.name + ".class");
		fos.write(c.visit().toByteArray());
		fos.flush();
		fos.close();
	}
	
}