package com.voisd.sun.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.bk886.njxzs.R;

import java.io.File;

/**
 * description：图片加载工具类
 */
public class PictureUtil {

    public static void load(Context context, ImageView imageView, String url){

        if(!CommonUtils.isEmpty(url)) {

            if(url.startsWith("http://")) {

                Picasso.with(context).load(url).resize(200, 200).placeholder(R.drawable.ic_user)
                        .centerCrop().into(imageView);
            }else{

                Picasso.with(context).load(new File(url)).resize(400, 400).placeholder(R.drawable.ic_user)
                        .centerCrop().into(imageView);
            }
        }else{
            imageView.setImageResource(R.drawable.ic_user);
        }

    }

    public static void load(Context context, ImageView imageView, String url,int w,int h){

        if(!CommonUtils.isEmpty(url)) {

            if(url.startsWith("http://")) {

                Picasso.with(context).load(url).placeholder(R.drawable.loding)
                        .into(imageView);
            }else{
                Picasso.with(context).load(new File(url)).placeholder(R.drawable.loding)
                        .into(imageView);
            }
        }else{
            imageView.setImageResource(R.drawable.loding);
        }

    }
}
