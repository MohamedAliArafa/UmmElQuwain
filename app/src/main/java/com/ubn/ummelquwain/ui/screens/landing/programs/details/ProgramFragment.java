package com.ubn.ummelquwain.ui.screens.landing.programs.details;


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

import com.booking.rtlviewpager.RtlViewPager;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.ui.screens.landing.programs.details.commnts.ProgramCommentsFragment;
import com.ubn.ummelquwain.ui.screens.landing.programs.details.info.ProgramInfoFragment;
import com.ubn.ummelquwain.utilities.Constants;
import com.ubn.ummelquwain.utilities.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment implements ProgramContract.ModelView, LifecycleRegistryOwner, View.OnClickListener {

    @BindView(R.id.tl_details)
    TabLayout mTabLayout;
    @BindView(R.id.vp_details)
    RtlViewPager mViewPager;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;
    @BindView(R.id.iv_play)
    ImageView mPlayBtn;
    @BindView(R.id.view_list_indicator)
    View mIndicatorView;
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

    ProgramPresenter mPresenter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    List<Integer> supplierNames =
            Arrays.asList(R.string.title_comment, R.string.title_info);
    private static List<Fragment> supplierFragments;

    private static int mProgramID;
    private Context mContext;

    public ProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgramPresenter(getContext(), mProgramID, this, getLifecycle());
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
        if (model.getIsLiveAudio() != null)
            mProgramAnchorLiveTextView.setText(model.getIsLiveAudio() ? mContext.getString(R.string.header_live) : mContext.getString(R.string.header_on));
        switch (model.isPlaying()) {
            case Paused:
                mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                mIndicatorView.setVisibility(View.GONE);
                break;
            case Playing:
                mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                mIndicatorView.setVisibility(View.VISIBLE);
                break;
            case Stopped:
                mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_paly_liste));
                mIndicatorView.setVisibility(View.GONE);
                break;
            case Buffering:
                mPlayBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_puss));
                mIndicatorView.setVisibility(View.VISIBLE);
                break;
        }
        mPlayBtn.setOnClickListener(view -> mPresenter.playStream());
        mProgramLogoImageView.setTransitionName("prog_"+String.valueOf(model.getProgramID()));
        if (isAdded())
            GlideApp.with(this)
                    .load(model.getProgramLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                    .fitCenter()
                    .into(mProgramLogoImageView);
        if (isAdded())
            GlideApp.with(this)
                    .load(model.getProgramImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0))
                    .fitCenter()
                    .into(mProgramBackImageView);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
//        for (int i = 0; i < supplierFragments.size(); i++)
        adapter.addFragmentsResources(supplierFragments, supplierNames);
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
