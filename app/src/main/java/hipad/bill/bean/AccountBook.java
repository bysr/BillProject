package hipad.bill.bean;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by wangyawen on 2017/10/20 0020.
 * 账本对象
 */
@Table(name = "book")
public class AccountBook extends BmobObject {
    //    @Column(name = "id", isId = true)
//    private long androidBookId;
    /*账本名称*/
    @Column(name = "name")
    private String accountName;
    /*账本时间*/
    @Column(name = "time")
    private long accountTime;
    /*账本属性*/
    @Column(name = "type")
    private String accountType;
    /*操作时间,用于排序*/
    @Column(name = "position")
    private long accountPosition;
    /*服务器id*/
    @Column(name = "sid", isId = true, autoGen = false)
    private String accountBookId;
    /*用户id*/
    @Column(name = "uid")
    private String userId;

    //新增账本同步问题，对未同步的数据处理
    @Column(name = "state")
    private int bState;//同步状态，0 最新同步状态  -1  删除数据   1.添加数据  2. 修改数据
    @Column(name = "objId")
    private String objId;//Bmob数据库

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    //复写objectId获取方法
    @Override
    public String getObjectId() {
        if (objId == null)
            objId = super.getObjectId();
        return objId;
    }

    /**
     * 查询所有
     *
     * @param db
     * @return
     * @throws DbException
     */
    public List<AccountInfo> getInfo(DbManager db) throws DbException {
        return db.selector(AccountInfo.class).where("bid", "='", this.accountBookId+"'").findAll();
    }

    /**
     * 查询规定时间时间
     *
     * @param db
     * @param staTime 开始时间
     * @param endTime 结束时间
     * @return 订货单
     * @throws DbException
     */
    public List<AccountInfo> getInfo(DbManager db, long staTime, long endTime) throws DbException {
        return db.selector(AccountInfo.class).where("bid", "= ", ""+this.accountBookId+"").and("time", ">", staTime).and("time", "<", endTime).orderBy("time", false).findAll();
    }

    /**
     * 查询规定时间类某个属性对象的集合
     *
     * @param db
     * @param staTime 开始时间
     * @param endTime 结束时间
     * @param type    属性值
     * @return
     * @throws DbException
     */
    public List<AccountInfo> getInfo(DbManager db, long staTime, long endTime, int type) throws DbException {
        return db.selector(AccountInfo.class).where("bid", "=", this.accountBookId+"").and("time", ">", staTime).and("time", "<", endTime).and("type", "=", type).orderBy("time", false).findAll();
    }

    /**
     * 查询收入集合对象
     *
     * @param db
     * @param staTime
     * @param endTime
     * @return
     * @throws DbException
     */
    public List<AccountInfo> getInfoPos(DbManager db, long staTime, long endTime) throws DbException {
        return db.selector(AccountInfo.class).where("bid", "=", this.accountBookId+"").and("time", ">", staTime).and("time", "<", endTime).and("money", ">=", 0).orderBy("time", false).findAll();
    }

    /**
     * 查询支出集合对象
     *
     * @param db
     * @param staTime
     * @param endTime
     * @return
     * @throws DbException
     */
    public List<AccountInfo> getInfoNeg(DbManager db, long staTime, long endTime) throws DbException {
        return db.selector(AccountInfo.class).where("bid", "=", this.accountBookId).and("time", ">", staTime).and("time", "<", endTime).and("money", "<", 0).orderBy("time", false).findAll();
    }


    /**
     * 查询总和
     *
     * @param db
     * @param staTime
     * @param endTime
     * @return
     * @throws DbException
     */
    public float getSum(DbManager db, long staTime, long endTime) throws DbException {
        Cursor cursor;
        String sth = "";
        cursor = db.execQuery("select sum(money) from account where time between " + staTime + " and " + endTime + " and bid = '" + this.accountBookId+"'");
        if (cursor.moveToNext()) { //定位到下一个
            sth = cursor.getString(0);
        }
        return Float.valueOf(TextUtils.isEmpty(sth) ? "0" : sth);
    }

    /**
     * 获取收入总和
     *
     * @param db
     * @param staTime
     * @param endTime
     * @return
     * @throws DbException
     */
    public float getSumIn(DbManager db, long staTime, long endTime) throws DbException {
        Log.d("book", staTime + "====" + endTime);
        Cursor cursor;
        String sth = "";
        cursor = db.execQuery("select sum(money) from account where time between " + staTime + " and " + endTime + " and bid = '" + this.accountBookId + "' and money > 0");
        if (cursor.moveToNext()) { //定位到下一个
            sth = cursor.getString(0);
        }
        return Float.valueOf(TextUtils.isEmpty(sth) ? "0" : sth);
    }

    /**
     * 获取某一类型账单的月收入
     *
     * @param db
     * @param staTime
     * @param endTime
     * @param type
     * @return
     * @throws DbException
     */
    public float getSumIn(DbManager db, long staTime, long endTime, int type) throws DbException {
        Log.d("book", staTime + "====" + endTime);
        Cursor cursor;
        String sth = "";
        cursor = db.execQuery("select sum(money) from account where time between " + staTime + " and " + endTime + " and bid = '" + this.accountBookId + "' and money > 0 and type = " + type);
        if (cursor.moveToNext()) { //定位到下一个
            sth = cursor.getString(0);
        }
        return Float.valueOf(TextUtils.isEmpty(sth) ? "0" : sth);
    }

    /**
     * 获取支出总和
     *
     * @param db
     * @param staTime
     * @param endTime
     * @return
     * @throws DbException
     */
    public float getSumEx(DbManager db, long staTime, long endTime) throws DbException {
        Cursor cursor;
        String sth = "";
        cursor = db.execQuery("select sum(money) from account where time between " + staTime + " and " + endTime + " and bid = '" + this.accountBookId + "' and money < 0");
        if (cursor.moveToNext()) { //定位到下一个
            sth = cursor.getString(0);
        }
        return Float.valueOf(TextUtils.isEmpty(sth) ? "0" : sth);
    }


    public AccountBook() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount_Name() {
        return accountName;
    }

    public void setAccount_Name(String account_Name) {
        this.accountName = account_Name;
    }

    public long getAccount_time() {
        return accountTime;
    }

    public void setAccount_time(long account_time) {
        this.accountTime = account_time;
    }

    public String getAccount_type() {
        return accountType;
    }

    public void setAccount_type(String account_type) {
        this.accountType = account_type;
    }

    public long getPosition() {
        return accountPosition;
    }

    public void setPosition(long position) {
        this.accountPosition = position;
    }

    public String getSid() {
        return accountBookId;
    }

    public void setSid(String sid) {
        this.accountBookId = sid;
    }

    public int getbState() {
        return bState;
    }

    public void setbState(int bState) {
        this.bState = bState;
    }

    @Override
    public String toString() {
        return "AccountBook{" +
                "accountName='" + accountName + '\'' +
                ", accountTime=" + accountTime +
                ", accountType='" + accountType + '\'' +
                ", accountPosition=" + accountPosition +
                ", accountBookId='" + accountBookId + '\'' +
                ", userId=" + userId +
                ", bState=" + bState +
                ", objId='" + objId + '\'' +
                '}';
    }
}
