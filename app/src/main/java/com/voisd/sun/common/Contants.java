package com.voisd.sun.common;

import android.os.Environment;

import com.bk886.njxzs.R;

/**
 * 常量
 * Created by voisd on 15/12/7.
 */
public class Contants {

    public static String APK_STORE_FOLDER = Environment.getExternalStorageDirectory() + "/baibu_apk/";

    public static final class Request{
        public static final int PAGE_NUMBER = 20;
    }

    public static final class Integers {
        public static final int PAGE_LAZY_LOAD_DELAY_TIME_MS = 200;
    }

    public static final class LoadView {

        public final static String LOADING = "正在加载中...";//正在加载中

        public final static String CLICKLOAD = "点击加载数据";//点击加载
    }

    public static final class NetStatus {

        public final static String NETDISABLE = "网络不给力";
        public final static String NETDISABLEORNETWORKDISABLE = "网络不给力或者服务器异常";

        public final static String LOGIN_MSG_EMPTY = "登录信息为空,请重新登录";

        public final static String NETWORK_MAYBE_DISABLE = "暂时木有网络,刷新看看?";
    }


    public static final class Refresh{

        public final static int[] refreshColorScheme = {R.color.theme_color, R.color.theme_color, R.color.theme_color};
    }

    public static final class Column {

        public final static int ONE = 1;//1列

        public final static int TWO = 2;//2列

        public final static int THREE = 3;//3列

        public final static int FOUR = 4;//4列
    }

    /**
     * 请求状态
     */
    public static final class HttpStatus {

        /**
         * 刷新请求状态
         */
        public static final int refresh_data = 1;

        /**
         * 加载更多请求状态
         */
        public static final int loadmore_data = 2;
    }


    /**
     * Preference 相关key
     */
    public static final class Preference {

        public final static String loginMsg = "login_message";//登录信息

        public final static String UserName = "Username"; //账号
        public final static String UserPassword = "UserPassword"; //密码

        public final static String Uid = "uid"; //个人ID
        public final static String Name = "name"; //个人名字
        public final static String Token = "token"; //个人token
        public final static String Avatar = "avatar"; //个人头像

    }

    public static final class SMSS{

        //正式版本
        public final static String APPKEY = "17d5f9e8c238e";
        public final static String APPSECRET = "56d4f67f19826f84b05ab619029e283e";
    }


}
