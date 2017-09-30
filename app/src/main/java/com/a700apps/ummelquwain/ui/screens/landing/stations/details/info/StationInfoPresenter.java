package com.a700apps.ummelquwain.ui.screens.landing.stations.details.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.response.program.ProgramResultModel;
import com.a700apps.ummelquwain.ui.screens.landing.programs.details.ProgramFragment;

import static com.a700apps.ummelquwain.utilities.Constants.PROGRAM_FRAGMENT_KEY;

/**
 * Created by mohamed.arafa on 8/24/2017.
 */

public class StationInfoPresenter implements StationInfoContract.UserAction {

    private Context mContext;
    private FragmentManager mFragmentManager;

    public StationInfoPresenter(Context context, FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mContext = context;
    }

    @Override
    public void openSite(String url) {
        if (url != null) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(in);
        } else {
            Toast.makeText(mContext.getApplicationContext(), R.string.error_open, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openProgram(ProgramResultModel model) {
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
                PROGRAM_FRAGMENT_KEY + String.valueOf(model.getProgramID()), 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, ProgramFragment.newInstance(model.getProgramID()));
            ft.addToBackStack(PROGRAM_FRAGMENT_KEY + String.valueOf(model.getProgramID()));
            ft.commit();
        }
    }
}
