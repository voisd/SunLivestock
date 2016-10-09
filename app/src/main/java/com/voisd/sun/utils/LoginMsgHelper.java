package com.voisd.sun.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;

import com.voisd.sun.been.LoginMsg;
import com.voisd.sun.common.Contants;
import com.voisd.sun.ui.activity.LoginActivity;


/**
 * Created by voisd on 2016/6/18.
 */
public class LoginMsgHelper {

    public static boolean isLogin(Context mContext){
        String Token = PreferenceUtils.getPrefString(mContext, Contants.Preference.Token, null);
        if(StringHelper.isEmpty(Token))
            return false;
        return true;
    }

    //登录之后返回的结果
    public static LoginMsg getResult(Context mContext){

        String result = PreferenceUtils.getPrefString(mContext, Contants.Preference.loginMsg, null);

        if(!StringHelper.isEmpty(result)) {

            try {
                JsonHelper<LoginMsg> jsonHelper = new JsonHelper<LoginMsg>(LoginMsg.class);
                LoginMsg loginMsg = jsonHelper.getData(result, "data");

                return loginMsg;

            }catch(Exception ex){
                return null;
            }
        }
        return null;
    }

//    /**
//     * 需要重新登陆
//     * @param context
//     */
    public static void reLogin(Context context)
    {
        PreferenceUtils.setPrefString(context, Contants.Preference.loginMsg, null);

//        判断如果是后台运行的话不弹出登陆框
        if(!CommonUtils.isAppRunningForeground(context))
        {
            return;
        }

        Intent intentToBeNewRoot = new Intent(context, LoginActivity.class);

        ComponentName cn = intentToBeNewRoot.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        context.startActivity(mainIntent);
    }

}
