package com.voisd.sun.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.been.SubCommentList;
import com.voisd.sun.listeners.IRecyclerViewItemListener;
import com.voisd.sun.utils.PictureUtil;
import com.voisd.sun.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by voisd on 2016/9/23.
 */
public class NewsReplyChildsListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<SubCommentList> notes;
    private IRecyclerViewItemListener mListener;

    public NewsReplyChildsListAdapter(Context context, List<SubCommentList> noteList, IRecyclerViewItemListener myItemClickListener) {
        mContext = context;
        notes = noteList;
        mListener = myItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.adapter_newsreplychildslist, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.replyUsernameTv.setText(notes.get(position).getUser().getName());
        viewHolder.replyContentTv.setText(notes.get(position).getContent());
        viewHolder.replyTimeTv.setText(TimeUtils.fromLongToDate(notes.get(position).getReport_time()+"000"));
        PictureUtil.load(mContext, viewHolder.replyUserImg, notes.get(position).getUser().getAvatar());
        for(SubCommentList subCommentList:notes){
            if(notes.get(position).getParent_id().equals(subCommentList.getComment_id())){
                viewHolder.replyUsernameTv.setText(notes.get(position).getUser().getName() + " 回复 "
                + subCommentList.getUser().getName());
                break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.reply_arrow)
        ImageView replyArrow;
        @InjectView(R.id.reply_user_img)
        ImageView replyUserImg;
        @InjectView(R.id.reply_username_tv)
        TextView replyUsernameTv;
        @InjectView(R.id.reply_content_tv)
        TextView replyContentTv;
        @InjectView(R.id.reply_time_tv)
        TextView replyTimeTv;
        @InjectView(R.id.reply_reply_tv)
        TextView replyReplyTv;
        @InjectView(R.id.reply_rel)
        RelativeLayout replyRel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.reply_rel)
        public void clickItem() {
            if (mListener != null) {
                mListener.onItemClick(getPosition());
            }
        }

    }
}
