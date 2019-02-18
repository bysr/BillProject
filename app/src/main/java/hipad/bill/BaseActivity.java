package hipad.bill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/19 0019.
 */

public abstract class BaseActivity extends AppCompatActivity {

    //标题
    protected TextView nav_title;
    //右侧图片
    protected ImageView nav_right_img;
    //左侧返回图片
    protected ImageView nav_img_left;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutResId());

    }


    /**
     * 设置标题
     *
     * @param str
     */
    public void showTitle(String str) {
        nav_title = (TextView) findViewById(R.id.nav_title);
        if (null == nav_title) {
            Toast.makeText(this, "当前界面找不到标题组件，请检查布局中是否存在此部件！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        nav_title.setVisibility(View.VISIBLE);
        nav_title.setText("" + str);
    }


    /**
     * 显示返回按钮
     *
     * @param isShow
     */
    public void showBackBtn(boolean isShow) {
        nav_img_left = (ImageView) findViewById(R.id.nav_img_left);
        if (null == nav_img_left) {
            Toast.makeText(this, "当前界面找不到标题组件，请检查布局中是否存在此部件！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (isShow) {
            nav_img_left.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                 finish();
                }

            });
            nav_img_left.setVisibility(View.VISIBLE);
        } else {
            nav_img_left.setVisibility(View.GONE);
        }
    }

    /**
     * 抽象布局文件，子类直接实现
     * @return 资源文件
     */
    public abstract int LayoutResId();
}
