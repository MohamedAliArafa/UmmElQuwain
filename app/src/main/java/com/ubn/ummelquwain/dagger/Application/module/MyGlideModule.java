package com.ubn.ummelquwain.dagger.Application.module;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.ubn.ummelquwain.utilities.Constants;

/**
 * Created by mohamed.arafa on 10/2/2017.
 */

@GlideModule
public final class MyGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        RequestOptions.option(Option.memory(Constants.GLIDE_TIMEOUT), 0);
    }
}
