package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.voisd.sun.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.NewsDetail;
import com.voisd.sun.been.UpdateUserInfo;
import com.voisd.sun.common.Contants;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.PreferenceUtils;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.http.HttpStatusUtil;
import com.voisd.sun.view.iviews.ICommonViewUi;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/30.
 * 联系方式：531972376@qq.com
 */
public class MyInfoActivity extends BaseActivity implements ICommonViewUi {
    @InjectView(R.id.toolbar_top_layout)
    LinearLayout toolbarTopLayout;
    @InjectView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbarTitle;
    @InjectView(R.id.toolbar_right_title)
    TextView toolbarRightTitle;
    @InjectView(R.id.toolbar_back_btn)
    ImageButton toolbarBackBtn;
    @InjectView(R.id.common_toolbar_line)
    TextView commonToolbarLine;
    @InjectView(R.id.user_name_et)
    MaterialEditText userNameEt;
    @InjectView(R.id.user_phone_et)
    MaterialEditText userPhoneEt;

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("个人资料");
        toolbarTitle.setText("修改资料");
        toolbarRightTitle.setText("保存");
        userNameEt.setText(PreferenceUtils.getPrefString(mContext, Contants.Preference.Name,""));
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        userNameEt.setSelection(userNameEt.getText().toString().length());
    }

    @OnClick(R.id.toolbar_right_title)
    public void onClickRight(){
        if(StringHelper.isEmpty(userNameEt)){
            showToastShort("请填写用户名");
            return;
        }
        toRequest(ApiContants.EventTags.USERUPDATE_DO);
    }

    @Override
    public void toRequest(int eventTag) {
        if(eventTag==ApiContants.EventTags.USERUPDATE_DO){
            RequestParams requestParams = new RequestParams();
            requestParams.put("name", AES.getSingleton().encrypt(userNameEt.getText().toString().trim()));
            iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.USERUPDATE_DO ,requestParams);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
//        showToastLong(HttpStatusUtil.getStatusDate(result));
        JsonHelper<UpdateUserInfo> jsonHelper = new JsonHelper<UpdateUserInfo>(UpdateUserInfo.class);
        UpdateUserInfo updateUserInfo = jsonHelper.getData(result, "data");
        showToastLong("修改成功");

        PreferenceUtils.setPrefString(this, Contants.Preference.Name,updateUserInfo.getName());
        CommonUtils.postEventBus(EventBusTags.LOGIN_UPDATE);
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
        if(status){
            showProgress("更新...");
        }else{
            dimissProgress();
        }
    }
}
