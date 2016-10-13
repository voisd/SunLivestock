package com.voisd.sun.wxapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.bk886.njxzs.R;

import java.io.File;
import java.util.List;

/**
 * 增加适配是否显示拍照按钮
 */
public class SelectImageTakePhotoStatusAdaptet extends RecyclerView.Adapter<SelectImageTakePhotoStatusAdaptet.MyViewHolder> {

    private Context mContext;

    private OnItemListener mOnItemListener;

    private int mChooseType;

    private boolean mNeedTakePhoto;

    private List<ImagesModel> mImagesModels;

    public SelectImageTakePhotoStatusAdaptet(Context context, int chooseType, List<ImagesModel> imagesModels, boolean isNeedTakePhoto, OnItemListener onItemListener){
        mContext = context;
        mChooseType = chooseType;
        mImagesModels = imagesModels;
        mNeedTakePhoto = isNeedTakePhoto;
        mOnItemListener = onItemListener;
    }

    public interface OnItemListener{
        void click(int position);
    }

    @Override
    public SelectImageTakePhotoStatusAdaptet.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.selectimage_gridview_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectImageTakePhotoStatusAdaptet.MyViewHolder holder, int position) {

        if(mNeedTakePhoto){
            //需要拍照

            if(0 == position){
                holder.localImg.setVisibility(View.GONE);
                holder.cameraLayout.setVisibility(View.VISIBLE);
                holder.checkedTextView.setVisibility(View.GONE);
            }else{

                if(mChooseType == SelectImageActivity.SINGLE_CHOOSE_TYPE){
                    holder.checkedTextView.setVisibility(View.GONE);
                }else{
                    holder.checkedTextView.setVisibility(View.VISIBLE);
                }

                ImagesModel imagesModel = mImagesModels.get(position - 1);

                holder.localImg.setVisibility(View.VISIBLE);
                holder.cameraLayout.setVisibility(View.GONE);

                if(imagesModel.status){
                    holder.checkedTextView.setChecked(true);
                }else{
                    holder.checkedTextView.setChecked(false);
                }

                Picasso.with(mContext).load(new File(imagesModel.url)).resize(200, 200)
                        .centerCrop().into(holder.localImg);
            }

        }else{
            //不需要拍照

            if(mChooseType == SelectImageActivity.SINGLE_CHOOSE_TYPE){
                holder.checkedTextView.setVisibility(View.GONE);
            }else{
                holder.checkedTextView.setVisibility(View.VISIBLE);
            }

            ImagesModel imagesModel = mImagesModels.get(position);

            holder.localImg.setVisibility(View.VISIBLE);
            holder.cameraLayout.setVisibility(View.GONE);

            if(imagesModel.status){
                holder.checkedTextView.setChecked(true);
            }else{
                holder.checkedTextView.setChecked(false);
            }

            Picasso.with(mContext).load(new File(imagesModel.url)).resize(200, 200)
                    .centerCrop().into(holder.localImg);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("size", mImagesModels.size() + 1 + "");
        if(mNeedTakePhoto) {
            return mImagesModels.size() + 1;
        }else{
            return mImagesModels.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        RelativeLayout cardView;
        ImageView localImg;
        RelativeLayout cameraLayout;
        CheckedTextView checkedTextView;

        public MyViewHolder(View view)
        {
            super(view);
            cardView = (RelativeLayout) view.findViewById(R.id.card_view_layout);
            localImg = (ImageView) view.findViewById(R.id.local_image_iv);
            cameraLayout = (RelativeLayout) view.findViewById(R.id.camera_item);
            checkedTextView = (CheckedTextView) view.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemListener.click(getPosition());
        }
    }
}
