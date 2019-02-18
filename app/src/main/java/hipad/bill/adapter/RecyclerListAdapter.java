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
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hipad.bill.bean.AccountBook;
import hipad.bill.help.ItemTouchHelperAdapter;
import hipad.bill.help.ItemTouchHelperViewHolder;
import hipad.bill.help.OnStartDragListener;
import hipad.billproject.R;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<AccountBook> mList;
    private Context mContext;


    private final OnStartDragListener mDragStartListener;

    private boolean isEditState=false;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener,List<AccountBook> mList) {
        mDragStartListener = dragStartListener;
        this.mList=mList;
        this.mContext=context;
    }

    /**
     *设置编辑状态
     * @param isB
     */
    public void setEditState(boolean isB){
        this.isEditState=isB;
        notifyDataSetChanged();

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.tv_account_name.setText(mList.get(position).getAccount_Name());

        if (TextUtils.equals("1",mList.get(position).getAccount_type())){
            holder.tv_account_type.setText("账本");
            holder.img_account_book.setImageResource(R.mipmap.zhangben);
        }else{
            holder.tv_account_type.setText("AA账本");
            holder.img_account_book.setImageResource(R.mipmap.aazhangben);
        }
        if (isEditState)
            holder.img_move.setVisibility(View.VISIBLE);
        else
            holder.img_move.setVisibility(View.GONE);



        // Start a drag whenever the handle view it touched
        holder.img_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener!=null){
                    listener.itemClick(position);
                }

            }
        });

    }

    @Override
    public void onItemDismiss(int position) {

        if (listener!=null)
            listener.onItemRemove(position);


    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
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

        public final ImageView img_move;
        public final TextView tv_account_type;
        public final TextView tv_account_name;
        public final ImageView img_account_book;
        public final FrameLayout item;




        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_account_name = (TextView) itemView.findViewById(R.id.tv_account_name);
            img_move = (ImageView) itemView.findViewById(R.id.img_move);
            tv_account_type=(TextView) itemView.findViewById(R.id.tv_account_type);
            img_account_book=(ImageView) itemView.findViewById(R.id.img_account_book);
            item=itemView.findViewById(R.id.item);
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

    public interface  OnItemClickListener{
        void itemClick(int position);
        void onItemRemove(int position);//删除position

    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener l){
        this.listener=l;
    }




}
