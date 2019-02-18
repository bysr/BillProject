package hipad.bill.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.mingle.widget.LoadingView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.adapter.RecyclerBillAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.AccountInfo;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;
import hipad.dateutils.DateListener;
import hipad.dateutils.TimeConfig;
import hipad.dateutils.TimeSelectorDialog;

/**
 * 账户详情
 */
public class AccountInfoAty extends BaseActivity implements View.OnClickListener {
    public TextView tv_toolbar_title, tv_toolbar_right;
    public Toolbar mToolbar;
    public ImageView iv_toolbar_right;
    private RecyclerView recycler_view_bill;
    private RecyclerBillAdapter adapter;
    private List<AccountInfo> list;
    private boolean isClickBtime = true;

    private FloatingActionButton fab_Add;


    private float mSelfHeight = 0;  //用以判断是否得到正确的宽高值
    private float mTitleScale;      //标题缩放值
    private float mTestScaleY;      //测试按钮y轴缩放值
    private float mTestScaleX;      //测试按钮x轴缩放值
    private float mTestImageY;  //中间图片Y轴缩放值

    AppBarLayout mAppBar;
    /*时间选择，收入，支出,总金额*/
    RelativeLayout re_bill_time, re_bill_income, re_bill_expense, re_bill_count;
    ImageView img_line;
    //时间
    TextView tv_choose_month, tv_bill_year;
    TextView tv_bill_count, tv_now_month;
    TextView tv_bill_income, tv_income_month;
    TextView tv_bill_expense, tv_expense_month;

    private int cYear, cMonth;

    private AccountBook book;
    private DbManager db;
    private LoadingView loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initView();
        initEvent();
        queryData();
    }

    private void queryData() {
//        loadView.setVisibility(View.VISIBLE);
        BmobQuery<AccountInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("bid", book.getSid());

        query.findObjects(new FindListener<AccountInfo>() {
            @Override
            public void done(List<AccountInfo> list, BmobException e) {

                if (e != null) {
                    //数据同步成功
                } else {
                    for (AccountInfo info : list) {
                        info.setObjId(info.getObjectId());//赋值到本地数据库
                        saveOrUpdate(info);
                    }
                }
//                loadView.setVisibility(View.GONE);

                initData();//本地读取

            }
        });

    }

    /**
     * 保存对象
     *
     * @param info
     */
    private boolean saveOrUpdate(AccountInfo info) {
        try {
            db.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    private void initEvent() {

        re_bill_time.setOnClickListener(this);

        iv_toolbar_right.setOnClickListener(this);

        fab_Add.setOnClickListener(this);

        adapter.setOnItemClickListener(new RecyclerBillAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {

                AccountInfo info = list.get(position);
                Intent intent = new Intent(AccountInfoAty.this, AccRemarkAty.class);
                intent.putExtra("info", info);
                intent.putExtra("book",book);
                startActivity(intent);

            }
        });


    }






    private void initView() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_right = (TextView) findViewById(R.id.tv_toolbar_right);
        iv_toolbar_right = (ImageView) findViewById(R.id.iv_toolbar_right);
        tv_toolbar_right.setVisibility(View.GONE);
        iv_toolbar_right.setVisibility(View.VISIBLE);


        recycler_view_bill = (RecyclerView) findViewById(R.id.recycler_view_bill);
        recycler_view_bill.setHasFixedSize(true);
        recycler_view_bill.setAdapter(adapter);
        recycler_view_bill.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new RecyclerBillAdapter(this, list);
        recycler_view_bill.setAdapter(adapter);
        book = (AccountBook) getIntent().getSerializableExtra("book");
        db = MyApplication.getInstance().getDbManager();


        tv_choose_month = (TextView) findViewById(R.id.tv_choose_month);
        tv_bill_year = (TextView) findViewById(R.id.tv_bill_year);
        tv_bill_count = (TextView) findViewById(R.id.tv_bill_count);
        tv_now_month = (TextView) findViewById(R.id.tv_now_month);
        tv_bill_income = (TextView) findViewById(R.id.tv_bill_income);
        tv_income_month = (TextView) findViewById(R.id.tv_income_month);
        tv_bill_expense = (TextView) findViewById(R.id.tv_bill_expense);
        tv_expense_month = (TextView) findViewById(R.id.tv_expense_month);
        loadView= (LoadingView) findViewById(R.id.loadView);
        /*获取年月*/
        cYear = DateUtils.nowYear();
        cMonth = DateUtils.nowMonth();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置默认值
        initTime();
    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_account_info_aty2;
    }

    private void initToolbar() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.yellow_gray));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        fab_Add = (FloatingActionButton) findViewById(R.id.Fab_Add);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //设置默认不可见
        mToolbar.setNavigationIcon(null);

        //给页面设置工具栏
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbar != null) {
            //设置隐藏图片时候ToolBar的颜色
            collapsingToolbar.setContentScrimColor(Color.parseColor("#FACB29"));
            //设置工具栏标题
//            collapsingToolbar.setTitle("编程是一种信仰");
        }

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        /*支出*/
        re_bill_expense = (RelativeLayout) findViewById(R.id.re_expense);
        /*收入*/
        re_bill_income = (RelativeLayout) findViewById(R.id.re_income);
        /*平移到上方toolBar*/
        re_bill_count = (RelativeLayout) findViewById(R.id.re_bill_count);
        /*消失在人群中*/
        re_bill_time = (RelativeLayout) findViewById(R.id.re_bill_time);
        /*中间图片*/
        img_line = (ImageView) findViewById(R.id.img_line);

        final float screenW = getResources().getDisplayMetrics().widthPixels;
        final float toolbarHeight = getResources().getDimension(R.dimen.tool_bar_height);
        final float initHeight = getResources().getDimension(R.dimen.app_bar_height);
        final float mMargin = getResources().getDimension(R.dimen.activity_horizontal_margin);

        /**
         *   移动效果值／最终效果值 =  移动距离／ 能移动总距离（确定）
         */
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("vivi", "onOffsetChanged: " + verticalOffset);
                isClickBtime = verticalOffset == 0 ? true : false;

                if (mSelfHeight == 0) {

                    //获取标题高度
                    mSelfHeight = re_bill_count.getHeight();

                    //得到标题的高度差
                    float distanceTitle = re_bill_count.getTop() - (toolbarHeight - re_bill_count.getHeight()) / 2.0f;
//                    //得到测试按钮的高度差
                    float distanceTest = /*re_bill_income.getTop()*/re_bill_income.getTop() - (toolbarHeight - re_bill_income.getHeight()) / 2.0f;
//                    float top=ll_income_All.getTop();
//                    float gety=ll_income_All.getY();


//                    //得到图片的高度差
                    float distanceImg = img_line.getY() - (toolbarHeight - img_line.getHeight()) / 2.0f;
//                    //得到测试按钮的水平差值  屏幕宽度一半 - 按钮宽度一半
//                    float distanceSubscribeX = screenW / 2.0f - (re_bill_income.getWidth() / 2.0f);
                    float distanceX = re_bill_income.getLeft() + /*(re_bill_income.getWidth() / 2.0f)*/ -mMargin;


                    //得到高度差缩放比值  高度差／能滑动总长度 以此类推
                    mTitleScale = distanceTitle / (initHeight - toolbarHeight);
                    mTestScaleY = distanceTest / (initHeight - toolbarHeight);
                    mTestImageY = distanceImg / (initHeight - toolbarHeight);
//                    mTestScaleX = distanceSubscribeX / (initHeight - toolbarHeight);
                    mTestScaleX = distanceX / (initHeight - toolbarHeight);


                }
                //得到文本框、头像缩放值 不透明 ->透明  文本框x跟y缩放
                float scale = 1.0f - (-verticalOffset) / (initHeight - toolbarHeight);

                re_bill_time.setScaleX(scale);
                re_bill_time.setScaleY(scale);
                re_bill_time.setAlpha(scale);
