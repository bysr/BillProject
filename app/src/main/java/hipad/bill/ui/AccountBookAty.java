package hipad.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingle.widget.LoadingView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import hipad.bill.BaseActivity;
import hipad.bill.MyApplication;
import hipad.bill.adapter.RecyclerListAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.help.OnStartDragListener;
import hipad.bill.help.SimpleItemTouchHelperCallback;
import hipad.bill.utils.DateUtils;
import hipad.bill.utils.MathTools;
import hipad.bill.view.DialogAccount;
import hipad.billproject.R;

/**
 * 账单首页列表
 */
public class AccountBookAty extends BaseActivity implements OnStartDragListener, View.OnClickListener, DialogAccount.OnItemDialogClickListener {

    private LinearLayout ll_finish, ll_fun;
    private TextView tv_edit, tv_create;

    private ItemTouchHelper mItemTouchHelper;
    private List<AccountBook> list;
    RecyclerListAdapter adapter;
    SimpleItemTouchHelperCallback callback;
    RecyclerView recyclerView;
    DialogAccount dialogAccount_A, dialogAccount_B;
    LoadingView loadView;

    DbManager db;

    private String userId;//用户名称
    private String type = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e262e41e40077dceecd9bea3c394badf");

        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            // 允许用户使用应用
            userId=bmobUser.getUsername();
            queryData();

        } else {
            //缓存用户对象为空时， 可打开用户注册界面
            getLogin();
        }


    }

    private void queryData() {
        loadView.setVisibility(View.VISIBLE);
        BmobQuery<AccountBook> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);

        query.findObjects(new FindListener<AccountBook>() {
            @Override
            public void done(List<AccountBook> list, BmobException e) {

                if (e != null) {
                    initData();//本地读取
                } else {
                    for (AccountBook book : list) {
                        book.setObjId(book.getObjectId());//赋值到本地数据库
                        saveOrUpdate(book);
                    }
                }
                loadView.setVisibility(View.GONE);

                initData();//本地读取

            }
        });

    }


    private void initView() {
        showTitle(getString(R.string.choose));
        ll_finish = (LinearLayout) findViewById(R.id.ll_finish);
        ll_fun = (LinearLayout) findViewById(R.id.ll_fun);
        tv_create = (TextView) findViewById(R.id.tv_create);
        tv_edit = (TextView) findViewById(R.id.tv_edit);

        list = new ArrayList<>();
        adapter = new RecyclerListAdapter(this, this, list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Account);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        nav_right_img = (ImageView) findViewById(R.id.nav_right_img);
        nav_right_img.setImageResource(R.mipmap.chahao);
        nav_right_img.setVisibility(View.VISIBLE);

        db = MyApplication.getInstance().getDbManager();
        loadView = (LoadingView) findViewById(R.id.loadView);
    }

    private void initData() {
        list.clear();
        try {
            List<AccountBook> findAll = db.selector(AccountBook.class).expr("uid  = "+userId).orderBy("position").findAll();//主要是用来进行一些特定条件的查找
            if (findAll != null)
                list.addAll(findAll);
        } catch (DbException e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();


    }

    private void initEvent() {

        tv_edit.setOnClickListener(this);
        tv_create.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        nav_right_img.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerListAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                //
                Intent intent = new Intent(AccountBookAty.this, AccountInfoAty.class);
                intent.putExtra("book", list.get(position));
                startActivity(intent);


            }

            @Override
            public void onItemRemove(final int position) {
                //删除数据
                final AccountBook book = list.get(position);

                book.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        //删除数据
                        if (e == null) {
                            if (delBook(position))
                                Toast.makeText(AccountBookAty.this, "删除成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AccountBookAty.this, "本地删除异常", Toast.LENGTH_SHORT).show();
                        } else {
                            //编辑对象
                            book.setbState(-1);
                            if (saveOrUpdate(book))
                                Toast.makeText(AccountBookAty.this, "本地删除标记成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AccountBookAty.this, "本地删除标记异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    /**
     * 删除数据
     *
     * @param position
     * @return
     */
    private boolean delBook(int position) {
        boolean b = true;
        try {
            db.delete(list.get(position));
        } catch (DbException e) {
            b = false;
            e.printStackTrace();
        }

        list.remove(position);
        adapter.notifyItemRemoved(position);
        return b;
    }


    @Override
    public int LayoutResId() {
        return R.layout.activity_account_book_aty;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_edit:
                adapter.setEditState(true);
                ll_finish.setVisibility(View.VISIBLE);
                ll_fun.setVisibility(View.GONE);
                callback.setMove(true);
                break;
            case R.id.tv_create:
                adapter.setEditState(false);
                callback.setMove(false);

                dialogAccount_A = new DialogAccount(this, R.layout.dialog_account, new int[]{R.id.re_account_common, R.id.re_account_special, R.id.img_cancel});
                dialogAccount_A.setOnItemDialogClickListener(this);
                dialogAccount_A.show();

                break;
            case R.id.ll_finish:
                adapter.setEditState(false);
                ll_finish.setVisibility(View.GONE);
                ll_fun.setVisibility(View.VISIBLE);
                callback.setMove(false);

                /*整理list position*/

                sortList();

                break;

            case R.id.nav_right_img:
                getLogin();
                break;


        }

    }

    private void getLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 整理list
     */
    private void sortList() {
        List<BmobObject> bmob = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPosition(i);
            bmob.add(list.get(i));
        }

        BmobBatch batch = new BmobBatch();
        batch.updateBatch(bmob);
        loadView.setVisibility(View.VISIBLE);
        //执行操作
        batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {

                if (e != null) {
                    Toast.makeText(AccountBookAty.this, "批量操作失败", Toast.LENGTH_SHORT).show();
                } else {
                    saveOrUpdate();
                }
                loadView.setVisibility(View.GONE);
                initData();//本地读取


            }
        });

    }

    /**
     * 更新集合数据
     */
    private void saveOrUpdate() {
        try {
            db.saveOrUpdate(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCenterItemClick(DialogAccount dialog, View view, String editT) {

        switch (view.getId()) {
            case R.id.re_account_common:

                dialogAccount_A = null;
                dialogAccount_B = new DialogAccount(this, R.layout.dialog_edit, new int[]{R.id.dialog_sure, R.id.dialog_cancel}, new Object[]{R.id.dialog_text, getString(R.string.account_name), R.id.editT});
                dialogAccount_B.setOnItemDialogClickListener(this);
                dialogAccount_B.show();
                type = "1";

                break;
            case R.id.re_account_special:
                Toast.makeText(this, "AA账本完善中....", Toast.LENGTH_SHORT).show();
//                dialogAccount_B = new DialogAccount(this, R.layout.dialog_edit, new int[]{R.id.dialog_sure, R.id.dialog_cancel}, new Object[]{R.id.dialog_text, getString(R.string.account_name_a), R.id.editT});
//                dialogAccount_B.setOnItemDialogClickListener(this);
//                dialogAccount_B.show();
//                type = "2";
                break;
            case R.id.img_cancel:
                break;

            case R.id.dialog_sure:
                AccountBook cBook = new AccountBook();
                getBook(editT, cBook);
                saveService(cBook);


                //------------------添加数据库----------------------------
                break;
        }

    }

    private void saveService(final AccountBook cBook) {

        cBook.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

                if (e == null) {
                    //插入成功，保存到后台数据库中
                    cBook.setObjId(s);
                } else {
                    Toast.makeText(AccountBookAty.this, "插入数据失败", Toast.LENGTH_SHORT).show();
                    cBook.setbState(1);
                }
                saveOrUpdate(cBook);
                initData();//查询数据是否存在
            }
        });

    }

    /**
     * 保存对象
     *
     * @param book
     */
    private boolean saveOrUpdate(AccountBook book) {
        try {
            db.saveOrUpdate(book);

        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void getBook(String editT, AccountBook cBook) {
        cBook.setAccount_Name(editT);
        cBook.setAccount_time(DateUtils.Timestamp());
        cBook.setPosition(list.size());
        cBook.setAccount_type(type);
        cBook.setUserId(userId);
        cBook.setSid(MathTools.getUUID());//设置id
        cBook.setbState(0);//默认设置成功


    }

}
