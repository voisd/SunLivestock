package com.voisd.sun.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.voisd.sun.view.MyWebView;

/**
 * Created by voisd on 2016/9/26.
 * 联系方式：531972376@qq.com
 */
public class WebViewUtils {

    public static void setWebViewSetting(WebView mWebView, Activity activity){
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // mWebView.setInitialScale(50);//为50%，最小缩放等级

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setBlockNetworkImage(false); // 是否阻止网络图像
        mWebSettings.setBlockNetworkLoads(false); // 是否阻止网络请求
        mWebSettings.setJavaScriptEnabled(true); // 是否加载JS
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 覆盖方式启动缓存
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 本地有用本地，否则网络
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setUseWideViewPort(false); // 使用广泛视窗
        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.NORMAL); // 渲染优先级
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= 16) {
            mWebSettings.setAllowFileAccessFromFileURLs(true);
        }
        mWebSettings.setDatabaseEnabled(true);
//        String dir = getActivity().getApplicationContext()
//                .getDir("database", Context.MODE_PRIVATE).getPath();
//        // 设置数据库路径
//        mWebSettings.setDatabasePath(dir);

        mWebSettings.setUserAgentString(mWebSettings.getUserAgentString()
                + ";native-android");// 获得浏览器的环境

//        mWebView.setWebViewClient(webViewClient);
//        mWebView.setWebChromeClient(webChromeClient);

        mWebView.addJavascriptInterface(new WebJavaScriptObject(activity), "android");
    }

    public static void loadWebViewStr(MyWebView webView,String ceontent){
        StringBuffer sb=new StringBuffer();
        //添加html
        sb.append("<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'>");
        sb.append("<meta charset='utf-8'  content='1'>" +
                "<style type=\\\"text/css\\\">img { height: auto; width: auto\\\\9; width:100%%; }\\n " +
                "a:link {color: #007AFF;text-decoration: none;}\\n a:visited " +
                "{color: #007AFF;text-decoration: none;}\\na:hover {color: #007AFF; text-decoration: none; }  </style>" +
                "</head><body style='color: black'><p></p>");
        //
        //< meta http-equiv="refresh" content="time" url="url" >
        //添加文件的内容
        sb.append(ceontent);
        //加载本地文件
        // sb.append("<img src='file:///"+AContext.getFileUtil().getDownloadsPath()+"'>");
        sb.append("</body></html>");
        // webView.loadData(data, mimeType, encoding);
        //设置字符编码，避免乱码
        webView.getSettings().setDefaultTextEncodingName("utf-8") ;
        webView.loadDataWithBaseURL(null,sb.toString(), "text/html", "utf-8", null);
    }

}
