package hipad.bill.bean;

import java.io.Serializable;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 日账单
 */

public class DateAccout implements Serializable {
    private String data;//日，一天
    /*收入*/
    private float income;
    /*支出*/
    private float expend;

    public DateAccout() {
    }

    public DateAccout(String data, float income, float expend) {
        this.data = data;
        this.income = income;
        this.expend = expend;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
