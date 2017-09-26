package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.favorite.FavFragment;
import com.a700apps.ummelquwain.ui.screens.landing.media.AlbumsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.MoreFragment;
import com.a700apps.ummelquwain.ui.screens.landing.programs.ProgramsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsFragment;
import com.a700apps.ummelquwain.utilities.TouchListener;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.a700apps.ummelquwain.utilities.Constants.POSITION_KEY;


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
    List<Fragment> supplierFragments =
            Arrays.asList(StationsFragment.newInstance(), FavFragment.newInstance(),
                    ProgramsFragment.newInstance(), AlbumsFragment.newInstance(),
                    MoreFragment.newInstance());
    StationResultModel mStationModel;
    ProgramResultModel mProgramModel;
    boolean changed = false;
    private LandingProvider mProvider;
    private int mPosition = 0;
    private Realm mRealm;
    private Context mContext;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mProvider = new LandingProvider(this, getContext(), getLifecycle());
        mRealm = MyApplication.get(getContext()).getRealm();
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < supplierFragments.size(); i++)
            adapter.addFragment(supplierFragments.get(i), getString(supplierNames.get(i)));
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
        if (mStationModel != null)
            if (model.getStationID().equals(mStationModel.getStationID()))
                changed = true;
        mStationModel = model;
        if (isAdded())
            mStationModel.addChangeListener(realmModel -> {
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(mStationModel.isPlaying() ?
                        R.drawable.ic_puss : R.drawable.ic_paly_liste));
                mLikeImageView.setImageDrawable(mContext.getResources()
                        .getDrawable(mStationModel.getIsFavourite() == 1 ?
                                R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
                changed = true;
            });
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
        mPlayImageView.setOnClickListener(view -> {
                    mProvider.playStream(model);
                }
        );
        Animation anim = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);
        if (!changed)
            mConstrainContainer.setAnimation(anim);
        mConstrainContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void showPlayer(ProgramResultModel model) {
        changed = false;
        if (mProgramModel != null)
            if (model.getProgramID().equals(mProgramModel.getProgramID()))
                changed = true;
        mProgramModel = model;
        if (isAdded())
            mProgramModel.addChangeListener(realmModel -> {
                mPlayImageView.setImageDrawable(mContext.getResources().getDrawable(mProgramModel.isPlaying() ?
                        R.drawable.ic_puss : R.drawable.ic_paly_liste));
                mLikeImageView.setImageDrawable(mContext.getResources()
                        .getDrawable(mProgramModel.getIsFavourite() == 1 ?
                                R.drawable.ic_favorite_liste_active : R.drawable.ic_favorite_liste_unactive));
                changed = true;
            });
        mStationNameTextView.setText(mProgramModel.getProgramName());
        mProgramNameTextView.setText(mProgramModel.getBroadcasterName());
        mCommentImageView.setAlpha(1f);
        mLikeImageView.setAlpha(0.3f);
        mLikeImageView.setOnClickListener(view -> {
            mProvider.setFav(model.getStationID(), model.getIsFavourite(), fav -> {
                mRealm.beginTransaction();
                model.setIsFavourite(fav);
                mRealm.commitTransaction();
            });
        });
        mPlayImageView.setOnClickListener(view -> {
                    mProvider.playStream(model);
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
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
