package com.pinhongbao.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinhongbao.R;

/**
 * Created by Administrator on 2016/12/2.
 */
public class Indicator extends LinearLayout {
    //Indicator的宽和高
    private int mWidth;
    private int mHeight = 5;
    //Indicator的左、上坐标
    private int mTop;
    private int mLeft;
    //自定义属性
    private int mColor;
    //LinearLayout的子View数
    private int mChildCount;
    private int pos;
    private Paint mPaint;

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setBackgroundColor(Color.TRANSPARENT);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
        mColor = array.getColor(R.styleable.Indicator_indicatorColor, Color.RED);
        array.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取子View数量，也就是TextView数
        mChildCount = getChildCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTop = getMeasuredHeight();
        //LinearLayout的宽和高
        int width = getMeasuredWidth();
        int height = mTop + mHeight;
        mWidth = width / mChildCount;
        //设置测量款和高
        setMeasuredDimension(width,height);
    }

    /**
     * 只要ViewPager一直在滑动就会调用onPageScrolled方法进而调用scroll方法
     * 参数position是当前ViewPager位置
     * 参数offset是当前ViewPager的偏移量，大小是0~1的，
     * 而我们指示器的偏移正好是使用这个参数实现的
     */
    public void scroll(int position,float offset){
        mLeft = (int) ((position + offset) * mWidth);
        pos = position;
        //ViewPager不断偏移就不断重绘指示器位置
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制指示器
        Log.d("宽度",mLeft+".."+mWidth);
        Rect rect = new Rect(mLeft+60,mTop,mLeft+mWidth-60,mTop + mHeight);
        canvas.drawRect(rect, mPaint);
        //每次重绘都重置文字颜色
        resetColor();
        //并设置被选中的和当前ViewPager对应的文字颜色
        TextView tv = (TextView) getChildAt(pos);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setTextSize(16);
    }

    private void resetColor() {
        for (int i = 0;i < mChildCount;i++){
            TextView tv = (TextView) getChildAt(i);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(16);
        }
    }
}
