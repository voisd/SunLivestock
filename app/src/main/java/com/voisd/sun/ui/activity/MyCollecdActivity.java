package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.News;
import com.voisd.sun.common.Contants;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.listeners.LoadMoreClickListener;
import com.voisd.sun.loading.ILoadView;
import com.voisd.sun.loading.ILoadViewImpl;
import com.voisd.sun.presenter.IRecyclerViewPresenter;
import com.voisd.sun.presenter.impl.RecyclerViewPresenterImpl;
import com.voisd.sun.ui.adapter.HomeNewInformationAdapter;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.NetUtils;
import com.voisd.sun.view.headerfooterrecyclerview.ExStaggeredGridLayoutManager;
import com.voisd.sun.view.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.voisd.sun.view.headerfooterrecyclerview.HeaderSpanSizeLookup;
import com.voisd.sun.view.headerfooterrecyclerview.OnRecyclerViewScrollListener;
import com.voisd.sun.view.headerfooterrecyclerview.RecyclerViewUtils;
import com.voisd.sun.view.iviews.IRecyclerViewUi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by voisd on 2016/9/30.
 * 联系方式：531972376@qq.com
 */
public class MyCollecdActivity extends BaseActivity implements IRecyclerViewUi, IRecyclerViewItemListener,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreClickListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private String mContent = "???";


    private HeaderAndFooterRecyclerViewAdapter adapter;
    private ExStaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<News> mResultList = new ArrayList<News>();

    private IRecyclerViewPresenter iRecyclerViewPresenter = null;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    private boolean isFirstLoad = true;


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home_newinformation;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("我的收藏");
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);

        iLoadView = new ILoadViewImpl(mContext, this);

        loadMoreView = iLoadView.inflate();

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new MyScrollListener());

        firstRefresh();
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("nid",mResultList.get(position).getNId());
        bundle.putSerializable("news",(Serializable) mResultList.get(position));
        CommonUtils.goActivity(this, NewsDetailActivity.class,bundle);
    }

    @Override
    public void toRefreshRequest() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        dimissRefreshRetry();

        mCurrentPage = 0;

        Map<String, String> map = new HashMap<String, String>();
        map.put("page", AES.getSingleton().encrypt(mCurrentPage));
        map.put("size", AES.getSingleton().encrypt(Contants.Request.PAGE_NUMBER));

        String s = AES.getSingleton().encrypt(mCurrentPage);

//        System.out.println("解密=========page:"+AES.getSingleton().decrypt(s));
        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, ApiContants.Urls.COLLECTIONLIST_DO, map);
    }

    @Override
    public void toLoadMoreRequest() {
        if (isRequesting)
            return;

        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            iLoadView.showErrorView(loadMoreView);
            return;
        }

        if (mResultList.size() < Contants.Request.PAGE_NUMBER) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "" + AES.getSingleton().encrypt(mCurrentPage));
        map.put("size", "" + AES.getSingleton().encrypt(Contants.Request.PAGE_NUMBER));
        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, ApiContants.Urls.COLLECTIONLIST_DO, map);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {

        List<News> resultList = parseResult(result);
        mResultList.clear();
        mResultList.addAll(resultList);
        System.out.println(" ------------"+mResultList.size());
        refreshListView();

        //显示为空提示
        if(resultList.size() == 0){
            showPageStatusView("列表为空");
        }else{
            hidePageStatusView();
        }
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<News> resultList = parseResult(result);
        if(resultList.size() == 0){
            iLoadView.showFinishView(loadMoreView);
        }
        mResultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<News> parseResult(String result) {
//        实际数据解析
        JsonHelper<News> dataParser = new JsonHelper<News>(News.class);
        return dataParser.getDatas(result, "data");

    }


    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        if (mCurrentPage > 0)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
        showToastShort(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        if (mCurrentPage > 0)
            mCurrentPage--;

        if (eventTag == Contants.HttpStatus.loadmore_data) {
            iLoadView.showErrorView(loadMoreView);
        }
        if (mCurrentPage == 0 && mResultList.size() == 0) {
            showRefreshRetry(Contants.NetStatus.NETWORK_MAYBE_DISABLE);
        } else {
            showToastShort(msg);
        }
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        isRequesting = status;
        if (!status) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void clickLoadMoreData() {
        toLoadMoreRequest();
    }

    @Override
    public void onRefresh() {
        toRefreshRequest();
    }


    private void firstRefresh() {
        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);
                        toRefreshRequest();
                    }
                }, Contants.Integers.PAGE_LAZY_LOAD_DELAY_TIME_MS);
            }
        }else{
            showRefreshRetry(Contants.NetStatus.NETWORK_MAYBE_DISABLE);
        }
    }

    public void refreshListView() {

        HomeNewInformationAdapter mIntermediary = new HomeNewInformationAdapter(mContext, mResultList, this);

        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);

        recyclerView.setAdapter(adapter);

        staggeredGridLayoutManager = new ExStaggeredGridLayoutManager(Contants.Column.ONE, StaggeredGridLayoutManager.VERTICAL);

        staggeredGridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter)recyclerView.getAdapter(), staggeredGridLayoutManager.getSpanCount()));

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        if (mResultList.size() >= Contants.Request.PAGE_NUMBER) {

            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
    }

    @Override
    public void clickRefreshRetryBtn() {

        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }

        super.clickRefreshRetryBtn();

        firstRefresh();
    }


    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            toLoadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

}
