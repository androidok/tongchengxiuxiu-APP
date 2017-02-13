package com.pinhongbao.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pinhongbao.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/29.
 */

public class SelecPopuview extends PopupWindow {


    @InjectView(R.id.tv_all)
    TextView tvAll;
    @InjectView(R.id.tv_oing)
    TextView tvOing;
    @InjectView(R.id.tv_wan)
    TextView tvWan;
    @InjectView(R.id.tv_tui)
    TextView tvTui;

    @OnClick({R.id.tv_all,R.id.tv_oing,R.id.tv_wan,R.id.tv_tui})
    public void onClick(View view) {
        dismiss();
        switch (view.getId()){
            case R.id.tv_all:
                setonListener.setonlistfener(view,-1);
                break;
            case R.id.tv_oing:
                setonListener.setonlistfener(view,0);
                break;
            case R.id.tv_wan:
                setonListener.setonlistfener(view,2);
                break;
            case R.id.tv_tui:
                setonListener.setonlistfener(view,3);
                break;
        }
    }

    public interface setonListener {
        void setonlistfener(View view, int index);
    }

    private final View mPopuview;
    setonListener setonListener;

    public SelecPopuview(Context context, setonListener setonListener) {
        this.setonListener = setonListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mPopuview = inflater.inflate(R.layout.popuview_item, null);
        ButterKnife.inject(this, mPopuview);
        //设置SelectPicPopupWindow的View
        this.setContentView(mPopuview);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
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
    }
}
