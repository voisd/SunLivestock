package com.voisd.sun.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;
import com.voisd.sun.R;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 仿微信选择照片
 */
public class SelectImageActivity extends BaseActivity implements SelectImageTakePhotoStatusAdaptet.OnItemListener{

    public final static String CHOOSE_TYPE_INTENT_KEY = "choose_type_intent_key";//选择的类型
    public final static int MULTI_CHOOSE_TYPE = 1;//多选
    public final static int SINGLE_CHOOSE_TYPE = 2;//单选
    public int mChooseType = SINGLE_CHOOSE_TYPE;

    public final static String CHOOSE_IMAGE_NUMBER_INTENT_KEY = "choose_image_number_intent_key";//选择照片的张数
    public int mChooseNumber = 1;

    public final static String RESULT_PAGE_INTENT_KEY = "result_intent_key";//结果页面
    public final static int SEARCH_INTENT_PAGE = 1;//不发广播了，跳转到结果页面
    public int mResultIntent = 0;

    public final static String IS_NEED_TAKE_PHOTO = "is_need_take_phtot";//是否需要拍照
    public boolean mIsNeedTakePhoto;

    public final static String IS_ONLE_TAKE_PHOTO = "is_only_take_photo";//是否只拍照
    public boolean mIsOnlyTakePhoto;

    private RecyclerView recyclerView;
    private GridLayoutManager staggeredGridLayoutManager;
    private SelectImageTakePhotoStatusAdaptet chooseImageWechatAdapter;
    private List<ImagesModel> imageList;

    private ArrayList<ImagesEntry> albumList;

    private TextView selectLayout;
    private RelativeLayout bottomLayout;

    // 列表选择
//    private PopupWindow popupWindow;
    private PopupListAdapter adapter;
    private ListView listView;
    private ImageView bgView;
//    private View popupView;

    //单选
    private String mSelectImagePath = "";

    //多选
    private ArrayList<String> mSelectedItems = new ArrayList<String>();

    // 防止三星某些手机拍照不行
    private String demandImages = "demandImages";
    // 拍照地址
    private String cameraPath = null;
    private boolean isNeedRestorePhoto = true;

