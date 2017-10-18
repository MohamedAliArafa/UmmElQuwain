package com.ubn.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.Albums.AlbumResultModel;
import com.ubn.ummelquwain.ui.screens.landing.media.AlbumsContract;
import com.ubn.ummelquwain.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private List<AlbumResultModel> mList = new ArrayList<>();
    private int mLayout;
    private AlbumsContract.UserAction mPresenter;

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

    public AlbumsAdapter(Context context, ArrayList<AlbumResultModel> list, int layout, AlbumsContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
    }

    public void updateData(List<AlbumResultModel> list) {
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
        AlbumResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getAlbumName());
        GlideApp.with(holder.itemView)
                .load(model.getAlbumImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                .fitCenter()
                .into(holder.mThumpImageView);
        holder.itemView.setOnClickListener(view -> {
            mPresenter.openDetails(model.getAlbumID(), model.getAlbumDescription());
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
