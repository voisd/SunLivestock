package com.voisd.sun.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.News;
import com.voisd.sun.been.NewsDetail;
import com.voisd.sun.been.PostResult;
import com.voisd.sun.common.Contants;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.adapter.NewsReplyListAdapter;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.NetUtils;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.WebViewUtils;
import com.voisd.sun.utils.http.HttpStatusUtil;
import com.voisd.sun.view.MyLinearLayout;
import com.voisd.sun.view.MyWebView;
import com.voisd.sun.view.iviews.ICommonViewUi;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/23.
 */
public class NewsDetailActivity extends BaseActivity implements ICommonViewUi, SwipeRefreshLayout.OnRefreshListener, ReplyPopupWindow.OnReplySubmitClickListener {


    ReplyPopupWindow replyPopupWindow;
    @InjectView(R.id.toolbar_top_layout)
    LinearLayout toolbarTopLayout;
    @InjectView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbarTitle;
    @InjectView(R.id.toolbar_back_btn)
    ImageButton toolbarBackBtn;
    @InjectView(R.id.toolbar_right_title)
    TextView toolbarRightTitle;
    @InjectView(R.id.toolbar_right_btn)
    ImageButton toolbarRightBtn;
    @InjectView(R.id.common_toolbar_line)
    TextView commonToolbarLine;
    @InjectView(R.id.news_share_img)
    ImageView newsShareImg;
    @InjectView(R.id.news_collect_img)
    ImageView newsCollectImg;
    @InjectView(R.id.news_comments_tv)
    TextView newsCommentsTv;
    @InjectView(R.id.news_comments_img)
    RelativeLayout newsCommentsImg;
    @InjectView(R.id.news_reply_cardView)
    CardView newsReplyCardView;
    @InjectView(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @InjectView(R.id.webView)
    MyWebView webView;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.mLinearLayout)
    MyLinearLayout mLinearLayout;

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    private String nid;

    private News news;

    private String replyStr;

    private NewsDetail newsDetail;

    private boolean LoadOver = false;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("咨询详情");
        nid = getIntent().getExtras().getString("nid", "");
        news = (News) getIntent().getExtras().getSerializable("news");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        swipeRefreshLayout.setOnRefreshListener(this);
        replyPopupWindow = new ReplyPopupWindow(this, this);

