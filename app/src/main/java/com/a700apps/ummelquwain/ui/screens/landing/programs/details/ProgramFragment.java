package com.a700apps.ummelquwain.ui.screens.landing.programs.details;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
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
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.programs.details.commnts.ProgramCommentsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.programs.details.info.ProgramInfoFragment;
import com.a700apps.ummelquwain.utilities.ViewPagerAdapter;
import com.booking.rtlviewpager.RtlViewPager;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment implements ProgramContract.View, LifecycleRegistryOwner, View.OnClickListener{

    @BindView(R.id.tl_details)
    TabLayout mTabLayout;
    @BindView(R.id.vp_details)
    RtlViewPager mViewPager;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.iv_program_logo)
    ImageView mProgramLogoImageView;
    @BindView(R.id.iv_program_image)
    ImageView mProgramBackImageView;
    @BindView(R.id.tv_program_name)
    TextView mProgramNameTextView;
    @BindView(R.id.tv_program_category)
    TextView mProgramCategoryTextView;
    @BindView(R.id.tv_program_anchor_live)
    TextView mProgramAnchorLiveTextView;
    @BindView(R.id.tv_program_anchor)
    TextView mProgramAnchorTextView;

    Picasso mPicasso;

    ProgramPresenter mPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_comment, R.string.title_info);
    private static List<Fragment> supplierFragments;

    private static int mProgramID;

    public ProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicasso = MyApplication.get(getContext()).getPicasso();
        mPresenter = new ProgramPresenter(getContext(), mProgramID,this, getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        return view;
    }

    public static ProgramFragment newInstance(int programID) {
        mProgramID = programID;
        return new ProgramFragment();
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
        }
    }

    @Override
    public void updateUI(ProgramResultModel model) {
        mProgramNameTextView.setText(model.getProgramName());
        mProgramCategoryTextView.setText(model.getCategorName());
        mProgramAnchorTextView.setText(model.getBroadcasterName());
        mProgramAnchorLiveTextView.setText(model.getIsLiveAudio() ? getString(R.string.header_live) : getString(R.string.header_on));
        mPicasso.load(model.getProgramLogo()).into(mProgramLogoImageView);
        mPicasso.load(model.getProgramImage()).into(mProgramBackImageView);
        supplierFragments = Arrays.asList(ProgramCommentsFragment.newInstance(model),
                ProgramInfoFragment.newInstance(model));
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
}
