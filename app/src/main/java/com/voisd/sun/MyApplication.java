package com.voisd.sun;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.voisd.sun.utils.FileUtils;

import cn.smssdk.SMSSDK;

/**
 * Created by voisd on 2016/9/26.
 * 联系方式：531972376@qq.com
 */
public class MyApplication extends Application{



    @Override
    public void onCreate() {
        super.onCreate();

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        FileUtils.deleteFolder(FileUtils.getH5WorkFilePath());
        FileUtils.initFileCachePath(this);
        FileUtils.copyAssetsToSD(this,"NewsDetailTemplate.html");
        UMShareAPI.get(this);
    }
}
