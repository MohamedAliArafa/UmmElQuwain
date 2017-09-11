package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Station.Schedule.ScheduleModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class ScheduleNestedAdapter extends RecyclerView.Adapter<ScheduleNestedAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ScheduleModel> mList = new ArrayList<>();
    private int mLayout;
    Picasso mPicasso;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_schedule_sub)
        RecyclerView mRecycler;

        @BindView(R.id.tv_date_title)
        TextView mDateTitle;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ScheduleNestedAdapter(Context context, List<ScheduleModel> list, int layout) {
        mContext = context;
        for (ScheduleModel scheduleModel: list)
            if (!scheduleModel.getSchedule().isEmpty())
                mList.add(scheduleModel);
        mLayout = layout;
        mPicasso = ((MyApplication) context.getApplicationContext()).getPicasso();
    }

    public void updateData(List<ScheduleModel> list) {
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
        ScheduleModel model = mList.get(position);
        StationScheduleAdapter adapter = new StationScheduleAdapter(model.getSchedule(), R.layout.list_item_schedule);
        holder.mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mRecycler.setAdapter(adapter);
        holder.mDateTitle.setText(model.getDayName());

//        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(model.getStationID()));
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
