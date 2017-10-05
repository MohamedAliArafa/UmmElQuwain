package com.ubn.ummelquwain.ui.screens.landing.media.albums;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.ui.screens.landing.media.albums.media.images.ImagesFragment;
import com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos.VideosFragment;
import com.ubn.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment implements AlbumContract.ModelView, LifecycleRegistryOwner, View.OnClickListener {

    @BindView(R.id.tl_albums)
    TabLayout mTabLayout;
    @BindView(R.id.vp_albums)
    ViewPager mViewPager;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    AlbumPresenter mPresenter;

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_images, R.string.title_videos);

    private static List<Fragment> supplierFragments;

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AlbumPresenter(this, getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums_program, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
//        for (int i = 0; i < supplierFragments.size(); i++)
            adapter.addFragmentsResources(supplierFragments, supplierNames);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void setupTabLayout() {
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
        switch (viewId){
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }

    public static AlbumFragment newInstance(int albumID, String albumDesc) {
        supplierFragments = Arrays.asList(ImagesFragment.newInstance(albumID, albumDesc),
                VideosFragment.newInstance(albumID, albumDesc));
        return new AlbumFragment();
    }
}
