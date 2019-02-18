package hipad.bill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import hipad.bill.constant.SortUtils;
import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/10/23 0023.
 * 流水账单适配器
 */

public class EditClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<SortUtils> mList;

    private Context mContext;
    private View mHeaderView;



    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void addDatas(List<SortUtils> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    public EditClassifyAdapter(List<SortUtils> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;

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

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {

            if (viewHolder instanceof ChartHolder);

        } else {
            final int pos = getRealPosition(viewHolder);
            final SortUtils info = mList.get(pos);
            if (viewHolder instanceof Holder) {
                ((Holder) viewHolder).tv_bill_name.setText(info.getName());

                if (info.isSelect()){
                    SortUtils.loadDrawable(((Holder) viewHolder).img_classify, mContext, info.getCode());
                    ((Holder) viewHolder).tv_bill_name.setTextColor(Color.parseColor(info.getColor()));
                }else{
                    SortUtils.defaultDrawable(((Holder) viewHolder).img_classify, mContext, info.getCode());
                    ((Holder) viewHolder).tv_bill_name.setTextColor(mContext.getResources().getColor(R.color.black));
                }


                if (mListener == null) return;
                ((Holder) viewHolder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos);
                    }
                });
            }
        }


    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView img_classify;
        TextView  tv_bill_name;
        RelativeLayout item;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            else {
                tv_bill_name = itemView.findViewById(R.id.tv_bill_name);
                img_classify=itemView.findViewById(R.id.img_classify);
                item=itemView.findViewById(R.id.item);

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


}
