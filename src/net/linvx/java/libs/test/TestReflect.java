package net.linvx.java.libs.test;

import java.lang.reflect.InvocationTargetException;

import net.linvx.java.libs.enhance.MyReflectCache;

public class TestReflect {
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		ClassA a = new ClassA();
		java.lang.reflect.Field f2 = MyReflectCache.getInstance().getReflectField(ClassA.class, "f1");
		System.out.println(f2);
		//f2.setAccessible(true);
		f2.set(a, "f1value");
		System.out.println(f2.get(a));
	}
}
