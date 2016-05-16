package net.linvx.java.libs.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyReflectUtils {
	public static JSONObject toJson(Object o, boolean getSuper, boolean strict) {
		JSONObject retJson = new JSONObject();
		List<Class<?>> clslist = new ArrayList<Class<?>>();
		Class<?> clazz = o.getClass();
		while (!clazz.getSimpleName().equalsIgnoreCase("object")) {
			clslist.add(clazz);
			clazz = clazz.getSuperclass();
		}
		for (int i = clslist.size() - 1; i >= 0; i--) {
			MyReflectUtils.toJSON(o, clslist.get(i), retJson, strict);
		}
		clslist.clear();
		clslist = null;
		return retJson;
	}

	private static void toJSON(Object o, Class<?> clazz, JSONObject json, boolean strict) {
		JSONObject retJson = (json == null ? new JSONObject() : json);

		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			java.lang.reflect.Field field = fields[i];
			if (strict && (!java.lang.reflect.Modifier.isPublic(field.getModifiers())
					|| java.lang.reflect.Modifier.isStatic(field.getModifiers()))) {
				continue;
			}
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;
			try {
				value = field.get(o);
				Class<?> fc = field.getType();
				if (fc.getName().equals("java.util.Map")) {
					value = JSONObject.fromObject(value);
				} else if (fc.getName().equals("java.util.List")) {
					value = JSONArray.fromObject(value);
				}
				retJson.put(name, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// class A {
		// public String a = "1";
		// private ArrayList list = new ArrayList<String>();
		// public HashMap map = new HashMap();
		//
		// public A() {
		// list.add("list1");
		// list.add("list2");
		// map.put("key1", "value1");
		// map.put("key2", "value2");
		// }
		// }
		// class B extends A {
		// public B() {
		// super();
		// }
		//
		// public String a = "b1";
		// }
		// System.out.println(MyReflectUtils.toJson(new B(), true, true));
	}
}
