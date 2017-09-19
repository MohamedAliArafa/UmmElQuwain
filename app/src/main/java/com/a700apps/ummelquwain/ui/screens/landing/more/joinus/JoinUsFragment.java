package com.a700apps.ummelquwain.ui.screens.landing.more.joinus;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        mProvider = new JoinUsProvider(getContext(), this, getFragmentManager());
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
                    mNameEditText.setBackgroundResource(R.drawable.bg_trans_holo_red);
                    nameBool = false;
                } else {
                    requestModel.setName(mNameEditText.getText().toString());
                    mNameEditText.setBackgroundResource(R.drawable.bg_trans_holo_white);
                    nameBool = true;
                }

                if (mEmailEditText.getText().toString().isEmpty()) {
                    mEmailEditText.setBackgroundResource(R.drawable.bg_trans_holo_red);
                    emailBool = false;
                } else {
                    if (isEmailValid(mEmailEditText.getText().toString())) {
                        requestModel.setEmail(mEmailEditText.getText().toString());
                        mEmailEditText.setBackgroundResource(R.drawable.bg_trans_holo_white);
                        emailBool = true;
                    } else {
                        mEmailEditText.setBackgroundResource(R.drawable.bg_trans_holo_red);
                        emailBool = false;
                    }
                }

                if (mPhoneEditText.getText().toString().isEmpty()) {
                    mPhoneEditText.setBackgroundResource(R.drawable.bg_trans_holo_red);
                    phoneBool = false;
                } else {
                    requestModel.setPhone(mPhoneEditText.getText().toString());
                    mPhoneEditText.setBackgroundResource(R.drawable.bg_trans_holo_white);
                    phoneBool = true;
                }

                if (mAddressEditText.getText().toString().isEmpty()) {
                    mAddressEditText.setBackgroundResource(R.drawable.bg_trans_holo_red);
                    addressBool = false;
                } else {
                    requestModel.setAddress(mAddressEditText.getText().toString());
                    mAddressEditText.setBackgroundResource(R.drawable.bg_trans_holo_white);
                    addressBool = true;
                }

                if (mFile == null || mFile.isEmpty()) {
                    Toast.makeText(getContext(), "Please Upload File", Toast.LENGTH_SHORT).show();
                    mAttachBtn.setBackgroundResource(R.drawable.bg_trans_holo_red);
                    fileBool = false;
                } else {
                    requestModel.setAttachment(mFile);
                    mAttachBtn.setBackgroundResource(R.drawable.bg_trans_holo_white);
                    fileBool = true;
                }

                if (nameBool && emailBool && phoneBool && addressBool && fileBool)
                    mProvider.join(requestModel);
                break;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_AUDIO) {
            try {
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    try (Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();

                }
                mAttachBtn.setText(displayName);
                mProvider.uploadFile(data, fileName -> {
                    mFile = fileName;
                    return mFile;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
