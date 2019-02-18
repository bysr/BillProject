package hipad.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import hipad.bill.BaseActivity;
import hipad.billproject.R;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    private EditText etName, etPsd;
    private Button btn_login;
    private LinearLayout ll_register;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e262e41e40077dceecd9bea3c394badf");
        initView();
        initData();
        initEvent();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=etName.getText().toString();
                password=etPsd.getText().toString();

                if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
                    BmobUser bean = new BmobUser();
                    bean.setUsername(etName.getText().toString());
                    bean.setPassword(etPsd.getText().toString());
                    bean.loginByAccount(username, password, new LogInListener<BmobUser>() {
                        @Override
                        public void done(BmobUser user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "用户名或者密码为空", Toast.LENGTH_SHORT).show();
                }



            }
        });

        ll_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initData() {

    }

    private void initView() {

        etName = (EditText) findViewById(R.id.et_username);
        etPsd = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        ll_register = (LinearLayout) findViewById(R.id.ll_register);


    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_login;
    }


}