    // menu
    private Menu titleMenu;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (cameraPath != null) {
            outState.putString(demandImages, cameraPath);
        }
    }

    // 恢复数据
    private void initializeInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            isNeedRestorePhoto = false;

            String imagePath = savedInstanceState.getString(demandImages);

            mSelectImagePath =imagePath;

            //显示图片
            mSelectedItems.add(mSelectImagePath);
            sumbitImages();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        titleMenu = menu;

        if(mChooseType != SINGLE_CHOOSE_TYPE) {
            menu.add("确定").setTitle("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @OnClick(R.id.toolbar_back_btn)
    public void onclcikBack(){
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        CharSequence itemTitle = item.getTitle();

        if(itemTitle != null)
        {
            if(itemTitle.toString().contains("确定"))
            {
                sumbitImages();
                return true;
            }
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_image;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initializeInstanceState(savedInstanceState);
        listView = (ListView)findViewById(R.id.listview);
        bgView=(ImageView)findViewById(R.id.image);
    }

    @Override
    protected void initViewsAndEvents() {

        initialize();
        initializeViews();
        initializeListeners();

        queryAllAlbums();

        //如果只拍照的话，直接跳过去takePhoto
        if(mIsOnlyTakePhoto){
            takePhoto();
        }
    }

    /**
     * 确定图片
     */
    public void sumbitImages() {
        if (mSelectedItems != null && mSelectedItems.size() > 0) {

            if(mChooseType == SINGLE_CHOOSE_TYPE){
                if(mResultIntent == 1){
                    //搜索页面
                    showToastShort("提交图片");

                    finish();
                }else{
                    sendBroadcastRefresh();
                }
            }else {
                sendBroadcastRefresh();
            }
        } else {
            showToastShort("请选择图片");

            return;
        }
    }

    /**
     * 发送广播传递选择的图片
     */
    public void sendBroadcastRefresh() {
        CommonUtils.OnEventBus(mSelectImagePath, EventBusTags.PHOTO_PHOTO);
        finish();
    }

    private void initialize() {
        imageList = new ArrayList<ImagesModel>();
        albumList = new ArrayList<ImagesEntry>();

        mChooseType = getIntent().getIntExtra(CHOOSE_TYPE_INTENT_KEY, SINGLE_CHOOSE_TYPE);
        mChooseNumber = getIntent().getIntExtra(CHOOSE_IMAGE_NUMBER_INTENT_KEY, 1);
        mResultIntent = getIntent().getIntExtra(RESULT_PAGE_INTENT_KEY, -1);
        mIsNeedTakePhoto = getIntent().getBooleanExtra(IS_NEED_TAKE_PHOTO, true);
        mIsOnlyTakePhoto = getIntent().getBooleanExtra(IS_ONLE_TAKE_PHOTO, false);
    }

    private void initializeViews() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight =dm.heightPixels;
        setToolbarTitle("选择图片");

        recyclerView = (RecyclerView) findViewById(R.id.local_image_recycler_view);

        staggeredGridLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        chooseImageWechatAdapter = new SelectImageTakePhotoStatusAdaptet(SelectImageActivity.this, mChooseType, imageList, mIsNeedTakePhoto, this);
//        recyclerView.setAdapter(chooseImageWechatAdapter);

        bottomLayout = (RelativeLayout) findViewById(R.id.bottom_bar);
        selectLayout = (TextView) findViewById(R.id.select_albums);
    }

    private void refreshRecyclerView(){
        chooseImageWechatAdapter = new SelectImageTakePhotoStatusAdaptet(SelectImageActivity.this, mChooseType, imageList, mIsNeedTakePhoto, this);
        recyclerView.setAdapter(chooseImageWechatAdapter);

    }

    private void initializeListeners() {
        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listView.getVisibility() == View.VISIBLE) {
                    dissmissChoice();
                } else {
                    showChoice();
                }
            }
        });
    }

    private void initializePopupWindow() {

        listView = (ListView)findViewById(R.id.listview);
        bgView=(ImageView)findViewById(R.id.image);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                ImagesEntry bucketEntry = albumList.get(position);

                queryAlbumImages(bucketEntry);

                listView.setSelected(true);

                dissmissChoice();
            }
        });

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissChoice();
            }
        });

        adapter = new PopupListAdapter(SelectImageActivity.this, 0, albumList,
                false);
        listView.setAdapter(adapter);
    }
    private int screenHeight;
    private void showChoice(){
        selectLayout.setEnabled(false);
        listView.setVisibility(View.VISIBLE);
        bgView.setVisibility(View.VISIBLE);
        AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                selectLayout.setEnabled(true);

            }
        }, listView, screenHeight, 0, 300);

        AnimationUtil.setAlpha(null, bgView, 0, 1, 300);
    }

    private void dissmissChoice() {
        selectLayout.setEnabled(false);
        AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                selectLayout.setEnabled(true);
                listView.setVisibility(View.GONE);

            }
        }, listView, 0, screenHeight, 300);

        AnimationUtil.setAlpha(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                bgView.setVisibility(View.GONE);
            }
        }, bgView, 1, 0, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (cameraPath != null && isNeedRestorePhoto) {

                mSelectImagePath = cameraPath;

                mSelectedItems.clear();
                mSelectedItems.add(mSelectImagePath);

                sumbitImages();
            }
        }else{
            finish();
        }
    }

    private void queryAllAlbums() {

        Tasks.executeInBackground(SelectImageActivity.this, new BackgroundWork<List<ImagesEntry>>() {

            @Override
            public List<ImagesEntry> doInBackground() throws Exception {

                return LocalImagesQuery.query(SelectImageActivity.this);
            }
        }, new Completion<List<ImagesEntry>>() {
            @Override
            public void onSuccess(Context context, List<ImagesEntry> result) {

                if (result.size() > 0) {
                    albumList.addAll(result);

                    initializePopupWindow();

                    for (int i = 0; i < albumList.size(); i++) {
                        ImagesEntry imagesEntry = albumList.get(i);
                        if ("CAMERA".equals(imagesEntry.bucketName.toUpperCase())) {
                            queryAlbumImages(imagesEntry);
                            return;
                        }
                    }
                    queryAlbumImages(result.get(0));
                } else {
                    showToastShort("找不到图片文件");
                    refreshRecyclerView();
                    recyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onError(Context context, Exception e) {
                showToastShort("找不到图片文件或请确保手机内存可用");
            }
        });
    }

    private void queryAlbumImages(final ImagesEntry imagesEntry) {

        selectLayout.setText(imagesEntry.bucketName);

        Tasks.executeInBackground(SelectImageActivity.this, new BackgroundWork<List<ImagesModel>>() {
            @Override
            public List<ImagesModel> doInBackground() throws Exception {
                return LocalImagesQuery.getAlbumImages(SelectImageActivity.this, imagesEntry.bucketUrl, imagesEntry.bucketId);
            }
        }, new Completion<List<ImagesModel>>() {
            @Override
            public void onSuccess(Context context, List<ImagesModel> result) {
                imageList.clear();
                imageList.addAll(result);

//                adapter.notifyDataSetChanged();

                refreshRecyclerView();

//                chooseImageWechatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onError(Context context, Exception e) {
                showToastShort("找不到图片文件");

            }
        });
    }

    /**
     * 判断该怎么处理
     */
    public void judgeType(ImagesModel imagesModel){

        if(mChooseType == MULTI_CHOOSE_TYPE){
            //多选
            if(imagesModel.status){
                imagesModel.status = false;
                mSelectedItems.remove(imagesModel.url);
            }else{

                if(mSelectedItems.size() < mChooseNumber) {
                    imagesModel.status = true;
                    mSelectedItems.add(imagesModel.url);
                }else{
                    showToastShort("最多添加" + mChooseNumber + "张图片");
                }
            }
            chooseImageWechatAdapter.notifyDataSetChanged();

            displaySelectedImagesNumber();

        }else{
            //单选
            mSelectedItems.add(imagesModel.url);

            sumbitImages();
        }
    }

    /**
     * 显示已经选择图片的数字
     */
    public void displaySelectedImagesNumber() {
        if (mSelectedItems.size() != 0) {

            String selectImgs = "确定(" + mSelectedItems.size() + "/"
                    + mChooseNumber + ")";

            titleMenu.getItem(0).setTitle(selectImgs);

        } else {

            titleMenu.getItem(0).setTitle("确定");
        }
    }

    @Override
    public void click(int position) {

        if(!mIsNeedTakePhoto){

            ImagesModel imagesModel = imageList.get(position);
            mSelectImagePath = imagesModel.url;

            judgeType(imagesModel);

        }else{
            if(0 == position){
                takePhoto();
            }else{
                ImagesModel imagesModel = imageList.get(position - 1);
                mSelectImagePath = imagesModel.url;

                judgeType(imagesModel);
            }
        }
    }

    /**
     * 跳出来拍照
     */
    private void takePhoto() {
        isNeedRestorePhoto = true;

        File cameraFile = PhotoUtil
                .camera(SelectImageActivity.this);
        if (cameraFile != null)
            cameraPath = cameraFile.getAbsolutePath();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            if (listView!=null){
                if (listView.getVisibility()== View.VISIBLE) {
                    dissmissChoice();
                    return true;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
