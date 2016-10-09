package com.voisd.sun.utils.asyn;

import android.content.Context;
import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.LoginMsg;
import com.voisd.sun.common.Contants;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.Md5;
import com.voisd.sun.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 网络请求方法
 */
public class AsyncHttpHelper {
	
	private static final String TAG = "HttpClient";
	
	private static final int TIME_OUT_MILLIS = 30 * 1000;

	private static AsyncHttpClient client = null;

	public static void post(Context context, String url, RequestParams params,
							AsyncHttpResponseHandler responseHandler) {

		getInstance().setTimeout(TIME_OUT_MILLIS);
		getInstance().setConnectTimeout(TIME_OUT_MILLIS);
		getInstance().setResponseTimeout(TIME_OUT_MILLIS * 3);
		getInstance().addHeader("voisd", "baseHeader");

//		LoginMsg loginMsg = LoginMsgHelper.getResult(context);
//		if(!getIgnoreUrls().contains(url)) {
//			if (loginMsg != null) {
//				params.put("token", loginMsg.getToken() + "");
//				Log.e("token",loginMsg.getToken()+"");
//			}
//		}


		if(null!=PreferenceUtils.getPrefString(context, Contants.Preference.Uid,null)
				&&null!=PreferenceUtils.getPrefString(context, Contants.Preference.Token,null)){
			params.put("uid", AES.getSingleton().encrypt(PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")));
			String value =  url+"?uid="+PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")+
					"&token="+PreferenceUtils.getPrefString(context, Contants.Preference.Token,"");
			System.out.println("__"+value);
			params.put("sign", AES.getSingleton().encrypt(Md5.str2Md5(value)));
		}

		System.out.println("《" + url + "》《" + params.toString() + "》");

		getInstance().post(url, params, responseHandler);
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	private synchronized static AsyncHttpClient getInstance() {
		if (null == client) {
			client = new AsyncHttpClient();
		}
		return client;
	}

	/**
	 * 得到忽略提示的接口列表
	 * @return
	 */
	private static List<String> getIgnoreUrls(){

		List<String> urls = new ArrayList<String>();

		urls.add(ApiContants.Urls.TEST_ACCOUNT);

		return urls;
	}
}
