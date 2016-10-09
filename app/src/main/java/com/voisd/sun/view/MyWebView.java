package com.voisd.sun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by voisd on 2016/9/26.
 * 联系方式：531972376@qq.com
 */
public class MyWebView extends WebView{

    ScrollInterface web;

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    boolean isTop = false;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(web!=null)
            web.onSChanged(l, t, oldl, oldt);
    }

    // 设置父控件是否可以获取到触摸处理权限
    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
//            case MotionEvent.ACTION_DOWN:
//                setParentScrollAble(false);
//                System.out.println("ACTION_DOWN=======收取ontouch权限");
//            case MotionEvent.ACTION_MOVE:
//                if(isTop()){
//                    setParentScrollAble(true);
//                    System.out.println("ACTION_MOVE=======交出ontouch权限");
//                }else{
//                    setParentScrollAble(false);
//                    System.out.println("ACTION_MOVE=======收取ontouch权限");
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // 当手指松开时，让父控件重新获取onTouch权限
//                System.out.println("ACTION_UP=======交出ontouch权限");
//                setParentScrollAble(true);
//                break;
//
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    public void setOnCustomScroolChangeListener(ScrollInterface t) {
        this.web = t;
    }

    /**
     * 定义滑动接口
     */
    public interface ScrollInterface {

        public void onSChanged(int l, int t, int oldl, int oldt);
    }


}