        init();
    }

    public void onEvent(PostResult result) {

        if (result != null) {
            if (EventBusTags.LOGIN_SUCCESS.equals(result.getTag())) {
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                toRequest(ApiContants.EventTags.NEWSDETAIL_DO);
                            }
                        }, Contants.Integers.PAGE_LAZY_LOAD_DELAY_TIME_MS);
                    }
                }
            }
        }
    }

    @Override
    public void toRequest(int eventTag) {
        RequestParams requestParams = new RequestParams();
        switch (eventTag) {
            case ApiContants.EventTags.NEWSDETAIL_DO:
                requestParams.put("nid", AES.getSingleton().encrypt(nid));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.NEWSDETAIL_DO, requestParams);
                break;
            case ApiContants.EventTags.REPORTCOMMENT_DO:
                showProgress("评论...");
                requestParams.put("content", AES.getSingleton().encrypt(replyStr));
                requestParams.put("parent_id", AES.getSingleton().encrypt("0"));
                requestParams.put("nid", AES.getSingleton().encrypt(nid));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.REPORTCOMMENT_DO, requestParams);
                break;
            case ApiContants.EventTags.COLLECTIONADD_DO:
                showProgress("收藏...");
                requestParams.put("nid", AES.getSingleton().encrypt(nid));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.COLLECTIONADD_DO, requestParams);
                break;
            case ApiContants.EventTags.COLLECTIONDEL_DO:
                showProgress("处理...");
                requestParams.put("nid", AES.getSingleton().encrypt(nid));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.COLLECTIONDEL_DO, requestParams);
                break;
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if (ApiContants.EventTags.NEWSDETAIL_DO == eventTag) {
            JsonHelper<NewsDetail> jsonHelper = new JsonHelper<NewsDetail>(NewsDetail.class);
            newsDetail = jsonHelper.getData(result, "data");
            initNewsDetail(newsDetail);
        } else if (ApiContants.EventTags.REPORTCOMMENT_DO == eventTag) {
            showToastLong(HttpStatusUtil.getStatusMsg(result));
            Bundle bundle = new Bundle();
            bundle.putString("nid", nid);
            CommonUtils.goActivity(mContext, NewsReplyListActivity.class, bundle);
        } else if (ApiContants.EventTags.COLLECTIONADD_DO == eventTag) {
            showToastLong(HttpStatusUtil.getStatusDate(result));
            newsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
            newsDetail.setCollection(true);
        } else if (ApiContants.EventTags.COLLECTIONDEL_DO == eventTag) {
//            showToastLong(HttpStatusUtil.getStatusDate(result));
            newsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            newsDetail.setCollection(false);
        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        showToastLong(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        showToastLong(msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        switch (eventTag) {
            case ApiContants.EventTags.NEWSDETAIL_DO:
                LoadOver = !status;
                if (!status) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                }
                break;
            case ApiContants.EventTags.REPORTCOMMENT_DO:
            case ApiContants.EventTags.COLLECTIONADD_DO:
            case ApiContants.EventTags.COLLECTIONDEL_DO:
                if (!status) {
                    dimissProgress();
                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        toRequest(ApiContants.EventTags.NEWSDETAIL_DO);
    }

    private void initNewsDetail(NewsDetail newsDetail) {
        if (null == newsDetail) {
            return;
        }
        String num = StringHelper.isEmpty(newsDetail.getCommentCount()) ? "" :
                Integer.parseInt(newsDetail.getCommentCount()) < 100 ? newsDetail.getCommentCount() : "99+";
        newsCommentsTv.setText(num);
        if (StringHelper.isEmpty(num)) {
            newsCommentsTv.setVisibility(View.GONE);
        } else {
            newsCommentsTv.setVisibility(View.VISIBLE);
        }
        if (newsDetail.isCollection()) {
            newsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
        } else {
            newsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
        }

        webView.loadUrl("javascript:fillContent('" + newsDetail.getContent() + "')");
        webView.loadUrl("javascript:fillLink('" + newsDetail.getUrl() + "')");
        webView.loadUrl("javascript:fillTime('" + newsDetail.getTime() + "')");
        webView.loadUrl("javascript:fillTitle('" + newsDetail.getTitle() + "')");
    }

    @OnClick({R.id.news_reply_cardView, R.id.news_collect_img, R.id.news_share_img})
    public void onClickNeedLogin(View view) {
        if(!LoadOver){
            return;
        }
        if (!LoginMsgHelper.isLogin(this)) {
            CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
            return;
        }
        switch (view.getId()) {
            case R.id.news_reply_cardView:
                replyPopupWindow.show(mLinearLayout);
                break;
            case R.id.news_collect_img:
                if (!newsDetail.isCollection())
                    toRequest(ApiContants.EventTags.COLLECTIONADD_DO);
                else
                    toRequest(ApiContants.EventTags.COLLECTIONDEL_DO);
                break;
            case R.id.news_share_img:
                initShared();
                break;
        }
    }

    @OnClick({R.id.news_comments_img})
    public void onClickNoNeedLogin(View view) {
        if(!LoadOver){
            return;
        }
        switch (view.getId()) {
            case R.id.news_comments_img:
                Bundle bundle = new Bundle();
                bundle.putString("nid", nid);
                CommonUtils.goActivity(mContext, NewsReplyListActivity.class, bundle);
                break;
        }
    }

    void init() {
//        WebViewUtils.setWebViewSetting(newsWebview, this);
        WebViewUtils.setWebViewSetting(webView, this);

        webView.loadUrl("file:///android_asset/NewsDetailTemplate.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                toRequest(ApiContants.EventTags.NEWSDETAIL_DO);
                            }
                        }, Contants.Integers.PAGE_LAZY_LOAD_DELAY_TIME_MS);
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }
        });


    }

    @Override
    public void OnSubmit(String str) {
        if (!StringHelper.isEmpty(str)) {
            replyStr = str;
            toRequest(ApiContants.EventTags.REPORTCOMMENT_DO);
        }
    }

    public void initShared() {
        UMImage image;
        if (StringHelper.isEmpty(news.getImgUrl())) {
            image = new UMImage(mContext, R.drawable.ic_launcher);
        } else {
            image = new UMImage(mContext, news.getImgUrl());//网络图片
        }

        new ShareAction(this).withText(news.getBrief())
//                 .withMedia(image)
                .withTitle(news.getTitle())
                .withTargetUrl("http://www.bk886.com")
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();

    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            showToastShort("授权成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            showToastShort("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            showToastShort("授权取消");
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}


