package hipad.bill.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hipad.bill.utils.AnimationUtil;
import hipad.billproject.R;


/**
 * Created by wangyawen on 2017/10/26 0026.
 */
public class KeyboardView extends RelativeLayout implements View.OnClickListener {

    private Animation animationIn;
    private Animation animationOut;

    private LinearLayout keyboard_income, keyboard_expend;

    public enum KEYBOARD {
        INCOME, EXPEND
    }


    private RelativeLayout keyboard_1, keyboard_2, keyboard_3, keyboard_4, keyboard_5, keyboard_6, keyboard_7, keyboard_8, keyboard_9, keyboard_0, keyboard_point,
            keyboard_del, keyboard_minus, keyboard_add, keyboard_finish;

    private TextView tv_finish;

    private TextView tv_remark_content, tv_date_select;

    /**
     * 已经输入的字符
     */
    private String existedText = "";
    /**
     * 是否计算过
     */
    private boolean isCounted = false;
    /**
     * 以负号开头，且运算符不是是减号
     * 例如：-21×2
     */
    private boolean startWithOperator = false;
    /**
     * 以负号开头，且运算符是减号
     * 例如：-21-2
     */
    private boolean startWithSubtract = false;
    /**
     * 不以负号开头，且包含运算符
     * 例如：21-2
     */
    private boolean noStartWithOperator = false;


    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this);


        initView();
        initEvent();
        initAnimation();
        setKey(KEYBOARD.INCOME);

    }

    public void setKey(KEYBOARD key) {
        switch (key) {
            case INCOME:
                keyboard_income.setBackgroundColor(getResources().getColor(R.color.title_yellow));
                keyboard_expend.setBackgroundColor(getResources().getColor(R.color.bill_background));
                break;

            case EXPEND:
                keyboard_expend.setBackgroundColor(getResources().getColor(R.color.title_yellow));
                keyboard_income.setBackgroundColor(getResources().getColor(R.color.bill_background));
                break;
        }
    }

    private void initEvent() {

        keyboard_0.setOnClickListener(this);
        keyboard_1.setOnClickListener(this);
        keyboard_2.setOnClickListener(this);
        keyboard_3.setOnClickListener(this);
        keyboard_4.setOnClickListener(this);
        keyboard_5.setOnClickListener(this);
        keyboard_6.setOnClickListener(this);
        keyboard_7.setOnClickListener(this);
        keyboard_8.setOnClickListener(this);
        keyboard_9.setOnClickListener(this);


        keyboard_del.setOnClickListener(this);
        keyboard_minus.setOnClickListener(this);
        keyboard_add.setOnClickListener(this);
        keyboard_finish.setOnClickListener(this);

        keyboard_point.setOnClickListener(this);

        //时间选择和备注按钮
        tv_remark_content.setOnClickListener(new MyClickListener());
        tv_date_select.setOnClickListener(new MyClickListener());


    }

    private void initView() {
        keyboard_0 = findViewById(R.id.keyboard_0);
        keyboard_1 = findViewById(R.id.keyboard_1);
        keyboard_2 = findViewById(R.id.keyboard_2);
        keyboard_3 = findViewById(R.id.keyboard_3);
        keyboard_4 = findViewById(R.id.keyboard_4);
        keyboard_5 = findViewById(R.id.keyboard_5);
        keyboard_6 = findViewById(R.id.keyboard_6);
        keyboard_7 = findViewById(R.id.keyboard_7);
        keyboard_8 = findViewById(R.id.keyboard_8);
        keyboard_9 = findViewById(R.id.keyboard_9);
        keyboard_del = findViewById(R.id.keyboard_del);
        keyboard_minus = findViewById(R.id.keyboard_minus);
        keyboard_add = findViewById(R.id.keyboard_add);
        keyboard_finish = findViewById(R.id.keyboard_finish);

        keyboard_income = findViewById(R.id.keyboard_income);
        keyboard_expend = findViewById(R.id.keyboard_expend);
        keyboard_point = findViewById(R.id.keyboard_point);
        //修改== 文本
        tv_finish = findViewById(R.id.tv_finish);

        //底部区域
        tv_date_select = findViewById(R.id.tv_date_select);
        tv_remark_content = findViewById(R.id.tv_remark_content);
    }

    /**
     * 设置初始值
     * @param existedText
     */
    public void setExistedText(String existedText) {
        this.existedText = existedText;
    }


    /**
     * 添加时间
     * @param time
     */
    public void setSelectTime(String time){
        tv_date_select.setText(time);
    }

    /**
     * 添加备注
     * @param content
     */
    public  void setRemarkContent(String content){
        tv_remark_content.setText(content);
    }



    // 初始化动画效果
    private void initAnimation() {
        animationIn = /*AnimationUtils.loadAnimation(getContext(), R.anim.keyboard_in)*/AnimationUtil.moveToViewLocation();
        animationOut = /*AnimationUtils.loadAnimation(getContext(), R.anim.keyboard_out)*/AnimationUtil.moveToViewBottom();
    }

    // 弹出软键盘
    public void show() {
        startAnimation(animationIn);
        setVisibility(VISIBLE);
    }

    // 关闭软键盘
    public void dismiss() {
        if (isVisible()) {
            startAnimation(animationOut);
            setVisibility(GONE);
        }
    }

    // 判断软键盘的状态
    public boolean isVisible() {
        if (getVisibility() == VISIBLE) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /**
             * 数字
             */
            case R.id.keyboard_0:
                existedText = isOverRange(existedText, "0");
                break;
            case R.id.keyboard_1:
                existedText = isOverRange(existedText, "1");
                break;
            case R.id.keyboard_2:
                existedText = isOverRange(existedText, "2");
                break;
            case R.id.keyboard_3:
                existedText = isOverRange(existedText, "3");
                break;
            case R.id.keyboard_4:
                existedText = isOverRange(existedText, "4");
                break;
            case R.id.keyboard_5:
                existedText = isOverRange(existedText, "5");
                break;
            case R.id.keyboard_6:
                existedText = isOverRange(existedText, "6");
                break;
            case R.id.keyboard_7:
                existedText = isOverRange(existedText, "7");
                break;
            case R.id.keyboard_8:
                existedText = isOverRange(existedText, "8");
                break;
            case R.id.keyboard_9:
                existedText = isOverRange(existedText, "9");
                break;
            /**
             * 运算符
             */
            case R.id.keyboard_add:
                /**
                 * 判断已有的字符是否是科学计数
                 * 是 置零
                 * 否 进行下一步
                 *
                 * 判断表达式是否可以进行计算
                 * 是 先计算再添加字符
                 * 否 添加字符
                 *
                 * 判断计算后的字符是否是 error
                 * 是 置零
                 * 否 添加运算符
                 */
                if (!existedText.contains("e")) {

                    if (judgeExpression()) {
                        existedText = getResult();
                        if (existedText.equals("error")) {

                        } else {
                            existedText += "+";
                        }
                    } else {

                        if (isCounted) {
                            isCounted = false;
                        }

                        if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText = existedText.replace("-", "+");
                        } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText = existedText.replace("×", "+");
                        } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText = existedText.replace("÷", "+");
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("+")) {
                            existedText += "+";
                        }
                    }
                } else {
                    existedText = "0";
                }

                break;
            case R.id.keyboard_minus:

                if (!existedText.contains("e")) {
                    if (judgeExpression()) {
                        existedText = getResult();
                        if (existedText.equals("error")) {

                        } else {
                            existedText += "-";
                        }
                    } else {

                        if (isCounted) {
                            isCounted = false;
                        }

                        if ((existedText.substring(existedText.length() - 1)).equals("+")) {
//                    Log.d("Anonymous", "onClick: " + "进入减法方法");
                            existedText = existedText.replace("+", "-");
                        } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText = existedText.replace("×", "-");
                        } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText = existedText.replace("÷", "-");
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText += "-";
                        }
                    }
                } else {
                    existedText = "0";
                }
                break;

            case R.id.keyboard_finish:
                existedText = getResult();
                if (isCounted) {
                    //取消键盘
                    tv_finish.setText("=");
                    tv_finish.setTextColor(getResources().getColor(R.color.black));
                    dismiss();
                } else {
                    tv_finish.setText("完成");
                    tv_finish.setTextColor(getResources().getColor(R.color.yellow_gray));
                }

                isCounted = true;
                break;
            /**
             * 其他
             */
            case R.id.keyboard_point:
                /**
                 * 判断是否运算过
                 * 否
                 *   判断是否有运算符，有 判断运算符之后的数字，无 判断整个数字
                 *   判断数字是否过长，是则不能添加小数点，否则可以添加
                 *   判断已经存在的数字里是否有小数点
                 * 是
                 *   字符串置为 0.
                 */
                if (!isCounted) {

                    if (existedText.contains("+") || existedText.contains("-") ||
                            existedText.contains("×") || existedText.contains("÷")) {

                        String param1 = null;
                        String param2 = null;

                        if (existedText.contains("+")) {
                            param1 = existedText.substring(0, existedText.indexOf("+"));
                            param2 = existedText.substring(existedText.indexOf("+") + 1);
                        } else if (existedText.contains("-")) {
                            param1 = existedText.substring(0, existedText.indexOf("-"));
                            param2 = existedText.substring(existedText.indexOf("-") + 1);
                        } else if (existedText.contains("×")) {
                            param1 = existedText.substring(0, existedText.indexOf("×"));
                            param2 = existedText.substring(existedText.indexOf("×") + 1);
                        } else if (existedText.contains("÷")) {
                            param1 = existedText.substring(0, existedText.indexOf("÷"));
                            param2 = existedText.substring(existedText.indexOf("÷") + 1);
                        }
                        Log.d("Anonymous param1", param1);
                        Log.d("Anonymous param2", param2);

                        boolean isContainedDot = param2.contains(".");
                        if (param2.length() >= 9) {

                        } else if (!isContainedDot) {
                            if (param2.equals("")) {
                                existedText += "0.";
                            } else {
                                existedText += ".";
                            }
                        } else {
                            return;
                        }
                    } else {
                        boolean isContainedDot = existedText.contains(".");
                        if (existedText.length() >= 9) {

                        } else if (!isContainedDot) {
                            existedText += ".";
                        } else {
                            return;
                        }
                    }
                    isCounted = false;

                } else {
                    existedText = "0.";
                    isCounted = false;
                }


                break;

            case R.id.keyboard_del:
                /**
                 * 字符串长度大于 0 时才截取字符串
                 * 如果长度为 1，则直接把字符串设置为 0
                 */
                if (existedText.equals("error")) {
                    existedText = "0";
                } else if (existedText.length() > 0) {
                    if (existedText.length() == 1) {
                        existedText = "0";
                    } else {
                        existedText = existedText.substring(0, existedText.length() - 1);
                    }
                }
                break;
        }
        /**
         * 设置显示
         */
        if (l != null)
            l.OnClickKeyBoardValue(existedText);
    }


    /**
     * 设置不同的接口回调返回
     */
    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.tv_date_select:
                    if (l != null)
                        l.OnSelectTime();

                    break;
                case R.id.tv_remark_content:
                    if (l != null)
                        l.onPopWindow();

                    break;

            }

        }
    }


    public interface OnKeyBoardClickListener {
        void OnClickKeyBoardValue(String value);

        void OnSelectTime();

        void onPopWindow();

    }


    public void setOnKeyBoardClickListener(OnKeyBoardClickListener listener) {
        this.l = listener;
    }

    OnKeyBoardClickListener l;

    /**
     * 进行运算，得到结果
     *
     * @return 返回结果
     */
    private String getResult() {

        /**
         * 结果
         */
        String tempResult = null;
        /**
         * 两个String类型的参数
         */
        String param1 = null;
        String param2 = null;
        /**
         * 转换后的两个double类型的参数
         */
        double arg1 = 0;
        double arg2 = 0;
        double result = 0;

        getCondition();

        /**
         * 如果有运算符，则进行运算
         * 没有运算符，则把已经存在的数据再传出去
         */
        if (startWithOperator || noStartWithOperator || startWithSubtract) {

            if (existedText.contains("+")) {
                /**
                 * 先获取两个参数
                 */
                param1 = existedText.substring(0, existedText.indexOf("+"));
                param2 = existedText.substring(existedText.indexOf("+") + 1);
                /**
                 * 如果第二个参数为空，则还是显示当前字符
                 */
                if (param2.equals("")) {
                    tempResult = param1;
                } else {
                    /**
                     * 转换String为Double
                     * 计算后再转换成String类型
                     * 进行正则表达式处理
                     */
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 + arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }


            } else if (existedText.contains("×")) {

                param1 = existedText.substring(0, existedText.indexOf("×"));
                param2 = existedText.substring(existedText.indexOf("×") + 1);

                if (param2.equals("")) {
                    tempResult = param1;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 * arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            } else if (existedText.contains("÷")) {

                param1 = existedText.substring(0, existedText.indexOf("÷"));
                param2 = existedText.substring(existedText.indexOf("÷") + 1);

                if (param2.equals("0")) {
                    tempResult = "error";
                } else if (param2.equals("")) {
                    tempResult = existedText;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 / arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            } else if (existedText.contains("-")) {

                /**
                 * 这里是以最后一个 - 号为分隔去取出两个参数
                 * 进到这个方法，必须满足有运算公式
                 * 而又避免了第一个参数是负数的情况
                 */
                param1 = existedText.substring(0, existedText.lastIndexOf("-"));
                param2 = existedText.substring(existedText.lastIndexOf("-") + 1);

                if (param2.equals("")) {
                    tempResult = param1;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 - arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            }
            /**
             * 如果数据长度大于等于10位，进行科学计数
             *
             * 如果有小数点，再判断小数点前是否有十个数字，有则进行科学计数
             */
            if (tempResult.length() >= 10) {
                tempResult = String.format("%e", Double.parseDouble(tempResult));
            } else if (tempResult.contains(".")) {
                if (tempResult.substring(0, tempResult.indexOf(".")).length() >= 10) {
                    tempResult = String.format("%e", Double.parseDouble(tempResult));
                }
            }
        } else {
            tempResult = existedText;
        }

        return tempResult;
    }


    /**
     * 先判断是否按过等于号
     * 是 按数字则显示当前数字
     * 否 在已有的表达式后添加字符
     * <p>
     * 判断数字是否就是一个 0
     * 是 把字符串设置为空字符串。
     * 1、打开界面没有运算过的时候，AC键归零或删除完归零的时候，会显示一个 0
     * 2、当数字是 0 的时候，设置成空字符串，再按 0 ，数字会还是 0，不然可以按出 000 这种数字
     * 否 添加按下的键的字符
     * <p>
     * 判断数字是否包含小数点
     * 是 数字不能超过十位
     * 否 数字不能超过九位
     * <p>
     * 进行上面的判断后，再判断数字是否超过长度限制
     * 超过不做任何操作
     * 没超过可以再添数字
     */
    private String isOverRange(String existedText, String s) {
        /**
         * 判断是否计算过
         */
        if (!isCounted) {
            //
            tv_finish.setText("=");
            tv_finish.setTextColor(getResources().getColor(R.color.black));

            /**
             * 判断是否是科学计数
             * 是 文本置零
             */
            if (existedText.contains("e")) {
                existedText = "0";
            }
            /**
             * 判断是否只有一个 0
             * 是 文本清空
             */
            if (existedText.equals("0")) {
                existedText = "";
            }
            /**
             * 判断是否有运算符
             * 是 判断第二个数字
             * 否 判断整个字符串
             */
            if (existedText.contains("+") || existedText.contains("-") ||
                    existedText.contains("×") || existedText.contains("÷")) {
                /**
                 * 包括运算符时 两个数字 判断第二个数字
                 * 两个参数
                 */
                String param2 = null;
                if (existedText.contains("+")) {
                    param2 = existedText.substring(existedText.indexOf("+") + 1);
                } else if (existedText.contains("-")) {
                    param2 = existedText.substring(existedText.indexOf("-") + 1);
                } else if (existedText.contains("×")) {
                    param2 = existedText.substring(existedText.indexOf("×") + 1);
                } else if (existedText.contains("÷")) {
                    param2 = existedText.substring(existedText.indexOf("÷") + 1);
                }

//            Log.d("Anonymous param1",param1);
//            Log.d("Anonymous param2",param2);
                if (existedText.substring(existedText.length() - 1).equals("+") ||
                        existedText.substring(existedText.length() - 1).equals("-") ||
                        existedText.substring(existedText.length() - 1).equals("×") ||
                        existedText.substring(existedText.length() - 1).equals("÷")) {
                    existedText += s;
                } else {
                    if (param2.contains(".")) {
                        if (param2.length() >= 10) {

                        } else {
                            existedText += s;
                        }
                    } else {
                        if (param2.length() >= 9) {

                        } else {
                            existedText += s;
                        }
                    }
                }
            } else {
                /**
                 * 不包括运算符时 一个数字
                 */
                if (existedText.contains(".")) {
                    if (existedText.length() >= 10) {

                    } else {
                        existedText += s;
                    }
                } else {
                    if (existedText.length() >= 9) {

                    } else {
                        existedText += s;
                    }
                }
            }

            isCounted = false;

        } else {

            existedText = s;
            isCounted = false;

        }


        return existedText;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s 传入的字符串
     * @return 修改之后的字符串
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 判断表达式
     * <p>
     * 为了按等号是否进行运算
     * 以及出现两个运算符（第一个参数如果为负数的符号不计）先进行运算再添加运算符
     */
    private boolean judgeExpression() {

        getCondition();

        String tempParam2 = null;

        if (startWithOperator || noStartWithOperator || startWithSubtract) {

            if (existedText.contains("+")) {
                /**
                 * 先获取第二个参数
                 */
                tempParam2 = existedText.substring(existedText.indexOf("+") + 1);
                /**
                 * 如果第二个参数为空，表达式不成立
                 */
                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }
            } else if (existedText.contains("×")) {

                tempParam2 = existedText.substring(existedText.indexOf("×") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            } else if (existedText.contains("÷")) {

                tempParam2 = existedText.substring(existedText.indexOf("÷") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            } else if (existedText.contains("-")) {

                /**
                 * 这里是以最后一个 - 号为分隔去取出两个参数
                 * 进到这个方法，必须满足有运算公式
                 * 而又避免了第一个参数是负数的情况
                 */
                tempParam2 = existedText.substring(existedText.lastIndexOf("-") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * 取得判断条件
     */
    private void getCondition() {
        /**
         * 以负号开头，且运算符不是是减号
         * 例如：-21×2
         */
        startWithOperator = existedText.startsWith("-") && (existedText.contains("+") ||
                existedText.contains("×") || existedText.contains("÷"));
        /**
         * 以负号开头，且运算符是减号
         * 例如：-21-2
         */
        startWithSubtract = existedText.startsWith("-") && (existedText.lastIndexOf("-") != 0);
        /**
         * 不以负号开头，且包含运算符
         * 例如：21-2
         */
        noStartWithOperator = !existedText.startsWith("-") && (existedText.contains("+") ||
                existedText.contains("-") || existedText.contains("×") || existedText.contains("÷"));
    }


}
