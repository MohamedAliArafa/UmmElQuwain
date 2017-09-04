package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.images.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.vp_media)
    ViewPager mViewPager;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mPageIndicatorView;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_image, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        setupViewPager();
        return view;
    }

    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < 3; i++)
            adapter.addFragment(new ImagePagerFragment(), "Fragment" + i);
        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setInteractiveAnimation(true);
        mPageIndicatorView.setAnimationType(AnimationType.FILL);
        mViewPager.setAdapter(adapter);
    }

    public static ImageDetailsFragment newInstance(int mediaID) {
        return new ImageDetailsFragment();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }
}
