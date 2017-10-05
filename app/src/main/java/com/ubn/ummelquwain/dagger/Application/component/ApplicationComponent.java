package com.ubn.ummelquwain.dagger.Application.component;

import com.ubn.ummelquwain.dagger.Application.module.ApiServiceModule;
import com.ubn.ummelquwain.dagger.Application.module.PicassoModule;
import com.ubn.ummelquwain.dagger.Application.scope.ApplicationScope;
import com.ubn.ummelquwain.service.ApiService;
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
