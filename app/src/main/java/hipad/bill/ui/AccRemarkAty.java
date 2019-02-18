package hipad.bill.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.AccountInfo;
import hipad.bill.constant.SortUtils;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

/**
 * 账单细节备注说明
 */
public class AccRemarkAty extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_edit;
    private ImageView img_bill; //图片
    private TextView tv_bill_name; //名称
    private TextView tv_bill_money; //金额
    private TextView tv_bill_time; //备注时间
    private ImageView img_bill_remark;//备注图片
    private TextView tv_remark_content;

    private AccountInfo info;
    private AccountBook book;//当前账本

    private DbManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            initData();
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    private void initView() {

        showTitle("收入");

        showBackBtn(true);
        nav_img_left = (ImageView) findViewById(R.id.nav_img_left);
        nav_img_left.setImageResource(R.mipmap.fanhuihei);
        nav_right_img = (ImageView) findViewById(R.id.nav_right_img);
        nav_right_img.setImageResource(R.mipmap.delete);
        nav_right_img.setVisibility(View.VISIBLE);

        info = (AccountInfo) getIntent().getSerializableExtra("info");
        book = (AccountBook) getIntent().getSerializableExtra("book");

        img_bill = (ImageView) findViewById(R.id.img_bill);
        img_bill_remark = (ImageView) findViewById(R.id.img_bill_remark);
        tv_bill_money = (TextView) findViewById(R.id.tv_bill_money);
        tv_bill_name = (TextView) findViewById(R.id.tv_bill_name);
        tv_bill_time = (TextView) findViewById(R.id.tv_bill_time);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        tv_remark_content = (TextView) findViewById(R.id.tv_remark_content);

        db = MyApplication.getInstance().getDbManager();


    }

    private void initData() throws DbException {
        DbManager db = MyApplication.getInstance().getDbManager();
        info = info.getInfo(db);

        tv_bill_time.setText(DateUtils.stampToDate(info.getBill_time(), DateUtils.DATE_TEMPLATE_DEFAULT));
        tv_remark_content.setText(info.getBill_remark());
        SortUtils utils = SortUtils.get(info.getType());
        tv_bill_money.setText(info.getBill_money() + "");
        tv_bill_money.setTextColor(Color.parseColor(utils.getColor()));
        tv_bill_name.setText(utils.getName());
        SortUtils.loadDrawable(img_bill, this, utils.getCode());
    }

    private void initEvent() {

        ll_edit.setOnClickListener(this);
        nav_right_img.setOnClickListener(this);


    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_acc_remark_aty;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_edit:
                Intent intent = new Intent(this, EditAccountActivity.class);
                intent.putExtra("info", info);
                intent.putExtra("book", book);
                startActivityForResult(intent, 2);
                break;
            case R.id.nav_right_img:
                //删除
                delService();
                break;

        }
    }


    private void delService() {
        info.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {

                if (e == null) {
                    if (delBook()) {
                        //删除成功
                        Toast.makeText(AccRemarkAty.this, "删除成功", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    //删除失败
                    info.setbState(-1);
                    //要删除的数据，服务器不通，暂时先备注
                    try {
                        db.saveOrUpdate(info);
                    } catch (DbException e1) {
                        e1.printStackTrace();
                    }
                }


                finish();

            }
        });
    }


    /**
     * 删除数据
     *
     * @return
     */
    private boolean delBook() {
        boolean b = true;
        try {
            db.delete(info);
        } catch (DbException e) {
            b = false;
            e.printStackTrace();
        }
        return b;
    }


}
