package com.voisd.sun.utils.PropertyAnimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by jm on 2016/6/25.
 * AnimatorSet
 * 组合动画
 * 提供一个play（）给传入一个Animator对象(ValueAnimator或ObjectAnimator)
 * after(Animator anim)   将现有动画插入到传入的动画之后执行
 * after(long delay)   将现有动画延迟指定毫秒后执行
 * before(Animator anim)   将现有动画插入到传入的动画之前执行
 * with(Animator anim)   将现有动画和传入的动画同时执行
 *
 */
public class AnimatorSet_ {

    AnimatorSet animatorSet;
    ValueAnimator valueAnimator;
    ObjectAnimator alpha,moveIn,rotate;

    public void AnimatorSet_(View view){
        animatorSet = new AnimatorSet();
        valueAnimator = ValueAnimator.ofInt(0,100);
        moveIn = ObjectAnimator.ofFloat(view,"translationX",view.getTranslationX(),-500f,view.getTranslationX());
        alpha = ObjectAnimator.ofFloat(view,"alpha",1f,0f,1f);
        rotate = ObjectAnimator.ofFloat(view,"rotation",0f,360f);
    }

    public void test1(){
        /***
         * 让旋转和淡入淡出动画同时进行，并把它们插入到了平移动画的后
         * */
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(alpha).after(moveIn).before(valueAnimator);
        animSet.setDuration(5000);
        animSet.start();
    }

    public void test2(){
        /***
         * Animator监听器
         * 添加动画状态
         * 可适用于ValuvAnimator , ObjectAnimator 和 AnimatorSet
         * */
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //onAnimationStart()方法会在动画开始的时候调用
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //onAnimationRepeat()方法会在动画重复执行的时候调用
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //onAnimationEnd()方法会在动画结束的时候调用
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //onAnimationCancel()方法会在动画被取消的时候调用
            }
        });
    }

    public void test3(){
        /**
         * 监听AnimatorListener 的adapter
         * 只想要监听动画结束这一个事件，
         * 那么每次都要将四个接口全部实现一遍就显得非常繁琐。
         * 为此Android提供了一个适配器类，叫作AnimatorListenerAdapter
         * */
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }



}
