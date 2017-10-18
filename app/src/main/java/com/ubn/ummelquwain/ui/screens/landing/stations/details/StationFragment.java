package com.ubn.ummelquwain.ui.screens.landing.stations.details;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.booking.rtlviewpager.RtlViewPager;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.ui.screens.landing.stations.details.info.StationInfoFragment;
import com.ubn.ummelquwain.ui.screens.landing.stations.details.schedule.StationScheduleFragment;
import com.ubn.ummelquwain.utilities.Constants;
import com.ubn.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements StationContract.ModelView, View.OnClickListener {

    @BindView(R.id.tl_details)
    TabLayout mTabLayout;
    @BindView(R.id.vp_details)
    RtlViewPager mViewPager;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;
    @BindView(R.id.iv_like)
    ImageView mLikeBtn;
    @BindView(R.id.iv_play)
    ImageView mPlayBtn;
    @BindView(R.id.view_list_indicator)
    View mIndicatorView;

    @BindView(R.id.iv_station_logo)
    ImageView mStationLogoImageView;
    @BindView(R.id.iv_station_image)
    ImageView mStationBackImageView;
    @BindView(R.id.tv_station_name)
    TextView mStationNameTextView;
    @BindView(R.id.tv_station_category)
    TextView mStationCategoryTextView;
    @BindView(R.id.tv_station_current_program)
    TextView mStationCurrentProgramTextView;
    @BindView(R.id.tv_station_current_program_Live_on)
    TextView mStationCurrentProgramIsLiveTextView;
    @BindView(R.id.tv_station_desc)
    TextView mStationDescTextView;

    private Context mContext;

    StationPresenter mPresenter;

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_info, R.string.title_schedule);
    private static List<Fragment> supplierFragments;

    private static int mStationID;
    private Realm mRealm;
    private RotateAnimation mRotateAnimation;

    public StationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static StationFragment newInstance(int stationID) {
        mStationID = stationID;
        return new StationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StationPresenter(getContext(), mStationID, getFragmentManager(), this, getLifecycle());
        mRealm = Realm.getDefaultInstance();
        mRotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(500);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void updateUI(StationResultModel model) {
        if (!isAdded()) return;
        mStationNameTextView.setText(model.getStationName());
        mStationCategoryTextView.setText(model.getCategoryName());
        mStationCurrentProgramTextView.setText(model.getCurrentProgramName());
        mStationDescTextView.setText(model.getStationInfo());
        supplierFragments = Arrays.asList(StationInfoFragment.newInstance(model),
                StationScheduleFragment.newInstance(model));
        mPlayBtn.setOnClickListener(view -> mPresenter.playStream());
        try {
            mStationCurrentProgramIsLiveTextView.setText(String.format("%s %s", mContext.getString(R.string.title_now), mContext.getString(R.string.title_current_program)));
            switch (model.isPlaying()) {
                case Paused:
                    mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    mIndicatorView.setVisibility(View.GONE);
                    mRotateAnimation.cancel();
                    break;
                case Playing:
                    mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    mIndicatorView.setVisibility(View.VISIBLE);
                    mRotateAnimation.cancel();
                    break;
                case Stopped:
                    mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    mIndicatorView.setVisibility(View.GONE);
                    mRotateAnimation.cancel();
                    break;
                case Buffering:
                    mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    mIndicatorView.setVisibility(View.VISIBLE);
                    mPlayBtn.startAnimation(mRotateAnimation);
                    break;
            }
            mLikeBtn.setImageDrawable(mContext.getResources()
                    .getDrawable(model.getIsFavourite() == 1 ?
                            R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
            mLikeBtn.setOnClickListener((View view) -> {
                mPresenter.setFav(model.getStationID(), model.getIsFavourite(), fav -> {
                    mRealm.beginTransaction();
                    model.setIsFavourite(fav);
                    mRealm.commitTransaction();
                    mLikeBtn.setImageDrawable(mContext.getResources()
                            .getDrawable(model.getIsFavourite() == 1 ?
                                    R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
                });
            });

            mStationLogoImageView.setTransitionName("stat_"+String.valueOf(model.getStationID()));

            GlideApp.with(this)
                    .load(model.getStationLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                    .fitCenter()
                    .into(mStationLogoImageView);

            GlideApp.with(this)
                    .load(model.getStationImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                    .fitCenter()
                    .into(mStationBackImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setupViewPager() {
        if (!isAdded()) return;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
//        for (int i = 0; i < supplierFragments.size(); i++)
        adapter.addFragmentsResources(supplierFragments, supplierNames);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void setupTabLayout() {
        if (!isAdded()) return;
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < supplierNames.size(); i++) {
            TextView tab = (TextView) LayoutInflater.from(getContext()).inflate(
                    R.layout.tab_item_album, null);
            tab.setText(getString(supplierNames.get(i)));
            tab.setTextColor(getResources().getColorStateList(R.color.tab_colors_list_bright));
            mTabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
