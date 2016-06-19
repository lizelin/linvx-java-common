package net.linvx.java.libs.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by lizelin on 16/2/5.
 * 日期的相关常用函数
 */
public class MyDateUtils {
    private MyDateUtils() {
    }

    /**
     * 转换字符串为Date
     *
     * @param gmt，格式："Sun, 13-Dec-2082 19:41:09 GMT"
     * @return
     * @throws ParseException
     */
    public static Date parseGMTString(String gmt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(gmt);
        return date;
    }

    /**
     * 日期格式化：Sun, 13-Dec-2082 19:41:09 GMT
     * @param date
     * @return 
     */
    public static String dateToGMTString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    /**
     * 日期转字符串：yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String dateToString(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    /**
     * 日期相差的毫秒数
     * @param before
     * @param after
     * @return
     */
    public static long diffMilliseconds(Date before, Date after) {
        return after.getTime() - before.getTime();
    }

    /**
     * 日期相差的秒数
     * @param before
     * @param after
     * @return
     */
    public static long diffSeconds(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L;
    }

    /**
     * 日期相差的分钟数
     * @param before
     * @param after
     * @return
     */
    public static long diffMinutes(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L;
    }

    /**
     * 日期相差的小时数
     * @param before
     * @param after
     * @return
     */
    public static long diffHours(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L / 60L;
    }

    /**
     * 日期相差的天数
     * @param before
     * @param after
     * @return
     */
    public static long diffDays(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L / 60L / 24L;
    }

    /**
     * 获取时间戳字符串
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取时间戳字符串，格式：yyyy年MM月dd日 HH时mm分ss秒.SSSS
     * @return
     */
    public static String getTimeStampFormatString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒.SSSS");
        return sdf.format(new Date());
    }
    
    /**
     * long转化为日期
     * @param ldt 自1970的豪秒数
     * @return
     */
    public static Date millsLong2Date(long ldt) {
    	return new Date(ldt);
    }
    
    /**
     * 日期转化为long（结果为自1970的豪秒数）
     * @param dt
     * @return
     */
    public static long date2MillsLong(Date dt) {
    	return dt.getTime();
    }
    
    /**
     * long转化为日期
     * @param ldt 自1970的秒数
     * @return
     */
    public static Date secondLong2Date(long ldt) {
    	return new Date(ldt*1000l);
    }
    
    /**
     * 日期转化为long（结果为自1970的秒数）
     * @param dt
     * @return
     */
    public static long date2SecondLong(Date dt) {
    	return dt.getTime()/1000l;
    }
    
    /**
     * 日期转字符串
     * @param str  格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String str) {  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = null;  
        try {  
            date = format.parse(str);   
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
                                              
        return date;  
    }  
    
    public static Timestamp toTimestatmp(long millis) {
    	return new Timestamp(millis);
    }
    
    public static Timestamp now() {
    	return new Timestamp(System.currentTimeMillis());
    }
    
    public static void main(String[] args){
    	System.out.println(stringToDate("2016-12-01 12:03:23"));
    }
}
