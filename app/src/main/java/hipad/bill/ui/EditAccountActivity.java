package hipad.bill.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.AccountInfo;
import hipad.bill.constant.SortUtils;
import hipad.bill.ui.fragment.EditInFragment;
import hipad.bill.ui.fragment.EditOutFragment;
import hipad.bill.utils.DateUtils;
import hipad.bill.utils.MathTools;
import hipad.bill.view.KeyboardView;
import hipad.bill.view.RemarkPopwindow;
import hipad.billproject.R;
import hipad.dateutils.DateListener;
import hipad.dateutils.TimeConfig;
import hipad.dateutils.TimeSelectorDialog;

public class EditAccountActivity extends BaseActivity implements View.OnClickListener, EditInFragment.OnFragmentIncome, EditOutFragment.OnFragmentExpend {

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

    private RadioButton rbIncome, rbExpend;
    private RelativeLayout layout_back, layout_right;
    private ImageView nav_img_left, nav_img_right;

    private KeyboardView keyboard;//底部键盘


    AccountInfo info;
    AccountBook book;
    SortUtils sort = SortUtils.PAY;//默认设置工资

    private boolean isAdd = true;//默认从添加页面进入

    //顶部展示项
    //注意
    private ImageView img_bill;
    private TextView tv_bill_name;
    private ImageView img_photo;
    private TextView tv_bill_money;
    private RelativeLayout re_show;
    private DbManager db;


    private RemarkPopwindow popwindow;

    private String content;
    private long time;
    private String sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public int LayoutResId() {
        return R.layout.activity_edit_account;
    }

    private void initData() {


    }

    private void initView() {
        rbIncome = (RadioButton) findViewById(R.id.rb_bill_income);
        rbExpend = (RadioButton) findViewById(R.id.rb_bill_expend);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        layout_right = (RelativeLayout) findViewById(R.id.layout_right);
        nav_img_left = (ImageView) findViewById(R.id.nav_img_left);
        nav_img_right = (ImageView) findViewById(R.id.nav_img_right);
        layout_right.setVisibility(View.VISIBLE);
        nav_img_right.setImageResource(R.mipmap.duigou);
        nav_img_left.setImageResource(R.mipmap.chahao);


        //功能展示项目
        img_bill = (ImageView) findViewById(R.id.img_bill);
        tv_bill_money = (TextView) findViewById(R.id.tv_bill_money);
        tv_bill_name = (TextView) findViewById(R.id.tv_bill_name);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        re_show = (RelativeLayout) findViewById(R.id.re_show);


        info = (AccountInfo) getIntent().getSerializableExtra("info");
        book = (AccountBook) getIntent().getSerializableExtra("book");
        if (info != null) {
            isAdd = false;
            sort = SortUtils.get(info.getType());
            if (sort.getType() == 2) {
                mViewPager.setCurrentItem(1);
                rbIncome.setTextColor(getResources().getColor(R.color.yellow_gray));
                rbExpend.setTextColor(getResources().getColor(R.color.white));
                rbExpend.setChecked(true);
            }
            sId = info.getSid();
            tv_bill_money.setText(info.getBill_money() + "");
            time = info.getBill_time();

        } else {
            sId = MathTools.getUUID();
            info = new AccountInfo(1);
            time = DateUtils.Timestamp();//设置默认时间
        }
        //设置值
        tv_bill_name.setText(sort.getName());
        SortUtils.loadDrawable(img_bill, this, sort.getCode());


        keyboard = (KeyboardView) findViewById(R.id.keyboard);
        keyboard.dismiss();
        keyboard.setSelectTime(DateUtils.stampToDate(time, DateUtils.DATE_TEMPLATE_DATE));//设置时间
        if (!TextUtils.isEmpty(info.getBill_remark()))
            keyboard.setRemarkContent(info.getBill_remark());

        db = MyApplication.getInstance().getDbManager();

    }

    private void initEvent() {

        layout_right.setOnClickListener(this);
        layout_back.setOnClickListener(this);


        rbIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mViewPager.setCurrentItem(0);//设置当前页面
                    keyboard.setKey(KeyboardView.KEYBOARD.INCOME);
                    if (inl != null)
                        inl.updateFunction();
                } else {
                    keyboard.setKey(KeyboardView.KEYBOARD.EXPEND);
                    mViewPager.setCurrentItem(1);//设置当前页面
                    if (outl != null)
                        outl.updateFunction();
                }


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

        keyboard.setOnKeyBoardClickListener(new KeyboardView.OnKeyBoardClickListener() {
            @Override
            public void OnClickKeyBoardValue(String value) {
                //发消息到fragment页面中
                /*1.消费额度 2.消费类型
                * 3.底部显示方案*/
                if (TextUtils.isEmpty(value))
                    value = "0";
                tv_bill_money.setText(value);


            }

            @Override
            public void OnSelectTime() {
                showTime();

            }

            @Override
            public void onPopWindow() {
                showPopWindow();
            }
        });

