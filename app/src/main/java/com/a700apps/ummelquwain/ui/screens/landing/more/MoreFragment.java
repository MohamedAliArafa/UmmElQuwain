package com.a700apps.ummelquwain.ui.screens.landing.more;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.ui.screens.landing.more.aboutus.AboutUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.contactus.ContactUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.EventsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.joinus.JoinUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.news.NewsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.sponsors.SponsorsFragment;
import com.a700apps.ummelquwain.utilities.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {


    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout LanguageBtn = view.findViewById(R.id.ll_language);
        LinearLayout eventsBtn = view.findViewById(R.id.ll_events);
        LinearLayout newsBtn = view.findViewById(R.id.ll_news);
        LinearLayout sponsorsBtn = view.findViewById(R.id.ll_sponser);
        LinearLayout joinBtn = view.findViewById(R.id.ll_join);
        LinearLayout aboutBtn = view.findViewById(R.id.ll_about);
        LinearLayout contactBtn = view.findViewById(R.id.ll_contact);
        LinearLayout logoutBtn = view.findViewById(R.id.ll_logout);

        LanguageBtn.setOnClickListener(this);
        eventsBtn.setOnClickListener(this);
        newsBtn.setOnClickListener(this);
        sponsorsBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        contactBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        LandingFragment landingFragment = (LandingFragment) getParentFragment();
        switch (viewId){
            case R.id.ll_language:
                if (Utility.isConnected(getContext())) {
                    MyApplication.get(getContext()).toggleLanguage();
                    Intent i = getActivity().getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getPackageName());
                    assert i != null;
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    Toast.makeText(getContext(), getString(R.string.error_network_connectivity), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_events:
                landingFragment.startFragmentFromChild(new EventsFragment());
                break;
            case R.id.ll_news:
                landingFragment.startFragmentFromChild(new NewsFragment());
                break;
            case R.id.ll_sponser:
                landingFragment.startFragmentFromChild(new SponsorsFragment());
                break;
            case R.id.ll_join:
                landingFragment.startFragmentFromChild(new JoinUsFragment());
                break;
            case R.id.ll_about:
                landingFragment.startFragmentFromChild(new AboutUsFragment());
                break;
            case R.id.ll_contact:
                landingFragment.startFragmentFromChild(new ContactUsFragment());
                break;
            case R.id.ll_logout:

                break;
        }
    }
}
