package com.a700apps.ummelquwain.ui.screens.landing.favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;

import java.util.ArrayList;

public class FavFragment extends Fragment {

    public FavFragment() {
        // Required empty public constructor
    }

    public static FavFragment newInstance() {
        return new FavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        RecyclerView StationRecycler = view.findViewById(R.id.recycler_stations);
        StationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        StationRecycler.setAdapter(new StationAdapter());

        RecyclerView SpenserRecycler = view.findViewById(R.id.recycler_spenser);
        SpenserRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SpenserRecycler.setAdapter(new SpenserAdapter());

        return view;
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

    class SpenserAdapter extends RecyclerView.Adapter<SpenserAdapter.MyViewHolder> {

        private ArrayList<String> list = new ArrayList<>();

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
            }
        }

        SpenserAdapter() {
        }

        public SpenserAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sponser, parent, false);
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
