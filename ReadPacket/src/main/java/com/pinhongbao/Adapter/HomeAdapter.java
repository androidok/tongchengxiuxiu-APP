package com.pinhongbao.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pinhongbao.Model.homeInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/7.
 */

public class HomeAdapter extends BaseAdapter {
    Activity context;
    List<homeInfo> infoList;

    public HomeAdapter(Activity context, List<homeInfo> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
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
            view = context.getLayoutInflater().inflate(R.layout.item_listview_time, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        homeInfo dataBean = infoList.get(i);
        viewHolder.niname.setText(dataBean.getNickname());
        viewHolder.progressBar.setMax(Integer.valueOf(dataBean.getNum()));
        viewHolder.progressBar.setProgress(Integer.valueOf(dataBean.getNumed()));
        viewHolder.tvTitle.setText(dataBean.getTitle());
        viewHolder.pay.setText(dataBean.getPay()+"元参与");
        viewHolder.numed.setText("参与人数"+dataBean.getNumed()+"/"+dataBean.getNum());
        int pay = Integer.parseInt(dataBean.getPay());
        int num = Integer.parseInt(dataBean.getNum());
        double price = pay * num - (num - 1) * 0.01;
        viewHolder.tvPrice.setText(""+price);
        Glide.with(context)
                .load(dataBean.getAvatar())
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(viewHolder.imageHead);
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.image_head)
        ImageView imageHead;
        @InjectView(R.id.niname)
        TextView niname;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.pay)
        TextView pay;
        @InjectView(R.id.numed)
        TextView numed;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.tv_price)
        TextView tvPrice;
        @InjectView(R.id.progressBar)
        ProgressBar progressBar;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
