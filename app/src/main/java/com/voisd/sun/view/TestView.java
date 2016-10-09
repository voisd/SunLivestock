package com.voisd.sun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jm on 2016/6/25.
 */
public class TestView extends TextView{

    int mWidth,mHeidth;

    public TestView(Context context) {
        super(context);
        init(context, null,0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( context,  attrs, 0);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr){
        mWidth = getWidth();

    }

    /**
     * 测量View的大小
     * widthMeasureSpec 系统默认的宽
     * heightMeasureSpec 系统默认的高
     * Mode 根据定义设置的宽和高来设置
     * EXACTLY 精确模式 父View已经检测出了子View的大小
     * AT_MOST 父view还没检测子View的大小，但是指出子View 不能超出的范围  match  fial
     * UNSPECIFIED  父没测出子View大小，并且不限制子View视图的大小
     * **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if(widthMode == MeasureSpec.EXACTLY&&
                heightMode ==MeasureSpec.EXACTLY) //精确模式
        {
            System.out.println(mWidth+"____0____"+mHeidth);
            setMeasuredDimension(mWidth,mHeidth);
        }else if(widthMode == MeasureSpec.AT_MOST&&
                heightMode ==MeasureSpec.AT_MOST){
            System.out.println(mWidth+"____1____"+mHeidth);
            setMeasuredDimension(widthSize,heightSize);
        }
        else if(widthMode==MeasureSpec.AT_MOST){
            System.out.println(mWidth+"____2____"+heightSize);
            setMeasuredDimension(mWidth,heightSize);
        }else if(heightMode==MeasureSpec.AT_MOST){
            System.out.println(widthSize+"____3____"+mHeidth);
            setMeasuredDimension(widthSize,mHeidth);
        }

    }
}
