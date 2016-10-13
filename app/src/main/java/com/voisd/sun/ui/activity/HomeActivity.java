package com.voisd.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.bk886.njxzs.R;
import com.voisd.sun.api.ApiContants;
import com.voisd.sun.been.Category;
import com.voisd.sun.been.Login;
import com.voisd.sun.common.EventBusTags;
import com.voisd.sun.presenter.ICommonRequestPresenter;
import com.voisd.sun.presenter.impl.CommonRequestPresenterImpl;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.ui.fragment.HomeMyInfoFragment;
import com.voisd.sun.ui.fragment.HomeNewInformationFragment;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.JsonHelper;
import com.voisd.sun.utils.TimeUtils;
import com.voisd.sun.view.NoScrollViewpager;
import com.voisd.sun.view.iviews.BaseUi;
import com.voisd.sun.view.iviews.ICommonViewUi;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by voisd on 2016/9/22.
 */
public class HomeActivity extends BaseActivity implements ICommonViewUi {

    @InjectView(R.id.tabs_tl)
    TabLayout tabsTl;
    @InjectView(R.id.viewpager)
    NoScrollViewpager viewpager;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.listView)
    ListView listView;


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    private ICommonRequestPresenter iCommonRequestPresenter = null;

    MyHomeTabAdapter tabAdapter;

    List<Category> categoryList;

    boolean isLoad = false;

    @Override
    protected void initViewsAndEvents() {
        iCommonRequestPresenter = new CommonRequestPresenterImpl(mContext, this);

        tabAdapter = new MyHomeTabAdapter(mContext, getSupportFragmentManager());
        viewpager.setNoScroll(true);
        viewpager.setAdapter(tabAdapter);
        viewpager.setOffscreenPageLimit(2);
        tabsTl.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        toRequest(ApiContants.EventTags.CATEGORY_DO);
    }

    public void openDrawerLayout(){
        if(isLoad){
            showToastShort("加载中，请稍后再试");
            return;
        }
        if(categoryList==null){
            toRequest(ApiContants.EventTags.CATEGORY_DO);
        }else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void toRequest(int eventTag) {
        if(ApiContants.EventTags.CATEGORY_DO == eventTag){
            RequestParams requestParams = new RequestParams();
            iCommonRequestPresenter.request(eventTag, mContext, ApiContants.Urls.CATEGORY_DO, requestParams);
        }
    }

    @Override
    public void getRequestData(int eventTag, String result) {
        if(ApiContants.EventTags.CATEGORY_DO == eventTag){
            JsonHelper<Category> jsonHelper = new JsonHelper<Category>(Category.class);
            categoryList = jsonHelper.getDatas(result, "data");
            initList();
        }
    }

    private void initList(){
        if(categoryList==null)
            return;
//        DrawerLayout.LayoutParams lp =  (DrawerLayout.LayoutParams)listView.getLayoutParams();
//        lp.setMargins(0,CommonUtils.getSysHeight(mContext),0,0);
//        lp.gravity = Gravity.RIGHT;
//        listView.setLayoutParams(lp);
        Category category = new Category();
        category.setCid("0");
        category.setName("农家小助手");
        categoryList.add(0,category);
        Ladapter ladapter = new Ladapter();
        listView.setAdapter(ladapter);
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
            showProgress("初始化...");
        }else{
            dimissProgress();
        }
        isLoad = status;
    }

    public void setDrawerLayout(boolean Status){
        if(Status){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public class MyHomeTabAdapter extends FragmentPagerAdapter {

        public String[] pageName = {"最新资讯", "个人中心"};

        public MyHomeTabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (0 == position){
                return HomeNewInformationFragment.newInstance(pageName[position]);
            }else{
                return HomeMyInfoFragment.newInstance(pageName[position]);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageName[position];
        }

        @Override
        public int getCount() {
            return pageName.length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult============");
        if (resultCode != RESULT_OK){
            showToastShort("操作失败");
            return;
        }
        System.out.println("onActivityResult============resultCode");
        if(requestCode ==HomeMyInfoFragment.REQUEST_PHOTO){
            System.out.println("onActivityResult============resultCode_REQUEST_PHOTO");
            String[] proj = { MediaStore.Images.Media.DATA };
            // 获取选中图片的路径
            Cursor cursor = getContentResolver().query(data.getData(),proj, null, null, null);
            String photo_path = "";
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                photo_path = cursor.getString(column_index);
                if (photo_path == null) {
                    photo_path = CommonUtils.getPath(getApplicationContext(),data.getData());
                }
            }
            cursor.close();
            CommonUtils.OnEventBus(photo_path,EventBusTags.PHOTO_PHOTO);

        }else if(requestCode == HomeMyInfoFragment.REQUEST_CAMERA){
            System.out.println("onActivityResult============resultCode_REQUEST_CAMERA");
            CommonUtils.postEventBus(EventBusTags.PHOTO_CAMERA);
        }else{
            System.out.println("onActivityResult============requestCode_over+"+requestCode);
        }

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }else if(viewpager.getCurrentItem()!=0){
                viewpager.setCurrentItem(0);
            }else if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class Ladapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewListHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listview_item,
                        null);
                holder = new ViewListHolder();
                holder.categortTv = (TextView) convertView.findViewById(R.id.categort_tv);
                convertView.setTag(holder);
            }else{
                holder = (ViewListHolder)convertView.getTag();
            }
            holder.categortTv.setText(categoryList.get(position).getName());
            holder.categortTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.OnEventBus(categoryList.get(position),EventBusTags.HOME_CATEGORY_CHOOSE);
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            });
            return convertView;
        }
    }

    class ViewListHolder{
        public TextView categortTv;
    }
}
