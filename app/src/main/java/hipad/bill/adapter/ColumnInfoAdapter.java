/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hipad.bill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hipad.bill.bean.AccountInfo;
import hipad.bill.bean.DateAccout;
import hipad.bill.constant.SortUtils;
import hipad.bill.help.ItemTouchHelperAdapter;
import hipad.bill.help.ItemTouchHelperViewHolder;
import hipad.bill.utils.DateUtils;
import hipad.bill.utils.MathTools;
import hipad.billproject.R;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class ColumnInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AccountInfo> mList;
    private Context mContext;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private int type;
    private float count, tcount;
    private View mHeaderView;
    private Map<String, DateAccout> map;

    public ColumnInfoAdapter(Context context, List<AccountInfo> mList, int type) {
        this.mList = mList;
        this.mContext = context;
        this.type = type;
        map=new HashMap<>();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void addDatas(List<AccountInfo> datas, float Count, float tCount) {
        mList.clear();
        this.count = Count;
        this.tcount = tCount;
        mList.addAll(datas);
        initData();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new HeadHolder(mHeaderView);

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ItemViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_HEADER) {

            if (holder instanceof HeadHolder) {
                SortUtils utils=SortUtils.get(type);
                ((HeadHolder) holder).tv_scale.setText(MathTools.getScale(tcount, count));
                ((HeadHolder) holder).tv_cate.setText(utils.getName());
                ((HeadHolder) holder).tv_consume.setText(count+"");
            }


        } else {
            final int pos = getRealPosition(holder);
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).tv_bill_money.setText(mList.get(pos).getBill_money() + "");
                ((ItemViewHolder) holder).tv_bill_remark.setText(mList.get(pos).getBill_remark());

                int type = mList.get(pos).getType();

                if (pos == getPositionForSection(pos)) {
                    ((ItemViewHolder) holder).ll_month_count.setVisibility(View.VISIBLE);
                    String time = DateUtils.stampToDate(mList.get(pos).getBill_time(), DateUtils.DATE_TEMPLATE_DATE);

                    String month = time.substring(8, 10);
                    DateAccout accout = map.get(time);


                    ((ItemViewHolder) holder).tv_bill_time.setText(String.format(mContext.getResources().getString(R.string.data_week), month, DateUtils.getWeekData(mList.get(pos).getBill_time())));
                    ((ItemViewHolder) holder).tv_bill_price.setText(String.format(mContext.getResources().getString(R.string.account_money), accout.getExpend() + "", accout.getIncome() + ""));


                } else {
                    ((ItemViewHolder) holder).ll_month_count.setVisibility(View.GONE);
                }

                SortUtils utils = SortUtils.get(type);
                SortUtils.loadDrawable(((ItemViewHolder) holder).img_bill, mContext, utils.getCode());
                ((ItemViewHolder) holder).tv_bill_name.setText(utils.getName());
                // Start a drag whenever the handle view it touched

                ((ItemViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (listener != null) {
                            listener.itemClick(pos);
                        }

                    }
                });

            }
        }


    }
    private void initData() {
        map.clear();
        for (int i = 0; i < mList.size(); i++) {
            /*获取遍历到的第一个位置的时间值*/
            long sortTime = mList.get(i).getBill_time();
            String time = DateUtils.stampToDate(sortTime, DateUtils.DATE_TEMPLATE_DATE);//得到数据时间
            float income = mList.get(i).getBill_money();
            float expend = 0.0f;
            if (income < 0) {
                expend = income;
                income = 0.0f;
            }
            //此处保存时间戳对象
            if (!map.containsKey(time)) {
                DateAccout accout = new DateAccout(time, income, expend);
                map.put(time, accout);
            } else {
                DateAccout accout = map.get(time);//获取当前月信息对象
                accout.setIncome(accout.getIncome() + income);
                accout.setExpend(accout.getExpend() + expend);
            }

        }


    }



    /**
     * 当前对象的时间戳
     *
     * @param position
     * @return
     */
    public int getPositionForSection(int position) {

        long sectionTime = mList.get(position).getBill_time();
        //转化为年月日
        String section = DateUtils.stampToDate(sectionTime, DateUtils.DATE_TEMPLATE_DATE);

        for (int i = 0; i < getItemCount(); i++) {
            /*获取遍历到的第一个位置的时间值*/
            long sortTime = mList.get(i).getBill_time();
            String time = DateUtils.stampToDate(sortTime, DateUtils.DATE_TEMPLATE_DATE);
            if (TextUtils.equals(section, time)) {//得到相同时间戳的对象
                return i;
            }
        }
        return -1;
    }



    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        //比列，类别，消费占比
        TextView tv_scale, tv_cate, tv_consume;

        public HeadHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                tv_scale = itemView.findViewById(R.id.tv_scale);
                tv_cate = itemView.findViewById(R.id.tv_cate);
                tv_consume = itemView.findViewById(R.id.tv_consume);
            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_bill;
        public TextView tv_bill_remark;
        public TextView tv_bill_name;
        public TextView tv_bill_money;
        public LinearLayout item;

        public  LinearLayout ll_month_count;

        public TextView tv_bill_time,tv_bill_price;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_bill_remark = (TextView) itemView.findViewById(R.id.tv_bill_remark);
            img_bill = (ImageView) itemView.findViewById(R.id.img_bill);
            tv_bill_name = (TextView) itemView.findViewById(R.id.tv_bill_name);
            tv_bill_money = (TextView) itemView.findViewById(R.id.tv_bill_money);
            ll_month_count = itemView.findViewById(R.id.ll_month_count);
            item = itemView.findViewById(R.id.item);
            tv_bill_time=itemView.findViewById(R.id.tv_bill_time);
            tv_bill_price=itemView.findViewById(R.id.tv_bill_price);
        }

    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }


}
