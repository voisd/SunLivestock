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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.common.Contants;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.PreferenceUtils;
import com.voisd.sun.utils.StringHelper;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by voisd on 2016/10/12.
 * 联系方式：531972376@qq.com
 */
public class PassWorkActivity extends BaseActivity {
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
    @InjectView(R.id.pass_phone)
    TextView passPhone;
    @InjectView(R.id.pass_huoqu_cd)
    CardView passHuoquCd;
    @InjectView(R.id.pass_ems_et)
    EditText passEmsEt;
    @InjectView(R.id.pass_next_cd)
    CardView passNextCd;

    PassHandler passHandler;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_password;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbarTitle("修改密码");
        SMSSDK.initSDK(this, Contants.SMSS.APPKEY,  Contants.SMSS.APPSECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
        passHandler = new PassHandler(this);
        passPhone.setText(PreferenceUtils.getPrefString(mContext, Contants.Preference.UserName,""));
    }

    @OnClick({R.id.pass_huoqu_cd,R.id.pass_next_cd})
    public void onclcik(View view){
        switch (view.getId()){
            case R.id.pass_huoqu_cd:

                SMSSDK.getVerificationCode("86", passPhone.getText().toString().trim(), new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });
                break;
            case R.id.pass_next_cd:
//                CommonUtils.goActivity(mContext,PassWorkSubmitActivity.class,null,true);
                if(StringHelper.isEmpty(passEmsEt)){
                    showToastLong("填写信息不能为空");
                    break;
                }
                SMSSDK.submitVerificationCode("86",passPhone.getText().toString().trim(),
                        passEmsEt.getText().toString().trim());
                showProgress("校验中...");
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
                    passHandler.sendEmptyMessage(2);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    System.out.println("获取验证码成功");
                    passHandler.sendEmptyMessage(1);

                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){

                }
            }else{
                ((Throwable)data).printStackTrace();
                Message message = new Message();
                message.what = 3;
                message.obj = data;
                passHandler.sendMessage(message);
            }
        }
    };


    public class PassHandler extends Handler {
        WeakReference<Context> mWeakContext;

        public PassHandler(Context context) {
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
                    showToastShort("获取验证码成功");
                    passNextCd.setCardBackgroundColor(getResources().getColor(R.color.theme_color));
                    break;
                case 2:
                    dimissProgress();
                    CommonUtils.goActivity(mContext,PassWorkSubmitActivity.class,null,true);
                    break;
                case 3:
                    dimissProgress();
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


}
