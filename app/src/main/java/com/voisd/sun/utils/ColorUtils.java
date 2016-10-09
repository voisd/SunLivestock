package com.voisd.sun.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

/**
 * Created by voisd on 2016/9/27.
 * 联系方式：531972376@qq.com
 */
public class ColorUtils {

    public static void tintImg(Context context,int drawableId,int colorId,ImageView imageView){
        Drawable icon = context.getResources().getDrawable(drawableId);
        Drawable tintIcon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(tintIcon, context.getResources().getColorStateList(colorId));
        imageView.setImageDrawable(tintIcon);
    }
}
