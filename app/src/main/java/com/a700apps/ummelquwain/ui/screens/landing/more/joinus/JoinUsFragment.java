package com.a700apps.ummelquwain.ui.screens.landing.more.joinus;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.JoinUsRequestModel;
import com.a700apps.ummelquwain.utilities.SwipeToDismissHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class JoinUsFragment extends Fragment implements View.OnClickListener, JoinUsContract.View {

    @BindView(R.id.iv_toolbar_back)
    ImageView mBackToolbarBtn;

    @BindView(R.id.btn_submit)
    Button mSubmitBtn;

    @BindView(R.id.et_upload)
    EditText mAttachBtn;

    @BindView(R.id.et_name)
    EditText mNameEditText;

    @BindView(R.id.et_email)
    EditText mEmailEditText;

    @BindView(R.id.et_phone)
    EditText mPhoneEditText;

    @BindView(R.id.et_address)
    EditText mAddressEditText;

    JoinUsRequestModel requestModel = new JoinUsRequestModel();

    private GestureDetector mGesture;
    private JoinUsProvider mProvider;
    private final int SELECT_AUDIO = 1;
    private String mFile;

    boolean nameBool, emailBool, phoneBool, addressBool, fileBool;

    public JoinUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_us, container, false);
        ButterKnife.bind(this, view);
        mBackToolbarBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mAttachBtn.setOnClickListener(this);
        mGesture = new GestureDetector(getActivity(),
                new SwipeToDismissHelper(getFragmentManager()));
        mProvider = new JoinUsProvider(getContext(), this);
        view.setOnTouchListener((v, event) -> mGesture.onTouchEvent(event));
        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_toolbar_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.et_upload:
                mFile = null;
                mAttachBtn.setText(getString(R.string.btn_upload_mp3_or_video));
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), SELECT_AUDIO);
                break;
            case R.id.btn_submit:
                if (mNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    nameBool = false;
                }
                else {
                    requestModel.setName(mNameEditText.getText().toString());
                    nameBool = true;
                }

                if (mEmailEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    emailBool = false;
                }
                else {
                    requestModel.setEmail(mEmailEditText.getText().toString());
                    emailBool = true;
                }

                if (mPhoneEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Your Phone", Toast.LENGTH_SHORT).show();
                    phoneBool = false;
                }
                else {
                    requestModel.setPhone(mPhoneEditText.getText().toString());
                    phoneBool = true;
                }

                if (mAddressEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                    addressBool = false;
                }
                else {
                    requestModel.setAddress(mAddressEditText.getText().toString());
                    addressBool = true;
                }

                if (mFile == null || mFile.isEmpty()) {
                    Toast.makeText(getContext(), "Please Upload File", Toast.LENGTH_SHORT).show();
                    fileBool = false;
                }
                else {
                    requestModel.setAttachment(mFile);
                    fileBool = true;
                }

                if (nameBool && emailBool && phoneBool && addressBool && fileBool)
                    mProvider.join(requestModel);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_AUDIO) {
            mAttachBtn.setText(data.getData().getPath());
            mFile = mProvider.uploadFile(data);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