        re_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keyboard.isShown())
                    keyboard.show();


            }
        });
    }

    private void showPopWindow() {
        popwindow = new RemarkPopwindow(EditAccountActivity.this, EditAccountActivity.this);
        popwindow.setRemarkContent(content);
        popwindow.showAtLocation(mViewPager, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popwindow.setOnPopWindowListener(new RemarkPopwindow.OnPopWindowListener() {
            @Override
            public void popSave(String remarkContent, Uri uri) {
                //数据保存,包含图片的上传工作
                content = remarkContent;
                keyboard.setRemarkContent(content);
                keyboard.show();


            }

            @Override
            public void SelectImg() {
                //选择图片


                // start multiple photos selector
                Intent intent = new Intent(EditAccountActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // start the selector
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for (String result : mResults) {
                    sb.append(result).append("\n");
                }
//                tvResults.setText(sb.toString());
                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    /**
     * 设置头部功能
     *
     * @param Type
     */
    private void setHead(int Type) {
        sort = SortUtils.get(Type);
        tv_bill_name.setText(sort.getName());
        SortUtils.loadDrawable(img_bill, this, sort.getCode());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.layout_right:
                //确定
                float money = Float.valueOf(tv_bill_money.getText().toString());
                if (mViewPager.getCurrentItem() == 1) {
                    money = -money;
                }
                info.setType(sort.getCode());
                info.setBill_money(money);
                info.setBill_remark(content);
                info.setBill_time(time);
                info.setSid(sId);
                info.setbState(0);//默认添加成功
                info.setBid(book.getSid());//账本id

                if (isAdd) {
                    saveService();
                } else {
                    EditService();
                }

                break;
            case R.id.layout_back:
                //取消
                finish();
                break;


        }

    }

    /**
     * 编辑数据
     */
    private void EditService() {
        info.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    //编辑失败
                    info.setbState(2);
                    Toast.makeText(EditAccountActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
                }
                saveOrUpdate();
                //编辑数据
            }
        });

    }

    private void saveService() {
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    info.setObjId(s);//存储唯一标识符
                } else {
                    info.setbState(1);//添加未成功
                }
                saveOrUpdate();
            }
        });

    }

    /**
     * 本地保存数据
     */
    private void saveOrUpdate() {
        try {
            db.saveOrUpdate(info);
            finish();
        } catch (DbException e) {
            //保存异常
            e.printStackTrace();
        }


    }

    private void showTime() {

        TimeSelectorDialog dialog = new TimeSelectorDialog(this);
        //设置标题
        dialog.setTimeTitle("选择时间:");
        //显示类型
        dialog.setIsShowtype(TimeConfig.YEAR_MONTH_DAY);
        //默认时间
        dialog.setCurrentDate(/*"2017-10-11　14:50:11"*/DateUtils.stampToDate(time, DateUtils.DATE_TEMPLATE_DEFAULT));
        //隐藏清除按钮
        dialog.setEmptyIsShow(false);
        //设置起始时间
        dialog.setStartYear(2010);
        dialog.setDateListener(new DateListener() {
            @Override
            public void onReturnDate(String SelectTime, int year, int month, int day, int hour, int minute, int isShowType) {

                keyboard.setSelectTime(SelectTime);//设置时间
                time = DateUtils.dateToStamp(SelectTime, DateUtils.DATE_TEMPLATE_DATE);


            }

            @Override
            public void onReturnDate(String empty) {
            }
        });
        dialog.show();
    }


    @Override
    public void onFragmentExpendAct(AccountInfo info, int Type) {
        //消费回调
        setHead(Type);

    }


    @Override
    public void onFragmentIncomeAct(AccountInfo info, int Type) {
        //收入回调
        setHead(Type);
    }


    /**
     * 定义一个接口
     *
     * @author zqy
     */
    public interface OnButtonClickedListener {
        /**
         * 保存回调Fragment
         */
        void updateFunction();//设置页面功能

    }

    /**
     * 写两个绑定接口主要是为了避免fragment在绑定过程中的覆盖问题
     */
    /**
     * 收入监听接口
     *
     * @param listener
     */
    public void setClickedIncomeListener(OnButtonClickedListener listener) {
        this.inl = listener;
    }

    /**
     * 支出监听接口
     *
     * @param listener
     */
    public void setClickedExpendListener(OnButtonClickedListener listener) {
        this.outl = listener;
    }

    private OnButtonClickedListener inl, outl;


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

                    return EditInFragment.newInstance(info);
                case 1:

                    return EditOutFragment.newInstance(info);
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
