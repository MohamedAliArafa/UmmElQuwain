package com.a700apps.ummelquwain.ui.screens.landing.stations;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements StationContract.View{


    public StationFragment() {
        // Required empty public constructor
    }

    public static StationFragment newInstance() {
        return new StationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        RecyclerView rv = view.findViewById(R.id.recycler_stations);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new Adapter());
        return view;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        private ArrayList<String> list = new ArrayList<>();

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
            }
        }

        Adapter() {
        }

        public Adapter(ArrayList<String> list) {
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
            return 15;
//            return list.size();
        }
    }

}
