package com.voisd.sun.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.voisd.sun.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.Login;
import com.voisd.sun.been.Reg;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by voisd on 2016/10/8.
 * 联系方式：531972376@qq.com
 */
public class RegActivity extends BaseActivity implements ICommonViewUi {
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
    @InjectView(R.id.common_toolbar_line)
    TextView commonToolbarLine;
    @InjectView(R.id.reg_phone_et)
    EditText regPhoneEt;
    @InjectView(R.id.reg_huoqu_cd)
    CardView regHuoquCd;
    @InjectView(R.id.reg_ems_et)
    EditText regEmsEt;
    @InjectView(R.id.reg_password_et)
    EditText regPasswordEt;
    @InjectView(R.id.reg_suiji_img)
    ImageView regSuijiImg;
    @InjectView(R.id.reg_name_et)
    EditText regNameEt;
    @InjectView(R.id.reg_submit_cd)
    CardView regSubmitCd;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_reg;
    }

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    private  boolean isReg = false;

    WeakRefHandler weakRefHandler;

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("用户注册");
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        weakRefHandler = new WeakRefHandler(this);
        SMSSDK.initSDK(this, "17b125854afcc", "d609da9e9cbfb9107361cb3b4d558c5c");
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    @OnClick({R.id.reg_suiji_img,R.id.reg_huoqu_cd,R.id.reg_submit_cd})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.reg_suiji_img:
                toRequest(ApiContants.EventTags.RANDOMNAME);
                break;
            case R.id.reg_huoqu_cd:
                if(StringHelper.isEmpty(regPhoneEt)){
                    showToastLong("手机号不能为空");
                    break;
                }
                toRequest(ApiContants.EventTags.CHECKPHONE);
                break;
            case R.id.reg_submit_cd:
                if(StringHelper.isEmpty(regPhoneEt,regEmsEt,regPasswordEt,regNameEt)){
                    showToastLong("填写信息不能为空");
                    break;
                }
                SMSSDK.submitVerificationCode("86",regPhoneEt.getText().toString().trim(),
                        regEmsEt.getText().toString().trim());
                break;
        }
    }

    @Override
    public void toRequest(int eventTag) {
        RequestParams requestParams = new RequestParams();
        switch (eventTag){
            case ApiContants.EventTags.CHECKPHONE:
                requestParams.put("account", AES.getSingleton().encrypt(regPhoneEt.getText().toString()));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.CHECKPHONE, requestParams);
                break;
            case ApiContants.EventTags.RANDOMNAME:
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.RANDOMNAME, requestParams);
                break;
            case ApiContants.EventTags.USERREGISTER:
                requestParams.put("account", AES.getSingleton().encrypt(regPhoneEt.getText().toString()));
                requestParams.put("password", AES.getSingleton().encrypt(regPasswordEt.getText().toString()));
                requestParams.put("name", AES.getSingleton().encrypt(regNameEt.getText().toString()));
                iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.USERREGISTER, requestParams);
                break;
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        System.out.println(result);
        switch (eventTag){
            case ApiContants.EventTags.CHECKPHONE:
                SMSSDK.getVerificationCode("86", regPhoneEt.getText().toString().trim(), new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });

                break;
            case ApiContants.EventTags.RANDOMNAME:
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    regNameEt.setText(jsonObject.optString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiContants.EventTags.USERREGISTER:
                System.out.println("注册结果"+result);
                JsonHelper<Reg> jsonHelper = new JsonHelper<Reg>(Reg.class);
                Reg reg = jsonHelper.getData(result, "data");
                PreferenceUtils.setPrefString(this, Contants.Preference.Token,AES.getSingleton().decrypt(reg.getToken()));
                PreferenceUtils.setPrefString(this, Contants.Preference.Uid,reg.getUid());
                PreferenceUtils.setPrefString(this, Contants.Preference.loginMsg,result);
                PreferenceUtils.setPrefString(this, Contants.Preference.Name,reg.getName());
                PreferenceUtils.setPrefString(this, Contants.Preference.Avatar,reg.getAvatar());
                PreferenceUtils.setPrefString(this, Contants.Preference.UserName,reg.getAccount());
                PreferenceUtils.setPrefString(this, Contants.Preference.UserPassword,reg.getPassword());
                CommonUtils.postEventBus(EventBusTags.LOGIN_SUCCESS);
                finish();
                break;
        }
    }

    EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    System.out.println("提交验证码成功");
                    weakRefHandler.sendEmptyMessage(2);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    System.out.println("获取验证码成功");
                    isReg = true;
                    weakRefHandler.sendEmptyMessage(1);

                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){

                }
            }else{
                ((Throwable)data).printStackTrace();
                Message message = new Message();
                message.what = 3;
                message.obj = data;
                weakRefHandler.sendMessage(message);
            }
        }


    };


    public class WeakRefHandler extends Handler {
        WeakReference<Context> mWeakContext;

        public WeakRefHandler(Context context) {
            mWeakContext = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakContext == null)
                return;
            if ((mWeakContext.get() instanceof Activity) && ((Activity) mWeakContext.get()).isFinishing())
                return;
            switch (msg.what){
                case 1:
                    regSubmitCd.setCardBackgroundColor(getResources().getColor(R.color.theme_color));
                    break;
                case 2:
                    toRequest(ApiContants.EventTags.USERREGISTER);
                    break;
                case 3:
                    try {
                        Throwable throwable = (Throwable) msg.obj;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            showToastShort(des);
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                    }
                    break;
            }
            super.handleMessage(msg);
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
            showProgress("请求中...");
        }else{
            dimissProgress();
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();

    }
}
