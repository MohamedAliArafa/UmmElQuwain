package com.a700apps.ummelquwain.ui.screens.landing.stations.details;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.info.StationInfoFragment;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.schedule.StationScheduleFragment;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;
import com.booking.rtlviewpager.RtlViewPager;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements StationContract.ModelView, LifecycleRegistryOwner, View.OnClickListener {

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

    private Picasso mPicasso;

    private Context mContext;

    StationPresenter mPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_info, R.string.title_schedule);
    private static List<Fragment> supplierFragments;

    private static int mStationID;
    private Realm mRealm;

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
        mPicasso = MyApplication.get(getContext()).getPicasso();
        mPresenter = new StationPresenter(getContext(), mStationID, getFragmentManager(), this, getLifecycle());
        mRealm = Realm.getDefaultInstance();
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
        mStationNameTextView.setText(model.getStationName());
        mStationCategoryTextView.setText(model.getCategoryName());
        mStationCurrentProgramIsLiveTextView.setText(model.getIsLive() ? mContext.getString(R.string.header_live) : mContext.getString(R.string.header_on));
        mStationCurrentProgramTextView.setText(model.getCurrentProgramName());
        mStationDescTextView.setText(model.getStationInfo());
        mPicasso.load(model.getStationLogo()).into(mStationLogoImageView);
        mPicasso.load(model.getStationImage()).into(mStationBackImageView);
        supplierFragments = Arrays.asList(StationInfoFragment.newInstance(model),
                StationScheduleFragment.newInstance(model));
        mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(model.isPlaying() ?
                R.drawable.ic_puss : R.drawable.ic_paly_liste));
        mPlayBtn.setOnClickListener(view -> mPresenter.playStream());
        mIndicatorView.setVisibility(model.isPlaying() ?
                View.VISIBLE : View.GONE);
        try {
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < supplierFragments.size(); i++)
            adapter.addFragment(supplierFragments.get(i), getString(supplierNames.get(i)));
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
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
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
