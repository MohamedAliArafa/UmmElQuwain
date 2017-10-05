package com.ubn.ummelquwain.ui.screens.player;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.utilities.TouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    @BindView(R.id.iv_play)
    ImageView mPlayImageView;
    @BindView(R.id.cl_container)
    ConstraintLayout mConstrainContainer;
    @BindView(R.id.iv_player_bg)
    ImageView mPlayerImageView;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
//        mPlayImageView.setOnClickListener(view1 -> MyApplication.get(LandingFragment.this.getContext()).startService());
        mPlayerImageView.setOnTouchListener(new TouchListener(mConstrainContainer));
        mPlayerImageView.setClickable(true);
        return view;
    }

}
