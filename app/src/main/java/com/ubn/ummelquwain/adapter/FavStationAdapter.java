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
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.landing.favorite.FavContract;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class FavStationAdapter extends RealmRecyclerViewAdapter<StationResultModel, FavStationAdapter.MyViewHolder> {

    private RealmResults<StationResultModel> mList;
    private FavContract.UserAction mPresenter;
    private Context mContext;
    private Realm mRealm;

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

    public FavStationAdapter(Context context, RealmResults<StationResultModel> list, FavContract.UserAction presenter) {
        super(list, true);
        mList = list;
        mPresenter = presenter;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
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
        if (model.getIsLive()){
            holder.mLiveOnTextView.setText(String.format("%s %s", mContext.getString(R.string.title_now), mContext.getString(R.string.title_current_program)));
            holder.mLiveOnTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.mProgramTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }else {
            holder.mLiveOnTextView.setTextColor(mContext.getResources().getColor(R.color.statusColor));
            holder.mProgramTextView.setTextColor(mContext.getResources().getColor(R.color.statusColor));
        }
        GlideApp.with(mContext)
                .load(model.getStationLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.mThumpImageView);
        try {
            holder.mLikeImageView.setImageDrawable(mContext.getResources()
                    .getDrawable(model.getIsFavourite() == 1 ?
                            R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
            holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(model.isPlaying() ?
                    R.drawable.ic_puss : R.drawable.ic_paly_liste));
            holder.mIndicatorView.setVisibility(model.isPlaying() ?
                    View.VISIBLE : View.GONE);
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
            holder.itemView.setOnClickListener(view ->
                    mPresenter.openDetails(model.getStationID())
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