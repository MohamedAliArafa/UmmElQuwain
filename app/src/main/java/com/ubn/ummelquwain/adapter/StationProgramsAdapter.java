package com.ubn.ummelquwain.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.program.ProgramResultModel;
import com.ubn.ummelquwain.ui.screens.landing.stations.details.info.StationInfoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mohamed.arafa on 8/28/2017.
 */

public class StationProgramsAdapter extends RecyclerView.Adapter<StationProgramsAdapter.MyViewHolder> {

    private final StationInfoPresenter mPresenter;
    private List<ProgramResultModel> mList = new ArrayList<>();
    private int mLayout;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_program_name)
        TextView mNameTextView;

        @BindView(R.id.tv_program_category)
        TextView mCategoryTextView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public StationProgramsAdapter(List<ProgramResultModel> list, int layout, StationInfoPresenter presenter) {
        mList = list;
        mLayout = layout;
        mPresenter = presenter;
    }

    public void updateData(List<ProgramResultModel> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_station_program, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProgramResultModel model = mList.get(position);
        holder.mNameTextView.setText(model.getProgramName());
        holder.mCategoryTextView.setText(model.getProgramImage());
//        holder.itemView.setOnClickListener(view -> mPresenter.openProgram(model));
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }
}
