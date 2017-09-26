package com.a700apps.ummelquwain.ui.screens.landing.more.news.details;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarResultModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends Fragment implements NewsDetailContract.ModelView, LifecycleRegistryOwner, View.OnClickListener {

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_news_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_news_date)
    TextView mDateTextView;

    @BindView(R.id.tv_news_time)
    TextView mTimeTextView;

    @BindView(R.id.tv_news_desc)
    TextView mDescTextView;

    NewsDetailPresenter mPresenter;
    Picasso mPicasso;

    @BindView(R.id.iv_back_image)
    ImageView mNewsImageView;

    @BindView(R.id.btn_share)
    Button mShareButton;


    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    static int mNewsID;

    public static NewsDetailsFragment newInstance(int newsId) {
        mNewsID = newsId;
        return new NewsDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicasso = ((MyApplication) getContext().getApplicationContext()).getPicasso();
        mPresenter = new NewsDetailPresenter(this, getContext(), getLifecycle(), mNewsID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_news, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void updateUI(NewsBarResultModel model) {
        mTitleTextView.setText(model.getNewsTitle());
        mTimeTextView.setText(model.getNewsTime());
        mDateTextView.setText(model.getNewsDate());
        mDescTextView.setText(model.getNewsDescription());
        mPicasso.load(model.getNewsImage()).into(mNewsImageView);
        mShareButton.setOnClickListener(view -> mPresenter.shareNews(model));
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
