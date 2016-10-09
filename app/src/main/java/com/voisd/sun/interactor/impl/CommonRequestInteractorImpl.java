package com.voisd.sun.interactor.impl;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.Request;
import com.voisd.sun.common.Contants;
import com.voisd.sun.interactor.ICommonRequestInteractor;
import com.voisd.sun.listeners.IRequestListener;
import com.voisd.sun.utils.HttpHelper;
import com.voisd.sun.utils.asyn.AsyncHttpHelper;
import com.voisd.sun.utils.http.HttpImageUtil;
import com.voisd.sun.utils.http.OkHttpResponseHandler;

import java.io.File;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by liucanwen on 16/5/23.
 */
public class CommonRequestInteractorImpl implements ICommonRequestInteractor {

    public IRequestListener iRequestListener;

    public CommonRequestInteractorImpl(IRequestListener iRequestListener) {
        this.iRequestListener = iRequestListener;
    }

    @Override
    public void request(final int eventTag, final Context context, final String url, RequestParams params) {

        AsyncHttpHelper.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);

//                if(headers != null && headers.length > 0){
//                    int length = headers[0].getElements().length;
//                    System.out.println("headers-length>" + length);
//                    String content = headers[0].getElements()[0].toString();
//                    System.out.println("headers-content>" + content);
//                }

                System.out.println("《" + url + "》《" + result + "》");

                if(!isActivityEnd(context)) {
                    iRequestListener.onSuccess(eventTag, result);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                if(!isActivityEnd(context)) {
                    iRequestListener.onError(eventTag, Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if(!isActivityEnd(context)) {
                    iRequestListener.isRequesting(eventTag, true);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(!isActivityEnd(context)) {
                    iRequestListener.isRequesting(eventTag, false);
                }
            }
        });
    }

    @Override
    public void upload(final int eventTag, final Context context, final String url, RequestParams params) {

//        AsyncHttpHelper.post(context, url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//                String result = new String(responseBody);
//
//                System.out.println("《" + url + "》《" + result + "》");
//
//                if(!isActivityEnd(context)) {
//                    iRequestListener.onSuccess(eventTag, result);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                if(!isActivityEnd(context)) {
//                    iRequestListener.onError(eventTag, Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//                }
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                if(!isActivityEnd(context)) {
//                    iRequestListener.isRequesting(eventTag, true);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                if(!isActivityEnd(context)) {
//                    iRequestListener.isRequesting(eventTag, false);
//                }
//            }
//        });
    }

    @Override
    public void upload(final int eventTag, final Context context, final String url, final File file) {
        new PostAsyncTask(eventTag,context,url,file).execute();
    }

    class PostAsyncTask extends AsyncTask<Integer, Integer, String[]> {
        int eventTag;
        Context context;
        String url;
        File file;
        public PostAsyncTask(final int eventTag, final Context context, final String url, final File file){
            this.eventTag = eventTag;
            this.context = context;
            this.url = url;
            this.file = file;
        }

        @Override
        protected String[] doInBackground(Integer... params) {

            String[] result  = HttpImageUtil.uploadFile(url,file,context);
            return result;

        }

        @Override
        protected void onPostExecute(String[] result) {
            if(!isActivityEnd(context)) {
                iRequestListener.onSuccess(eventTag, result[1]);
            }
            iRequestListener.isRequesting(eventTag, false);
        }

        @Override
        protected void onPreExecute() {
            if(!isActivityEnd(context)) {
                iRequestListener.isRequesting(eventTag, true);
            }
        }
    }


    //
//    @Override
//    public void upload(final int eventTag, final Context context, String url, String[] fileKeys, File[] files, Map<String, String> params) {
//
//        HttpHelper.getInstance().upload(context, url, fileKeys, files, params, new OkHttpResponseHandler<String>(context) {
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                if(!isActivityEnd(context)) {
//                    iRequestListener.isRequesting(eventTag, true);
//                }
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                if(!isActivityEnd(context)) {
//                    iRequestListener.isRequesting(eventTag, false);
//                }
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                System.out.println(json);
//
//                if(!isActivityEnd(context)) {
//                    iRequestListener.onSuccess(eventTag, json);
//                }
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//
//                if(!isActivityEnd(context)) {
//                    iRequestListener.onError(eventTag, Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//                }
//            }
//        });
//    }

    /**
     * 根据context判断activity是否已经结束
     * @param context
     * @return
     */
    public boolean isActivityEnd(final Context context){

        if(context != null){

            try {
                Activity activity = (Activity)context;

                if(activity == null || activity.isFinishing()){

                    System.out.println("context为null了");

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
