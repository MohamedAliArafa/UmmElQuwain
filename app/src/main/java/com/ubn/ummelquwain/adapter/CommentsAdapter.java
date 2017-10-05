package com.ubn.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.program.ProgramUserCommentResultModel;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ProgramUserCommentResultModel> mList = new ArrayList<>();
    private int mLayout;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_image)
        ImageView mUserImageView;

        @BindView(R.id.tv_user_name)
        TextView mUserTextView;

        @BindView(R.id.tv_comment)
        TextView mCommentTextView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CommentsAdapter(Context context, List<ProgramUserCommentResultModel> list, int layout) {
        mContext = context;
        mList = list;
        mLayout = layout;
    }

    public void updateData(List<ProgramUserCommentResultModel> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProgramUserCommentResultModel model = mList.get(position);
        GlideApp.with(holder.itemView)
                .load(model.getUserImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.mUserImageView);
        holder.mUserTextView.setText(model.getUserName());
        holder.mCommentTextView.setText(model.getCommentText());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
