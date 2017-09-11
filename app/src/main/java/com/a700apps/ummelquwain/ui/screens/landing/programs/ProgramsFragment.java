package com.a700apps.ummelquwain.ui.screens.landing.programs;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.adapter.ProgramAdapter;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.utilities.ClickableEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsFragment extends Fragment implements ProgramsContract.View, LifecycleRegistryOwner{

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    @BindView(R.id.recycler_programs)
    RecyclerView mRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.et_search)
    ClickableEditText mSearchEditText;

    ProgramAdapter mAdapter;
    ProgramsPresenter mPresenter;

    public ProgramsFragment() {
        // Required empty public constructor
    }

    public static ProgramsFragment newInstance() {
        return new ProgramsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgramsPresenter(getContext(), this, getParentFragment().getFragmentManager(), getLifecycle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programs, container, false);
        ButterKnife.bind(this, view);
        mRecycler = view.findViewById(R.id.recycler_programs);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProgramAdapter(getContext(), null, R.layout.list_item_program, mPresenter);
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
            }
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
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void updateUI(List<ProgramResultModel> models) {
        mAdapter.updateData(models);
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
