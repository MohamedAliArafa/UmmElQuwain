package com.a700apps.ummelquwain.ui.screens.landing;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;
import com.a700apps.ummelquwain.ui.screens.landing.favorite.FavFragment;
import com.a700apps.ummelquwain.ui.screens.landing.media.AlbumsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.MoreFragment;
import com.a700apps.ummelquwain.ui.screens.landing.programs.ProgramsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.stations.StationFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LandingFragment extends Fragment implements LandingContract.View, LifecycleRegistryOwner {

    @BindView(R.id.tl_landing)
    TabLayout mTabLayout;
    @BindView(R.id.vp_landing)
    ViewPager mViewPager;

    private LandingProvider mProvider;

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_stations, R.string.title_favs, R.string.title_programs,
                    R.string.title_media, R.string.title_more);

    List<Integer> supplierIcons =
            Arrays.asList(R.drawable.ic_stations, R.drawable.ic_fav, R.drawable.ic_program,
                    R.drawable.ic_media, R.drawable.ic_more);

    List<Fragment> supplierFragments =
            Arrays.asList(StationFragment.newInstance(), FavFragment.newInstance(),
                    ProgramsFragment.newInstance(), AlbumsFragment.newInstance(),
                    MoreFragment.newInstance());

    public LandingFragment() {
        // Required empty public constructor
    }

    public static LandingFragment newInstance() {
        return new LandingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProvider = new LandingProvider(this, getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < supplierFragments.size(); i++)
            adapter.addFragment(supplierFragments.get(i), getString(supplierNames.get(i)));
        mViewPager.setAdapter(adapter);
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
