package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.Login;
import com.voisd.sun.been.PassWord;
import com.voisd.sun.common.Contants;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.PreferenceUtils;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.TimeUtils;
import com.voisd.sun.view.iviews.ICommonViewUi;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/10/12.
 * 联系方式：531972376@qq.com
 */
public class PassWorkSubmitActivity extends BaseActivity implements ICommonViewUi {
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
    @InjectView(R.id.pass_et)
    MaterialEditText passEt;
    @InjectView(R.id.pass_submit_cd)
    CardView passSubmitCd;

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_password_submit;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("修改密码");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

    }

    @OnClick(R.id.pass_submit_cd)
    public void onclick(){
        if(StringHelper.isEmpty(passEt)){
            showToastShort("密码不能为空");
            return;
        }
        toRequest(ApiContants.EventTags.UPDATEPWD);
    }

    @Override
    public void toRequest(int eventTag) {
        if(ApiContants.EventTags.UPDATEPWD == eventTag){
            RequestParams requestParams = new RequestParams();
            requestParams.put("account", AES.getSingleton().encrypt(PreferenceUtils.getPrefString(mContext, Contants.Preference.UserName,"")));
            requestParams.put("newPwd", AES.getSingleton().encrypt(passEt.getText().toString().trim()));
            iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.UPDATEPWD_DO, requestParams);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(ApiContants.EventTags.UPDATEPWD == eventTag){
            JsonHelper<PassWord> jsonHelper = new JsonHelper<PassWord>(PassWord.class);
            PassWord passWord = jsonHelper.getData(result, "data");
            PreferenceUtils.setPrefString(this, Contants.Preference.Uid,passWord.getUid());
            PreferenceUtils.setPrefString(this, Contants.Preference.Name,passWord.getName());
            PreferenceUtils.setPrefString(this, Contants.Preference.Avatar,passWord.getAvatar());
            PreferenceUtils.setPrefString(this, Contants.Preference.UserPassword,passEt.getText().toString().trim());
            showToastShort("修改密码成功");
            finish();

        }
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        showToastShort(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        showToastShort(msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if(status){
            showProgress("提交...");
        }else{
            dimissProgress();
        }
    }
}
