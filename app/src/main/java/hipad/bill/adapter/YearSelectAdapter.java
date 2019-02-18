package hipad.bill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import hipad.bill.bean.YearAccout;
import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 流水账单适配器
 */

public class YearSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<YearAccout> mList;

    private Context mContext;
    private View mHeaderView;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, YearAccout data);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void addDatas(List<YearAccout> datas) {
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    public YearSelectAdapter(List<YearAccout> mList, Context mContext) {
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
        } else {
            final int pos = getRealPosition(viewHolder);
            final YearAccout accout = mList.get(pos);
            if (viewHolder instanceof Holder) {
                ((Holder) viewHolder).tv_record_month.setText((accout.getYear()) + "年");
                ((Holder) viewHolder).tv_record_income.setText("收  " + accout.getIncome());
                ((Holder) viewHolder).tv_record_expend.setText("支  " + accout.getExpend());
                ((Holder) viewHolder).tv_record_balance.setText("" + (accout.getIncome() - accout.getExpend()));

                if (mListener == null) return;
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos, accout);
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







}
