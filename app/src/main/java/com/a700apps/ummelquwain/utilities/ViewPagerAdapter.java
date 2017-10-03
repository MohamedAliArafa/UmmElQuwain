package com.a700apps.ummelquwain.utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed.arafa on 8/23/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private Context mContext;
    public ViewPagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFragments(List<Fragment> fragment, List<String> title) {
        mFragmentList = fragment;
        mFragmentTitleList = title;
        notifyDataSetChanged();
    }

    public void addFragmentsResources(List<Fragment> fragment, List<Integer> title) {
        mFragmentList = fragment;
        for (int i = 0; i < title.size(); i++)
            mFragmentTitleList.add(mContext.getString(title.get(i)));
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

