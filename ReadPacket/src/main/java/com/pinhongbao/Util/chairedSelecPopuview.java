package com.pinhongbao.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pinhongbao.R;

/**
 * Created by Administrator on 2017/2/14.
 */

public class chairedSelecPopuview extends PopupWindow{

    private final ImageView imageView;
    private final TextView tv_gong;
    private final TextView tv_money;

    public interface setonListener {
        void setonlistfener(View view, ImageView imageView, TextView tv_gong,TextView tv_mon);
    }

    private final View mPopuview;
    setonListener setonListener;

    public chairedSelecPopuview(Context context, final setonListener setonListener) {
        this.setonListener = setonListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mPopuview = inflater.inflate(R.layout.layout_pupuview_chaired, null);
        imageView = (ImageView) mPopuview.findViewById(R.id.image);
        tv_gong = (TextView) mPopuview.findViewById(R.id.tv_gong);
        tv_money = (TextView) mPopuview.findViewById(R.id.tv_money);
        //设置SelectPicPopupWindow的View
        this.setContentView(mPopuview);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        mPopuview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mPopuview.findViewById(R.id.layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setonListener.setonlistfener(v,imageView,tv_gong,tv_money);
            }
        });
    }
}
