package com.voisd.sun.presenter.impl;

import android.content.Context;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;
import com.voisd.sun.interactor.ICommonRequestInteractor;
import com.voisd.sun.interactor.impl.CommonRequestInteractorImpl;
import com.voisd.sun.listeners.IRequestListener;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.http.HttpStatusUtil;
import com.voisd.sun.view.iviews.ICommonViewUi;

import org.json.JSONObject;
import java.io.File;
import java.util.Map;

/**
 * Created by voisd on 16/5/23.
 */
public class CommonRequestPresenterImpl implements ICommonRequestPresenter, IRequestListener {

    public Context context;

    public ICommonViewUi iCommonViewUi;

    public ICommonRequestInteractor iCommonRequestInteractor;

    public CommonRequestPresenterImpl(Context context, ICommonViewUi iCommonViewUi) {

        this.context = context;
        this.iCommonViewUi = iCommonViewUi;
        iCommonRequestInteractor = new CommonRequestInteractorImpl(this);
    }

    @Override
    public void request(int eventTag, Context context, String url, RequestParams params) {

        iCommonRequestInteractor.request(eventTag, context, url, params);
    }

//    @Override
//    public void upload(int eventTag, Context context, String url, String[] fileKeys, File[] files, Map<String, String> params) {
//        iCommonRequestInteractor.upload(eventTag, context, url, fileKeys, files, params);
//    }


    @Override
    public void upload(int eventTag, Context context, String url,  RequestParams params) {
        iCommonRequestInteractor.upload(eventTag, context, url,params);
    }

    @Override
    public void upload(int eventTag, Context context, String url,  File  file) {
        iCommonRequestInteractor.upload(eventTag, context, url,file);
    }

    @Override
    public void onSuccess(int eventTag, String data) {
        if (HttpStatusUtil.getStatus(data)) {
            iCommonViewUi.getRequestData(eventTag, data);
        } else {

            if(HttpStatusUtil.isRelogin(data)){
                try {
                    JSONObject object=new JSONObject(data);
                    String msg=object.getString("message");
                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
                LoginMsgHelper.reLogin(context); // 重启到登录页面
            }

            iCommonViewUi.onRequestSuccessException(eventTag, HttpStatusUtil.getStatusMsg(data));
        }
    }

    @Override
    public void onError(int eventTag, String msg) {
//        msg = Contants.NetStatus.NETWORK_MAYBE_DISABLE;
        //系统异常
        iCommonViewUi.onRequestFailureException(eventTag, msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        iCommonViewUi.isRequesting(eventTag, status);
    }
}
