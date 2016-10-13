package com.voisd.sun.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

import com.bk886.njxzs.R;
import com.voisd.sun.ui.base.BaseActivity;
import com.voisd.sun.utils.CommonUtils;
import com.voisd.sun.utils.StringHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by voisd on 2016/9/28.
 * 联系方式：531972376@qq.com
 */
public class ReplyPopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity  mContext;

    DialogHolder dialogHolder;

    OnReplySubmitClickListener onReplySubmitClickListener;

    public ReplyPopupWindow(Activity context,OnReplySubmitClickListener onReplySubmitClickListener) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.onReplySubmitClickListener = onReplySubmitClickListener;
    }

    public ReplyPopupWindow(Activity context, int width, int height) {
        this.mContext = context;
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.layout_reply_view, null));
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
        dialogHolder.layoutReplyEt.setText("");
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        backgroundAlpha(mContext,0.5f);//0.0-1.0
        showAtLocation(view,Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view,String hint) {
        dialogHolder.layoutReplyEt.setText("");
        dialogHolder.layoutReplyEt.setHint(hint);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        backgroundAlpha(mContext,0.5f);//0.0-1.0
        showAtLocation(view,Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    class DialogHolder {

        @InjectView(R.id.layout_reply_et)
        EditText layoutReplyEt;
        @InjectView(R.id.layout_reply_cardview)
        CardView layoutReplyCardview;
        @InjectView(R.id.layout_submit_cardview)
        CardView layoutSubmitCardview;
        @InjectView(R.id.layout_content_rel)
        RelativeLayout layoutContentRel;
        @InjectView(R.id.layout_root_rel)
        RelativeLayout layoutRootRel;

        DialogHolder(View view) {
            ButterKnife.inject(this, view);
            layoutSubmitCardview.setOnClickListener(ReplyPopupWindow.this);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.layout_submit_cardview){
            if(!StringHelper.isEmpty(dialogHolder.layoutReplyEt.getText().toString().trim())) {
                onReplySubmitClickListener.OnSubmit(dialogHolder.layoutReplyEt.getText().toString().trim());
                this.dismiss();
            }else{
                ((BaseActivity)mContext).showToastShort("评论内容不能为空");
            }
        }
    }

    public interface OnReplySubmitClickListener{
        public void OnSubmit(String str);
    }

}
