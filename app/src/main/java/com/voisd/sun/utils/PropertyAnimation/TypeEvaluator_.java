package com.voisd.sun.utils.PropertyAnimation;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by jm on 2016/6/27.
 * 自定义object 属性动画
 * evaluate 参数说明
 * fraction 完成进度（0-1）
 * startValue 动画的初始值
 * endValue 动画的结束值
 */
public class TypeEvaluator_ implements TypeEvaluator<PointF> {
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        PointF point = new PointF(x, y);
        return point;
    }
}
