package hipad.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.adapter.RecordAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.MonthAccout;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

/**
 * 流水统计账目
 */
public class AccountStatisAty extends BaseActivity {
    RecyclerView recycler_statistics;

    RelativeLayout re_year;
    TextView tv_year;

    RecordAdapter adapter;
    List<MonthAccout> list;
    private int cYear, cMonth;

    private DbManager db;
    private AccountBook book;//账本信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initEvent();

    }

    private void initView() {


        showTitle(getString(R.string.account_statis));
        showBackBtn(true);
        nav_img_left.setImageResource(R.mipmap.fanhuihei);
        recycler_statistics = (RecyclerView) findViewById(R.id.recycler_statistics);

        re_year = (RelativeLayout) findViewById(R.id.re_year);
        tv_year = (TextView) findViewById(R.id.tv_year);


        //-----------------------

        list = new ArrayList<>();
        adapter = new RecordAdapter(list, this);
        recycler_statistics.setAdapter(adapter);

        recycler_statistics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_statistics.setItemAnimator(new DefaultItemAnimator());


        setHeader(recycler_statistics);
        adapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, MonthAccout data) {

                if (data.getExpend() == 0 && data.getIncome() == 0) {
                    //无效
                } else {
                    Intent intent = new Intent(AccountStatisAty.this, AccountMonthAty.class);
                    intent.putExtra("year", cYear);
                    intent.putExtra("month", cMonth);
                    intent.putExtra("book", book);
                    startActivity(intent);
                }
            }
        });

        //获取默认值
        cYear = DateUtils.nowYear();//当前
        cMonth = DateUtils.nowMonth();
        db = MyApplication.getInstance().getDbManager();
        book = (AccountBook) getIntent().getSerializableExtra("book");
    }


    private void initEvent() {
        re_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountStatisAty.this, YearSelectAty.class);
                intent.putExtra("book",book);
                startActivityForResult(intent, 2);
            }
        });

    }

    private void initData() {
        tv_year.setText(cYear + "年");
        adapter.addDatas(generateData());
    }

    private List<MonthAccout> generateData() {

        int monSize = cMonth;//默认查询当前年，当前月之前的数据
        if (cYear - DateUtils.nowYear() < 0) {
            monSize = 12;
        }
        List<MonthAccout> mList = new ArrayList<>();//先判断当前时间是否超过限制
        for (int i = 0; i < monSize; i++) {

            float income = 0f, expend = 0f;
            long start = DateUtils.dateToStamp(DateUtils.getFirstDayOfMonth(cYear, i + 1,null));
            long end = DateUtils.dateToStamp(DateUtils.getLastDayOfMonth(cYear, i + 1,null));

            try {
                income = book.getSumIn(db, start, end);
                expend = book.getSumEx(db, start, end);
            } catch (DbException e) {
                e.printStackTrace();
            }
            MonthAccout accout = new MonthAccout(i, income, expend);
            mList.add(accout);
        }
        return mList;

    }


    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.item_head_chart, view, false);
        adapter.setHeaderView(header);
    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_account_statis_aty;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                cYear = data.getIntExtra("year", -1);
                initData();
            }

        }


    }
}
