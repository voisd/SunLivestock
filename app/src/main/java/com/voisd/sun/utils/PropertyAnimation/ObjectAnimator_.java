package com.voisd.sun.utils.PropertyAnimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jm on 2016/6/25.
 * ObjectAnimator
 * 可以直接对任意对象的任意属性进行动画操作的，比如说View的alpha属性。
 * 继承ValueAnimation
 * 需要对对象操作的属性有提供 get 和 set 方法
 * 比如 alpha 透明度 setwidth-getwidth
 * ObjectAnimator内部的工作机制并不是直接对我们传入的属性名进行操作的
 * 而是会去寻找这个属性名对应的get和set方法
 */
public class ObjectAnimator_ {
    View view;

    public void ObjectAnimation_(Context context){
        view = new View(context);
    }

    public void test1(){
        /**
         * alpha  透明度
         * */
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f);
        animator.setDuration(5000);
        animator.start();
    }

    public void test2(){
        /**
         * rotation  旋转
         * */
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(5000);
        animator.start();
    }

    public void test3(){
        /**
         * translationX  水平距离移动
         * */
        float curTranslationX = view.getTranslationX();//获取到当前的translationX的位置
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,
                "translationX", curTranslationX, -500f, curTranslationX);
        animator.setDuration(5000);
        animator.start();
    }

    public void test4(){
        /**
         * scaleY  垂直缩变化
         * */
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 3f, 1f);
        animator.setDuration(5000);
        animator.start();
    }

    public void test5(final View view){
        /**
         * 自定义高级属性特效
         * getLocationInWindow 获取绝对坐标
         *
         * */
//        int[] location = new int[2];
//        PointF startPointF,overPointF;
//        view.getLocationInWindow(location); //拿到view当前绝对坐标位置
//        startPointF = new PointF(location[0],location[1]);
//        overPointF = new PointF(location[0]+500,location[1]+500);
//
//        ObjectAnimator animator = ObjectAnimator.ofObject()
//
//                ofObject(new TypeEvaluator_(),startPointF,overPointF);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                PointF pointF = (PointF)animation.getAnimatedValue();
//                view.setX(pointF.x);
//                view.setY(pointF.y);
//                //动态修改坐标
//            }
//        });
    }


}

class MyTestView extends View{

    PointF currentPoint;
    int RADIUS = 50;
    private Paint mPaint;


    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentPoint == null) {
            currentPoint = new PointF(RADIUS, RADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.x;
        float y = currentPoint.y;
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    private void startAnimation() {
        Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator anim = ValueAnimator.ofObject(new TypeEvaluator_(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(5000);
        anim.start();
    }

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }
}
