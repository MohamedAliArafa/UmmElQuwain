package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.news.NewsContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsBarResultModel> mList = new ArrayList<>();
    private int mLayout;
    private Context mContext;
    private NewsContract.UserAction mPresenter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_title)
        TextView mTitleTextView;

        @BindView(R.id.tv_news_date)
        TextView mDateTextView;

        @BindView(R.id.tv_news_time)
        TextView mTimeTextView;

        @BindView(R.id.tv_news_desc)
        TextView mDescTextView;

        @BindView(R.id.tv_news_see_more)
        TextView mSeeMoreBtn;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public NewsAdapter(Context context, ArrayList<NewsBarResultModel> list, int layout, NewsContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mContext = context;
        mPresenter = presenter;
    }

    public void updateData(List<NewsBarResultModel> list) {
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
        NewsBarResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getNewsTitle());
        holder.mDateTextView.setText(model.getNewsDate());
        holder.mTimeTextView.setText(model.getNewsTime());
        holder.mDescTextView.setText(model.getNewsDescription());
        holder.mSeeMoreBtn.setOnClickListener(view -> {
            mPresenter.openDetails(model.getNewsID());
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
