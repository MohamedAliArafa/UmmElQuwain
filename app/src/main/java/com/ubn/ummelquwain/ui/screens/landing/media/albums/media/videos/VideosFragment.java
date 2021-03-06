package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.adapter.AlbumMediaAdapter;
import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;
import com.ubn.ummelquwain.ui.screens.landing.media.albums.media.MediaContract;
import com.ubn.ummelquwain.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment implements MediaContract.ModelView {

    MediaContract.UserAction mPresenter;

    @BindView(R.id.tv_album_desc)
    TextView mAlbumDescTextView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    AlbumMediaAdapter mAdapter;
    private static int mAlbumID;
    private static String mAlbumDesc;

    public VideosFragment() {
        // Required empty public constructor
    }

    public static VideosFragment newInstance(int albumID, String albumDesc) {
        mAlbumDesc = albumDesc;
        mAlbumID = albumID;
        return new VideosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_videos, container, false);
        ButterKnife.bind(this, view);
        mRecycler = view.findViewById(R.id.recycler);
        int spanCount = Utility.calculateNoOfColumns(getContext(), 100);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mPresenter = new VideosPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle(), mAlbumID, 2);
        mAdapter = new AlbumMediaAdapter(getContext(), null, R.layout.list_item_album, mPresenter);
        mRecycler.setAdapter(mAdapter);
        mAlbumDescTextView.setText(mAlbumDesc);
        return view;
    }

    @Override
    public void updateUI(List<MediaResultModel> model) {
        mAdapter.updateData(model);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
