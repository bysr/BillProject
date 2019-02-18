package hipad.bill.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import hipad.bill.BaseActivity;
import hipad.billproject.R;

public class RegisterActivity extends BaseActivity {

    private EditText et_account, et_password, et_email;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e262e41e40077dceecd9bea3c394badf");
        initView();
        initData();
        initEvent();

    }

    private void initEvent() {

    }

    private void initData() {


    }

    private void initView() {

        et_account = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_register= (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    //注册
                    BmobUser user = new BmobUser();

//                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {

                            if (e == null) {
                                //注册成功
                                Toast.makeText(RegisterActivity.this, bmobUser.toString(), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Log.d("vivi", e.toString());
                            }
                        }
                    });


                }


            }
        });

    }

    @Override
    public int LayoutResId() {
        return R.layout.activity_register;
    }
}
