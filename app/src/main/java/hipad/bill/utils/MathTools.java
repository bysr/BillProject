package hipad.bill.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

/**
 * Created by wangyawen on 2017/10/25 0025.
 */

public class MathTools {

    /**
     * 格式化百分比
     *
     * @param num
     * @param baseNum
     * @return 20.1%
     */
    public static String getScale(float num, float baseNum) {

        float percent = num / baseNum;
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(1);
        return nt.format(percent);

    }

    /**
     * Num转化为保留一位小数的字符串
     *
     * @param num
     * @return
     */
    public static String getNumToStr(float num) {

        DecimalFormat df = new DecimalFormat("0.0");//格式化小数
        String s = df.format(num);//返回的是String类型
        return s;
    }


    /**
     * Num转化为保留2位小数的字符串
     *
     * @param num
     * @return
     */
    public static String getNumToStr2(double num) {

        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s = df.format(num);//返回的是String类型
        return s;
    }

    /**
     * 向上取整【整数1000为基本单位】
     *
     * @param sum
     * @return
     */
    public static double getCeil(float sum) {
        float m = sum / 1000;
        return Math.ceil(m) * 1000;
    }

    /**
     * 向下去整【整数以1000为基本单位】
     *
     * @return
     */
    public static double getFloor(float sum) {

        float m = sum / 1000;
        return Math.floor(m) * 1000;

    }

    /**
     * 四舍五入向上取整
     *
     * @param sum
     * @return
     */
    public static String getBigDecimal(float sum) {
        return new BigDecimal(sum).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 获取全局位移的uuid，作为sid
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取该数据的绝对值
     * @param count
     * @return
     */
    public static float getAbs(float count){
        return Math.abs(count);
    }
}
