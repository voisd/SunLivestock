package com.voisd.sun.wxapi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;


public class AnimationUtil {

    public static void setTranslationY(final OnEndListener onEndListener, View view, float startY, float endY, int time){
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (onEndListener!=null){
                    onEndListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.setDuration(time);
        anim.start();
    }

    public interface OnEndListener {
        void onEnd();
    }

    public static void setAlpha(final OnEndListener onEndListener, View view, float startAlpha, float endAlpha, int time){
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        anim.setDuration(time);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (onEndListener != null) {
                    onEndListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();
    }

}
