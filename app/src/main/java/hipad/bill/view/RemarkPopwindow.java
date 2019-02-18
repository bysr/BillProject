package hipad.bill.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/11/6 0006.
 */

public class RemarkPopwindow extends PopupWindow {
    private View mMenuView;
    private EditText et_remark_content;
    private ImageView iv_remark;
    private TextView tv_remark, btn_remark;
    private Uri uri;
    private Activity activity;

    public RemarkPopwindow(Activity aty, Context context) {
        super(context);
        this.activity = aty;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.remark_edit, null);
        et_remark_content = mMenuView.findViewById(R.id.et_remark_content);
        iv_remark = mMenuView.findViewById(R.id.iv_remark);
        tv_remark = mMenuView.findViewById(R.id.tv_remark);
        btn_remark = mMenuView.findViewById(R.id.btn_remark);

        //设置PopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpaha(activity, 0.5f);
        btn_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content=et_remark_content.getText().toString().toString();
                tv_remark.setText(content);

                if (l != null) {
                    l.popSave(content, uri);
                    dismiss();
                }

            }
        });

        iv_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (l != null)
                    l.SelectImg();
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha(activity, 1.0f);


            }
        });

    }

    public void setRemarkContent(String content){
        et_remark_content.setText(content);
    }


    /**
     * 设置添加屏幕的背景透明度
     **/
    public void backgroundAlpaha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void setImage(Uri uri) {
        this.uri = uri;
    }


    public interface OnPopWindowListener {
        /**
         * @param remarkContent
         */
        void popSave(String remarkContent, Uri uri);

        void SelectImg();

    }


    public void setOnPopWindowListener(OnPopWindowListener l) {
        this.l = l;
    }


    OnPopWindowListener l;


}
