package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;
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
public class VideoPagerFragment extends Fragment implements VideosDetailContract.View {

    private MediaResultModel mMedia;
    @BindView(R.id.tv_media_desc)
    TextView mMediaDescTextView;

    @BindView(R.id.iv_play)
    ImageView mMediaPlayButton;

    @BindView(R.id.iv_media_vid)
    ImageView mMediaVideoImageView;

    Fragment mediaVideoFragment;

    Picasso mPicasso;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerSupportFragment mYouTubePlayerFragment;

    public VideoPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        // Inflate the layout for this mediaVideoFragment
        View view = inflater.inflate(R.layout.fragment_video_pager, container, false);
        ButterKnife.bind(this, view);
        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        mPicasso.load(mMedia.getVideoThumb()
                .replace("http://108.179.204.213:8093/UploadedImages/", ""))
                .into(mMediaVideoImageView);
        mMediaDescTextView.setText(mMedia.getDescription());
        return view;
    }

    @OnClick(R.id.iv_media_vid)
    void play() {
        if (mMedia.getAttachmentURL().contains("www.youtube.com/watch?v=")) {
            mediaVideoFragment = new YoutubeFragment();
            Bundle args = new Bundle();
            args.putString("video_url", extractYoutubeId(mMedia.getAttachmentURL()));
            mediaVideoFragment.setArguments(args);
        } else {
            mediaVideoFragment = new VideoPlayerFragment();
            Bundle args = new Bundle();
            args.putString("video_url", mMedia.getAttachmentURL());
            mediaVideoFragment.setArguments(args);
        }
        FragmentManager manager = getChildFragmentManager();
        manager.beginTransaction()
                .replace(R.id.iv_media_player, mediaVideoFragment, "youtube_Player")
                .addToBackStack(null)
                .commit();
    }

    public String extractYoutubeId(String url) {
        try {
            String query = new URL(url).getQuery();
            if (query == null || query.isEmpty())
                return "";
            String[] param = query.split("&");
            String id = null;
            for (String row : param) {
                String[] param1 = row.split("=");
                if (param1[0].equals("v")) {
                    id = param1[1];
                }
            }
            return id;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void playVideo(String url) {

    }
}
