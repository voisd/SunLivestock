package com.voisd.sun.utils;

import android.content.Context;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.voisd.sun.been.LoginMsg;
import com.voisd.sun.common.Contants;
import com.voisd.sun.utils.http.OkHttpClientManager;
import com.voisd.sun.utils.http.OkHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Http请求
 * Created by voisd on 16/5/23.
 */
public class HttpHelper {

    private static volatile HttpHelper instance = null;

    private HttpHelper() {
    }

    public static HttpHelper getInstance() {
        if (null == instance) {
            synchronized (HttpHelper.class) {
                if (null == instance) {
                    instance = new HttpHelper();
                }
            }
        }
        return instance;
    }

    /**
     * post 请求
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     */
    public void post(Context context, String url, Map<String, String> params,
                     OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;

//        LoginMsg loginMsg = LoginMsgHelper.getResult(context);
//
//        if(loginMsg != null) {
//
//            params.put("token", loginMsg.getToken() + "");
//
//        }

        if(null!=PreferenceUtils.getPrefString(context, Contants.Preference.Uid,null)
                &&null!=PreferenceUtils.getPrefString(context, Contants.Preference.Token,null)){
            params.put("uid", AES.getSingleton().encrypt(PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")));
            String value =  url+"?uid="+PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")+
                    "&token="+PreferenceUtils.getPrefString(context, Contants.Preference.Token,"");
            params.put("sign", AES.getSingleton().encrypt(Md5.str2Md5(value)));
        }

        System.out.println(url);
        System.out.println(params.toString());

        OkHttpClientManager.postAsyn(url, params, responseHandler);

    }

    /**
     * 上传文件
     * @param context
     * @param url
     * @param fileKeys
     * @param files
     * @param params
     * @param responseHandler
     */
    public void upload(Context context, String url, String[] fileKeys, File[] files, Map<String, String> params,
                       OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;

        LoginMsg loginMsg = LoginMsgHelper.getResult(context);

        if(null!=PreferenceUtils.getPrefString(context, Contants.Preference.Uid,null)
                &&null!=PreferenceUtils.getPrefString(context, Contants.Preference.Token,null)){
            params.put("uid", AES.getSingleton().encrypt(PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")));
            String value =  url+"?uid="+PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")+
                    "&token="+PreferenceUtils.getPrefString(context, Contants.Preference.Token,"");
            params.put("sign", AES.getSingleton().encrypt(Md5.str2Md5(value)));
        }

        System.out.println(url);
        System.out.println(fileKeys.toString());
        System.out.println(files.toString());
        System.out.println(params.toString());

        OkHttpClientManager.getUploadDelegate().postAsyn(url, fileKeys, files, OkHttpClientManager.getInstance().map2Params(params), responseHandler);
    }



}
