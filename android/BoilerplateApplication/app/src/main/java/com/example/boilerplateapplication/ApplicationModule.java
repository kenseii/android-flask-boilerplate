package com.example.boilerplateapplication;

import android.app.Application;
import android.content.Context;

import com.example.boilerplateapplication.REST.RetrofitClient;
import com.example.boilerplateapplication.Utils.Prefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sqh on 2/29/16.
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public Prefs providePrefs() {
        return new Prefs(application);
    }

    @Provides
    @Singleton
    public  RetrofitClient.Endpoints provideApi() {
        // TODO, this might not be the most graceful handling of the prefs here.
        return RetrofitClient.getApiService(providePrefs());
    }


}
