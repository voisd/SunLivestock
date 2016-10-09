package com.voisd.sun.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by voisd on 2016/9/27.
 * 联系方式：531972376@qq.com
 */
public class TimeUtils {
    public static String getTimeMillis(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }

    public static String fromLongToDate(String str){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time=new Long(str);
        String d = format.format(time);
        return d;
    }
}
