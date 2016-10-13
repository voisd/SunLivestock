package com.voisd.sun.ui.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.Login;
import com.voisd.sun.been.LoginMsg;
import com.voisd.sun.been.PostResult;
import com.voisd.sun.been.UploadAvatar;
import com.voisd.sun.common.Contants;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.activity.AboutActivity;
import com.voisd.sun.ui.activity.BrowserCompareImageActivity;
import com.voisd.sun.ui.activity.HomeActivity;
import com.voisd.sun.ui.activity.LoginActivity;
import com.voisd.sun.ui.activity.MyCollecdActivity;
import com.voisd.sun.ui.activity.MyInfoActivity;
import com.voisd.sun.ui.activity.PassWorkActivity;
import com.voisd.sun.ui.activity.PhotoPopupWindow;
import com.voisd.sun.ui.base.BaseFragment;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.FileUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.LoginMsgHelper;
import com.voisd.sun.utils.NetUtils;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.PreferenceUtils;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.http.HttpImageUtil;
import com.voisd.sun.view.MyImageView;
import com.voisd.sun.view.iviews.ICommonViewUi;
import com.voisd.sun.wxapi.SelectImageActivity;

import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/22.
 */
public class HomeMyInfoFragment extends BaseFragment implements PhotoPopupWindow.OnPhotoSubmitClickListener,
        ICommonViewUi {

    public static final int REQUEST_CAMERA = 301; //打开相机
    public static final int REQUEST_PHOTO = 3032; //打开相册

    @InjectView(R.id.user_header_img)
    MyImageView userHeaderImg;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;
    @InjectView(R.id.user_collect_rel)
    RelativeLayout userCollectRel;
    @InjectView(R.id.user_reply_rel)
    RelativeLayout userReplyRel;
    @InjectView(R.id.user_info_rel)
    RelativeLayout userInfoRel;
    @InjectView(R.id.user_system_rel)
    RelativeLayout userSystemRel;
    @InjectView(R.id.user_bottom_cardView)
    CardView userBottomCardView;
    @InjectView(R.id.user_bottom_tv)
    TextView userBottomTv;

    private String mContent = "???";

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    PhotoPopupWindow photoPopupWindow;

    File pathFile;

    public static HomeMyInfoFragment newInstance(String content) {
        HomeMyInfoFragment fragment = new HomeMyInfoFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    protected void onFirstUserVisible() {
        ((HomeActivity)getActivity()).setDrawerLayout(false);
    }

    @Override
    protected void onUserVisible() {
        ((HomeActivity)getActivity()).setDrawerLayout(false);
    }


    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViewsAndEvents() {
        photoPopupWindow = new PhotoPopupWindow(getActivity(),this);
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);
        initUI();
    }

    void initUI(){
        if(LoginMsgHelper.isLogin(mContext)) {
            userNameTv.setText(PreferenceUtils.getPrefString(mContext, Contants.Preference.Name, ""));
            PictureUtil.load(mContext, userHeaderImg, PreferenceUtils.getPrefString(mContext, Contants.Preference.Avatar, ""));
            userBottomTv.setText("退出账号");
        }else{
            userNameTv.setText("未登录");
            userHeaderImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
            userBottomTv.setText("点击登录");
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_myinfo;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    public void onEvent(PostResult result) {

        if (result != null) {
            if (EventBusTags.LOGIN_SUCCESS.equals(result.getTag())) {
                initUI();
            }
            else if (EventBusTags.LOGIN_UPDATE.equals(result.getTag())) {
                initUI();
            }else if (EventBusTags.PHOTO_CAMERA.equals(result.getTag())) {
                submitUserAvatar();
            }else if (EventBusTags.PHOTO_PHOTO.equals(result.getTag())) {

                String path = (String)result.getResult();
                if(StringHelper.isEmpty(path)){
                    showToastShort("图片不存在,请重新选择");
                    return;
                }
                pathFile = new File(path);
                submitUserAvatar();
            }
        }
    }

    public void submitUserAvatar(){
        if (pathFile != null) {
            if(!pathFile.exists()){
                showToastShort("图片不存在,请重新选择");
                return;
            }
        }else{
            showToastShort("图片不存在,请重新选择");
            return;
        }
        iCommonRequestPresenter.upload(ApiContants.EventTags.UPLOADAVATAR_DO,mContext,
                ApiContants.Urls.UPLOADAVATAR_DO,pathFile);
    }

    @OnClick({R.id.user_collect_rel, R.id.user_reply_rel, R.id.user_info_rel,R.id.user_system_rel,
            R.id.user_header_img,R.id.user_name_tv,R.id.user_pass_rel})
    public void onClickView(View view) {
        if (!LoginMsgHelper.isLogin(mContext)&&view.getId()!=R.id.user_system_rel) {
            CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
            return;
        }
        switch (view.getId()){
            case R.id.user_collect_rel:
                CommonUtils.goActivity(mContext, MyCollecdActivity.class,null);
                break;
            case R.id.user_reply_rel:
                break;
            case R.id.user_info_rel:
                CommonUtils.goActivity(mContext, MyInfoActivity.class,null);
                break;
            case R.id.user_system_rel:
                CommonUtils.goActivity(mContext, AboutActivity.class,null);
                break;
            case R.id.user_header_img:
                if(photoPopupWindow!=null){
                    photoPopupWindow.show(userHeaderImg);
                }
                break;
            case R.id.user_name_tv:
                CommonUtils.goActivity(mContext, LoginActivity.class,null);
                break;
            case R.id.user_pass_rel:
                CommonUtils.goActivity(mContext, PassWorkActivity.class,null);
                break;
        }
    }

    @OnClick(R.id.user_bottom_cardView)
    public void onclickBottom(){
        if (!LoginMsgHelper.isLogin(mContext)) {
            CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
        }else{
            PreferenceUtils.setPrefString(mContext, Contants.Preference.Token,null );
            PreferenceUtils.setPrefString(mContext, Contants.Preference.Uid,null);
            PreferenceUtils.setPrefString(mContext, Contants.Preference.loginMsg,null);
            PreferenceUtils.setPrefString(mContext, Contants.Preference.Name,null);
            PreferenceUtils.setPrefString(mContext, Contants.Preference.Avatar,null);
            CommonUtils.goActivity(mContext, LoginActivity.class, null, false);
            initUI();
        }
    }

    @Override
    public void openPhotoCHoose(int type) {
        if(type==0){
            Bundle bundle = new Bundle();
            bundle.putString(BrowserCompareImageActivity.INTENT_URL_KEY,PreferenceUtils.getPrefString(mContext, Contants.Preference.Avatar, ""));
            CommonUtils.goActivity(mContext, BrowserCompareImageActivity.class,bundle);
        }else if(type==1){
            pathFile = FileUtils.createFile(mContext);
            CommonUtils.startCamera(getActivity(),pathFile,REQUEST_CAMERA);
        }else if(type==2){
            Intent intent = new Intent(mContext, SelectImageActivity.class);
            intent.putExtra(SelectImageActivity.IS_ONLE_TAKE_PHOTO, false);
            intent.putExtra(SelectImageActivity.IS_NEED_TAKE_PHOTO, false);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("图片1路径:"+requestCode);
        if(resultCode==REQUEST_CAMERA){
//            Log.d(TAG, "onActivityResult:相册 " + data.getData().toString());
            System.out.println("图片相1机路径:");
            ContentResolver resolver = getActivity().getContentResolver();
            System.out.println("图片相机路径:"+data.getData().toString());
        }else if(resultCode==REQUEST_PHOTO){

        }
    }

    @Override
    public void toRequest(int eventTag) {

    }

    @Override
    public void getRequestData(int eventTag, String result) {
        System.out.println(eventTag+"__getRequestData___"+result);
        JsonHelper<UploadAvatar> jsonHelper = new JsonHelper<UploadAvatar>(UploadAvatar.class);
        UploadAvatar uploadAvatar = jsonHelper.getData(result, "data");
        PictureUtil.load(mContext, userHeaderImg,uploadAvatar.getUrl());
        PreferenceUtils.setPrefString(mContext, Contants.Preference.Avatar, uploadAvatar.getUrl());
        showToastShort("头像提交成功");
    }

    @Override
    public void onRequestSuccessException(int eventTag, String msg) {
        System.out.println(eventTag+"___onRequestSuccessException__"+msg);
        showToastShort(msg);
    }

    @Override
    public void onRequestFailureException(int eventTag, String msg) {
        System.out.println(eventTag+"___onRequestFailureException__"+msg);
        showToastShort(msg);
    }

    @Override
    public void isRequesting(int eventTag, boolean status) {
        if(eventTag==ApiContants.EventTags.UPLOADAVATAR_DO){
            if(status){
                showProgress("上传...");
            }else{
                dimissProgress();
            }
        }
    }
}
