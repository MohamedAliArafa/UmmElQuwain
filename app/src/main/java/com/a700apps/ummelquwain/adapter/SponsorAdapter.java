package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.MyViewHolder> {

    private List<SponsorResultModel> mList = new ArrayList<>();
    private int mLayout;
    private Context mContext;
    private Picasso mPicasso;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView mLogo;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SponsorAdapter(Context context, ArrayList<SponsorResultModel> list, int layout) {
        mList = list;
        mLayout = layout;
        mContext = context;
        mPicasso = MyApplication.get(mContext).getPicasso();
    }

    public void updateData(List<SponsorResultModel> list) {
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
        mPicasso.load(mList.get(position).getSponserImage()).into(holder.mLogo);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
