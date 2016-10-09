package com.voisd.sun.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.voisd.sun.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.common.Contants;
import com.voisd.sun.listeners.LoadMoreClickListener;
import com.voisd.sun.loading.ILoadView;
import com.voisd.sun.loading.ILoadViewImpl;
import com.voisd.sun.presenter.IRecyclerViewPresenter;
import com.voisd.sun.presenter.impl.RecyclerViewPresenterImpl;
import com.voisd.sun.ui.adapter.TestAdapter;
import com.voisd.sun.view.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.view.headerfooterrecyclerview.RecyclerViewUtils;
import com.voisd.sun.view.iviews.IRecyclerViewUi;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by voisd on 2016/6/18.
 */
public class Test2Activity extends BaseActivity implements IRecyclerViewUi,
        LoadMoreClickListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    private IRecyclerViewPresenter iRecyclerViewPresenter = null;

    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter;

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
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);
        iLoadView = new ILoadViewImpl(mContext,this);
        loadMoreView = iLoadView.inflate();
        TestAdapter testAdapter = new TestAdapter();
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(testAdapter);
        RecyclerViewUtils.setFooterView(recyclerView,loadMoreView);
    }


    @Override
    public void toRefreshRequest() {
        Map<String, String> map = new HashMap<String, String>();
        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, ApiContants.Urls.TEST_ACCOUNT, map);
    }

    @Override
    public void toLoadMoreRequest() {

    }

    @Override
    public void getRefreshData(int eventTag, String result) {

    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {

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

    @Override
    public void clickLoadMoreData() {

    }
}
