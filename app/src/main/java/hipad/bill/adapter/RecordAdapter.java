
package hipad.bill.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.bean.MonthAccout;
import hipad.bill.utils.MathTools;
import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 流水账单适配器
 */

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<MonthAccout> mList;

    private Context mContext;
    private View mHeaderView;

    private List<Entry> incomeEntry = new ArrayList<>();
    private List<Entry> expendEntry = new ArrayList<>();
    private List<Entry> balanceEntry = new ArrayList<>();

    private OnItemClickListener mListener;
    private float month_MAX = 2000, month_MIN = -1000;//坐标最大值/最小值
    private float SUM_COUNT = 0.0f, SUM_IN = 0.0f, SUM_EX = 0.0f; /*结余/收入/支出*/

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, MonthAccout data);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void addDatas(List<MonthAccout> datas) {
        mList.clear();
        mList.addAll(datas);
        iniData();
        notifyDataSetChanged();
    }

    public RecordAdapter(List<MonthAccout> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        //计算结果，循环遍历数据
    }


    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ChartHolder(mHeaderView);

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {

            if (viewHolder instanceof ChartHolder) {
                init(((ChartHolder) viewHolder).lineChart);
                ((ChartHolder) viewHolder).tv_year_count.setText(SUM_COUNT + "");
                ((ChartHolder) viewHolder).tv_year_income.setText(SUM_IN + "");
                ((ChartHolder) viewHolder).tv_year_expend.setText(SUM_EX + "");

            }

        } else {
            final int pos = getRealPosition(viewHolder);
            final MonthAccout monthAccout = mList.get(pos);
            if (viewHolder instanceof Holder) {
                ((Holder) viewHolder).tv_record_month.setText((monthAccout.getMonth() + 1) + "月");
                ((Holder) viewHolder).tv_record_income.setText("收  " + monthAccout.getIncome());
                ((Holder) viewHolder).tv_record_expend.setText("支  " + monthAccout.getExpend());
                ((Holder) viewHolder).tv_record_balance.setText("" + (monthAccout.getIncome() - monthAccout.getExpend()));

                if (mListener == null) return;
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos, monthAccout);
                    }
                });
            }

        }


    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_record_income, tv_record_month, tv_record_expend, tv_record_balance;

        //        //---------------------------头布局----------------
//        TextView tv_year;
//        LineChart lineChart;
        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            else {
                tv_record_balance = itemView.findViewById(R.id.tv_record_balance);
                tv_record_expend = itemView.findViewById(R.id.tv_record_expend);
                tv_record_income = itemView.findViewById(R.id.tv_record_income);
                tv_record_month = itemView.findViewById(R.id.tv_month);

            }
        }
    }

    class ChartHolder extends RecyclerView.ViewHolder {

        LineChart lineChart;
        TextView tv_year_income, tv_year_expend, tv_year_count;

        public ChartHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                lineChart = itemView.findViewById(R.id.lineChart);
                tv_year_count = itemView.findViewById(R.id.tv_year_count);
                tv_year_income = itemView.findViewById(R.id.tv_year_income);
                tv_year_expend = itemView.findViewById(R.id.tv_year_expend);
            }
        }
    }


    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }


    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }


