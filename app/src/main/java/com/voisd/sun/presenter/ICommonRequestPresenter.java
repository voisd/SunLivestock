package com.voisd.sun.presenter;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.Map;

/**
 * Created by voisd on 16/5/23.
 * 做页面请求控制
 *
 */
public interface ICommonRequestPresenter {

    void request(int eventTag, Context context, String url, RequestParams params);

//    void upload(int eventTag, Context context, String url, String[] fileKeys, File[] files, Map<String, String> params);

    void upload(int eventTag, Context context, String url,  RequestParams params);

    void upload(int eventTag, Context context, String url,  File file);
}
