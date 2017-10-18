package com.ubn.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.Station.StationResultModel;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.ui.screens.landing.favorite.FavFragment;
import com.ubn.ummelquwain.ui.screens.landing.media.AlbumsFragment;
import com.ubn.ummelquwain.ui.screens.landing.more.MoreFragment;
import com.ubn.ummelquwain.ui.screens.landing.programs.ProgramsFragment;
import com.ubn.ummelquwain.ui.screens.landing.stations.StationsFragment;
import com.ubn.ummelquwain.utilities.TouchListener;
import com.ubn.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.ubn.ummelquwain.utilities.Constants.LANDING_FRAGMENT_KEY;
import static com.ubn.ummelquwain.utilities.Constants.POSITION_KEY;


public class LandingFragment extends Fragment implements LandingContract.ModelView, LifecycleRegistryOwner {

    @BindView(R.id.tl_landing)
    TabLayout mTabLayout;
    @BindView(R.id.vp_landing)
    ViewPager mViewPager;
    @BindView(R.id.iv_play)
    ImageView mPlayImageView;
    @BindView(R.id.iv_fav)
    ImageView mLikeImageView;
    @BindView(R.id.iv_comment)
    ImageView mCommentImageView;
    @BindView(R.id.iv_share)
    ImageView mShareImageView;
    @BindView(R.id.tv_station_name)
    TextView mStationNameTextView;
    @BindView(R.id.tv_program_name)
    TextView mProgramNameTextView;
    @BindView(R.id.cl_container)
    ConstraintLayout mConstrainContainer;
    @BindView(R.id.iv_player_bg)
    ImageView mPlayerImageView;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    List<Integer> supplierNames =
            Arrays.asList(R.string.title_stations, R.string.title_favs, R.string.title_programs,
                    R.string.title_media, R.string.title_more);
    List<Integer> supplierIcons =
            Arrays.asList(R.drawable.ic_stations, R.drawable.ic_fav, R.drawable.ic_program,
                    R.drawable.ic_media, R.drawable.ic_more);
    List<Fragment> supplierFragments;
    StationResultModel mStationModel;
    ProgramResultModel mProgramModel;
    boolean changed = false;
    private LandingProvider mProvider;
    private int mPosition = 0;
    private Realm mRealm;
    private Context mContext;
    private RotateAnimation mRotateAnimation;

    public LandingFragment() {
        // Required empty public constructor
    }

    public static LandingFragment newInstance() {
        return new LandingFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (supplierFragments == null)
            supplierFragments = Arrays.asList(StationsFragment.newInstance(), FavFragment.newInstance(),
                    ProgramsFragment.newInstance(), AlbumsFragment.newInstance(),
                    MoreFragment.newInstance());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mProvider = new LandingProvider(this, getContext(), getLifecycle());
        mRealm = MyApplication.get(getContext()).getRealm();
        mRotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(500);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(POSITION_KEY, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(POSITION_KEY, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);
        mPlayerImageView.setOnTouchListener(new TouchListener(mConstrainContainer));
        mPlayerImageView.setClickable(true);
        return view;
    }

    @Override
    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
//        for (int i = 0; i < supplierFragments.size(); i++)
        adapter.addFragmentsResources(supplierFragments, supplierNames);
//            adapter.addFragment(supplierFragments.get(i), getString(supplierNames.get(i)));
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(mPosition, true);
    }