//-----------------------------设置图案-----------------------------------------

    private void init(LineChart mChart) {
        Typeface tf = null;
//        iniData();

        mChart.getDescription().setEnabled(false);
        /*是否可以触摸*/
        mChart.setTouchEnabled(true);
        /*是否显示表格颜色*/
        mChart.setDrawGridBackground(false);

        mChart.setDragEnabled(true);// 是否可以拖拽
        mChart.setScaleEnabled(true);// 是否可以缩放

        mChart.setData(generateLineData());
        mChart.animateX(1000);
        // 是否在折线图上添加边框
        mChart.setDrawBorders(false);


        setLabel(mChart, tf);

        setYAxis(mChart, tf);

        setXAxis(mChart, tf);


    }

    /**
     * 设置曲线值，范围等
     */
    private void iniData() {
        incomeEntry.clear();
        expendEntry.clear();
        balanceEntry.clear();
        SUM_COUNT = 0.0f;
        SUM_IN = 0.0f;
        SUM_EX = 0.0f;
        month_MAX = 2000.0f;
        month_MIN = -1000.0f;
        for (int i = 0; i < mList.size(); i++) {
            float x = mList.get(i).getIncome();
            float y = mList.get(i).getExpend();

            //设置赋值范围
            if (x > month_MAX)
                month_MAX = x;
            if (y < month_MIN)
                month_MIN = y;

            /*设置坐标位置*/
            incomeEntry.add(new Entry(i, x));
            expendEntry.add(new Entry(i, y));
            balanceEntry.add(new Entry(i, (x + y)));
//求收入，支出总和
            SUM_IN += x;
            SUM_EX += y;
        }
        SUM_COUNT = SUM_EX + SUM_IN;


    }

    /**
     * 设置标签说明
     *
     * @param tf
     */
    private void setLabel(LineChart mChart, Typeface tf) {
        Legend l = mChart.getLegend();//设置比例图
        l.setEnabled(false);//设置禁用比例块
        l.setTypeface(tf);
        l.setTextColor(mContext.getResources().getColor(R.color.text_gray));

    }

    /**
     * 设置Y轴
     *
     * @param tf
     */
    private void setYAxis(LineChart mChart, Typeface tf) {
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        float MAX = (float) MathTools.getCeil(month_MAX);
        float MIN = (float) MathTools.getFloor(month_MIN);
        leftAxis.setAxisMaximum(MAX);
        leftAxis.setAxisMinimum(MIN);
        leftAxis.setDrawGridLines(false);


        leftAxis.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        leftAxis.setAxisLineColor(mContext.getResources().getColor(R.color.white));

        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //设置成整数
                return MathTools.getBigDecimal(value);
            }
        });

       /*设置右侧显示坐标*/
        mChart.getAxisRight().setEnabled(false);
    }

    /**
     * 设置X轴
     *
     * @param tf
     */
    private void setXAxis(LineChart mChart, Typeface tf) {
        XAxis xAxis = mChart.getXAxis();
        /*是否显示X轴坐标轴*/
        xAxis.setEnabled(true);
        /*X轴刻度值颜色*/
//        xAxis.setTextColor(getResources().getColor(R.color.card_tropical));
        /*X轴出现的位置*/
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(incomeEntry.size());
        xAxis.setTypeface(tf);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(expendEntry.size());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value > 11)
                    return "";
                return (int) (value + 1) + "月";
            }
        });
    }


    /**
     * 结余曲线
     *
     * @return
     */
    protected LineDataSet getDataBalance() {

        LineDataSet ds1 = new LineDataSet(balanceEntry, "结余");
        ds1.setLineWidth(2f);
        ds1.setDrawCircles(true);
        ds1.setCircleColor(mContext.getResources().getColor(R.color.line_purple));
        ds1.setDrawCircleHole(false);
//        ds1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
//        ds1.setCubicIntensity(0.4f);
//        set1.setLineWidth(2f);
//        set1.setCircleRadius(3f);
        ds1.setValueTextColor(mContext.getResources().getColor(R.color.white));
        ds1.setColor(mContext.getResources().getColor(R.color.line_purple));
        return ds1;
    }

    /**
     * 收入曲线
     *
     * @return
     */
    protected LineDataSet getDateIncome() {
        LineDataSet ds2 = new LineDataSet(incomeEntry, "收入");
        //设置线条宽度
        ds2.setLineWidth(2f);
        //设置有原点
        ds2.setDrawCircles(false);
        ds2.setColor(mContext.getResources().getColor(R.color.line_green));
        ds2.setCircleColor(mContext.getResources().getColor(R.color.line_green));
        ds2.setDrawCircleHole(true);
        ds2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
        ds2.setCubicIntensity(0.9f);
        ds2.setValueTextColor(mContext.getResources().getColor(R.color.white));
//        ds2.setValueFormatter();
//        ds2.setCircleRadius(3f);



        return ds2;

    }


    /**
     * 支出曲线
     *
     * @return
     */
    protected LineDataSet getDateExpend() {
        LineDataSet ds2 = new LineDataSet(expendEntry, "支出");
        //设置线条宽度
        ds2.setLineWidth(2f);
        //设置有原点
        ds2.setDrawCircles(true);
        ds2.setColor(mContext.getResources().getColor(R.color.line_yellow));
        ds2.setCircleColor(mContext.getResources().getColor(R.color.line_yellow));
        ds2.setDrawCircleHole(false);
//        ds2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
//        ds2.setCubicIntensity(0.9f);
        ds2.setValueTextColor(mContext.getResources().getColor(R.color.white));
        return ds2;

    }

    /**
     * 结余曲线
     *
     * @return
     */
    protected LineData generateLineData() {
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(getDataBalance());
        sets.add(getDateIncome());
        sets.add(getDateExpend());
        LineData d = new LineData(sets);
        return d;
    }


}
