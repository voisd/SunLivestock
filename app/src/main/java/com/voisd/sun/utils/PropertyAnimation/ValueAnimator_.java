package com.voisd.sun.utils.PropertyAnimation;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by jm on 2016/6/25.
 * ValueAnimator
 * 负责管理动画的值改变、播放次数、播放模式、以及对动画设置监听器等。
 * 一个数值产生器，他本身不作用于任何一个对象，但是可以对产生的值进行动画处理
 *
 */

public class ValueAnimator_ {

    public  void test1(){
        /**
         * 基本用法
         * 值用0-1，300毫秒
         * */
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        //可设置过渡值得参数类型
        //ValueAnimator.ofInt(0,10);
        anim.setDuration(300);
        anim.start();
    }

    public  void test2(){
        /**
         * 基本用法
         * 添加改变监听
         * 值用0-1，300毫秒
         * */
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                Log.d("TAG", "cuurent value is " + currentValue);
            }
        });
        anim.start();
    }

    public  void test3(){
        /**
         * 基本用法
         * 可多个参数
         * 值用0-10-3-20，3000毫秒
         * */
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 10f,3f,20f);
        anim.setDuration(3000);
        anim.start();
    }

    public void test4(){
        /**
         * 基本用法
         * 可多个参数
         * 值用0-10-3-20，3000毫秒
         * */
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 10f,3f,20f);
        anim.setDuration(3000);
        anim.setStartDelay(3000);//设置延迟开始
        anim.setRepeatCount(1);//设置重复次数
        anim.setRepeatMode(ValueAnimator.RESTART); //设置循环播放的模式
        // 循环模式包括RESTART和REVERSE两种，分别表示重新播放和倒序播放
        anim.start();
    }

    public void test5(final View view){
        /**
         * 自定义高级对象计算
         * getLocationInWindow 获取绝对坐标
         *
         * */
        int[] location = new int[2];
        PointF startPointF,overPointF;
        view.getLocationInWindow(location); //拿到view当前绝对坐标位置
        startPointF = new PointF(location[0],location[1]);
        overPointF = new PointF(location[0]+500,location[1]+500);
        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator_(),startPointF,overPointF);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF)animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
                //动态修改坐标
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(0.5f)); //设置变换
        animator.setDuration(1300);
    }

}

