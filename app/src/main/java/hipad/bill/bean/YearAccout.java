package hipad.bill.bean;

import java.io.Serializable;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 年账单
 */

public class YearAccout implements Serializable {
    private int year;//年份
    /*收入*/
    private float income;
    /*支出*/
    private float expend;

    public YearAccout() {
    }

    public YearAccout(int year, float income, float expend) {
        this.year = year;
        this.income = income;
        this.expend = expend;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getExpend() {
        return expend;
    }

    public void setExpend(float expend) {
        this.expend = expend;
    }
}
