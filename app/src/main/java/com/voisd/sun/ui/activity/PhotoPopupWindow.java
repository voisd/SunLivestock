package com.voisd.sun.ui.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.StringHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by voisd on 2016/9/28.
 * 联系方式：531972376@qq.com
 */
public class PhotoPopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;

    DialogHolder dialogHolder;

    OnPhotoSubmitClickListener onPhotoSubmitClickListener;

    public PhotoPopupWindow(Activity context, OnPhotoSubmitClickListener onPhotoSubmitClickListener) {
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.onPhotoSubmitClickListener = onPhotoSubmitClickListener;
    }

    public PhotoPopupWindow(Activity context, int width, int height) {
        this.mContext = context;
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.layout_image_choose_view, null));
        setWidth(LayoutParams.FILL_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogHolder = new DialogHolder(getContentView());
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        setBackgroundDrawable(new BitmapDrawable());

        //设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.MaterialDialogSheetAnimation);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(mContext, 1f);
            }
        });

    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        backgroundAlpha(mContext, 0.5f);//0.0-1.0
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    class DialogHolder {

        @InjectView(R.id.photo_look_tv)
        TextView photoLookTv;
        @InjectView(R.id.popup_photo_camera)
        TextView popupPhotoCamera;
        @InjectView(R.id.popup_photo_choose)
        TextView popupPhotoChoose;
        @InjectView(R.id.popup_cancel)
        TextView popupCancel;

        DialogHolder(View view) {
            ButterKnife.inject(this, view);
            photoLookTv.setOnClickListener(PhotoPopupWindow.this);
            popupPhotoCamera.setOnClickListener(PhotoPopupWindow.this);
            popupPhotoChoose.setOnClickListener(PhotoPopupWindow.this);
            popupCancel.setOnClickListener(PhotoPopupWindow.this);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.photo_look_tv) {
            onPhotoSubmitClickListener.openPhotoCHoose(0);
        }else if(v.getId() == R.id.popup_photo_camera){
            onPhotoSubmitClickListener.openPhotoCHoose(1);
        }else if(v.getId() == R.id.popup_photo_choose){
            onPhotoSubmitClickListener.openPhotoCHoose(2);
        }else if(v.getId() == R.id.popup_cancel){
            onPhotoSubmitClickListener.openPhotoCHoose(3);
        }
        this.dismiss();
    }

    public interface OnPhotoSubmitClickListener {
        /**
         * 0 查看 1 拍照 2打开相册  3 取消
         * */
        public void openPhotoCHoose(int type);
    }

}
