package com.example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyClass {


    public static void main(String[] args) {

        long time = System.currentTimeMillis();
        System.out.println("" + time);
        System.out.println(getWeekData(1509506103842l));
        System.out.println(getWeekData(1509441800627l));
        System.out.println(getWeekData(1506787200000l));
        System.out.println(getWeekData(1509422399000l));

        System.out.println(getCeil(10125.36f));
        System.out.println(getFloor(-1025.36f));
        System.out.println("四舍五入取整:(2.9)=" +getBigDecimal(2.9f));

        /*System.out.println(dateToStamp(stampToDate(time)));
        System.out.println(monthToStamp("2017", "10"));


        System.out.println(getFirstDayOfMonth(2017, 10));
        System.out.println(getLastDayOfMonth(2017, 10));
        System.out.println(nowYear());
        System.out.println(nowMonth());
*/
    }

    public static String getBigDecimal(float m) {
        return new BigDecimal(m).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static double getCeil(float sum) {
        float m = sum / 1000;
        return Math.ceil(m) * 1000;
    }

    public static double getFloor(float sum) {

        float m = sum / 1000;
        return Math.floor(m) * 1000;

    }


    public static String getWeekData(long time) {
        Calendar cal = Calendar.getInstance();
        cal.clear();


        String week = "星期";

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


    /*
 * 将时间戳转换为时间
 */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }


    /**
     * 日期格式（MM月dd日）
     */
    public static final String DATE_TEMPLATE_MONTH = "MM月dd日";

    /**
     * 获取当前时间时间戳
     *
     * @return
     */
    public static long Timestamp() {
        return System.currentTimeMillis();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        System.out.println(ts + "=====");
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 根据年 月转化为标准时间
     *
     * @param year
     * @param month
     * @return
     */
    public static String monthToStamp(String year, String month) {

        StringBuffer sb = new StringBuffer();
        sb.append(year).append("-").append(month).append("-").append("01").append(" 00:00:00");
        return sb.toString();
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.HOUR, cal.getActualMaximum(Calendar.HOUR));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static String nowYear() {

        return stampToDate(Timestamp(), "yyyy");

    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static String nowMonth() {
        return stampToDate(Timestamp(), "MM");
    }

}
