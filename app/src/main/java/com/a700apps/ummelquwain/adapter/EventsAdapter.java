package com.a700apps.ummelquwain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.dagger.Application.module.GlideApp;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.EventsContract;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamed.arafa on 8/28/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private final Context mContext;
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

        @BindView(R.id.btn_event_share)
        FrameLayout mEventShareButton;

        @BindView(R.id.btn_event_calender)
        FrameLayout mEventCalenderButton;

        @BindView(R.id.iv_event_image)
        ImageView mEventImageView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public EventsAdapter(Context context, ArrayList<EventResultModel> list, int layout, EventsContract.UserAction presenter) {
        mContext = context;
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
        GlideApp.with(mContext)
                .load(model.getEventImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .timeout(0)
                .into(holder.mEventImageView);
        holder.itemView.setOnClickListener(view -> mPresenter.openDetails(model));
        holder.mEventShareButton.setOnClickListener(view -> mPresenter.shareEvent(model));
        holder.mEventCalenderButton.setOnClickListener(view -> mPresenter.addToCalender(model));
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
