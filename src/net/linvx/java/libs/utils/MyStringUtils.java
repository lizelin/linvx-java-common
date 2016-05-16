package net.linvx.java.libs.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by lizelin on 16/2/5.
 * String的一些常用函数
 * 其中编解码用到了/linvx-java-common/libs/commons-lang-2.4.jar
 */
public class MyStringUtils {
	public static final String EMPTY = "";
	
    private MyStringUtils() {
    }

    /**
     * 判断是否为null或者为空字符串
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    /**
     * 判断是否非空字符串并且非null
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 仿oracle nvl函数：
     *
     * @param s
     * @param defaultValue
     * @return 如果s为空，则返回缺省defaultValue，否则返回s
     */
    public static String nvlString(String s, String defaultValue) {
        return isEmpty(s) ? defaultValue : s;
    }

    /**
     * 判断字符串s是否包含find，忽略大小写
     *
     * @param s
     * @param find
     * @return
     */
    public static boolean containsIgnoreCase(String s, String find) {
        if (isEmpty(s) || isEmpty(find))
            return false;
        return s.toLowerCase(Locale.getDefault()).contains(find.toLowerCase(Locale.getDefault()));
    }

    /**
     * 判断字符串s是否以prefix开头，忽略大小写
     *
     * @param s
     * @param prefix
     * @return
     */
    public static boolean startWithIgnoreCase(String s, String prefix) {
        if (isEmpty(s) || isEmpty(prefix))
            return false;
        return s.toLowerCase(Locale.getDefault()).startsWith(prefix.toLowerCase(Locale.getDefault()));
    }

    /**
     * 根据字符集编码获取字符串s的byte数组
     *
     * @param s
     * @param charset
     * @return
     */
    public static byte[] getBytes(String s, String charset) {
        if (isEmpty(s) || isEmpty(charset))
            return null;
        byte[] t = null;
        try {
            t = s.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取字符串s的byte数组，缺省认为是utf-8编码
     *
     * @param s
     * @return
     */
    public static byte[] getBytes(String s) {
        return getBytes(s, "UTF-8");
    }

    /**
     * 将byte数组转为字符串
     *
     * @param b
     * @param charset
     * @return
     */
    public static String getString(byte[] b, String charset) {
        if (b == null || b.length == 0 || isEmpty(charset))
            return "";
        String s = "";
        try {
            s = new String(b, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将byte数组转为字符串，默认为utf-8编码
     *
     * @param b
     * @return
     */
    public static String getString(byte[] b) {
        return getString(b, "UTF-8");
    }

    /**
     * 检查是否包含中文
     *
     * @param sequence
     * @return
     */
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
     * @param sequence
     * @return
     */
    public static boolean checkName(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    }
    
    /**
     * 计算string的字节数
     * @param a
     * @param charset
     * @return
     */
    public static int getStringBytesCount(String a, String charset) {
    	byte[] arr = MyStringUtils.getBytes(a, charset);
    	if (arr == null)
    		return 0;
    	else 
    		return arr.length;
    }
    
    /**
     * 计算string的字节数
     * @param a
     * @return
     */
    public static int getStringBytesCount(String a) {
    	return MyStringUtils.getStringBytesCount(a, "UTF-8");
    }
    
    /**
     * 获取指定字节长度的字符串
     * @param a
     * @param charset
     * @param len
     * @return
     */
    public static String leftString(String a, String charset, int len) {
    	int cntAll = MyStringUtils.getStringBytesCount(a, charset);
    	if (cntAll <= len)
    		return a;
    	int cntTemp = 0;
    	for (int i=0; i< a.length(); i++) {
    		// subString(int begin, int end)从begin开始，不包含end
    		cntTemp += MyStringUtils.getStringBytesCount(a.substring(i, i+1), charset);
    		if (cntTemp > len) {
    			return a.substring(0, i);
    		}
    	}
    	return a;
    }
    /**
     * 获取指定字节长度的字符串
     * @param a
     * @param len
     * @return
     */
    public static String leftString(String a, int len) {
    	return MyStringUtils.leftString(a, "UTF-8", len);
    }
    
	public static String encodeHtml(String str){
		return StringEscapeUtils.escapeHtml(str);
	}
	
	public static String decodeHtml(String str) {
		return StringEscapeUtils.unescapeHtml(str);
	}
	
	public static String encodeXml(String str){
		return StringEscapeUtils.escapeXml(str);
	}
	
	public static String decodeXml(String str) {
		return StringEscapeUtils.unescapeXml(str);
	}
	
	public static String upperFirstLetter(String str) {
		if (MyStringUtils.isEmpty(str))
			return "";
		return str.substring(0, 1).toUpperCase(Locale.getDefault()) + str.substring(1);
	}
    public static void main(String[] args) {
    	System.out.println(MyStringUtils.upperFirstLetter("a中国123")); // result: 中国1
    	
    }
}
