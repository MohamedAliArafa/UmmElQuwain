package com.a700apps.ummelquwain.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.Schedule.ScheduleDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mohamed.arafa on 8/28/2017.
 */

public class StationScheduleAdapter extends RecyclerView.Adapter<StationScheduleAdapter.MyViewHolder> {

    private List<ScheduleDetailModel> mList = new ArrayList<>();
    private int mLayout;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date_title)
        TextView mTimeTextView;

        @BindView(R.id.tv_show_title)
        TextView mProgramTextView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public StationScheduleAdapter(List<ScheduleDetailModel> list, int layout) {
        mList = list;
        mLayout = layout;
    }

    public void updateData(List<ScheduleDetailModel> list) {
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
        ScheduleDetailModel model = mList.get(position);
        holder.mTimeTextView.setText(model.getProgramTime());
        holder.mProgramTextView.setText(model.getProgramName());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
