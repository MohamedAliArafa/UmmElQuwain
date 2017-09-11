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

/*
 * Created by mohamed.arafa on 8/28/2017.
 */

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.MyViewHolder> {

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

        @BindView(R.id.iv_program_logo)
        ImageView mThumpImageView;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramAdapter(Context context, ArrayList<ProgramResultModel> list, int layout, ProgramsContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
        mPicasso = ((MyApplication) context.getApplicationContext()).getPicasso();
    }

    public void updateData(List<ProgramResultModel> list) {
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
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
