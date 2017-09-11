package com.a700apps.ummelquwain.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.EventsContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private List<EventResultModel> mList = new ArrayList<>();
    private int mLayout;
    private EventsContract.UserAction mPresenter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_event_title)
        TextView mTitleTextView;

        @BindView(R.id.tv_event_start_date)
        TextView mStartDateTextView;

        @BindView(R.id.tv_event_end_date)
        TextView mEndDateTextView;

        @BindView(R.id.tv_event_location)
        TextView mLocationTextView;

        @BindView(R.id.tv_event_desc)
        TextView mDescTextView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public EventsAdapter(ArrayList<EventResultModel> list, int layout, EventsContract.UserAction presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
    }

    public void updateData(List<EventResultModel> list) {
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
        EventResultModel model = mList.get(position);
        holder.mTitleTextView.setText(model.getEventName());
        holder.mStartDateTextView.setText(model.getEventStartDate());
        holder.mEndDateTextView.setText(model.getEventEndDate());
        holder.mLocationTextView.setText(model.getEventPlace());
        holder.mDescTextView.setText(model.getEventDescription());
        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(model));
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
