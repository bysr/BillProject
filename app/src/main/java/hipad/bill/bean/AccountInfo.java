package hipad.bill.bean;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import cn.bmob.v3.BmobObject;

/**
 * Created by wangyawen on 2017/10/21 0021.
 * 账单详情
 */
@Table(name = "account")
public class AccountInfo extends BmobObject {

//    @Column(name = "id", isId = true)
//    private long id;
    /*账单消费金额*/
    @Column(name = "money")
    private float bill_money;

    /*备注*/
    @Column(name = "remark")
    private String bill_remark;
    /*账单记录时间*/
    @Column(name = "time")
    private long bill_time;
    /*关联账本id*/
    @Column(name = "bid")
    private String bid;
    /*服务器id*/
    @Column(name = "sid", isId = true, autoGen = false)
    private String sid;

    /*账单属性对象*/
    @Column(name = "type")
    private int type;

    @Column(name="url")
    private String imgUrl;

    //新增账本同步问题，对未同步的数据处理
    @Column(name = "state")
    private int bState;//0 最新同步状态  -1  删除数据   1.添加数据  2. 修改数据
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
     * 查询账本
     * @param db
     * @return
     * @throws DbException
     */
    public AccountBook getBook(DbManager db) throws DbException {
        return db.findById(AccountBook.class, bid);
    }

    public AccountInfo getInfo(DbManager db) throws DbException{
        return db.findById(AccountInfo.class,this.sid);

    }


    public AccountInfo() {
    }

    public AccountInfo(int type) {
        this.type = type;
    }

    public AccountInfo(float bill_money, String bill_remark, int bill_time,int bState) {
        this.bill_money = bill_money;
        this.bill_remark = bill_remark;
        this.bill_time = bill_time;
        this.bState=bState;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getbState() {
        return bState;
    }

    public void setbState(int bState) {
        this.bState = bState;
    }


    public float getBill_money() {
        return bill_money;
    }

    public String getBill_remark() {
        return bill_remark;
    }

    public void setBill_remark(String bill_remark) {
        this.bill_remark = bill_remark;
    }

    public long getBill_time() {
        return bill_time;
    }

    public void setBill_time(long bill_time) {
        this.bill_time = bill_time;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBill_money(float bill_money) {
        this.bill_money = bill_money;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "bill_money=" + bill_money +
                ", bill_remark='" + bill_remark + '\'' +
                ", bill_time=" + bill_time +
                ", bid='" + bid + '\'' +
                ", sid='" + sid + '\'' +
                ", type=" + type +
                ", imgUrl='" + imgUrl + '\'' +
                ", bState=" + bState +
                ", objId='" + objId + '\'' +
                '}';
    }
}
