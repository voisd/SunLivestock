package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voisd.sun.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.CommentList;
import com.voisd.sun.been.News;
import com.voisd.sun.been.PostResult;
import com.voisd.sun.common.Contants;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.listeners.LoadMoreClickListener;
import com.voisd.sun.loading.ILoadView;
import com.voisd.sun.loading.ILoadViewImpl;
import com.voisd.sun.presenter.IRecyclerViewPresenter;
import com.voisd.sun.presenter.impl.RecyclerViewPresenterImpl;
import com.voisd.sun.ui.adapter.HomeNewInformationAdapter;
import com.voisd.sun.ui.adapter.NewsReplyListAdapter;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by voisd on 2016/9/28.
 * 联系方式：531972376@qq.com
 */
public class NewsReplyListActivity extends BaseActivity implements
        IRecyclerViewUi, IRecyclerViewItemListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreClickListener {


    @InjectView(R.id.toolbar_top_layout)
    LinearLayout toolbarTopLayout;
    @InjectView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbarTitle;
    @InjectView(R.id.toolbar_back_btn)
    ImageButton toolbarBackBtn;
    @InjectView(R.id.common_toolbar_line)
    TextView commonToolbarLine;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.page_status_icon_iv)
    ImageView pageStatusIconIv;
    @InjectView(R.id.page_status_text_tv)
    TextView pageStatusTextTv;
    @InjectView(R.id.refresh_again_tv)
    TextView refreshAgainTv;
    @InjectView(R.id.refresh_again_btn)
    CardView refreshAgainBtn;

    private HeaderAndFooterRecyclerViewAdapter adapter;
    private ExStaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<CommentList> mResultList = new ArrayList<CommentList>();

    private IRecyclerViewPresenter iRecyclerViewPresenter = null;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    String nid;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_newsreplylist;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("评论列表");
        nid = getIntent().getExtras().getString("nid", "");

        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);

        iLoadView = new ILoadViewImpl(mContext, this);

        loadMoreView = iLoadView.inflate();

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new MyScrollListener());

        firstRefresh();
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


    public void onEvent(PostResult result) {

        if (result != null) {
            if (EventBusTags.LOGIN_SUCCESS.equals(result.getTag())) {

            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("nid",nid);
        bundle.putString("parent_id",mResultList.get(position).getParent_id());
        bundle.putString("root_parent_id",mResultList.get(position).getComment_id());
        bundle.putSerializable("commentList",(Serializable) mResultList.get(position));
        CommonUtils.goActivity(mContext,NewsReplyChildsListActivity.class,bundle,false);
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
        map.put("nid", AES.getSingleton().encrypt(nid));
        String s = AES.getSingleton().encrypt(mCurrentPage);

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, ApiContants.Urls.COMMENTLIST_DO, map);
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
        map.put("nid", AES.getSingleton().encrypt(nid));
        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, ApiContants.Urls.COMMENTLIST_DO, map);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        List<CommentList> resultList = parseResult(result);
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
        List<CommentList> resultList = parseResult(result);
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
    public List<CommentList> parseResult(String result) {
//        实际数据解析
        JsonHelper<CommentList> dataParser = new JsonHelper<CommentList>(CommentList.class);
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

    public void refreshListView() {

        NewsReplyListAdapter mIntermediary = new NewsReplyListAdapter(mContext, mResultList, this);

        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);

        recyclerView.setAdapter(adapter);

        staggeredGridLayoutManager = new ExStaggeredGridLayoutManager(Contants.Column.ONE, StaggeredGridLayoutManager.VERTICAL);

        staggeredGridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter)recyclerView.getAdapter(), staggeredGridLayoutManager.getSpanCount()));

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        if (mResultList.size() >= Contants.Request.PAGE_NUMBER) {

            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
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
