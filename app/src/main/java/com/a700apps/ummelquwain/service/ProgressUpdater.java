package com.a700apps.ummelquwain.service;

/**
 * Created by mohamed.arafa on 9/20/2017.
 */

public class ProgressUpdater implements Runnable {
    private long mUploaded;
    private long mTotal;
    private RetrofitFileUploadCallbacks mCallbacks;

    public ProgressUpdater(long uploaded, long total, RetrofitFileUploadCallbacks callbacks) {
        mUploaded = uploaded;
        mTotal = total;
        mCallbacks = callbacks;
    }

    @Override
    public void run() {
        mCallbacks.onFileUploadProgress((int)(100 * mUploaded / mTotal));
    }
}
