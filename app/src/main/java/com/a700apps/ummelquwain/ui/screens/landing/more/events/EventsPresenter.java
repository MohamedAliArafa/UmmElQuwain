package com.a700apps.ummelquwain.ui.screens.landing.more.events;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.a700apps.ummelquwain.MyApplication;
import com.a700apps.ummelquwain.R;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.response.Events.EventResultModel;
import com.a700apps.ummelquwain.models.response.Events.EventsModel;
import com.a700apps.ummelquwain.ui.screens.landing.more.events.details.EventDetailsFragment;
import com.a700apps.ummelquwain.utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamed.arafa on 8/30/2017.
 */

public class EventsPresenter implements EventsContract.UserAction, LifecycleObserver {

    private Context mContext;
    private EventsContract.ModelView mView;
    private FragmentManager mFragmentManager;
    private Call<EventsModel> mEventsCall;
    private List<EventResultModel> mModel;

    public EventsPresenter(Context mContext, EventsContract.ModelView mView, FragmentManager mFragmentManager, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        this.mContext = mContext;
        this.mView = mView;
        this.mFragmentManager = mFragmentManager;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        mView.showProgress();
        mEventsCall = MyApplication.get(mContext).getApiService()
                .getAllEvents(new LanguageRequestModel(MyApplication.get(mContext).getLanguage()));
        mEventsCall.enqueue(new Callback<EventsModel>() {
            @Override
            public void onResponse(@NonNull Call<EventsModel> call, @NonNull Response<EventsModel> response) {
                try {
                    mModel = response.body().getResult();
                    mView.updateUI(mModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<EventsModel> call, @NonNull Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getSimpleName(), "Internet Fail");
                t.printStackTrace();
                mView.hideProgress();
            }
        });
    }

    @Override
    public void openDetails(EventResultModel event) {
        mFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_container, EventDetailsFragment.newInstance(event)).commit();
    }

    @Override
    public void shareEvent(EventResultModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getEventName()
                + "\n" + model.getEventPlace()
                + "\n" + model.getEventStartDate() + "-" + model.getEventEndDate()
                + "\n" + model.getEventDescription());
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.share_with)));
    }

    @Override
    public void addToCalender(EventResultModel model) {
        if (ActivityCompat.checkSelfPermission(this.mContext.getApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            mView.requestReadPermission();
            return;
        }
        if (!mView.requestReadPermission())
            return;
        if (Utility.isEventInCal(mContext, model.getEventName())) {
            Toast.makeText(mContext.getApplicationContext(), R.string.toast_event_already_added, Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault());
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        try {
            Date date = sdf.parse(model.getEventStartDate());
            cal.setTime(date);
            intent.putExtra("beginTime", cal.getTimeInMillis());
            date = sdf.parse(model.getEventEndDate());
            cal.setTime(date);
            intent.putExtra("endTime", cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        intent.putExtra("allDay", true);
        intent.putExtra("title", model.getEventName());
        mContext.startActivity(intent);
    }
}
