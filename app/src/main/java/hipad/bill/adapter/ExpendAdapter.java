package hipad.bill.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import hipad.bill.bean.AccountInfo;
import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/23 0023.
 */

public class ExpendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<AccountInfo> mList;

    private Context mContext;
    private View mHeaderView;

    private List<Entry> incomeEntry = new ArrayList<>();
    private List<Entry> expendEntry = new ArrayList<>();
    private List<Entry> balanceEntry = new ArrayList<>();

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AccountInfo data);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void addDatas(List<AccountInfo> datas) {
        mList.addAll(datas);
        iniData();
        notifyDataSetChanged();
    }

    public ExpendAdapter(List<AccountInfo> mList, Context mContext) {
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

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {

            if (viewHolder instanceof ChartHolder)
                init(((ChartHolder) viewHolder).lineChart);

        } else {
            final int pos = getRealPosition(viewHolder);
            final AccountInfo info = mList.get(pos);
            if (viewHolder instanceof Holder) {
                ((Holder) viewHolder).tv_bill_money.setText(info.getBill_money() + "");//消费




                if (mListener == null) return;
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos, info);
                    }
                });
            }

        }


    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_bill_name, tv_bill_money, tv_scale;
        ImageView img_bill;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            else {
                tv_bill_name = itemView.findViewById(R.id.tv_bill_name);
                tv_bill_money = itemView.findViewById(R.id.tv_bill_money);
                tv_scale = itemView.findViewById(R.id.tv_scale);
                img_bill = itemView.findViewById(R.id.img_bill);

            }
        }
    }

    class ChartHolder extends RecyclerView.ViewHolder {

        TextView tv_year;
        LineChart lineChart;

        public ChartHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                tv_year = itemView.findViewById(R.id.tv_year);
                lineChart = itemView.findViewById(R.id.lineChart);
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

//        for (int i = 0; i < mList.size(); i++) {
//            float x = mList.get(i).getIncome();
//            float y = mList.get(i).getExpend();
//            incomeEntry.add(new Entry(i, x));
//            expendEntry.add(new Entry(i, -y));
//            balanceEntry.add(new Entry(i, (x - y)));
//        }


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
        leftAxis.setAxisMaximum(2000f);
        leftAxis.setAxisMinimum(-2000f);
        leftAxis.setDrawGridLines(false);


        leftAxis.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        leftAxis.setAxisLineColor(mContext.getResources().getColor(R.color.white));

        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + value;
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
        ds1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
        ds1.setCubicIntensity(0.4f);
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
        ds2.setDrawCircles(true);
        ds2.setColor(mContext.getResources().getColor(R.color.line_green));
        ds2.setCircleColor(mContext.getResources().getColor(R.color.line_green));
        ds2.setDrawCircleHole(false);
        ds2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
        ds2.setCubicIntensity(0.9f);
        ds2.setValueTextColor(mContext.getResources().getColor(R.color.white));
//        ds2.setValueFormatter();

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
        ds2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);// 改变折线样式，用曲线。
        ds2.setValueTextColor(mContext.getResources().getColor(R.color.white));
        ds2.setCubicIntensity(0.9f);
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
