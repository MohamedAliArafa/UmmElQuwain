package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.images.details;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ubn.ummelquwain.MyApplication;
import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;

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
    Picasso mPicasso;

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
        mPicasso = MyApplication.get(getContext()).getPicasso();
        mPicasso.load(mMedia.getAttachmentURL()).into(mMediaBackImageView);
        mMediaDescTextView.setText(mMedia.getDescription());
        mMediaBackImageView.setOnClickListener(this::showPopup);
        return view;
    }

    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.image_popup_layout, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        ImageView iv = popupView.findViewById(R.id.iv_back_image);
        iv.setOnClickListener(view -> popupWindow.setBackgroundDrawable(new ColorDrawable()));

        mPicasso.load(mMedia.getAttachmentURL()).into(iv);
        // Initialize more widgets from `popup_layout.xml`

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }
}
