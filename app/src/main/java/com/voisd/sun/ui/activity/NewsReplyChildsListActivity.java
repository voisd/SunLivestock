package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.CommentList;
import com.voisd.sun.been.Login;
import com.voisd.sun.been.PostResult;
import com.voisd.sun.been.SubCommentList;
import com.voisd.sun.common.Contants;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.listeners.LoadMoreClickListener;
import com.voisd.sun.loading.ILoadView;
import com.voisd.sun.loading.ILoadViewImpl;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.IRecyclerViewPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.presenter.impl.RecyclerViewPresenterImpl;
import com.voisd.sun.ui.adapter.NewsReplyChildsListAdapter;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.NetUtils;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.TimeUtils;
import com.voisd.sun.utils.http.HttpStatusUtil;
import com.voisd.sun.view.headerfooterrecyclerview.ExStaggeredGridLayoutManager;
import com.voisd.sun.view.headerfooterrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.voisd.sun.view.headerfooterrecyclerview.HeaderSpanSizeLookup;
import com.voisd.sun.view.headerfooterrecyclerview.OnRecyclerViewScrollListener;
import com.voisd.sun.view.headerfooterrecyclerview.RecyclerViewUtils;
import com.voisd.sun.view.iviews.ICommonViewUi;
import com.voisd.sun.view.iviews.IRecyclerViewUi;

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
public class NewsReplyChildsListActivity extends BaseActivity implements
        IRecyclerViewUi, IRecyclerViewItemListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreClickListener,
        ReplyPopupWindow.OnReplySubmitClickListener,ICommonViewUi {


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
    private List<SubCommentList> mResultList = new ArrayList<SubCommentList>();

    private List<SubCommentList> mResultListReport = new ArrayList<SubCommentList>();

    private IRecyclerViewPresenter iRecyclerViewPresenter = null;

    private ILoadView iLoadView = null;

    private View loadMoreView = null;

    private View topView = null;

    private TopHolder topHolder;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    String nid,parent_id,root_parent_id;
    CommentList commentList;
    String replyStr;
    int replyPosition = 0;

    ReplyPopupWindow replyPopupWindow;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_newsreplychildslist;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("评论详情");
        nid = getIntent().getExtras().getString("nid", "");
        parent_id = getIntent().getExtras().getString("parent_id", "");
        root_parent_id = getIntent().getExtras().getString("root_parent_id", "");
        commentList = (CommentList) getIntent().getExtras().getSerializable("commentList");

        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        iRecyclerViewPresenter = new RecyclerViewPresenterImpl(mContext, this);

        iLoadView = new ILoadViewImpl(mContext, this);

        loadMoreView = iLoadView.inflate();

        topView =  LayoutInflater.from(mContext).inflate(R.layout.adapter_newsreplychildslist_top, null);
        topHolder = new TopHolder(topView);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new MyScrollListener());

        replyPopupWindow = new ReplyPopupWindow(this,this);

        init();

        firstRefresh();
    }

    void init(){
        if(commentList!=null){
            RecyclerViewUtils.setHeaderView(recyclerView, topView);
            PictureUtil.load(mContext,topHolder.replyUserImg, commentList.getUser().getAvatar());
            topHolder.replyUsernameTv.setText(commentList.getUser().getName());
            topHolder.replyTimeTv.setText(TimeUtils.fromLongToDate(commentList.getReport_time()+"000"));
            topHolder.replyContentTv.setText(commentList.getContent());
            topHolder.replyReplyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginMsgHelper.isLogin(mContext)) {
                        CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
                        return;
                    }
                    replyPopupWindow.show(toolbarTopLayout,"回复 "+commentList.getUser().getName()+"...");
                    replyPosition = -1;
                }
            });
        }


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
        } else {
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
        if (!LoginMsgHelper.isLogin(mContext)) {
            CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
            return;
        }
       if(position==0){
           return;
       }
        SubCommentList subCommentList = mResultList.get(position-1);
        replyPopupWindow.show(toolbarTopLayout,"回复 "+subCommentList.getUser().getName()+"...");
        replyPosition = position-1;

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
        map.put("parent_id", AES.getSingleton().encrypt(parent_id));
        map.put("root_parent_id", AES.getSingleton().encrypt(root_parent_id));

        System.out.println("请求参数:page="+mCurrentPage+"size="+Contants.Request.PAGE_NUMBER+
                "parent_id="+parent_id+"root_parent_id="+root_parent_id);

        String s = AES.getSingleton().encrypt(mCurrentPage);

        iRecyclerViewPresenter.loadData(Contants.HttpStatus.refresh_data, mContext, ApiContants.Urls.SUBCOMMENTLIST_DO, map);
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
        map.put("parent_id", AES.getSingleton().encrypt(parent_id));
        map.put("root_parent_id", AES.getSingleton().encrypt(root_parent_id));
        iRecyclerViewPresenter.loadData(Contants.HttpStatus.loadmore_data, mContext, ApiContants.Urls.SUBCOMMENTLIST_DO, map);
    }

    @Override
    public void getRefreshData(int eventTag, String result) {
        List<SubCommentList> resultList = parseResult(result);
        mResultList.clear();
        mResultList.addAll(resultList);
        System.out.println(" ------------" + mResultList.size());
        refreshListView();

        //显示为空提示
        if (resultList.size() == 0) {
            showPageStatusView("列表为空");
        } else {
            hidePageStatusView();
        }
    }

    @Override
    public void getLoadMoreData(int eventTag, String result) {
        List<SubCommentList> resultList = parseResult(result);
        if (resultList.size() == 0) {
            iLoadView.showFinishView(loadMoreView);
        }
        mResultList.addAll(resultList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag==ApiContants.EventTags.REPORTCOMMENT_DO){
            JsonHelper<SubCommentList> jsonHelper = new JsonHelper<SubCommentList>(SubCommentList.class);
            SubCommentList subCommentList = jsonHelper.getData(result, "data");
            if(subCommentList!=null){
                mResultListReport.add(subCommentList);
                mResultList.add(subCommentList);
                adapter.notifyDataSetChanged();
                if (mResultList.size() >= Contants.Request.PAGE_NUMBER) {
                    RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
                }
                recyclerView.scrollToPosition(mResultList.size());
            }
            showToastLong(HttpStatusUtil.getStatusMsg(result));

        }
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    public List<SubCommentList> parseResult(String result) {
//        实际数据解析
        JsonHelper<SubCommentList> dataParser = new JsonHelper<SubCommentList>(SubCommentList.class);
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
        if (ApiContants.EventTags.REPORTCOMMENT_DO == eventTag) {
            if (!status) {
                dimissProgress();
            }
        }else {
            isRequesting = status;
            if (!status) {
                swipeRefreshLayout.setRefreshing(false);
            }
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


    public void toRequest(int eventTag) {
        switch (eventTag) {
            case ApiContants.EventTags.REPORTCOMMENT_DO:
                showProgress("评论...");
                RequestParams requestParams = new RequestParams();
                if(replyPosition == -1){
                    requestParams.put("parent_id", AES.getSingleton().encrypt(root_parent_id));
                }else{
                    requestParams.put("parent_id", AES.getSingleton().encrypt(mResultList.get(replyPosition).getComment_id()));
                }
                requestParams.put("root_parent_id", AES.getSingleton().encrypt(root_parent_id));
                requestParams.put("content", AES.getSingleton().encrypt(replyStr));
                requestParams.put("nid", AES.getSingleton().encrypt(nid));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.REPORTCOMMENT_DO, requestParams);

                break;
        }
    }


    public void refreshListView() {

        NewsReplyChildsListAdapter mIntermediary = new NewsReplyChildsListAdapter(mContext, mResultList, this);

        adapter = new HeaderAndFooterRecyclerViewAdapter(mIntermediary);

        recyclerView.setAdapter(adapter);

        staggeredGridLayoutManager = new ExStaggeredGridLayoutManager(Contants.Column.ONE, StaggeredGridLayoutManager.VERTICAL);

        staggeredGridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) recyclerView.getAdapter(), staggeredGridLayoutManager.getSpanCount()));

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        if (mResultList.size() >= Contants.Request.PAGE_NUMBER) {

            RecyclerViewUtils.setFooterView(recyclerView, loadMoreView);
        }
        if(commentList!=null) {
            RecyclerViewUtils.setHeaderView(recyclerView, topView);
        }

        if(commentList!=null){

            topHolder.replyReplyTv.performClick();
        }
    }


    @Override
    public void OnSubmit(String str) {
        if(!StringHelper.isEmpty(str)){
            replyStr = str;
            toRequest(ApiContants.EventTags.REPORTCOMMENT_DO);
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


    class TopHolder{
        @InjectView(R.id.reply_arrow)
        ImageView replyArrow;
        @InjectView(R.id.reply_user_img)
        ImageView replyUserImg;
        @InjectView(R.id.reply_username_tv)
        TextView replyUsernameTv;
        @InjectView(R.id.reply_content_tv)
        TextView replyContentTv;
        @InjectView(R.id.reply_time_tv)
        TextView replyTimeTv;
        @InjectView(R.id.reply_reply_tv)
        TextView replyReplyTv;
        @InjectView(R.id.reply_rel)
        RelativeLayout replyRel;

        TopHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
