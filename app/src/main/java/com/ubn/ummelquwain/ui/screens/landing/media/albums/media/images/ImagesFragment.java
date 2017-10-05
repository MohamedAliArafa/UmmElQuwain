package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.images;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
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
public class ImagesFragment extends Fragment implements LifecycleRegistryOwner, MediaContract.ModelView {

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
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

    public ImagesFragment() {
        // Required empty public constructor
    }

    public static ImagesFragment newInstance(int albumID, String albumDesc) {
        mAlbumID = albumID;
        mAlbumDesc = albumDesc;
        return new ImagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_images, container, false);
        ButterKnife.bind(this, view);
        mRecycler = view.findViewById(R.id.recycler);
        int spanCount = Utility.calculateNoOfColumns(getContext(), 100);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mPresenter = new ImagesPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle(), mAlbumID, 1);
        mAdapter = new AlbumMediaAdapter(getContext(), null, R.layout.list_item_album, mPresenter);
        mAlbumDescTextView.setText(mAlbumDesc);
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
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
