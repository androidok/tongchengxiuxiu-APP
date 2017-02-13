package com.pinhongbao.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pinhongbao.Model.lockListInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/9.
 */

public class LockListAdapter extends BaseAdapter {
    List<lockListInfo> lockListInfos;
    Activity activity;

    public LockListAdapter(Activity activity, List<lockListInfo> lockListInfos) {
        this.lockListInfos = lockListInfos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return lockListInfos.size();
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
             view = activity.getLayoutInflater().inflate(R.layout.item_listviwe_shouqi, null);
             viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
             viewHolder = (ViewHolder) view.getTag();
        }
        lockListInfo lockListInfo = lockListInfos.get(i);
        viewHolder.num.setText(lockListInfo.getBackmoney());
        viewHolder.tvName.setText(lockListInfo.getUser_nicename());
        Glide.with(activity)
                .load(lockListInfo.getAvatar())
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new GlideCircleTransform(activity))
                .into(viewHolder.imageHead);
        viewHolder.tvRanking.setText(lockListInfo.getRanking());
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.image_head)
        ImageView imageHead;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.tv_ranking)
        TextView tvRanking;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
