package com.example.administrator.learn.Tool;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.learn.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/29. 分享对话框  从底部弹出动画的popuview
 */

public class SelecPopuview extends PopupWindow {
    @InjectView(R.id.layout_weibo)
    LinearLayout layoutWeibo;
    @InjectView(R.id.layout_weixin)
    LinearLayout layoutWeixin;
    @InjectView(R.id.layout_friend)
    LinearLayout layoutFriend;
    @InjectView(R.id.layout_qq)
    LinearLayout layoutQq;
    @InjectView(R.id.layout_qqzone)
    LinearLayout layoutQqzone;
    public static final int WEIBO = 0;
    public static final int WEIXIN = 1;
    public static final int FRIEND = 2;
    public static final int QQ = 3;
    public static final int QQZONE = 4;


    @OnClick({R.id.layout_weibo, R.id.layout_weixin, R.id.layout_friend, R.id.layout_qq, R.id.layout_qqzone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_weibo:
                setonListener.setonlistfener(v, WEIBO);
                break;
            case R.id.layout_weixin:
                setonListener.setonlistfener(v, WEIXIN);
                break;
            case R.id.layout_friend:
                setonListener.setonlistfener(v, FRIEND);
                break;
            case R.id.layout_qq:
                setonListener.setonlistfener(v, QQ);
                break;
            case R.id.layout_qqzone:
                setonListener.setonlistfener(v, QQZONE);
                break;
        }
        this.dismiss();

    }

    public interface setonListener {
        void setonlistfener(View view, int index);
    }

    private final View mPopuview;
    setonListener setonListener;

    public SelecPopuview(Context context, setonListener setonListener) {
        this.setonListener = setonListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mPopuview = inflater.inflate(R.layout.layout_popuview, null);
        ButterKnife.inject(this,mPopuview);
        //设置SelectPicPopupWindow的View
        this.setContentView(mPopuview);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        mPopuview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mPopuview.findViewById(R.id.pop_layout).getTop();
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
