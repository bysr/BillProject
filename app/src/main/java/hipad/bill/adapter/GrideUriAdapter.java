package hipad.bill.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import hipad.billproject.R;

/**
 * Created by wangyawen on 2017/11/8 0008.
 */

public class GrideUriAdapter extends BaseAdapter {

    Context context;
    List<String> paths = new ArrayList<String>();

    public GrideUriAdapter(Context context, List<String> paths) {
        super();
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_grade_pic, parent, false);
            holder = new ViewHolder();
            holder.iv = (ImageView) view.findViewById(R.id.item_grida_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        return view;
    }

    class ViewHolder {
        ImageView iv;
    }
}
