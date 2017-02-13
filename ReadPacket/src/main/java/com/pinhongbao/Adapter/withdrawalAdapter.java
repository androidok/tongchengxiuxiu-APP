package com.pinhongbao.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinhongbao.Model.withdralInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.UtilTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/10.
 */

public class withdrawalAdapter extends BaseAdapter {
    Activity activity;
    List<withdralInfo> list;

    public withdrawalAdapter(Activity activity, List<withdralInfo> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            view = activity.getLayoutInflater().inflate(R.layout.item_layout_withdrawal, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        withdralInfo withdralInfo = list.get(i);
        viewHolder.tvAum.setText(withdralInfo.getBody());
        float yu = Float.parseFloat(withdralInfo.getFee()) - Float.parseFloat(withdralInfo.getMoney());
        viewHolder.tvPoundage.setText("手续费:" + UtilTool.floatzhu(yu)+"元");
        String status = withdralInfo.getStatus();
        int stat = Integer.parseInt(status);
        if (stat==0){//失败
            viewHolder.tvStatus.setText("失败");
        }else if (stat==1){//提交已完成
            viewHolder.tvStatus.setText("提现已完成");
        }else {//提现审核中
            viewHolder.tvStatus.setText("提现审核中");
        }
        viewHolder.tvTime.setText(UtilTool.timedate(withdralInfo.getTime()));

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_aum)
        TextView tvAum;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.tv_poundage)
        TextView tvPoundage;
        @InjectView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
