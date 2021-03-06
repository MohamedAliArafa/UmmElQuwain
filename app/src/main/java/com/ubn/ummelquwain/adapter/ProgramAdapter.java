package com.ubn.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.ui.screens.landing.programs.ProgramsContract;
import com.ubn.ummelquwain.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/*
 * Created by mohamed.arafa on 8/28/2017.
 */

public class ProgramAdapter extends RealmRecyclerViewAdapter<ProgramResultModel, ProgramAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ProgramResultModel> mList = new ArrayList<>();
    private int mLayout;
    private ProgramsContract.UserAction mPresenter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_program_name)
        TextView mTitleTextView;

        @BindView(R.id.tv_program_category)
        TextView mCategoryTextView;

        @BindView(R.id.tv_program_desc)
        TextView mProgramTextView;

        @BindView(R.id.iv_play)
        ImageView mPlayImageView;

        @BindView(R.id.iv_program_logo)
        ImageView mThumpImageView;

        @BindView(R.id.view_list_indicator)
        View mIndicatorView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramAdapter(Context context, RealmResults<ProgramResultModel> list, int layout, ProgramsContract.UserAction presenter) {
        super(list, true);
        mContext = context;
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
    }

    public void updateData(RealmResults<ProgramResultModel> list) {
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
        ProgramResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getProgramName());
        holder.mCategoryTextView.setText(model.getCategorName());
        holder.mProgramTextView.setText(model.getProgramDescription());
        GlideApp.with(mContext)
                .load(model.getProgramLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                .fitCenter()
                .into(holder.mThumpImageView);
        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(model.getProgramID(), holder.mThumpImageView));
        Log.e("nmn",model.getIsLive()+"");
        switch (model.isPlaying()) {
            case Paused:
                if (model.getIsLive() == 1) {
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    holder.mIndicatorView.setVisibility(View.GONE);
                } else {
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_gray));
                    holder.mIndicatorView.setVisibility(View.GONE);
                }
                break;
            case Playing:
                holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                holder.mIndicatorView.setVisibility(View.VISIBLE);
                break;
            case Stopped:
               if (model.getIsLive() == 1) {
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    holder.mIndicatorView.setVisibility(View.GONE);
                } else {
                    holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_gray));
                    holder.mIndicatorView.setVisibility(View.GONE);
                }
                break;
            case Buffering:
                holder.mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                holder.mIndicatorView.setVisibility(View.VISIBLE);
                break;
        }
        holder.mThumpImageView.setTransitionName("prog_" + String.valueOf(model.getProgramID()));
        holder.itemView.setOnClickListener(view ->
                mPresenter.openDetails(model.getProgramID(), holder.mThumpImageView)
        );
//        holder.mPlayImageView.setOnClickListener(view -> mPresenter.playStream(model)
//        );
        holder.mPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getIsLive() == 1) {
                    mPresenter.playStream(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
