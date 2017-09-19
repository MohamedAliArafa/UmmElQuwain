package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.favorite.FavFragment;
import com.a700apps.ummelquwain.ui.screens.landing.media.AlbumsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.MoreFragment;
import com.a700apps.ummelquwain.ui.screens.landing.programs.ProgramsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationsFragment;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LandingFragment extends Fragment implements LandingContract.View, LifecycleRegistryOwner {

    @BindView(R.id.tl_landing)
    TabLayout mTabLayout;
    @BindView(R.id.vp_landing)
    ViewPager mViewPager;
    @BindView(R.id.iv_play)
    ImageView mPlayImageView;
    @BindView(R.id.cl_container)
    ConstraintLayout mConstrainContainer;

    @BindView(R.id.iv_player_bg)
    ImageView mPlayerImageView;

    private LandingProvider mProvider;
    private final String POSITION_KEY = "position";

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
    private int mPosition = 0;

    public LandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    public static LandingFragment newInstance() {
        return new LandingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mProvider = new LandingProvider(this, getLifecycle());
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
//        mPlayImageView.setOnClickListener(view1 -> MyApplication.get(LandingFragment.this.getContext()).startService());
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

    private class TouchListener implements View.OnTouchListener {

        View slidingView;

        int initHeight;
        float initPos;
        private ConstraintLayout.LayoutParams params;
        int parentHeight;

        private TouchListener(ConstraintLayout slidingView) {
            this.slidingView = slidingView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (params == null) {
                params = (ConstraintLayout.LayoutParams) slidingView.getLayoutParams();
                parentHeight = slidingView.getHeight();
            }

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: //get initial state
                    initHeight = slidingView.getHeight();
                    initPos = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE: //do the sliding
                    float dPos = initPos - event.getRawY();
                    if ((initHeight + dPos) > parentHeight)
                        params.height = parentHeight;
                    else if ((initHeight + dPos) < parentHeight / 2)
                        params.height = Math.round( parentHeight / 2);
                    else
                        params.height = Math.round(initHeight + dPos);
                    slidingView.requestLayout(); //refresh layout
                    break;
            }

            return false;
        }
    }

}
