package net.linvx.java.libs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizelin on 16/3/29.
 */
public class MyNumberUtils {
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(String.format("%saaa%s", "dddd", "eeee"));
    }
}
