package com.voisd.sun.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by jm on 2016/6/28.
 */
public class AddCartView extends FrameLayout {

    View view;
    PointF startPointF,overPointF;

    public AddCartView(Context context) {
        super(context);
        init(context,null,0);
    }

    public AddCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public AddCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr){
//        view = LayoutInflater.from(context).inflate(R.);
        startPointF = new PointF();
        overPointF = new PointF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);

    }

    private void drawCircle(Canvas canvas) {
//        float x = pointF.x;
//        float y = pointF.y;

    }
}
