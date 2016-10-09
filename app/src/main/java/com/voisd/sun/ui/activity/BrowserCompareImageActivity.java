package com.voisd.sun.ui.activity;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.squareup.picasso.Picasso;
import com.voisd.sun.R;
import com.voisd.sun.common.Contants;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.FileUtils;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.PreferenceUtils;
import com.voisd.sun.utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/30.
 * 联系方式：531972376@qq.com
 */
public class BrowserCompareImageActivity extends BaseActivity {
    public static final String INTENT_URL_KEY = "url";
    @InjectView(R.id.compare_image_iv)
    TouchImageView compareImageIv;
    @InjectView(R.id.save_img_tv)
    TextView saveImgTv;
    private String mCompareImagePath;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_browser_compare_images;
    }

    @Override
    protected void initViewsAndEvents() {
        init();
        initViews();
    }

    private void init() {
        mCompareImagePath = getIntent().getStringExtra(INTENT_URL_KEY);
    }

    private void initViews() {
        PictureUtil.load(mContext,compareImageIv, mCompareImagePath,0,0);
    }

    @OnClick(R.id.save_img_tv)
    public void onClickSave(){
        if(FileUtils.saveFile(PreferenceUtils.getPrefString(mContext, Contants.Preference.Name,"")+
                TimeUtils.getTimeMillis()+".jpg",compareImageIv)){
            showToastShort("保存成功");
        }else{
            showToastShort("保存失败");
        }
    }


}
