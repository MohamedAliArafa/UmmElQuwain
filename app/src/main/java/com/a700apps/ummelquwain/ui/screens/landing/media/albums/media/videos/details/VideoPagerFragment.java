package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a700apps.ummelquwain.BuildConfig;
import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPagerFragment extends Fragment implements VideosDetailContract.View, YouTubePlayer.OnInitializedListener {

    private MediaResultModel mMedia;
    @BindView(R.id.tv_media_desc)
    TextView mMediaDescTextView;

    @BindView(R.id.iv_play)
    ImageView mMediaPlayButton;

    @BindView(R.id.iv_media_vid)
    ImageView mMediaVideoImageView;

    Picasso mPicasso;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerSupportFragment mYouTubePlayerFragment;

    public VideoPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMedia = getArguments().getParcelable("media");
        mPicasso = MyApplication.get(getContext()).getPicasso();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_pager, container, false);
        ButterKnife.bind(this, view);
        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        mPicasso.load(mMedia.getVideoThumb()
                .replace("http://108.179.204.213:8093/UploadedImages/", ""))
                .into(mMediaVideoImageView);
        mMediaDescTextView.setText(mMedia.getDescription());
        mMediaPlayButton.setVisibility(View.GONE);
        mMediaVideoImageView.setVisibility(View.GONE);
        mYouTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);

        return view;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("nCgQDjiotG0");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(getContext(), errorReason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.iv_media_vid)
    void play(){
//        String url = mMedia.getAttachmentURL()
//                .replace("http://108.179.204.213:8093/UploadedImages/", "");
//        getActivity().startActivity(new Intent(getActivity(), YouTubeActivity.class));
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        mMediaPlayButton.setVisibility(View.GONE);
        mMediaVideoImageView.setVisibility(View.GONE);
        mYouTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);

    }

    public String extractYoutubeId(String url) {
        try {
            String query = new URL(url).getQuery();
            String[] param = query.split("&");
            String id = null;
            for (String row : param) {
                String[] param1 = row.split("=");
                if (param1[0].equals("v")) {
                    id = param1[1];
                }
            }
            return id;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void playVideo(String url) {

    }
}
