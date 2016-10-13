package com.voisd.sun.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.been.CommentList;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.StringHelper;
import com.voisd.sun.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/23.
 */
public class NewsReplyListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<CommentList> notes;
    private IRecyclerViewItemListener mListener;

    public NewsReplyListAdapter(Context context, List<CommentList> noteList, IRecyclerViewItemListener myItemClickListener) {
        mContext = context;
        notes = noteList;
        mListener = myItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.adapter_newsreplylist, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.newsUsernameTv.setText(notes.get(position).getUser().getName());
        viewHolder.newsContentTv.setText(notes.get(position).getContent());
        viewHolder.newsTimeTv.setText(TimeUtils.fromLongToDate(notes.get(position).getReport_time()+"000"));
        String number = StringHelper.isEmpty(notes.get(position).getReportCount())?"0":notes.get(position).getReportCount();
        viewHolder.newsNumTv.setText(number+"条回复");
        PictureUtil.load(mContext, viewHolder.newsUserImg, notes.get(position).getUser().getAvatar());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.news_reply_rel)
        RelativeLayout newsReplyRel;
        @InjectView(R.id.news_arrow)
        ImageView newsArrow;
        @InjectView(R.id.news_user_img)
        ImageView newsUserImg;
        @InjectView(R.id.news_username_tv)
        TextView newsUsernameTv;
        @InjectView(R.id.news_content_tv)
        TextView newsContentTv;
        @InjectView(R.id.news_time_tv)
        TextView newsTimeTv;
        @InjectView(R.id.news_num_tv)
        TextView newsNumTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.news_reply_rel)
        public void clickItem() {
            if (mListener != null) {
                mListener.onItemClick(getPosition());
            }
        }

    }

}