    @Override
    public void setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < supplierNames.size(); i++) {
            TextView tab = (TextView) LayoutInflater.from(getContext()).inflate(
                    R.layout.tab_item_landing, null);
            tab.setText(getString(supplierNames.get(i)));
            tab.setTextColor(getResources().getColorStateList(R.color.tab_colors_list));
            if (supplierIcons.size() > i)
                tab.setCompoundDrawablesWithIntrinsicBounds(0, supplierIcons.get(i), 0, 0);
            mTabLayout.getTabAt(i).setCustomView(tab);
        }

        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
        linearLayout.setGravity(Gravity.BOTTOM);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);
    }

    @Override
    public boolean moveToHome() {
        if (mViewPager.getCurrentItem() == 0)
            return true;
        mViewPager.setCurrentItem(0, true);
        return false;
    }

    @Override
    public void showPlayer(StationResultModel model) {
        changed = false;
        if (mProgramModel != null)
            mProgramModel.removeAllChangeListeners();
        if (mStationModel != null)
            if (model.getStationID() == mStationModel.getStationID())
                changed = true;
        mStationModel = model;

        if (isAdded()) {
            mStationModel.addChangeListener(realmModel -> {
                try {
                    switch (mStationModel.isPlaying()) {
                        case Paused:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                            mRotateAnimation.cancel();
                            break;
                        case Playing:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                            mRotateAnimation.cancel();
                            break;
                        case Stopped:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                            mRotateAnimation.cancel();
                            break;
                        case Buffering:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                            mPlayImageView.startAnimation(mRotateAnimation);
                            break;
                    }
                    mLikeImageView.setImageDrawable(mContext.getResources()
                            .getDrawable(mStationModel.getIsFavourite() == 1 ?
                                    R.drawable.ic_favorite_active : R.drawable.ic_favorite));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                changed = true;
            });
            switch (mStationModel.isPlaying()) {
                case Paused:
                    mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    mRotateAnimation.cancel();
                    break;
                case Playing:
                    mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    mRotateAnimation.cancel();
                    break;
                case Stopped:
                    mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                    mRotateAnimation.cancel();
                    break;
                case Buffering:
                    mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                    mPlayImageView.startAnimation(mRotateAnimation);
                    break;
            }
            mLikeImageView.setImageDrawable(mContext.getResources()
                    .getDrawable(mStationModel.getIsFavourite() == 1 ?
                            R.drawable.ic_favorite_active : R.drawable.ic_favorite));
        }
        mCommentImageView.setAlpha(0.3f);
        mLikeImageView.setAlpha(1f);
        mStationNameTextView.setText(mStationModel.getStationName());
        mProgramNameTextView.setText(mStationModel.getCurrentProgramName());
        mLikeImageView.setOnClickListener(view -> {
            mProvider.setFav(model.getStationID(), model.getIsFavourite(), fav -> {
                mRealm.beginTransaction();
                model.setIsFavourite(fav);
                mRealm.commitTransaction();
            });
        });
        mShareImageView.setOnClickListener(view -> {
            mProvider.shareFromPlayer(model);
        });
        mPlayImageView.setOnClickListener(view -> {
                    mProvider.playStream(model);
                }
        );
        if (mContext != null) {
            Animation anim = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            if (!changed)
                mConstrainContainer.setAnimation(anim);
        }
        mConstrainContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlayer(ProgramResultModel model) {
        changed = false;
        if (mStationModel != null)
            mStationModel.removeAllChangeListeners();
        if (mProgramModel != null)
            if (model.getProgramID() == mProgramModel.getProgramID())
                changed = true;
        mProgramModel = model;
        if (isAdded())
            mProgramModel.addChangeListener(realmModel -> {
                try {
                    switch (mProgramModel.isPlaying()) {
                        case Paused:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                            mRotateAnimation.cancel();
                            break;
                        case Playing:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                            mRotateAnimation.cancel();
                            break;
                        case Stopped:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                            mRotateAnimation.cancel();
                            break;
                        case Buffering:
                            mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                            mPlayImageView.startAnimation(mRotateAnimation);
                            break;
                    }
                    mLikeImageView.setImageDrawable(mContext.getResources()
                            .getDrawable(mProgramModel.getIsFavourite() == 1 ?
                                    R.drawable.ic_favorite_active : R.drawable.ic_favorite));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                changed = true;
            });

        switch (model.isPlaying()) {
            case Paused:
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                mRotateAnimation.cancel();
                break;
            case Playing:
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                mRotateAnimation.cancel();
                break;
            case Stopped:
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                mRotateAnimation.cancel();
                break;
            case Buffering:
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                mPlayImageView.startAnimation(mRotateAnimation);
                break;
        }
        mLikeImageView.setImageDrawable(mContext.getResources()
                .getDrawable(mProgramModel.getIsFavourite() == 1 ?
                        R.drawable.ic_favorite_active : R.drawable.ic_favorite));

        mStationNameTextView.setText(mProgramModel.getProgramName());
        mProgramNameTextView.setText(mProgramModel.getBroadcasterName());
        mCommentImageView.setAlpha(1f);
        mLikeImageView.setAlpha(0.3f);
        mLikeImageView.setOnClickListener(view -> mProvider.setFav(model.getStationID(), model.getIsFavourite(), fav -> {
            mRealm.beginTransaction();
            model.setIsFavourite(fav);
            mRealm.commitTransaction();
        }));
        mShareImageView.setOnClickListener(view -> {
            mProvider.shareFromPlayer(model);
        });
        mPlayImageView.setOnClickListener(view -> {
                    mProvider.playStream(model);
                }
        );
        mCommentImageView.setOnClickListener(view -> {
                    mProvider.addComment(model);
                }
        );
        Animation anim = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);
        if (!changed)
            mConstrainContainer.setAnimation(anim);
        mConstrainContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlayer() {
        Animation anim = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down);
        mConstrainContainer.setAnimation(anim);
        mConstrainContainer.setVisibility(View.GONE);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    public void startFragmentFromChild(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container, fragment, LANDING_FRAGMENT_KEY);
        fragmentTransaction.addToBackStack(LANDING_FRAGMENT_KEY);
        fragmentTransaction.commit();
    }
}
