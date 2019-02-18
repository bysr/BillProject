package hipad.bill.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import hipad.billproject.R;


/**
 * Created by wangyawen on 2017/10/20 0020.
 */

public class DialogAccount extends Dialog implements View.OnClickListener {

    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id
    private Object[] TextID;       //1.title ID,2.title 值 3 editText ID
    private EditText editText;
    private TextView tvTitle;

    public DialogAccount(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    public DialogAccount(Context context, int layoutResID, int[] listenedItems, Object[] editTextID) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.TextID = editTextID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        setContentView(layoutResID);


        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);// 点击Dialog外部消失
        //遍历控件id,添加点击事件
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
        if (TextID != null && TextID.length == 3) {
            tvTitle = (TextView) findViewById((int) TextID[0]);
            tvTitle.setText((String) TextID[1]);
            editText = (EditText) findViewById((int) TextID[2]);

        }


    }


    private OnItemDialogClickListener listener;

    public interface OnItemDialogClickListener {
        void OnCenterItemClick(DialogAccount dialog, View view, String editT);
    }

    public void setOnItemDialogClickListener(OnItemDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        if (editText != null)
            listener.OnCenterItemClick(this, view, editText.getText().toString());
        else
            listener.OnCenterItemClick(this, view, null);
    }

}
