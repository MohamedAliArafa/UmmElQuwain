package com.ubn.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.landing.stations.StationsContract;
import com.ubn.ummelquwain.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class StationAdapter extends RealmRecyclerViewAdapter<StationResultModel, StationAdapter.MyViewHolder> {

    private RealmResults<StationResultModel> mList;
    private StationsContract.UserAction mPresenter;
    private Context mContext;
    private Realm mRealm;
    RotateAnimation mRotateAnimation;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_station_name)
        TextView mTitleTextView;

        @BindView(R.id.tv_station_category)
        TextView mCategoryTextView;

        @BindView(R.id.tv_program_desc)
        TextView mProgramTextView;

        @BindView(R.id.tv_on_live)
        TextView mLiveOnTextView;

        @BindView(R.id.iv_station_logo)
        ImageView mThumpImageView;

        @BindView(R.id.iv_play)
        ImageView mPlayImageView;

        @BindView(R.id.iv_like)
        ImageView mLikeImageView;

        @BindView(R.id.view_list_indicator)
        View mIndicatorView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public StationAdapter(Context context, RealmResults<StationResultModel> list, StationsContract.UserAction presenter) {
        super(list, true);
        mList = list;
        mPresenter = presenter;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
        mRotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(500);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);

    }

    public void updateData(RealmResults<StationResultModel> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_station, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StationResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getStationName());
        holder.mCategoryTextView.setText(model.getCategoryName());
        holder.mProgramTextView.setText(model.getCurrentProgramName());
        if (model.getStationLogo() != null) {
            GlideApp.with(mContext)
                    .load(model.getStationLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                    .fitCenter()
                    .into(holder.mThumpImageView);
        }else {
            GlideApp.with(mContext).clear(holder.mThumpImageView);
        }
        try {
            if (model.getIsLive()) {
                holder.mLiveOnTextView.setText(String.format("%s %s", mContext.getString(R.string.title_now), mContext.getString(R.string.title_current_program)));
                holder.mLiveOnTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                holder.mProgramTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                holder.mLiveOnTextView.setTextColor(mContext.getResources().getColor(R.color.statusColor));
                holder.mProgramTextView.setTextColor(mContext.getResources().getColor(R.color.statusColor));
            }

            holder.mLikeImageView.setImageDrawable(mContext.getResources()
                    .getDrawable(model.getIsFavourite() == 1 ?
                            R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
            switch (model.isPlaying()){
                case Paused:
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    holder.mIndicatorView.setVisibility(View.GONE);
                    mRotateAnimation.cancel();
                    break;
                case Playing:
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    holder.mIndicatorView.setVisibility(View.VISIBLE);
                    mRotateAnimation.cancel();
                    break;
                case Stopped:
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    holder.mIndicatorView.setVisibility(View.GONE);
                    mRotateAnimation.cancel();
                    break;
                case Buffering:
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    holder.mIndicatorView.setVisibility(View.VISIBLE);
                    holder.mPlayImageView.setAnimation(mRotateAnimation);
                    break;
            }

            holder.mLikeImageView.setOnClickListener(view -> {
                try {
                    //ToDo Fix Issue
                    mPresenter.setFav(model.getStationID(), model.getIsFavourite(), fav -> {
                        mRealm.beginTransaction();
                        model.setIsFavourite(fav);
                        mRealm.commitTransaction();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            holder.mThumpImageView.setTransitionName("stat_"+String.valueOf(model.getStationID()));

            holder.itemView.setOnClickListener(view ->
                    mPresenter.openDetails(model.getStationID(), holder.mThumpImageView)
            );
            holder.mPlayImageView.setOnClickListener(view -> {
                        mPresenter.playStream(model);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
