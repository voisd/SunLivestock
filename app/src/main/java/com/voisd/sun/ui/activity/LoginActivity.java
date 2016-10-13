package com.voisd.sun.ui.activity;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.Login;
import com.voisd.sun.been.NewsDetail;
import com.voisd.sun.been.PostResult;
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
import com.voisd.sun.utils.TimeUtils;
import com.voisd.sun.view.iviews.ICommonViewUi;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by voisd on 2016/9/27.
 * 联系方式：531972376@qq.com
 */
public class LoginActivity extends BaseActivity implements ICommonViewUi {
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
    @InjectView(R.id.login_name_et)
    MaterialEditText loginNameEt;
    @InjectView(R.id.login_password_et)
    MaterialEditText loginPasswordEt;
    @InjectView(R.id.login_submit_cardview)
    CardView loginSubmitCardview;
    @InjectView(R.id.login_reg_tv)
    TextView loginRegTv;


    private ICommonRequestPresenter iCommonRequestPresenter = null;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    public void onEvent(PostResult result) {
        if (result != null) {
            if (EventBusTags.LOGIN_SUCCESS.equals(result.getTag())) {
                finish();
            }
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("用户登录");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        loginNameEt.setText(PreferenceUtils.getPrefString(this, Contants.Preference.UserName,""));
        loginPasswordEt.setText(PreferenceUtils.getPrefString(this, Contants.Preference.UserPassword,""));
        loginNameEt.setSelection(loginNameEt.getText().toString().length());
    }

    @Override
    public void toRequest(int eventTag) {
        switch (eventTag){
            case ApiContants.EventTags.USERLOGIN_DO:

                RequestParams requestParams = new RequestParams();
                requestParams.put("account", AES.getSingleton().encrypt(loginNameEt.getText().toString()));
                String auth = "password="+loginPasswordEt.getText().toString() +"&&timestamp="+ TimeUtils.getTimeMillis();
                requestParams.put("auth", AES.getSingleton().encrypt(auth));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.USERLOGIN_DO, requestParams);
                break;
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(eventTag==ApiContants.EventTags.USERLOGIN_DO){
            JsonHelper<Login> jsonHelper = new JsonHelper<Login>(Login.class);
            Login login = jsonHelper.getData(result, "data");
            PreferenceUtils.setPrefString(this, Contants.Preference.Token,AES.getSingleton().decrypt(login.getToken()));
            PreferenceUtils.setPrefString(this, Contants.Preference.Uid,login.getUid());
            PreferenceUtils.setPrefString(this, Contants.Preference.loginMsg,result);
            PreferenceUtils.setPrefString(this, Contants.Preference.Name,login.getName());
            PreferenceUtils.setPrefString(this, Contants.Preference.Avatar,login.getAvatar());
            PreferenceUtils.setPrefString(this, Contants.Preference.UserName,loginNameEt.getText().toString().trim());
            PreferenceUtils.setPrefString(this, Contants.Preference.UserPassword,loginPasswordEt.getText().toString().trim());
            CommonUtils.postEventBus(EventBusTags.LOGIN_SUCCESS);
            finish();
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
        if(status){
            showProgress("登录ing...");
        }else{
            dimissProgress();
        }
    }

    @OnClick({R.id.login_submit_cardview,R.id.login_reg_tv})
    public void onClickLogin(View v){
        switch (v.getId()){
            case R.id.login_submit_cardview:
                if(StringHelper.isEmpty(loginNameEt,loginPasswordEt)){
                    showToastLong("账号或密码不能为空");
                    break;
                }
                toRequest(ApiContants.EventTags.USERLOGIN_DO);
                break;
            case R.id.login_reg_tv:
                CommonUtils.goActivity(mContext,RegActivity.class,null);
                break;
        }
    }
}
