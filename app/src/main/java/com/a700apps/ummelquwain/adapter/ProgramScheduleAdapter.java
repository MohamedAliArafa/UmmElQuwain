package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.program.ProgramScheduleResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.stations.details.StationFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a700apps.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class ProgramScheduleAdapter extends RecyclerView.Adapter<ProgramScheduleAdapter.MyViewHolder> {

    private final Context mContext;
    private final Picasso mPicasso;
    private final FragmentManager mFragmentManager;
    private List<ProgramScheduleResultModel> mList = new ArrayList<>();
    private int mLayout;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_schedule_time)
        TextView mTimeTextView;

        @BindView(R.id.tv_schedule_station)
        TextView mStationTextView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramScheduleAdapter(Context context, List<ProgramScheduleResultModel> list, int layout, FragmentManager fragmentManager) {
        mContext = context;
        mList = list;
        mLayout = layout;
        mPicasso = ((MyApplication) mContext.getApplicationContext()).getPicasso();
        mFragmentManager = fragmentManager;
    }

    public void updateData(List<ProgramScheduleResultModel> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProgramScheduleResultModel model = mList.get(position);
        holder.mTimeTextView.setText(model.getProgramTime());
        holder.mStationTextView.setText(model.getStationName());
        holder.itemView.setOnClickListener(view -> {
            boolean fragmentPopped = mFragmentManager.popBackStackImmediate(PROGRAM_FRAGMENT_KEY + String.valueOf(model.getStationID()), 0);
            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.fragment_container, StationFragment.newInstance(model.getStationID()));
                ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(model.getStationID()));
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
