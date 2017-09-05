package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.SponsorAdapter;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorResultModel;
import com.a700apps.ummelquwain.models.response.Station.StationResultModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavFragment extends Fragment implements FavContract.View, LifecycleRegistryOwner{

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recycler_spenser)
    RecyclerView mSpenserRecycler;

    private SponsorAdapter mSponsorAdapter;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public FavFragment() {
        // Required empty public constructor
    }

    public static FavFragment newInstance() {
        return new FavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FavPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        ButterKnife.bind(this, view);

        RecyclerView StationRecycler = view.findViewById(R.id.recycler_stations);
        StationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        StationRecycler.setAdapter(new StationAdapter());

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
    public void updateFavUI(List<StationResultModel> models) {

    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

        private ArrayList<String> list = new ArrayList<>();

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
            }
        }

        StationAdapter() {
        }

        public StationAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_station, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.title.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return 10;
//            return list.size();
        }
    }
}
