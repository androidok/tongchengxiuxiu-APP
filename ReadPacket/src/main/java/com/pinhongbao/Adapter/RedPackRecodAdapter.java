package com.pinhongbao.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinhongbao.Model.redPackRecordInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.UtilTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/10.
 */

public class RedPackRecodAdapter extends BaseAdapter {
    Activity activity;
    List<redPackRecordInfo> redPackRecordInfoList;

    public RedPackRecodAdapter(Activity activity, List<redPackRecordInfo> redPackRecordInfoList) {
        this.activity = activity;
        this.redPackRecordInfoList = redPackRecordInfoList;
    }

    @Override
    public int getCount() {
        return redPackRecordInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_redpackrecord, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        redPackRecordInfo redPackRecordInfo = redPackRecordInfoList.get(i);
        Log.e("红包记录",""+redPackRecordInfo.toString());
        viewHolder.tvTitle.setText(redPackRecordInfo.getTitle());
        viewHolder.tvPrice.setText(redPackRecordInfo.getPay()+"元");
        viewHolder.tvJin.setText("红包金额" + redPackRecordInfo.getBack());
        viewHolder.tvTime.setText(UtilTool.timedate(redPackRecordInfo.getPaytime()));
        return view;
    }


    static class ViewHolder {
        @InjectView(R.id.image_head)
        ImageView imageHead;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_price)
        TextView tvPrice;
        @InjectView(R.id.tv_jin)
        TextView tvJin;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
