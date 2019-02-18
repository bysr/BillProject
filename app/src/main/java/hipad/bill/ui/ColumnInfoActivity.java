package hipad.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.adapter.ColumnInfoAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.AccountInfo;
import hipad.bill.constant.SortUtils;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

/**
 * 栏目详情
 */
public class ColumnInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_up_month, iv_down_month;
    private RecyclerView recycler_column;
    private List<AccountInfo> list;
    private ColumnInfoAdapter adapter;
    private TextView tv_bill_month;

    private int cYear, cMonth, type;//年月
    private AccountBook book;//账本信息
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
        initData();
    }

    private void initView() {

        book = (AccountBook) getIntent().getSerializableExtra("book");
        cYear = getIntent().getIntExtra("year", DateUtils.nowYear());
        cMonth = getIntent().getIntExtra("month", DateUtils.nowMonth());
        type = getIntent().getIntExtra("type", -1);

        iv_up_month = (ImageView) findViewById(R.id.iv_left_month);
        iv_down_month = (ImageView) findViewById(R.id.iv_right_month);
        tv_bill_month = (TextView) findViewById(R.id.tv_bill_month);
        showBackBtn(true);
        nav_img_left.setImageResource(R.mipmap.fanhuihei);

        recycler_column = (RecyclerView) findViewById(R.id.recycler_column);

        list = new ArrayList<>();
        adapter = new ColumnInfoAdapter(this, list, type);
        recycler_column.setAdapter(adapter);

        recycler_column.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_column.setItemAnimator(new DefaultItemAnimator());

        setHeader(recycler_column);
        adapter.setOnItemClickListener(new ColumnInfoAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {

                AccountInfo info = list.get(position);
                Intent intent = new Intent(ColumnInfoActivity.this, AccRemarkAty.class);
                intent.putExtra("info", info);
                startActivity(intent);


            }
        });
        //接收上页数据


        SortUtils sortUtils = SortUtils.get(type);
        showTitle(sortUtils.getName());
        db = MyApplication.getInstance().getDbManager();

    }


    private void initData() {
        GenerateData generateData = new GenerateData().invoke();
        List<AccountInfo> list = generateData.getList();
        float count = generateData.getCount();
        float tCount = generateData.gettCount();

        adapter.addDatas(list, count, tCount);


    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.item_column_head, view, false);
        adapter.setHeaderView(header);
    }

    private void initEvent() {

        iv_down_month.setOnClickListener(this);
        iv_up_month.setOnClickListener(this);


    }


    @Override
    public int LayoutResId() {
        return R.layout.activity_column_info;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_left_month:
                if (cMonth == 1) {
                    Toast.makeText(this, "不能超过本年度", Toast.LENGTH_SHORT).show();
                    return;
                }
                cMonth--;
                break;
            case R.id.iv_right_month:
                if (cMonth == 12) {
                    Toast.makeText(this, "不能超过本年度", Toast.LENGTH_SHORT).show();
                    return;
                }
                cMonth++;
                break;

        }
        initData();//更新数据

    }

    private class GenerateData {
        private List<AccountInfo> list;
        private float count;
        private float tCount;

        public List<AccountInfo> getList() {
            return list;
        }

        public float getCount() {
            return count;
        }

        public float gettCount() {
            return tCount;
        }

        public GenerateData invoke() {
            //修改月份导致的是title变化
            tv_bill_month.setText(DateUtils.getFirstDayOfMonth(cYear, cMonth, DateUtils.DATE_TEMPLATE_MONTH)
                    + " - " + DateUtils.getLastDayOfMonth(cYear, cMonth, DateUtils.DATE_TEMPLATE_MONTH));
            //更新集合数据


           /*查询数据库*/
            long start = DateUtils.dateToStamp(DateUtils.getFirstDayOfMonth(cYear, cMonth, null));
            long end = DateUtils.dateToStamp(DateUtils.getLastDayOfMonth(cYear, cMonth, null));
            list = null;
            count = 0f;
            tCount = 0f;

            try {
                count = book.getSumIn(db, start, end);//月收入账单
                tCount = book.getSumIn(db, start, end, type);//某一类账单收入
                list = book.getInfo(db, start, end, type);//这个是查询出所有账单，要求是查询出
            } catch (DbException e) {
                e.printStackTrace();
            }
            return this;
        }
    }
}
