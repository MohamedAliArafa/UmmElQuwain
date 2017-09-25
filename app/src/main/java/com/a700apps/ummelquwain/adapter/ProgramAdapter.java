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
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.programs.ProgramsContract;
import com.squareup.picasso.Picasso;

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
    private Picasso mPicasso;

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
        mPicasso = ((MyApplication) context.getApplicationContext()).getPicasso();
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
        mPicasso.load(model.getProgramLogo()).into(holder.mThumpImageView);
        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(model.getProgramID()));
        holder.mPlayImageView.setImageDrawable(mContext.getResources()
                .getDrawable(model.isPlaying() ?
                        R.drawable.ic_puss : R.drawable.ic_paly_liste));
        holder.mIndicatorView.setVisibility(model.isPlaying() ?
                View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(view ->
                mPresenter.openDetails(model.getProgramID())
        );
        holder.mPlayImageView.setOnClickListener(view -> {
                    mPresenter.playStream(model);
                }
        );
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
