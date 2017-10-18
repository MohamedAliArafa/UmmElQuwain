package com.ubn.ummelquwain.utilities;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.InputStream;

/**
 * Created by mohamed.arafa on 10/12/2017.
 */

public class MyGlideModule implements ModelLoader<GlideUrl, InputStream> {
    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int width, int height, Options options) {
        return null;
    }

    @Override
    public boolean handles(GlideUrl glideUrl) {
        return false;
    }
}
