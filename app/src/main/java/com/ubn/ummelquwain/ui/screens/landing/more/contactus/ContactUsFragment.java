package com.ubn.ummelquwain.ui.screens.landing.more.contactus;


import android.Manifest;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.ContactUs.ContactUsResultModel;
import com.ubn.ummelquwain.utilities.SwipeToDismissHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ubn.ummelquwain.utilities.Constants.REQUEST_PHONE_CALL_PERMISSION;
import static com.ubn.ummelquwain.utilities.Utility.CheckPermission;
import static com.ubn.ummelquwain.utilities.Utility.RequestPermission;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements ContactUsContract.ModelView, LifecycleRegistryOwner {


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
        mPhoneTextView.setOnClickListener(view -> {
            if (mModel.getPhoneNumber() != null) {
                if (!CheckPermission(getContext(), Manifest.permission.CALL_PHONE)) {
                    RequestPermission(getActivity(), Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL_PERMISSION);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + (mModel.getPhoneNumber())));
                startActivity(intent);
            }
        });
        mFaxTextView.setText(mModel.getFax());
        mFaxTextView.setOnClickListener(view -> {
            if (mModel.getFax() != null) {
                if (!CheckPermission(getContext(), Manifest.permission.CALL_PHONE)) {
                    RequestPermission(getActivity(), Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL_PERMISSION);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + (mModel.getFax())));
                startActivity(intent);
            }
        });
        mWebsiteTextView.setText(mModel.getWebsiteLink());
        mWebsiteTextView.setOnClickListener(view -> {
            if (mModel.getWebsiteLink() != null) {
                String url;
                if (mModel.getWebsiteLink().contains("http"))
                    url = mModel.getWebsiteLink();
                else
                    url = "http://" + mModel.getWebsiteLink();
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(in);
            }
        });
        mMailTextView.setText(mModel.getEMail());
        mMailTextView.setOnClickListener(view -> {
            if (mModel.getEMail() != null) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mModel.getEMail()));
                startActivity(intent);
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
