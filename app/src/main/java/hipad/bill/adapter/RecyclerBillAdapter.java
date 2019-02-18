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
import android.graphics.Color;
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
import hipad.billproject.R;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerBillAdapter extends RecyclerView.Adapter<RecyclerBillAdapter.ItemViewHolder> {

    private List<AccountInfo> mList;
    private Context mContext;

    private Map<String, DateAccout> map;


    public RecyclerBillAdapter(Context context, List<AccountInfo> mList) {
        this.mList = mList;
        this.mContext = context;
        map = new HashMap<>();
    }

    /**
     * 更新数据，给map赋值
     */
    public void notifyData() {
        initData();
        notifyDataSetChanged();
    }

    private void initData() {
        map.clear();
        for (int i = 0; i < getItemCount(); i++) {
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


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        holder.tv_bill_money.setText(mList.get(position).getBill_money() + "");
        holder.tv_bill_remark.setText(mList.get(position).getBill_remark());

        int type = mList.get(position).getType();

        if (position == getPositionForSection(position)) {
            holder.ll_month_count.setVisibility(View.VISIBLE);
            String time = DateUtils.stampToDate(mList.get(position).getBill_time(), DateUtils.DATE_TEMPLATE_DATE);

            String month = time.substring(8, 10);
            DateAccout accout = map.get(time);


            holder.tv_bill_time.setText(String.format(mContext.getResources().getString(R.string.data_week), month, DateUtils.getWeekData(mList.get(position).getBill_time())));
            holder.tv_bill_price.setText(String.format(mContext.getResources().getString(R.string.account_money), accout.getExpend() + "", accout.getIncome() + ""));


        } else {
            holder.ll_month_count.setVisibility(View.GONE);
        }
        SortUtils utils = SortUtils.get(type);
        holder.tv_bill_name.setText(utils.getName());
        SortUtils.loadDrawable(holder.img_bill, mContext, utils.getCode());

        // Start a drag whenever the handle view it touched

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    listener.itemClick(position);
                }

            }
        });

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
        return mList.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final ImageView img_bill;
        public final TextView tv_bill_remark;
        public final TextView tv_bill_name;
        public final TextView tv_bill_money;
        public final LinearLayout item;

        public final LinearLayout ll_month_count;
        public final TextView tv_bill_time, tv_bill_price;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_bill_remark = (TextView) itemView.findViewById(R.id.tv_bill_remark);
            img_bill = (ImageView) itemView.findViewById(R.id.img_bill);
            tv_bill_name = (TextView) itemView.findViewById(R.id.tv_bill_name);
            tv_bill_money = (TextView) itemView.findViewById(R.id.tv_bill_money);
            ll_month_count = itemView.findViewById(R.id.ll_month_count);
            tv_bill_time = itemView.findViewById(R.id.tv_bill_time);
            tv_bill_price = itemView.findViewById(R.id.tv_bill_price);
            item = itemView.findViewById(R.id.item);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
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
