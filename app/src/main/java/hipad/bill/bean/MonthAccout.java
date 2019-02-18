package hipad.bill.bean;

import java.io.Serializable;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 月账单
 */

public class MonthAccout implements Serializable {
    private int month;//月份
    /*收入*/
    private float income;
    /*支出*/
    private float expend;

    public MonthAccout() {
    }

    public MonthAccout(int month, float income, float expend) {
        this.month = month;
        this.income = income;
        this.expend = expend;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
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
