package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.images.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment {

    @BindView(R.id.iv_media_pic)
    ImageView mMediaBackImageView;
    @BindView(R.id.tv_media_desc)
    TextView mMediaDescTextView;

    private MediaResultModel mMedia;

    public ImagePagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMedia = getArguments().getParcelable("media");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_pager, container, false);
        ButterKnife.bind(this, view);
        Picasso picasso = MyApplication.get(getContext()).getPicasso();
        picasso.load(mMedia.getAttachmentURL()).into(mMediaBackImageView);
        mMediaDescTextView.setText(mMedia.getDescription());
        return view;
    }
}
