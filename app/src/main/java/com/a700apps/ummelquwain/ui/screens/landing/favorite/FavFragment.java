package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.FavStationAdapter;
import com.a700apps.ummelquwain.adapter.SponsorAdapter;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class FavFragment extends Fragment implements FavContract.ModelView{

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recycler_spenser)
    RecyclerView mSpenserRecycler;
    @BindView(R.id.recycler_stations)
    RecyclerView mStationRecycler;

    private SponsorAdapter mSponsorAdapter;
    private FavStationAdapter mStationAdapter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    FavPresenter mPresenter;

    public FavFragment() {
        // Required empty public constructor
    }

    public static FavFragment newInstance() {
        return new FavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FavPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        ButterKnife.bind(this, view);

        mStationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mStationAdapter = new FavStationAdapter(getContext(), null, mPresenter);
        mStationRecycler.setAdapter(mStationAdapter);

        mSpenserRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSponsorAdapter = new SponsorAdapter(getContext(), null, R.layout.list_item_sponser);
        mSpenserRecycler.setAdapter(mSponsorAdapter);

        return view;
    }

    @Override
    public void updateSponsorUI(List<SponsorResultModel> models) {
        mSponsorAdapter.updateData(models);
    }

    @Override
    public void updateFavUI(RealmResults<StationResultModel> models) {
        mStationAdapter.updateData(models);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

}
