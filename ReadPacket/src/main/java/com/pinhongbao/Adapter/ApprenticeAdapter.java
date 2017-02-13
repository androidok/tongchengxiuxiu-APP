package com.pinhongbao.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pinhongbao.Model.ApprenticeInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;
import com.pinhongbao.Util.UtilTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/10.
 */

public class ApprenticeAdapter extends BaseAdapter {
    Activity activity;
    List<ApprenticeInfo> apprenticeInfoList;

    public ApprenticeAdapter(Activity activity, List<ApprenticeInfo> apprenticeInfoList) {
        this.activity = activity;
        this.apprenticeInfoList = apprenticeInfoList;
    }

    @Override
    public int getCount() {
        return apprenticeInfoList.size();
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
            view = activity.getLayoutInflater().inflate(R.layout.item_layout_apprentice, null);
             viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
             viewHolder = (ViewHolder) view.getTag();
        }
        ApprenticeInfo apprenticeInfo = apprenticeInfoList.get(i);
        viewHolder.tvId.setText(apprenticeInfo.getId());
        viewHolder.tvNiname.setText(apprenticeInfo.getUser_nicename());
        viewHolder.tvTime.setText(UtilTool.timedate(apprenticeInfo.getCreate_time()));
        Glide.with(activity)
                .load(apprenticeInfo.getAvatar())
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new GlideCircleTransform(activity))
                .into(viewHolder.imageHead);
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.image_head)
        ImageView imageHead;
        @InjectView(R.id.tv_niname)
        TextView tvNiname;
        @InjectView(R.id.tv_id)
        TextView tvId;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
