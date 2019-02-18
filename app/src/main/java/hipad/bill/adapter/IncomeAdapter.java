package hipad.bill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.bean.AccountInfo;
import hipad.bill.constant.SortUtils;
import hipad.bill.utils.MathTools;
import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 流水账单适配器
 */

public class IncomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<AccountInfo> mList;

    private Context mContext;
    private View mHeaderView;

    private float count;//总值

    private String title;//显示文本信息


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
        if (datas != null) {
            mList.clear();
            mList.addAll(datas);
            iniData();
            notifyDataSetChanged();
        }
    }

    public IncomeAdapter(List<AccountInfo> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        //计算结果，循环遍历数据

    }

    public void setTitle(String title) {
        this.title = title;
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
                init(((ChartHolder) viewHolder).pieChart);

        } else {
            final int pos = getRealPosition(viewHolder);
            final AccountInfo info = mList.get(pos);
            if (viewHolder instanceof Holder) {
                ((Holder) viewHolder).tv_bill_money.setText(info.getBill_money() + "");//消费
                ((Holder) viewHolder).tv_scale.setText(MathTools.getScale(info.getBill_money(), count));

                SortUtils utils = SortUtils.get(info.getType());
                ((Holder) viewHolder).tv_bill_name.setText(utils.getName());
                SortUtils.loadDrawable(((Holder) viewHolder).img_bill, mContext, utils.getCode());

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

        PieChart pieChart;

        public ChartHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                pieChart = itemView.findViewById(R.id.pieChart);
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

    private void init(PieChart mChart) {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mChart.setOnChartValueSelectedListener(mContext);
        setData(100, mChart);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);

    }

    private void setData(float range, PieChart mChart) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
//                    "test",
//                    mContext.getResources().getDrawable(R.drawable.maicai1)));
//        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            AccountInfo info = mList.get(i);
            SortUtils sortUtils = SortUtils.get(info.getType());
            PieEntry entry = new PieEntry(mList.get(i).getBill_money(), sortUtils.getName());
            entries.add(entry);
            colors.add(Color.parseColor(sortUtils.getColor()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "资产总览");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /**
     * 设置曲线值，范围等
     */
    private void iniData() {

        count = 0.0f;
        for (AccountInfo info : mList) {
            count += info.getBill_money();
        }


    }

    private SpannableString generateCenterSpannableText() {


        String strCount = MathTools.getNumToStr(count);

        String EditText = strCount + "\n" + title;

//        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        SpannableString s = new SpannableString(EditText);
        s.setSpan(new RelativeSizeSpan(1.8f), 0, strCount.length(), 0);
        s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, strCount.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.2f), strCount.length(), EditText.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.yellow_gray)), strCount.length(), EditText.length(), 0);
        return s;
    }

}
