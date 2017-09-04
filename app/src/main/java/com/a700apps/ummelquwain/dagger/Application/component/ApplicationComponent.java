package com.a700apps.ummelquwain.dagger.Application.component;

import com.a700apps.ummelquwain.dagger.Application.module.ApiServiceModule;
import com.a700apps.ummelquwain.dagger.Application.scope.ApplicationScope;
import com.a700apps.ummelquwain.dagger.Application.module.PicassoModule;
import com.a700apps.ummelquwain.service.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

@ApplicationScope
@Component(modules = {ApiServiceModule.class, PicassoModule.class})
public interface ApplicationComponent {

    Picasso getPicasso();

    ApiService getService();

}
