package hipad.bill.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user_wen on 2017/10/29.
 */

public class DateUtils {

    /**
     * 一分钟的毫秒值
     */
    public static final long ONE_MINUTE = 60 * 1000;
    /**
     * 一小时的毫秒值
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    /**
     * 一天的毫秒值
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;
    /**
     * 一月的毫秒值
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;
    /**
     * 一年的毫秒值
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;
    /**
     * 日期格式（yyyy-MM-dd HH:mm:ss）
     */
    public static final String DATE_TEMPLATE_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式（yyyy）
     */
    public static final String DATE_YEAR = "yyyy";
    /**
     * 日期格式（MM）
     */
    public static final String DATE_MONTH = "MM";
    /**
     * 日期格式HH:mm:ss
     */
    public static final String DATE_TEMPLATE_HOUR = "HH:mm:ss";
    /**
     * 日期格式（yyyy-MM-dd）
     */
    public static final String DATE_TEMPLATE_DATE = "yyyy-MM-dd";
    /**
     * 日期格式（yyyy.MM.dd）
     */
    public static final String DATE_TEMPLATE_DATE_POINT = "yyyy.MM.dd";
    /**
     * 日期格式（yyyy-MM-dd E HH:mm）
     */
    public static final String DATE_TEMPLATE_WEEK = "yyyy-MM-dd E HH:mm";
    /**
     * 日期格式（MM月dd日）
     */
    public static final String DATE_TEMPLATE_MONTH = "MM月dd日";
    /**
     * 日期格式（dd日）
     */
    public static final String DATE_TEMPLATE = "dd日";


    /**
     * 获取当前时间时间戳
     *
     * @return
     */
    public static long Timestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int nowYear() {

        return Integer.valueOf(stampToDate(Timestamp(), DATE_YEAR));

    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int nowMonth() {
        return Integer.valueOf(stampToDate(Timestamp(), DATE_MONTH));
    }


    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis, String format) {

        if (TextUtils.isEmpty(format))
            format = DATE_TEMPLATE_MONTH;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间转换为时间戳[时间格式“2017-10-01 00:00:00”]
     */
    public static long dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 时间戳转化为时间
     * @param s
     * @return
     */
    public static long dateToStamp(String s,String format) {
        if (format==null)
            return dateToStamp(s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 一月最后一天【2017-10-31 23:59:59】
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month, String format) {
        if (TextUtils.isEmpty(format))
            format = DATE_TEMPLATE_DEFAULT;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        /*24小时制*/
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 一年最后一天
     * @param year
     * @param format
     * @return
     */
    public static String getLastDayOfYear(int year,  String format) {
        if (TextUtils.isEmpty(format))
            format = DATE_TEMPLATE_DEFAULT;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        /*24小时制*/
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 一月第一天【2017-10-01 00:00:00】
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month, String format) {
        if (TextUtils.isEmpty(format))
            format = DATE_TEMPLATE_DEFAULT;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 一年最后一天
     * @param year
     * @param format
     * @return
     */
    public static String getFirstDayOfYear(int year, String format) {
        if (TextUtils.isEmpty(format))
            format = DATE_TEMPLATE_DEFAULT;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 时间戳转化为星期
     * @param time
     * @return
     */
    public static String getWeekData(long time) {
        Calendar cal = Calendar.getInstance();
        cal.clear();


        String week = "";

        cal.setTimeInMillis(time);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {

            case 1:
                return week + "日";

            case 2:
                return week + "一";
            case 3:
                return week + "二";
            case 4:
                return week + "三";
            case 5:
                return week + "四";
            case 6:
                return week + "五";
            case 7:
                return week + "六";
        }

        return null;
    }


}
