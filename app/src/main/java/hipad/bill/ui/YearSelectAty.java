package hipad.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.adapter.YearSelectAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.YearAccout;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

/**
 * 年份选择页面
 */
public class YearSelectAty extends BaseActivity {

    private RecyclerView recycler_year;
    private YearSelectAdapter adapter;

    private List<YearAccout> list;
    private AccountBook book;
    private DbManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intView();
        initData();
        initEvent();

    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_year_select_aty;
    }

    private void intView() {
        book = (AccountBook) getIntent().getSerializableExtra("book");
        db = MyApplication.getInstance().getDbManager();
        showBackBtn(true);
        showTitle("选择年份");
        nav_img_left.setImageResource(R.mipmap.fanhuihei);
        recycler_year= (RecyclerView) findViewById(R.id.recycler_year);
        list = new ArrayList<>();
        adapter = new YearSelectAdapter(list, this);
        recycler_year.setAdapter(adapter);

        recycler_year.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_year.setItemAnimator(new DefaultItemAnimator());

        try {
            adapter.addDatas(generateData());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setHeader(recycler_statistics);
        adapter.setOnItemClickListener(new YearSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, YearAccout data) {
               Intent intent=new Intent();
                intent.putExtra("year",data.getYear());
                setResult(RESULT_OK,intent);
                finish();

            }
        });


    }
    private List<YearAccout> generateData() throws Exception{


        //获取最近五年的数据
        List<YearAccout> mList = new ArrayList<>();

        int cYear = DateUtils.nowYear();

        for (int i = cYear - 5; i <= cYear; i++) {
            float income = 0f, expend = 0f;
            long start = DateUtils.dateToStamp(DateUtils.getFirstDayOfYear(i, null));
            long end = DateUtils.dateToStamp(DateUtils.getLastDayOfYear(i, null));
            income=book.getSumIn(db,start,end);
            expend=book.getSumEx(db,start,end);
            YearAccout accout = new YearAccout(i, income,expend);
            mList.add(accout);
        }
        return mList;

    }

    private void initData() {
    }

    private void initEvent() {
    }
}
