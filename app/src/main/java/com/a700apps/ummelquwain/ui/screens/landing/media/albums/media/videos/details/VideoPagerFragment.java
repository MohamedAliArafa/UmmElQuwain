package com.a700apps.ummelquwain.ui.screens.landing.media.albums.media.videos.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a700apps.ummelquwain.BuildConfig;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.Albums.MediaResultModel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPagerFragment extends Fragment implements VideosDetailContract.View{

    private MediaResultModel mMedia;
    @BindView(R.id.tv_media_desc)
    TextView mMediaDescTextView;

    public VideoPagerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_video_pager, container, false);
        ButterKnife.bind(this, view);
        String url = mMedia.getAttachmentURL().replace("http://108.179.204.213:8093/UploadedImages/", "");
        String youtubeUrl = extractYoutubeId(url);
        YouTubePlayerSupportFragment youtubeFragment = (YouTubePlayerSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.iv_media_vid);
        String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_API_KEY;

        youtubeFragment.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(youtubeUrl);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
        mMediaDescTextView.setText(mMedia.getDescription());
        return view;
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
    public void playVideo(String url) {}
}