//
                img_line.setScaleX(scale);
                img_line.setScaleY(scale);
//                //设置头像y轴平移
                img_line.setTranslationY(mTestImageY * verticalOffset);
                //设置标题y轴平移
                re_bill_count.setTranslationY(mTitleScale * verticalOffset);
//                //设置测试按钮x跟y平移
                re_bill_income.setTranslationY(mTestScaleY * verticalOffset);
                re_bill_income.setTranslationX(mTestScaleX * verticalOffset);
                re_bill_expense.setTranslationY(mTestScaleY * verticalOffset);
                re_bill_expense.setTranslationX(-mTestScaleX * verticalOffset);


            }
        });
    }

    private void initTime() {
        String m = cMonth + "";
        if (m.length() == 1)
            m = "0" + m;
        tv_bill_year.setText(cYear + "年");
        tv_choose_month.setText(m + "月");
        tv_now_month.setText(m + "月结余");
        tv_income_month.setText(m + "月收入");
        tv_expense_month.setText(m + "月支出");

        initData();

    }


    private void initData() {
        list.clear();
        float count = 0f, income = 0f, expend = 0f;
        long start = DateUtils.dateToStamp(DateUtils.getFirstDayOfMonth(cYear, cMonth,null));
        long end = DateUtils.dateToStamp(DateUtils.getLastDayOfMonth(cYear, cMonth,null));

        try {

            count = book.getSum(db, start, end);
            income = book.getSumIn(db, start, end);
            expend = book.getSumEx(db, start, end);
//            List<AccountInfo> list1 = book.getInfo(db);
            List<AccountInfo> mlist = book.getInfo(db, start, end);
            if (mlist != null)
                list.addAll(mlist);
        } catch (DbException e) {
            e.printStackTrace();
        }

        tv_bill_count.setText(count+"");
        tv_bill_income.setText(income+"");
        tv_bill_expense.setText(expend+"");

        adapter.notifyData();


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.re_bill_time:
                if (isClickBtime) {

                    //时间选择器
                    showTime();

                }
                break;

            case R.id.iv_toolbar_right:
                Intent intent = new Intent(this, AccountStatisAty.class);
                intent.putExtra("book",book);
                startActivity(intent);

                break;
            case R.id.Fab_Add:
                Intent intent1 = new Intent(this, EditAccountActivity.class);
                intent1.putExtra("book", book);
                startActivity(intent1);
                break;

        }

    }

    private void showTime() {

        TimeSelectorDialog dialog = new TimeSelectorDialog(this);
        //设置标题
        dialog.setTimeTitle("选择时间:");
        //显示类型
        dialog.setIsShowtype(TimeConfig.YEAR_MONTH);
        //默认时间
        dialog.setCurrentDate(/*"2017-10-11　14:50:11"*/DateUtils.getFirstDayOfMonth(cYear, cMonth,null));
        //隐藏清除按钮
        dialog.setEmptyIsShow(false);
        //设置起始时间
        dialog.setStartYear(2010);
        dialog.setDateListener(new DateListener() {
            @Override
            public void onReturnDate(String time, int year, int month, int day, int hour, int minute, int isShowType) {
                cYear = year;
                cMonth = month + 1;
                initTime();

            }

            @Override
            public void onReturnDate(String empty) {
            }
        });
        dialog.show();
    }


}
