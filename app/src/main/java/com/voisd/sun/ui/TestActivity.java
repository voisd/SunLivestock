package com.voisd.sun.ui;

import android.view.View;


import com.loopj.android.http.RequestParams;
import com.voisd.sun.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.loading.ILoadView;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.view.iviews.ICommonViewUi;

/**
 * Created by voisd on 2016/6/18.
 */
public class TestActivity extends BaseActivity implements ICommonViewUi {

    private ICommonRequestPresenter iCommonRequestPresenter = null;


    private ILoadView iLoadView = null;

    private View loadMoreView = null;


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.test;
    }

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext,this);
        toRequest(ApiContants.EventTags.TEST_ACCOUNT);
    }

    @Override
    public void toRequest(int eventTag) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("","");
        iCommonRequestPresenter.request(ApiContants.EventTags.TEST_ACCOUNT,mContext,ApiContants.Urls.TEST_ACCOUNT,requestParams);
    }

    @Override
    public void getRequestData(int eventTag, String result) {

    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {

    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {

    }

    @Override
    public void isRequesting(int eventTag, boolean status) {

    }


}
