package com.voisd.sun.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.been.News;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/23.
 */
public class HomeNewInformationAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<News> notes;
    private IRecyclerViewItemListener mListener;

    public HomeNewInformationAdapter(Context context, List<News> noteList, IRecyclerViewItemListener myItemClickListener) {
        mContext = context;
        notes = noteList;
        mListener = myItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.adapter_home_newinfomation, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder=(ViewHolder)holder;
        viewHolder.newsTitleTv.setText(notes.get(position).getTitle());
        viewHolder.newsContentTv.setText(notes.get(position).getBrief());
        viewHolder.newsTimeTv.setText(notes.get(position).getTime());
        String number = StringHelper.isEmpty(notes.get(position).getCommentCount())?"0":notes.get(position).getCommentCount();
        viewHolder.newsNumberTv.setText(number+"条评论");
        if(StringHelper.isEmpty(notes.get(position).getImgUrl())){
            viewHolder.newsShowImg.setVisibility(View.GONE);
        }else{
            viewHolder.newsShowImg.setVisibility(View.VISIBLE);
            PictureUtil.load(mContext, viewHolder.newsShowImg, notes.get(position).getImgUrl(),
                    viewHolder.newsShowImg.getWidth(),
                    viewHolder.newsShowImg.getHeight());
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.news_layout)
        LinearLayout newsLayout;
        @InjectView(R.id.news_show_img)
        ImageView newsShowImg;
        @InjectView(R.id.news_title_tv)
        TextView newsTitleTv;
        @InjectView(R.id.news_content_tv)
        TextView newsContentTv;
        @InjectView(R.id.news_time_tv)
        TextView newsTimeTv;
        @InjectView(R.id.news_number_tv)
        TextView newsNumberTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.news_layout)
        public void clickItem() {
            if (mListener != null) {
                mListener.onItemClick(getPosition());
            }
        }

    }
}
