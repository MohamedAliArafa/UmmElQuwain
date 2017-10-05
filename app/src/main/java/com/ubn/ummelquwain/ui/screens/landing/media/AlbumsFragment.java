package com.ubn.ummelquwain.ui.screens.landing.media;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.adapter.AlbumsAdapter;
import com.ubn.ummelquwain.models.response.Albums.AlbumResultModel;
import com.ubn.ummelquwain.ui.screens.landing.LandingFragment;
import com.ubn.ummelquwain.utilities.ClickableEditText;
import com.ubn.ummelquwain.utilities.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment implements AlbumsContract.ModelView, View.OnClickListener {

    LandingFragment mLandingFragment;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.et_search)
    ClickableEditText mSearchEditText;

    AlbumsAdapter mAdapter;
    AlbumsPresenter mPresenter;

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AlbumsPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle());
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLandingFragment = (LandingFragment) getParentFragment();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        int spanCount = Utility.calculateNoOfColumns(getContext(), 100);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mAdapter = new AlbumsAdapter(getContext(), null, R.layout.list_item_album, mPresenter);
        mRecycler.setAdapter(mAdapter);
        setupSearch();
        return view;
    }

    void setupSearch(){
        mSearchEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_search), null);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    mPresenter.getData();
                else
                    mPresenter.search(editable.toString());
            }
        });
        mSearchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (!mSearchEditText.getText().toString().isEmpty()) {
                mPresenter.search(mSearchEditText.getText().toString());
                return true;
            }
            return false;
        });
        mSearchEditText.setDrawableClickListener(target -> {
            switch (target) {
                case RIGHT:
                    //Do something here
                    if (!mSearchEditText.getText().toString().isEmpty())
                        mPresenter.search(mSearchEditText.getText().toString());
                    break;

                default:
                    break;
            }
        });
    }

    @Override
    public void updateUI(List<AlbumResultModel> model) {
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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
        }
    }
}
