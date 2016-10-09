package com.voisd.sun.presenter.impl;

import android.content.Context;

import com.voisd.sun.common.Contants;
import com.voisd.sun.interactor.IRecyclerViewInteractor;
import com.voisd.sun.interactor.impl.RecyclerViewInteractorImpl;
import com.voisd.sun.listeners.IRequestListener;
import com.voisd.sun.presenter.IRecyclerViewPresenter;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.http.HttpStatusUtil;
import com.voisd.sun.view.iviews.IRecyclerViewUi;

import java.util.Map;

/**
 * Created by voisd on 16/5/23.
 */
public class RecyclerViewPresenterImpl implements IRecyclerViewPresenter, IRequestListener {

    public Context context;
    public IRecyclerViewUi iRecyclerViewUi;
    public IRecyclerViewInteractor iRecyclerViewInteractor;

    public RecyclerViewPresenterImpl(Context context, IRecyclerViewUi iRecyclerViewUi){

        this.context = context;
        this.iRecyclerViewUi = iRecyclerViewUi;
        this.iRecyclerViewInteractor = new RecyclerViewInteractorImpl(this);
    }

    @Override
    public void loadData(int eventTag, Context context, String url, Map<String, String> map) {
        iRecyclerViewInteractor.loadData(eventTag, context, url, map);
    }

    @Override
    public void onSuccess(int eventTag, String data) {

        if(HttpStatusUtil.getStatus(data)){
            if (eventTag == Contants.HttpStatus.refresh_data) {
                iRecyclerViewUi.getRefreshData(eventTag, data);

            } else if (eventTag == Contants.HttpStatus.loadmore_data) {
                iRecyclerViewUi.getLoadMoreData(eventTag, data);
            }
        }else{
            if(HttpStatusUtil.isRelogin(data)){
                LoginMsgHelper.reLogin(context);
            }
            iRecyclerViewUi.onRequestSuccessException(eventTag, HttpStatusUtil.getStatusMsg(data));
        }
    }

    @Override
    public void onError(int eventTag, String msg) {

        msg = Contants.NetStatus.NETWORK_MAYBE_DISABLE;
        iRecyclerViewUi.onRequestFailureException(eventTag, msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        iRecyclerViewUi.isRequesting(eventTag, status);

    }
}

