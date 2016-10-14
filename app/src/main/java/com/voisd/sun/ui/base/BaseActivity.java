package com.voisd.sun.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.bk886.njxzs.R;
import com.voisd.sun.common.Contants;
import com.voisd.sun.ui.TestActivity;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.view.iviews.BaseUi;
import com.voisd.sun.view.iviews.IPageStatusUi;
import com.voisd.sun.view.iviews.IRefreshRetryUi;
import com.voisd.sun.view.iviews.IToolbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by voisd on 16/5/23.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseUi, IPageStatusUi, IRefreshRetryUi, IToolbar {

    /**
     * context
     */
    protected Context mContext = null;

    protected Toolbar mToolbar;

    protected SwipeRefreshLayout swipeRefreshLayout;

    private SystemBarTintManager tintManager;

    private ImageView pageStatusIconIv;
    private TextView pageStatusTextTv;

    private CardView refreshAgainBtn;
    private TextView refreshAgainTv;

    private TextView mToolbarLineTv;

    TextView toolbarTv;

    public ImageButton backBtn;
    private boolean isCanBack = true;

    private LinearLayout topLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;


//        setupSystemBar();

        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        initViewsAndEvents();
    }

    protected void setToolbarTitle(String title){
        setTitle(" ");
        if (toolbarTv!=null){
            toolbarTv.setText(title);
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.inject(this);

        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        toolbarTv=ButterKnife.findById(this, R.id.toolbar_title);
        topLayout=ButterKnife.findById(this,R.id.toolbar_top_layout);
        backBtn=ButterKnife.findById(this,R.id.toolbar_back_btn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            if (topLayout!=null){
//                topLayout.setVisibility(View.VISIBLE);
//            }

        }

        if (null != backBtn) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isCanBack) {
                        finish();
                    }
                }
            });
        }

        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout = ButterKnife.findById(this, R.id.swipe_refresh_layout);
        if(null != swipeRefreshLayout){
            swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        }

        pageStatusIconIv = ButterKnife.findById(this, R.id.page_status_icon_iv);
        pageStatusTextTv = ButterKnife.findById(this, R.id.page_status_text_tv);

        refreshAgainBtn = ButterKnife.findById(this, R.id.refresh_again_btn);
        refreshAgainTv = ButterKnife.findById(this, R.id.refresh_again_tv);

        mToolbarLineTv = ButterKnife.findById(this, R.id.common_toolbar_line);
    }

    public void setBackStatus(boolean status){
        isCanBack = status;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.reset(this);

        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSystemBar() {
        setupSystemBar(R.color.colorPrimaryDark);
    }


    public void setupSystemBar(int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
//            if (topLayout!=null){
//                topLayout.setVisibility(View.VISIBLE);
//            }

            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
//        tintManager.setStatusBarTintColor(android.R.color.black);

        setStatusBarDarkMode(true, this);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {

        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * 是否绑定eventBus
     */
    protected abstract boolean isBindEventBusHere();

    /**
     * 绑定布局xml文件
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 初始化布局和事件，在onFirstUserVisible之前执行
     */
    protected abstract void initViewsAndEvents();

    @Override
    public void showToastLong(String msg) {
        if (null != msg && !StringHelper.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void showToastShort(String msg) {
        if (null != msg && !StringHelper.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_SHORT);
        }
    }

    private Toast mToast = null;

    private void toast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, duration);
//            View toastRoot = getLayoutInflater().inflate(R.layout.layout_toast, null);
            setToastBackground(mToast);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public void setToastBackground(Toast toast) {
        View view = toast.getView();
        if(view!=null){
            TextView message=((TextView) view.findViewById(android.R.id.message));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            message.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void showPageStatusView(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }
    }

    @Override
    public void showPageStatusView(int iconRes, String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.VISIBLE);
            pageStatusIconIv.setImageResource(iconRes);
        }
    }

    @Override
    public void hidePageStatusView() {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.GONE);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRefreshRetry(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if(null != refreshAgainBtn){
            refreshAgainBtn.setVisibility(View.VISIBLE);
            refreshAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRefreshRetryBtn();
                }
            });
        }
    }

    public void showLoginRetry(String message) {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.VISIBLE);
            pageStatusTextTv.setText(message);
        }

        if(null != refreshAgainBtn){
            refreshAgainBtn.setVisibility(View.VISIBLE);
            refreshAgainTv.setText("立即登录");
            refreshAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dimissRefreshRetry();
                    CommonUtils.goActivity(mContext, TestActivity.class,null,false);

                }
            });
        }
    }

    @Override
    public void dimissRefreshRetry() {
        if (null != pageStatusTextTv) {
            pageStatusTextTv.setVisibility(View.GONE);
        }

        if (null != pageStatusIconIv) {
            pageStatusIconIv.setVisibility(View.GONE);
        }

        if (null != refreshAgainBtn) {
            refreshAgainBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void clickRefreshRetryBtn() {
        dimissRefreshRetry();
    }

    MaterialDialog builder = null;

    @Override
    public void showProgress(String label) {

        builder = new MaterialDialog.Builder(this)
                .content(label)
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void showProgress(String label, boolean isCancelable) {

        builder = new MaterialDialog.Builder(this)
                .content(label)
                .cancelable(isCancelable)
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

        builder.show();
    }

    @Override
    public void dimissProgress() {
        if (builder != null && builder.isShowing()) {
            builder.dismiss();
        }
    }

    @Override
    public void hideToolbarLine() {
//        if (null != mToolbarLineTv) {
//            mToolbarLineTv.setVisibility(View.GONE);
//        }
    }

    @Override
    public void showToolbarLine() {
//        if (null != mToolbarLineTv) {
//            mToolbarLineTv.setVisibility(View.VISIBLE);
//        }
    }
}
