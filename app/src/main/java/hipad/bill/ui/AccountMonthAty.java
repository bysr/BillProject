package hipad.bill.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import hipad.bill.BaseActivity;
import hipad.bill.bean.AccountBook;
import hipad.bill.ui.fragment.ExpendFragment;
import hipad.bill.ui.fragment.IncomeFragment;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

public class AccountMonthAty extends BaseActivity implements ExpendFragment.OnFragmentInteractionListener, IncomeFragment.OnFragmentInteractionListener, View.OnClickListener {


    private RadioButton rbIncome, rbExpend;
    private ImageView iv_up_month, iv_down_month;

    private AccountBook book;//账本信息
    private int cYear, cMonth;//当前年月
    private TextView tv_bill_month;//显示时间区域


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        initView();
        initData();
        initEvent();
    }


    private void initView() {

        rbIncome = (RadioButton) findViewById(R.id.rb_bill_income);
        rbExpend = (RadioButton) findViewById(R.id.rb_bill_expend);
        iv_up_month = (ImageView) findViewById(R.id.iv_left_month);
        iv_down_month = (ImageView) findViewById(R.id.iv_right_month);
        tv_bill_month = (TextView) findViewById(R.id.tv_bill_month);

        book = (AccountBook) getIntent().getSerializableExtra("book");
        cYear = getIntent().getIntExtra("year", DateUtils.nowYear());
        cMonth = getIntent().getIntExtra("month", DateUtils.nowMonth());


    }

    private void initData() {
        //修改月份导致的是title变化
        tv_bill_month.setText(DateUtils.getFirstDayOfMonth(cYear, cMonth, DateUtils.DATE_TEMPLATE_MONTH)
                + " - " + DateUtils.getLastDayOfMonth(cYear, cMonth, DateUtils.DATE_TEMPLATE_MONTH));

    }


    private void initEvent() {


        rbIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mViewPager.setCurrentItem(0);
                else
                    mViewPager.setCurrentItem(1);

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbIncome.setChecked(true);
                        rbIncome.setTextColor(getResources().getColor(R.color.white));
                        rbExpend.setTextColor(getResources().getColor(R.color.yellow_gray));

                        break;
                    case 1:
                        rbExpend.setChecked(true);
                        rbIncome.setTextColor(getResources().getColor(R.color.yellow_gray));
                        rbExpend.setTextColor(getResources().getColor(R.color.white));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iv_down_month.setOnClickListener(this);
        iv_up_month.setOnClickListener(this);

    }


    @Override
    public int LayoutResId() {
        return R.layout.activity_account_month_aty;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

        //通知fragment
        NotifyFragment();


    }

    /**
     * 月份修改通知
     */
    private void NotifyFragment() {

        initData();
        if (inl != null)
            inl.onClickedMonth(cYear, cMonth);
        if (exl != null)
            exl.onClickedMonth(cYear, cMonth);

    }

    /**
     * 定义一个接口
     *
     * @author zqy
     */
    public interface OnButtonClickedListener {
        /**
         * 月份
         *
         * @param month
         */
        void onClickedMonth(int year, int month);
    }

    /**
     * 收入绑定接口
     *
     * @param l 收入
     */
    public void setIncomeClickedListener(OnButtonClickedListener l) {
        this.inl = l;
    }

    /**
     * 支出绑定接口
     *
     * @param l
     */
    public void setExpendClickedListener(OnButtonClickedListener l) {
        this.exl = l;
    }


    private OnButtonClickedListener inl, exl;


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    //
                    return IncomeFragment.newInstance(cYear, cMonth, book);
                case 1:
                    return ExpendFragment.newInstance(cYear, cMonth, book);

            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
