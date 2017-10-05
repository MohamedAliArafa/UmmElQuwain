package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.images.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;
import com.ubn.ummelquwain.utilities.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

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
//    @BindView(R.id.pageIndicatorView)
//    PageIndicatorView mPageIndicatorView;

    private static int mInitPosition;
    private static List<MediaResultModel> mMedia;

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitleList = new ArrayList<>();
        for (int i = 0; i < mMedia.size(); i++) {
            Bundle b = new Bundle();
            b.putParcelable("media", mMedia.get(i));
            ImagePagerFragment fragment = new ImagePagerFragment();
            fragment.setArguments(b);
            fragmentList.add(fragment);
            fragmentTitleList.add(String.valueOf(mMedia.get(i).getMediaID()));
        }
        adapter.addFragments(fragmentList, fragmentTitleList);

//        mPageIndicatorView.setViewPager(mViewPager);
//        mPageIndicatorView.setInteractiveAnimation(true);
//        mPageIndicatorView.setAnimationType(AnimationType.WORM);
//        mPageIndicatorView.setStrokeWidth(0.5f);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mInitPosition, true);
    }

    public static ImageDetailsFragment newInstance(List<MediaResultModel> media, int position) {
        mMedia = media;
        mInitPosition = position;
        return new ImageDetailsFragment();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }
}
