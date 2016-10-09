package com.voisd.sun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by voisd on 2016/9/28.
 * 联系方式：531972376@qq.com
 * 监听跟VIew 在弹出输入法或者关闭输入法
 */
public class MyLinearLayout extends LinearLayout{
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnResizeListener mListener;
    public interface OnResizeListener {
        void OnResize(int w, int h, int oldw, int oldh);
    }
    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.OnResize(w, h, oldw, oldh);
        }
    }
}
