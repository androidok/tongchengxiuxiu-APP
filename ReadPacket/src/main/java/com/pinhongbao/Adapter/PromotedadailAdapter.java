package com.pinhongbao.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinhongbao.Model.promotedatailInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.UtilTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/9.
 */

public class PromotedadailAdapter extends BaseAdapter {
    Activity context;
    List<promotedatailInfo> promotedatailInfoList;

    public PromotedadailAdapter(Activity context, List<promotedatailInfo> promotedatailInfoList) {
        this.context = context;
        this.promotedatailInfoList = promotedatailInfoList;
    }

    @Override
    public int getCount() {
        return promotedatailInfoList.size();
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
        if (view==null){
             view = context.getLayoutInflater().inflate(R.layout.item_promote_detail, null);
             viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
             viewHolder = (ViewHolder) view.getTag();
        }
        promotedatailInfo promotedatailInfo = promotedatailInfoList.get(i);
        viewHolder.tvJaing.setText(promotedatailInfo.getBonus());
        viewHolder.tvPrifit.setText(promotedatailInfo.getIncome());
        viewHolder.tvStatus.setText(promotedatailInfo.getStatus().equalsIgnoreCase("1")? "已发放":"未发放");
        viewHolder.tvTime.setText(UtilTool.timedate(promotedatailInfo.getTime()));
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.tv_prifit)
        TextView tvPrifit;
        @InjectView(R.id.tv_jaing)
        TextView tvJaing;
        @InjectView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
