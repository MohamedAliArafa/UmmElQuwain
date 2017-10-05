package com.ubn.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubn.ummelquwain.R;
import com.ubn.ummelquwain.dagger.Application.module.GlideApp;
import com.ubn.ummelquwain.models.response.Albums.MediaResultModel;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPagerFragment extends Fragment implements VideosDetailContract.ModelView {

    private MediaResultModel mMedia;
    @BindView(R.id.tv_media_desc)
    TextView mMediaDescTextView;

    @BindView(R.id.iv_play)
    ImageView mMediaPlayButton;

    @BindView(R.id.iv_media_vid)
    ImageView mMediaVideoImageView;

    Fragment mediaVideoFragment;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this mediaVideoFragment
        View view = inflater.inflate(R.layout.fragment_video_pager, container, false);
        ButterKnife.bind(this, view);
        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        GlideApp.with(view)
                .load(mMedia.getVideoThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
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
