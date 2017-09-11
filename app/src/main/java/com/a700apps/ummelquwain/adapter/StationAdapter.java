package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

    private List<StationResultModel> mList = new ArrayList<>();
    private int mLayout;
    private StationsContract.UserAction mPresenter;
    private Picasso mPicasso;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_station_name)
        TextView mTitleTextView;

        @BindView(R.id.tv_station_category)
        TextView mCategoryTextView;

        @BindView(R.id.tv_program_desc)
        TextView mProgramTextView;

        @BindView(R.id.iv_station_logo)
        ImageView mThumpImageView;

        @BindView(R.id.iv_play)
        ImageView mPlayImageView;

        @BindView(R.id.iv_like)
        ImageView mLikeImageView;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public StationAdapter(Context context, ArrayList<StationResultModel> list, int layout, StationsContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
        mContext = context;
        mPicasso = ((MyApplication) context.getApplicationContext()).getPicasso();
    }

    public void updateData(List<StationResultModel> list) {
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
        StationResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getStationName());
        holder.mCategoryTextView.setText(model.getCategoryName());
        holder.mProgramTextView.setText(model.getCurrentProgramName());
        mPicasso.load(model.getStationLogo()).into(holder.mThumpImageView);
        holder.mLikeImageView.setImageDrawable(mContext.getResources()
                .getDrawable(model.getIsFavourite() == 1 ?
                        R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
        holder.mLikeImageView.setOnClickListener(view -> {
            String userID = ((MyApplication) mContext.getApplicationContext()).getUser();
            if (!userID.equals("-1")) {
                holder.mLikeImageView.setImageDrawable(mContext.getResources()
                        .getDrawable(model.getIsFavourite() == 1 ?
                                R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
                mPresenter.setFav(model.getStationID(), model.getIsFavourite() == 1 ? 0 : 1);
            } else
                mPresenter.openLogin();
        });
        holder.itemView.setOnClickListener(view ->
                mPresenter.openDetails(model.getStationID())
        );
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
