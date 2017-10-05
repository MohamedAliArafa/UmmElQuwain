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
import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;
import com.ubn.ummelquwain.ui.screens.landing.media.albums.media.MediaContract;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class AlbumMediaAdapter extends RecyclerView.Adapter<AlbumMediaAdapter.MyViewHolder> {

    private List<MediaResultModel> mList = new ArrayList<>();
    private int mLayout;
    private MediaContract.UserAction mPresenter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_album_title)
        TextView mTitleTextView;

        @BindView(R.id.iv_logo)
        ImageView mThumpImageView;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AlbumMediaAdapter(Context context, ArrayList<MediaResultModel> list, int layout, MediaContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
    }

    public void updateData(List<MediaResultModel> list) {
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
        MediaResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getDescription());
        GlideApp.with(holder.itemView)
                .load(model.getVideoThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.mThumpImageView);
        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(mList, position));
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
