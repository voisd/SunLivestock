package com.voisd.sun.interactor;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.Map;

/**
 * Created by voisd on 16/5/23.
 */
public interface ICommonRequestInteractor {

    void request(int eventTag, Context context, String url, RequestParams params);

//    void upload(int eventTag, Context context, String url, String[] fileKeys, File[] files, Map<String, String> params);

    void upload(int eventTag, Context context, String url,  RequestParams params);
    void upload(int eventTag, Context context, String url, File file);
}
