package net.linvx.java.libs.enhance;

import java.util.HashSet;
import java.util.Hashtable;

public class MyReflectCache {
	private static MyReflectCache instance = null;
	
	public static MyReflectCache getInstance() {
		if (instance == null) {
			synchronized (MyReflectCache.class) {
				if (instance == null) {
					instance = new MyReflectCache();
				}
			}
		}
		return instance;
	}
	
	private MyReflectCache() {
		
	}
	
	private Hashtable<String, java.lang.reflect.Field> reflectFields = new Hashtable<String, java.lang.reflect.Field>();
	private HashSet<String> analysedClasses = new HashSet<String>();
	
	private Object mutx = new Object();
	public void clearAll() {
		synchronized (mutx) {
			analysedClasses.clear();
			reflectFields.clear();
		}
	}
	
	private void analyseReflectFields(Class<?> clazz) {
		synchronized (mutx) {
			String clsName = clazz.getName();
			if (analysedClasses.contains(clsName))
				return;
			java.lang.reflect.Field[] fs = clazz.getDeclaredFields();
			for (int i=0; i<fs.length; i++) {
				String fieldName = clsName + "." + fs[i].getName();
				fs[i].setAccessible(true);
				reflectFields.put(fieldName, fs[i]);
			}
			analysedClasses.add(clsName);
		}
	}

	public java.lang.reflect.Field getReflectField(Class<?> clazz, String fieldName) {
		if (!analysedClasses.contains(clazz.getName())) {
			analyseReflectFields(clazz);
		}
		if (analysedClasses.contains(clazz.getName())) {
			return reflectFields.get(clazz.getName() + "." + fieldName);
		} else {
			return null;
		}
	}
}
