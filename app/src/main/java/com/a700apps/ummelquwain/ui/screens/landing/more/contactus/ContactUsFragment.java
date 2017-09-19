package com.a700apps.ummelquwain.ui.screens.landing.more.contactus;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsResultModel;
import com.a700apps.ummelquwain.utilities.SwipeToDismissHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements ContactUsContract.View, LifecycleRegistryOwner {


    public ContactUsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_phone)
    TextView mPhoneTextView;

    @BindView(R.id.tv_fax)
    TextView mFaxTextView;

    @BindView(R.id.tv_location)
    TextView mLocationTextView;

    @BindView(R.id.tv_website)
    TextView mWebsiteTextView;

    @BindView(R.id.iv_facebook)
    ImageView mFacebookButton;

    @BindView(R.id.iv_twitter)
    ImageView mTwitterButton;

    @BindView(R.id.iv_insta)
    ImageView mInstaButton;

    @BindView(R.id.iv_linked_in)
    ImageView mLinkedInButton;

    @BindView(R.id.tv_mail)
    TextView mMailTextView;

    GestureDetector mGesture;
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    ContactUsPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        setupView(view);
        return view;
    }

    void setupView(View view) {
        mPresenter = new ContactUsPresenter(getContext(), this, getLifecycle());
        ButterKnife.bind(this, view);
//        mBackToolbarBtn.setOnClickListener(this);
        mGesture = new GestureDetector(getActivity(),
                new SwipeToDismissHelper(getFragmentManager()));
        mLocationTextView.setPaintFlags(mLocationTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        view.setOnTouchListener((v, event) -> mGesture.onTouchEvent(event));
    }

    @OnClick({R.id.iv_toolbar_back, R.id.iv_facebook, R.id.iv_twitter, R.id.iv_insta,
            R.id.iv_linked_in, R.id.tv_location})
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.iv_facebook:
                mPresenter.showFacebook();
                break;
            case R.id.iv_twitter:
                mPresenter.showTwitter();
                break;
            case R.id.iv_linked_in:
                mPresenter.showLinkedIn();
                break;
            case R.id.iv_insta:
                mPresenter.showInstagram();
                break;
            case R.id.tv_location:
                mPresenter.openMap();
                break;
        }
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
    public void updateUI(ContactUsResultModel mModel) {
        mPhoneTextView.setText(mModel.getPhoneNumber());
        mFaxTextView.setText(mModel.getFax());
        mWebsiteTextView.setText(mModel.getWebsiteLink());
        mMailTextView.setText(mModel.getEMail());
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
