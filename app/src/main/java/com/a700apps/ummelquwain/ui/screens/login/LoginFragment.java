package com.a700apps.ummelquwain.ui.screens.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.ui.screens.main.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_facebook_login)
    LinearLayout mFacebookLoginButton;

    @BindView(R.id.btn_twitter_login)
    LinearLayout mTwitterLoginButton;

    @BindView(R.id.tv_skip)
    TextView mSkipTextView;

    private CallbackManager mCallbackManager;
    private LoginPresenter mPresenter;
    private TwitterAuthClient mTwitterAuthClient;
    private Callback<TwitterSession> mTwitterCallback;
    private static String mDeviceId;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenter(getContext());
        mDeviceId = MyApplication.get(getActivity()).getDeviceID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        mFacebookLoginButton.setOnClickListener(this);
        mTwitterLoginButton.setOnClickListener(this);
        mSkipTextView.setOnClickListener(this);
        initFacebook();
        initTwitter();
        return view;
    }

    void initTwitter() {
        mTwitterAuthClient = new TwitterAuthClient();
        mTwitterCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                User user = new User();
                user.setSocialMediaID(String.valueOf(result.data.getUserId())); // 01/31/1980 format
                user.setName(result.data.getUserName());
                user.setSocialMediaType(2); //1 for facebook 2 for twitter
                user.setDeviceID(mDeviceId);
                mTwitterAuthClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        user.setEmail(result.data);
                        mPresenter.socialLogin(user);
                        if (getFragmentManager().getBackStackEntryCount() == 0)
                            ((MainActivity) getActivity()).launchLanding();
                        else
                            getFragmentManager().popBackStack();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getContext(), "Twitter Email Login error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getContext(), "Twitter Login error", Toast.LENGTH_SHORT).show();
            }
        };
    }

    void initFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        Toast.makeText(getContext(), "Facebook Login Sucess", Toast.LENGTH_SHORT).show();
                        User user = new User();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    Log.v("LoginFragment", response.toString());
                                    // Application code
                                    try {
                                        user.setEmail(object.getString("email"));
                                        user.setSocialMediaID(object.getString("id"));
                                        user.setDeviceID(mDeviceId);
                                        user.setName(object.getString("name"));
                                        user.setSocialMediaType(1); //1 for facebook 2 for twitter
                                        mPresenter.socialLogin(user);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                        if (getFragmentManager().getBackStackEntryCount() == 0)
                            ((MainActivity) getActivity()).launchLanding();
                        else
                            getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getContext(), "Facebook Login canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getContext(), "Facebook Login error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_facebook_login:
                LoginManager.getInstance().logInWithReadPermissions(
                        this,
                        Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
                );
                break;
            case R.id.btn_twitter_login:
                mTwitterAuthClient.authorize(getActivity(), mTwitterCallback);
                break;
            case R.id.tv_skip:
                if (getFragmentManager().getBackStackEntryCount() == 0)
                    ((MainActivity) getActivity()).launchLanding();
                else
                    getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
