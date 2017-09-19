package com.a700apps.ummelquwain.ui.screens.landing.more;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.ui.screens.landing.LandingFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.aboutus.AboutUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.contactus.ContactUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.EventsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.joinus.JoinUsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.news.NewsFragment;
import com.a700apps.ummelquwain.ui.screens.landing.more.sponsors.SponsorsFragment;
import com.a700apps.ummelquwain.ui.screens.login.LoginFragment;
import com.a700apps.ummelquwain.utilities.Utility;
import com.facebook.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.ll_language)
    LinearLayout LanguageBtn;
    @BindView(R.id.ll_news)
    LinearLayout newsBtn;
    @BindView(R.id.ll_events)
    LinearLayout eventsBtn;
    @BindView(R.id.ll_sponser)
    LinearLayout sponsorsBtn;
    @BindView(R.id.ll_join)
    LinearLayout joinBtn;
    @BindView(R.id.ll_about)
    LinearLayout aboutBtn;
    @BindView(R.id.ll_contact)
    LinearLayout contactBtn;
    @BindView(R.id.ll_logout)
    LinearLayout logoutBtn;

    @BindView(R.id.tv_logout)
    TextView logoutTextView;

    Realm mRealm;

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
    public void onResume() {
        super.onResume();
//        ((LandingFragment) getParentFragment()).moveToHome();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mRealm = Realm.getDefaultInstance();
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
        switch (viewId) {
            case R.id.ll_language:
                if (Utility.isConnected(getContext())) {
                    MyApplication.get(getContext()).toggleLanguage();
                    getActivity().recreate();
                } else {
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
                String user = MyApplication.get(getContext()).getUser();
                if (!user.equals("-1")) {
                    mRealm.beginTransaction();
                    mRealm.where(User.class).findAll().deleteAllFromRealm();
                    mRealm.commitTransaction();
                    getParentFragment().getFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container,
                            new LoginFragment(), "login_fragment")
                            .addToBackStack(null).commit();
                    logoutTextView.setText(getString(R.string.title_log_out));
                    LoginManager.getInstance().logOut();
                }
                else {
                    getParentFragment().getFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container,
                            new LoginFragment(), "login_fragment")
                            .addToBackStack(null).commit();
                    logoutTextView.setText(getString(R.string.title_log_in));
                    LoginManager.getInstance().logOut();
                }
                break;
        }
    }
}
