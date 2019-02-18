package hipad.bill.constant;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/25 0025.
 */

public enum SortUtils {

    //收入类型
    PAY(1, "工资", "#07c460", 1), RED_PACKET(2, "红包", "#ff4949", 1), JOB_TIME(3, "兼职", "#0749ff", 1), INVEST(4, "投资", "#10AEFF", 1), BONUS(5, "奖金", "#FEBB50", 1),

    //支出类型
    EAT(6, "吃喝", "#FEBB50", 2), TRAFFIC(7, "交通", "#07C460", 2), CLOTH(8, "服饰", "#7049FF", 2), PLEASURE(9, "日用品", "#4985FF", 2), FANCY(10, "化装护肤", "#FF49DB", 2),
    BUY_FOOD(11, "买菜", "#4AC407", 2), DISPORT(12, "娱乐", "#D157FD", 2), RENT(13, "房租", "#D38C59", 2), RED_PA(14, "红包", "#FF4949", 2), SHOPPING(15, "网购", "#FEBB50", 2),
    BILL(16, "话费", "#10AEFF", 2), CHILDREN(17, "孩子", "#D38C59", 2), MEDICAL(18, "医疗", "#FF4949", 2), PET(19, "宠物", "#FEBB50", 2), WATER(20, "水电", "#07C460", 2),
    DIGITAL(21, "数码", "#4985FF", 2),
    /*其他类型*/
    OTHER(0, "其他", "#D38C59", 0);

    final int code;
    final String name;
    final String color;
    final int type;//类型，收入和支出两部分 0 其他 1. 收入 2.支出
    private boolean isSelect;//选中

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    SortUtils(int code, String name, String color, int type) {
        this.code = code;
        this.name = name;
        this.color = color;
        this.type = type;
    }

    // 通过code 获取name
    public static String getName(int index) {
        for (SortUtils c : SortUtils.values()) {
            if (c.getCode() == index) {
                return c.name;
            }
        }
        return null;
    }

    /**
     * 获取枚举对象
     *
     * @param index
     * @return
     */
    public static SortUtils get(int index) {
        for (SortUtils c : SortUtils.values()) {
            if (c.getCode() == index) {
                return c;
            }
        }
        return null;
    }


    public int getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 填充图片，选中图片
     *
     * @param iv
     * @param context
     * @param code
     */
    public static void loadDrawable(ImageView iv, Context context, int code) {

        int mipmap = R.mipmap.qita2;
        switch (code) {
            case 0:
                mipmap = R.mipmap.qita2;
                break;
            case 1:
                mipmap = R.mipmap.gongzi2;

                break;
            case 2:
                mipmap = R.mipmap.hongbao2;
                break;
            case 3:
                mipmap = R.mipmap.jianzhi2;
                break;
            case 4:
                mipmap = R.mipmap.touzi2;
                break;

            case 5:
                mipmap = R.mipmap.jiangjin2;

                break;
            case 6://日用品

                mipmap = R.mipmap.chihe2;
                break;
            case 7:
                mipmap = R.mipmap.jiaotong2;
                break;
            case 8:
                mipmap = R.mipmap.fushi2;
                break;
            case 9:
                mipmap = R.mipmap.riyongpin2;
                break;
            case 10:
                mipmap = R.mipmap.huazhuang2;
                break;

            case 11:
                mipmap = R.mipmap.maicai2;
                break;
            case 12:
                mipmap = R.mipmap.yule2;
                break;

            case 13:
                mipmap = R.mipmap.fangzu2;
                break;
            case 14:
                mipmap = R.mipmap.hongbao2;
                break;

            case 15:
                mipmap = R.mipmap.wanggou2;
                break;

            case 16:
                mipmap = R.mipmap.huafei2;
                break;
            case 17:
                mipmap = R.mipmap.haiz2;
                break;

            case 18:
                mipmap = R.mipmap.yiliao2;
                break;

            case 19:
                mipmap = R.mipmap.chongwu2;
                break;
            case 20:
                mipmap = R.mipmap.shuidian2;
                break;
            case 21:
                mipmap = R.mipmap.shuma2;
                break;


        }
        iv.setImageResource(mipmap);

    }

    /**
     * 添加默认图片
     *
     * @param iv
     * @param context
     * @param code
     */
    public static void defaultDrawable(ImageView iv, Context context, int code) {

        int mipmap = R.mipmap.qita1;
        switch (code) {
            case 0:
                mipmap = R.mipmap.qita1;
                break;
            case 1:
                mipmap = R.mipmap.gongzi1;

                break;
            case 2:
                mipmap = R.mipmap.hongbao1;
                break;
            case 3:
                mipmap = R.mipmap.jianzhi1;
                break;
            case 4:
                mipmap = R.mipmap.touzi1;
                break;

            case 5:
                mipmap = R.mipmap.jiangjin1;

                break;
            case 6://日用品

                mipmap = R.mipmap.chihe1;
                break;
            case 7:
                mipmap = R.mipmap.jiaotong1;
                break;
            case 8:
                mipmap = R.mipmap.fushi1;
                break;
            case 9:
                mipmap = R.mipmap.riyongpin1;
                break;
            case 10:
                mipmap = R.mipmap.huazhuang1;
                break;

            case 11:
                mipmap = R.mipmap.maicai1;
                break;
            case 12:
                mipmap = R.mipmap.yule1;
                break;

            case 13:
                mipmap = R.mipmap.fangzu1;
                break;
            case 14:
                mipmap = R.mipmap.hongbao1;
                break;

            case 15:
                mipmap = R.mipmap.wanggou1;
                break;

            case 16:
                mipmap = R.mipmap.huafei1;
                break;
            case 17:
                mipmap = R.mipmap.haizi1;
                break;

            case 18:
                mipmap = R.mipmap.yiliao1;
                break;

            case 19:
                mipmap = R.mipmap.chongwu1;
                break;
            case 20:
                mipmap = R.mipmap.shuidian1;
                break;
            case 21:
                mipmap = R.mipmap.shuma1;
                break;


        }
        iv.setImageResource(mipmap);

    }


    public String getColor() {
        if (TextUtils.isEmpty(color))
            return "#000000";
        return color;
    }
}
