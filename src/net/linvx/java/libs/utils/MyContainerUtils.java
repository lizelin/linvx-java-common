package net.linvx.java.libs.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * 容器的相关函数
 * @author lizelin
 *
 */
public class MyContainerUtils {

	private MyContainerUtils() {
	}
	
	/***
     * Join a collection of strings by a seperator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Collection<String> strings, String sep) {
        return join(strings.iterator(), sep);
    }

    /***
     * Join a collection of strings by a seperator
     * @param strings iterator of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Iterator<String> strings, String sep) {
        if (!strings.hasNext())
            return "";

        String start = strings.next();
        if (!strings.hasNext()) // only one, avoid builder
            return start;

        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
    }
}
